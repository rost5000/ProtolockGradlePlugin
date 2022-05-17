package ru.rost5000.protolock.gradle.extensions;

import lombok.Data;
import lombok.NonNull;
import org.gradle.api.Named;

@Data
public class ProtobufLocations implements Named {
    private final String name;
    private String protorootDir = ".";
    private String lockDir = ".";
    private boolean throwOnError = true;

    public ProtobufLocations(@NonNull String name) {
        this.name = name;
    }
}
