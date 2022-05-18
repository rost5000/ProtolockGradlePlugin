package io.github.rost5000.gradle.protolock.service;

import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;
import io.github.rost5000.gradle.protolock.extensions.ProtobufLocations;
import io.github.rost5000.gradle.protolock.extensions.ProtolockPluginExtension;
import io.github.rost5000.gradle.protolock.utils.ProtolockRunner;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;


@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProtolockServiceDecorator implements ProtolockService {
    final ProtolockRunner runner;
    final Collection<ProtobufLocations> protobufLocations;
    Collection<ProtolockService> protolockServices;

    private ProtolockServiceDecorator(ProtolockRunner runner, Collection<ProtobufLocations> protobufLocations) {
        this.protobufLocations = protobufLocations;
        this.runner = runner;
    }


    public ProtolockServiceDecorator(@NonNull ProtolockPluginExtension extension) {
        this(extension.getProtolock(), extension.getLocation());
    }

    public ProtolockServiceDecorator(ExecutableLocator protoc, Collection<ProtobufLocations> locations) {
        this(new ProtolockRunner(protoc), locations);
    }

    private Collection<ProtolockService> getSercices() {
        if (this.protolockServices == null) {
            protolockServices = protobufLocations.stream()
                    .map(location -> new ProtolockServiceImpl(runner, location))
                    .collect(Collectors.toList());
        }
        return protolockServices;
    }

    @Override
    public void init() {
        this.getSercices().forEach(ProtolockService::init);
    }

    @Override
    public void commit() {
        this.getSercices().forEach(ProtolockService::commit);
    }

    @Override
    public void status() {
        this.getSercices().forEach(ProtolockService::status);
    }
}
