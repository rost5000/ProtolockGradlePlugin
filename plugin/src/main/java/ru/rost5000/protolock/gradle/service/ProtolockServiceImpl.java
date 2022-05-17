package ru.rost5000.protolock.gradle.service;

import lombok.NonNull;
import org.gradle.api.GradleException;
import ru.rost5000.protolock.gradle.extensions.ProtobufLocations;
import ru.rost5000.protolock.gradle.utils.ProtolockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProtolockServiceImpl implements ProtolockService{
    private static final Logger LOGGER = Logger.getLogger(ProtolockServiceImpl.class.getName());
    private static final String PROTOLOCK_FILE = "proto.lock";
    private final ProtolockRunner protocRunner;
    private final ProtobufLocations extension;

    public ProtolockServiceImpl(@NonNull ProtolockRunner runner, @NonNull ProtobufLocations extension) {
        this.protocRunner = runner;
        this.extension = extension;
    }

    public void init() {
        if (isProtolockFileExist()) {
            LOGGER.log(Level.INFO, "Protolock already initialized");
            return;
        }
        this.run("init");
    }

    public void commit() {
        if (!isProtolockFileExist()) {
            return;
        }
        this.run("commit");
    }

    public void status() {
        if (!isProtolockFileExist()) {
            return;
        }
        try {
            this.run("status");
        } catch (GradleException e) {
            if (extension.isThrowOnError()) {
                throw e;
            }
            LOGGER.log(Level.INFO, "Protolock status failed", e);
        }
    }

    private List<String> getAdditionalParams() {
        return Arrays.asList("--lockdir", extension.getLockDir(), "--protoroot", extension.getProtorootDir());
    }

    private void run(String command) {
        run(command, this.getAdditionalParams());
    }

    private void run(String command, List<String> args) {
        List<String> commands = new ArrayList<String>(Collections.singletonList(command));
        commands.addAll(args);
        protocRunner.run(commands.toArray(new String[commands.size()]));
    }

    private boolean isProtolockFileExist() {
        return new File(extension.getLockDir() + File.separator + PROTOLOCK_FILE).exists();
    }
}


