[![Build Status](https://travis-ci.org/NikitaKozlov/Pury.svg?branch=master)](https://travis-ci.org/NikitaKozlov/Pury)
[![Gitter](https://badges.gitter.im/gitterHQ/gitter.svg)](https://gitter.im/NikitaKozlov/Pury)
#Pury
**Pury** is a profiling library for measuring time between multiple independent events. 
Events can be triggered with one of the annotations or with a method call. 
All events for a single scenario are united into one report.

Output for launching an example app:
```
App Start --> 0ms
  Splash Screen --> 5ms
    Splash Load Data --> 37ms
    Splash Load Data <-- 1042ms, execution = 1005ms
  Splash Screen <-- 1042ms, execution = 1037ms
  Main Activity Launch --> 1043ms 
    onCreate() --> 1077ms 
    onCreate() <-- 1100ms, execution = 23ms
    onStart() --> 1101ms 
    onStart() <-- 1131ms, execution = 30ms
  Main Activity Launch <-- 1182ms, execution = 139ms
App Start <-- 1182ms
```
As you can see, Pury measured time for launching the application, including intermediate stages, 
like loading data on splash screen and activity’s lifecycle methods. 
For each stage start and stop timestamps are displayed and so as execution time. 

Output for a screen with pagination:
```
Get Next Page --> 0ms
  Load --> avg = 1.80ms, min = 1ms, max = 3ms, for 5 runs
  Load <-- avg = 258.40ms, min = 244ms, max = 278ms, for 5 runs
  Process --> avg = 261.00ms, min = 245ms, max = 280ms, for 5 runs
  Process <-- avg = 114.20ms, min = 99ms, max = 129ms, for 5 runs
Get Next Page <-- avg = 378.80ms, min = 353ms, max = 411ms, for 5 runs
```
In this example, you can see some statistical information that was collected during 5 runs. For each stage start timestamp and execution time are displayed.

##How to profile with Pury?

There are three basic annotations:

1. `@StartProfiling` — triggers an event to start Stage or Run. Profiling will start before method execution. 

    ```
    @StartProfiling(profilerName = "List pagination", runsCounter = 3, stageName = "Loading", 
    stageOrder = 0)
    private void loadNextPage() { }
    ```
    
    It can accept up to 5 arguments:
    1. **profilerName** — name of the profiler is displayed in the result. Along with runsCounter identifies the Profiler.
    2. **runsCounter** — amount of runs for Profiler to wait for. Result is available only after all runs are stopped.
    3. **stageName** — identifies  a stage to start. Name is displayed in the result.
    4. **stageOrder** — stage order reflects the hierarchy of stages. In order to start a new stage, it must be bigger then order of current most nested active stage. Stage order is a subject to one more limitation: first start event must have order number equal zero.
    5. **enabled** — if set to false, an annotation is skipped.
    
    Profiler is identified by combination of profilerName and runsCounter. So if you are using same profilerName, but different runsCounter, then you will get two separate results, instead of a combined one.
    
2. `@StopProfiling` — triggers an event to stop Stage or Run. Profiling will be stopped after method execution. Once Stage or Run is stopped, all nested stages are also stopped.
    
    ```
    @StopProfiling(profilerName = "List pagination", runsCounter = 3, stageName = "Loading")
    private void displayNextPage() { }
    ```

    It has the same arguments as `@StartProfiling`, except **stageOrder**.

3. `@MethodProfiling` — combination of StartProfiling and StopProfiling.

    ```
    @MethodProfiling(profilerName = "List pagination", runsCounter = 3, stageName = "Process", `
    stageOrder = 1)
    private List<String> processNextPage() { }
    ```
    
    It has exact same arguments as StartProfiling with one small remark. If stageName is empty then it will be generated from  method’s name and class. This is made in order to be able to use MethodProfiling without any arguments and get a meaningful result.


Since Java 7 doesn’t support repeatable annotations, group annotations for each of annotation above were made:
``` 
@StartProfilings(StartProfiling[] value)

@StopProfilings(StopProfiling[] value)

@MethodProfilings(MethodProfiling[] value)
```

As already mentioned, you can call start or stop profiling with a direct call:
```
Pury.startProfiling();

Pury.stopProfiling();
```
Arguments are exactly the same as in corresponding annotations.

##Logging Results
By default  Pury uses default logger, but it also allows you to set your own one. All you need to do is to implement Logger interface and set it via `Pury.setLogger()`.
```
public interface Logger {
    void result(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);
}
```
By default result goes to `Log.d`, warning to `Log.w` and error to `Log.e`.


##How to start using Pury?
In order to start using Pury, you need to do only two simple steps. 

First, apply AspectJ weaving plugin, there are more then one such a plugin out there. 
Pury inside uses [WeaverLite](https://github.com/NikitaKozlov/WeaverLite) plugin.
So you can use it as well.

Second, include following dependencies:
```
dependencies {
   compile 'com.nikitakozlov.pury:annotations:1.0.1'
   debugCompile 'com.nikitakozlov.pury:pury:1.0.2'
}
```
If you want to profile on release, then use compile instead of compileDebug for a second dependency.

##License 
MIT