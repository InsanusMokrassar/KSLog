# Changelog

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
