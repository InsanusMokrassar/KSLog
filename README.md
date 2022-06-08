# How to use

![JVM](https://img.shields.io/badge/JVM-red?style=for-the-badge&logo=openjdk&logoColor=white)
![Android](https://img.shields.io/badge/Android-green?style=for-the-badge&logo=android&logoColor=white)
![Js](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)

[![KDocs](https://img.shields.io/badge/KDocs-323330?style=for-the-badge&logo=Kotlin&logoColor=7F52FF)](https://insanusmokrassar.github.io/KSLog/)
[![Tutorials](https://img.shields.io/badge/Tutorials-0288D1?style=for-the-badge&logo=bookstack&logoColor=white)](https://insanusmokrassar.github.io/KSLog/)

It is simple logging tool which is using built-in tools for logging:

* `java.util.logging.Logger` for `JVM`
* `android.util.Log` for `Android`
* `Console` for `JS`

## How to use

### Fast-travel

Just use some boring extensions like:

```kotlin
KSLog.i("Some message")
// OR
KSLog.i("Some tag", "Some message")
// OR
KSLog.i("Some tag", "Some message", IllegalArgumentException("So, that is exception :)"))
// OR EVEN
KSLog.l(LogLevel.INFO, "Some tag", "Some message", IllegalArgumentException("So, that is exception :)"))
```

### A little bit deeper

There are several important things in context of this library:

* Default logger (available via `KSLog`)
* Local logger (can be created via `KSLog` functions and passed anywhere as `KSLog`)
* Logging shortcuts like `KSLog.i`/`KSLog.info`

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
