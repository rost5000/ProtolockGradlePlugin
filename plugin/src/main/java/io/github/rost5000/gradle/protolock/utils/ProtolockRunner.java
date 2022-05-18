package io.github.rost5000.gradle.protolock.utils;

import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;
import org.gradle.api.GradleException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProtolockRunner {
    private final static Logger logger = Logger.getLogger(ProtolockRunner.class.getName());
    private final ExecutableLocator executableLocator;

    public ProtolockRunner(ExecutableLocator executableLocator) {
        this.executableLocator = executableLocator;
    }

    public void run(String... args) {
        this.run(getProtolockPluginPath(), args);
    }

    public void run(String execPath, String... args) {
        ArrayList<String> command = new ArrayList<String>(args.length + 1);
        command.add(execPath);
        command.addAll(Arrays.asList(args));


        Process result = null;
        try {
            result = new ProcessBuilder(command).start();
            result.waitFor();
        } catch (IOException e) {
            throw new GradleException("Cannot execute protoc: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new GradleException("Interrupted process: " + e.getMessage(), e);
        }
        StringBuffer stderr = new StringBuffer();

        if (result.exitValue() == 0) {
            String output = new BufferedReader(new InputStreamReader(result.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            logger.info(
                    output
            );
        } else {
            String outputError = new BufferedReader(new InputStreamReader(result.getErrorStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String output = new BufferedReader(new InputStreamReader(result.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            throw new GradleException(
                    "An error: " +
                            outputError +
                            "\nInfo: " +
                            output
            );
        }

    }

    public String getProtolockPluginPath() {
        if (executableLocator.getPath() == null) {
            logger.log(Level.WARNING, "Protolock plugin path is not set. Using default path.");
            return "protolock";
        }
        if (executableLocator.getArtifact() != null) {
            throw new UnsupportedOperationException("Artifact path is not supported yet.");
        }
        File file = new File(executableLocator.getPath());
        if (!file.canExecute() && !file.setExecutable(true)) {
            throw new GradleException("Cannot set ${file} as executable");
        }
        logger.info("Resolved artifact: " + file.getAbsolutePath());
        return file.getPath();
    }
}
