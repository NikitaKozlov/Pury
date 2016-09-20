[![Build Status](https://travis-ci.org/NikitaKozlov/Pury.svg?branch=master)](https://travis-ci.org/NikitaKozlov/Pury)
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
As you can see, Pury measured time for launching the application, including intermediate stages, `
like loading data on splash screen and activity’s lifecycle methods. 
For each stage start and stop timestamps are displayed and so as execution time. 

Output for fragment with pagination:
```
Get Next Page --> 0ms
  Load --> avg = 1.80ms, min = 1ms, max = 3ms, for 5 runs
  Load <-- avg = 258.40ms, min = 244ms, max = 278ms, for 5 runs
  Process --> avg = 261.00ms, min = 245ms, max = 280ms, for 5 runs
  Process <-- avg = 114.20ms, min = 99ms, max = 129ms, for 5 runs
Get Next Page <-- avg = 378.80ms, min = 353ms, max = 411ms, for 5 runs
```
In this example, you can see some statistical information that was collected during 5 runs. For each stage start timestamp and execution time are displayed.

##How to start using Pury?
In order to start using Pury, you need to do only two simple steps. 
First, apply AspectJ weaving plugin, there are more then one such a plugin out there. 
Pury inside uses [WeaverLite](https://github.com/NikitaKozlov/WeaverLite) plugin.
So you can use it as well.

##Logging Results
By default  Pury uses default logger, but it also allows you to set your own one. All you need to do is to implement Logger interface and set it via Pury.setLogger().
```
public interface Logger {
    void result(String tag, String message);

    void warning(String tag, String message);

    void error(String tag, String message);
}
```
By default result goes to Log.d, warning to Log.w and error to Log.e.

##License 
MIT