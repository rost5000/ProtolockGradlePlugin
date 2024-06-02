# This subproject is for deploing artifact for maven repository

What the submodule do:
1. Download artifacts from [release](https://github.com/nilslice/protolock/releases) page
2. Unpack artifacts
3. Form a Maven repository structure by [Gradle publish](https://docs.gradle.org/current/userguide/publishing_maven.html) plugin
4. Publish to the Maven Central repository with [jReleaser](https://jreleaser.org/) plugin