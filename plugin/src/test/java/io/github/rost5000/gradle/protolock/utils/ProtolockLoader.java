package io.github.rost5000.gradle.protolock.utils;


import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;

import java.util.Objects;

public class ProtolockLoader {

    public String getProtolockPath() {
        String path;
        ClassLoader classLoader = getClass().getClassLoader();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = Objects.requireNonNull(classLoader.getResource("protolock.exe")).getPath();
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            path = Objects.requireNonNull(classLoader.getResource("protolock")).getPath();
        } else {
            throw new RuntimeException("Unknown OS");
        }
        return path;
    }

    public ExecutableLocator getExecutableLocator() {
        ExecutableLocator protoc = new ExecutableLocator("protoc");
        protoc.setPath(getProtolockPath());
        return protoc;
    }
}
