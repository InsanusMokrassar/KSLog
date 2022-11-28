# KSLog

It is simple and easy-to-use tool for logging on the most popular platforms in Kotlin Multiplatform:

![JVM](https://img.shields.io/badge/JVM-red?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-green?style=for-the-badge&logo=android&logoColor=white)
![Js](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)
![ARM x64](https://img.shields.io/badge/ARMx64-0091BD?style=for-the-badge&logo=arm&logoColor=F7DF1E)
![ARM x32](https://img.shields.io/badge/ARMx32-0091BD?style=for-the-badge&logo=arm&logoColor=F7DF1E)
![Linux x64](https://img.shields.io/badge/Linuxx64-FCC624?style=for-the-badge&logo=linux&logoColor=F7DF1E)

[![KDocs](https://img.shields.io/badge/KDocs-323330?style=for-the-badge&logo=Kotlin&logoColor=7F52FF)](https://insanusmokrassar.github.io/KSLog/)
[![Tutorials](https://img.shields.io/badge/Tutorials-0288D1?style=for-the-badge&logo=bookstack&logoColor=white)](https://bookstack.inmo.dev/books/kslog)

By default, KSLog is using built-in tools for logging on each supported platform:

* `java.util.logging.Logger` for `JVM`
* `android.util.Log` for `Android`
* `Console` for `JS`

But you always may create your logger and customize as you wish:

```kotlin
KSLog.default = KSLog { level: LogLevel, tag: String?, message: Any, throwable: Throwable? ->
    // do your logging
}
```

**This library also supports native targets in experimental mode. By default all native targets will use simple printing in the console**

## How to use

### Fast-travel

Just use some boring extensions like:

```kotlin
KSLog.i("Some message")
// OR
KSLog.i("Some tag", "Some message")
// OR
KSLog.i("Some tag", "Some message", IllegalArgumentException("So, that is exception :)"))
// OR
KSLog.i("Some optional tag", Exception("Optional")) { "Lazy inited message" }
// OR
KSLog.iS("Some optional tag", Exception("Optional")) { "Lazy inited message for suspendable calculation of text" }
// OR EVEN
KSLog.l(LogLevel.INFO, "Some tag", "Some message", IllegalArgumentException("So, that is exception :)"))
// OR
KSLog.l(LogLevel.INFO, "Some optional tag", IllegalArgumentException("So, that is exception :)")) { "And lazily inited message" }
```

### A little bit deeper

There are several important "terms" in context of this library:

* Default logger (available via `KSLog.default` or simply `KSLog`)
* Local logger (can be created via `KSLog` functions and passed anywhere as `KSLog`)
* Logging shortcuts like `KSLog.i`/`KSLog.info`
* Built-in extension `Any.logger` which allow you to create logger binded to the default with the tag based on the class of receiver
    * __Be careful with the receivers: if you will use some extension like `apply`, the receiver will be different with your class inside of that `apply`__

Every logging extension (like `KSLog.i`) have its analog with lazy inited message text and the same one with suffix `S` (like `KSLog.iS`) for the suspendable message calculation.

Default logger can be created by passing `defaultTag` and one of variants log level filters: set or minimal loggable level. In `JVM` you also may setup any logger as base logger for default realizations of `KSLog`. Besides, you may use your own callback (on **any target platform**) as output of logging:

```kotlin
val logger = KSLog { logLevel, optionalTag, message, optionalThrowable ->
    println("[$logLevel] $optionalTag - $message: $optionalThrowable.stackTraceToString()")
}
```

In the example above we will take the `logger` which will just print incoming data as common output.

## Installation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.inmo/kslog/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.inmo/kslog)

### Gradle

```groovy
implementation "dev.inmo:kslog:$kslog_version"
```

### Maven

```xml
<dependency>
  <groupId>dev.inmo</groupId>
  <artifactId>kslog</artifactId>
  <version>${kslog_version}</version>
</dependency>
```
