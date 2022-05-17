# Protolock gradle plugin


This plugin is a Gradle plugin that allows you to use the [Protolock](https://github.com/nilslice/protolock) in gradle.

You can check more examples in the [repository](./samples)

## How to use
1. Add Protolock in your `PATH` (you can download it from [here](https://github.com/nilslice/protolock/releases))
2. Add the plugin to your `build.gradle` file
```groovy
plugins {
    id 'ru.rost5000.protolock.gradle' 
}
```
## How to configure

You can configure the directory of proto by:

```groovy
protolock {
    location{
        defaultLocation {
            setProtorootDir("$projectDir/src/main/proto")
            lockDir = "$projectDir/src/main/proto"
        }
    }
}
```

You can configure many locations by:

```groovy   
protolock {
    location {
        defaultLocation {
            setProtorootDir("$projectDir/src/main/proto")
            lockDir = "$projectDir/src/main/proto"
        }
        location {
            setProtorootDir("$projectDir/src/main/proto2")
            lockDir = "$projectDir/src/main/proto2"
        }
    }
}
```

Plugin tries run protolock from your `PATH` environment. 
You can specify the location of protolock by:

```groovy
protolock {
    protolock {
        path = "path-to-protolock/protolock"
    }
}
```

if you want do not fail your build if protolock found some errors, you can configure the plugin by:

```groovy
protolock {
    firstLocation {
        setProtorootDir("$projectDir/src/main/proto")
        lockDir = "$projectDir/src/main/proto"
        throwOnError = fasle
    }
}
``` 

You can use [protobuf gradle plugin](https://github.com/google/protobuf-gradle-plugin), then dirrectory `src/main/proto` 
in your project will be added automatically.

### Commands

1. `gradle protolockInit` -- initialize your directories with file `proto.lock`. Analog to `protolock init`
2. `gradle protolockStatus` -- check backwards compatibility. Analog to `protolock status`. This command will be executed automatically when you run `gradle test`.
3. `gradle protolockCommit` -- commit your changes. Analog `protolock commit`

### Project structure

1. [plugin](./plugin) directory contains plugin source code.
2. [samples](./samples) directory contains samples for usage plugin.