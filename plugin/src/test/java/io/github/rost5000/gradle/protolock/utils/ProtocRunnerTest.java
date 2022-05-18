package io.github.rost5000.gradle.protolock.utils;

import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;
import org.gradle.api.GradleException;
import org.junit.jupiter.api.BeforeEach;

import static org.gradle.internal.impldep.org.junit.Assert.assertThrows;
import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;

class ProtocRunnerTest {
    ProtolockLoader loader = new ProtolockLoader();
    ProtolockRunner protocRunner;

    @BeforeEach
    void initialize() {
        protocRunner = new ProtolockRunner(
                new ExecutableLocator("protoc") {{
                    setPath(loader.getProtolockPath());
                }}
        );
    }

    @org.junit.jupiter.api.Test
    void checkProtocPath() {
        assertTrue(
                protocRunner.getProtolockPluginPath().contains(
                        "protolock"
                )
        );
    }

    @org.junit.jupiter.api.Test
    void runHelpWithoutException() {
        protocRunner.run(new String[]{"--help"});
    }

    @org.junit.jupiter.api.Test
    void runCommandWithException() {
        assertThrows(GradleException.class, () -> protocRunner.run(new String[]{"commit"}));
    }


}