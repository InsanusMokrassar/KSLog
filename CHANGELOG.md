# Changelog

## 1.3.1

* `Kotlin`: `1.9.21`

## 1.3.0

**THIS UPDATE CONTAINS CHANGES OF LOG LEVELS ACCORDANCES. LOOK AT THE LEVELS ACCORDANCE IN THE [DOCUMENTATION](https://docs.inmo.dev/kslog/logging.html#logging)
FOR MORE INFO**

* Add `trace` logging level
* Change overall table of log level accordance
* Now you may change default platform logger

## 1.2.4

**This update contains migration onto gradle 8+ (for both wrapper and android plugin). Use it with caution**

## 1.2.3

* `Kotlin`: `1.9.20`

## 1.2.2

**wasm32 target has been removed**
**mingwX86 target has been removed**

* `Kotlin`: `1.9.20-RC`

## 1.2.1

* `Kotlin`: `1.9.10`
* Latest supported android sdk now is `34`

## 1.2.0

* `Kotlin`: `1.9.0`

## 1.1.2

* `Kotlin`: `1.8.22`

## 1.1.1

* Rollback `KSLog` with `String` arg factory to use `DefaultKSLog`

## 1.1.0

* `Kotlin`: `1.8.20`
* Standard factory `KSLog` with `String` arg now use `TagLogger`

## 1.0.0

* `Kotlin`: `1.8.0`

## 0.5.4

* `Kotlin`: `1.7.22`

## 0.5.3

* `Kotlin`: `1.7.20`

## 0.5.2

* Add support of next platform:
  * Linux ARM x64
  * Linux ARM x32 HFP
  * Linux x64
  * WASM x32
  * MinGW x64
  * MinGW x86

## 0.5.1

* New property `defaultMessageFormatterWithErrorPrint` for messages format with errors

## 0.5.0

* `Kotlin`: `1.7.10`

## 0.4.2

* Add `setDefaultKSLog` and `addDefaultKSLog`
* Add `plus` operation for two `KSLog` instances to call them both on each log performing
* Add `KSLog` factory for simple creation of `CallbackKSLog`
* Add `MessageFormatter` factory

## 0.4.1

* Fixes in resolution ambiguity for functions with messages

## 0.4.0

**THIS UPDATE CONTAINS BREAKING CHANGES**

* **BREAKING CHANGE** Any `KSLog` now accept any object. It was made to allow to work with structured logging
* New factory for `TagLogger`: fun `taggedLogger`
* New logger has been added: `TypedLogger`
* New logger has been added: `FilterLogger`
* **BREAKING CHANGE** Logger `DefaultKSLog` lost its constructor with `filter`. Currently, it is still available as factory function

## 0.3.2

* Add duplication functions for all extensions which allow to send log without tag but with exception and message builder

## 0.3.1

* Technical fixes for compatibility with Java 8

## 0.3.0

* **BREAKING CHANGE**: Filters will not accept message since this update
* New `KSLog` realization `DefaultKSLog`
* Now platforms realize their default loggers callbacks instead of `KSLog` factories

## 0.2.0

* Full rewrite of API

## 0.1.1

* Changes in `JVM` levels
* Fixes in TagLogger

## 0.1.0

* All the platform specific logged factories has been renamed

## 0.0.3

* Fixes in `JVM` logging

## 0.0.2

* New logger `TagLogger` and now extension `Any#logger` will use it to decrease objects allocations
* Now it is possible to create `KSLog` using any iterable
* Now it is possible to create `KSLog` using vararg log levels

## 0.0.1

* Project has been inited
