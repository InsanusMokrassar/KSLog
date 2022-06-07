# How to use

That is a template for Kotlin Multiplatform Projects. How to use it:

* Create your repository from this template
* Add `local.properties` file in case you plan to use `Android` target (you must set up location of SDK, it will not be tracked by `git` and it is correct behaviour). In the snippet below you may see approximate content of `local.properties` file:
```properties
## This file must *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.
#
# Location of the SDK. This is only used by Gradle.
# For customization when using a Version Control System, please read the
# header note.
sdk.dir=/your/path/to/android/sdk
```
* Replace in a whole project `project_group` by your group
* Replace in a whole project `project_name` by your **ROOT** project name
* Update your subproject packages. It is not critical, but recommended especially in case you plan to publish your
  library

## Subprojects

In this template there is only one subproject with name `lib`. You are always able to rename it, but remember that in
this case you must rename it in `settings.gradle` file.

## JVM sources in Android target

By default JVM code is not included in Android target. In case you wish to include JVM sources in Android build, use
next method in the end of your `build.gradle`:

```groovy
enableIncludingJvmCodeInAndroidPart()
```

In case when you need to be sure that JVM sources are not included in Android part, use this snippet:

```groovy
disableIncludingJvmCodeInAndroidPart()
```

## Types of projects

### `mppProjectWithSerialization`

This type of preset have `JVM`, `JS` and `Android` targets and available using
`apply from: "$mppProjectWithSerializationPresetPath"`. Template for project with this preset looks like next snippet:

```groovy
plugins {
    id "org.jetbrains.kotlin.multiplatform"
    id "org.jetbrains.kotlin.plugin.serialization"
    id "com.android.library"
}

apply from: "$mppProjectWithSerializationPresetPath"

// The code below is optional

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // common dependencies
            }
        }
        commonTest {
            dependencies {
                // common test dependencies
            }
        }
        jvmMain {
            dependencies {
                // jvm dependencies
            }
        }
        jvmTest {
            dependencies {
                // jvm test dependencies
            }
        }
        jsMain {
            dependencies {
                // js dependencies
            }
        }
        jsTest {
            dependencies {
                // js test dependencies
            }
        }
        androidMain {
            dependencies {
                // android dependencies
            }
        }
        androidTest {
            dependencies {
                // android test dependencies
            }
        }
    }
}
```

### `mppJavaProject`

This type of preset have only `JVM` target and available using `apply from: "$mppJavaProjectPresetPath"`. Template for
project with this preset looks like next snippet:

```groovy
plugins {
    id "org.jetbrains.kotlin.multiplatform"
}

apply from: "$mppJavaProjectPresetPath"

// The code below is optional

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // common dependencies
            }
        }
        commonTest {
            dependencies {
                // common test dependencies
            }
        }
        jvmMain {
            dependencies {
                // jvm dependencies
            }
        }
        jvmTest {
            dependencies {
                // jvm test dependencies
            }
        }
    }
}
```

### `mppJsProject`

This type of preset have only `JS` target and available using `apply from: "mppJsProjectPresetPath"`. Template for
project with this preset looks like next snippet:

```groovy
plugins {
    id "org.jetbrains.kotlin.multiplatform"
}

apply from: "$mppJsProjectPresetPath"

// The code below is optional

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // common dependencies
            }
        }
        commonTest {
            dependencies {
                // common test dependencies
            }
        }
        jsMain {
            dependencies {
                // jvm dependencies
            }
        }
        jsTest {
            dependencies {
                // jvm test dependencies
            }
        }
    }
}
```

### `mppAndroidProject`

This type of preset have only `Android` target and available using `apply from: "$mppAndroidProjectPresetPath"`. Template for
project with this preset looks like next snippet:

```groovy
plugins {
    id "org.jetbrains.kotlin.multiplatform"
    id "com.android.library"
}

apply from: "$mppAndroidProjectPresetPath"

// The code below is optional

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // common dependencies
            }
        }
        commonTest {
            dependencies {
                // common test dependencies
            }
        }
        androidMain {
            dependencies {
                // android dependencies
            }
        }
        androidTest {
            dependencies {
                // android test dependencies
            }
        }
    }
}
```
