package ru.rost5000.protolock.gradle;

import org.gradle.api.GradleException;
import ru.rost5000.protolock.gradle.extensions.ExecutableLocator;
import ru.rost5000.protolock.gradle.extensions.ProtobufLocations;
import ru.rost5000.protolock.gradle.service.ProtolockServiceImpl;
import ru.rost5000.protolock.gradle.utils.ProtolockLoader;
import ru.rost5000.protolock.gradle.utils.ProtolockRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProtolockServiceTest {
    static final String ANOTHER_PROTO = "syntax = \"proto3\";\n" +
            "\n" +
            "message SearchRequest {\n" +
            "  string query = 1;\n" +
            "  int32 result_per_page = 3;\n" +
            "}\n";
    static final String TEST_PROTO_PATH = "src/test/proto";
    ProtolockServiceImpl protolockService;
    ProtolockLoader protolockLoader = new ProtolockLoader();

    @org.junit.jupiter.api.BeforeEach
    void initialize() {
        ExecutableLocator locator = protolockLoader.getExecutableLocator();
        ProtolockRunner runner = new ProtolockRunner(locator);
        ProtobufLocations extension = new ProtobufLocations("lol");
        extension.setLockDir(TEST_PROTO_PATH);
        extension.setProtorootDir(TEST_PROTO_PATH);

        protolockService = new ProtolockServiceImpl(runner, extension);

        protolockService.init();
    }

    @org.junit.jupiter.api.AfterEach
    void cleanup() {
        File protolockFile = new File(TEST_PROTO_PATH + "/proto.lock");
        assertTrue(protolockFile.delete());
    }

    @org.junit.jupiter.api.Test
    void testInit() {
        File protolockFile = new File(TEST_PROTO_PATH + "/proto.lock");
        assertNotNull(protolockFile);
        assertTrue(protolockFile.exists());
    }

    @org.junit.jupiter.api.Test
    void testCommit() {
        protolockService.commit();
    }

    @org.junit.jupiter.api.Test
    void testStatus() {
        protolockService.status();
    }

    @org.junit.jupiter.api.Test
    void testStatusWithException() throws IOException {
        String file = TEST_PROTO_PATH + "/search_request.proto";
        BufferedReader reader = new BufferedReader(
                new FileReader(file)
        );
        String fileContent = reader.lines().collect(Collectors.joining("\n"));
        reader.close();

        try {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(ANOTHER_PROTO.getBytes());
            outputStream.close();

            assertThrows(GradleException.class, () -> protolockService.status());
        } finally {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        }
    }

}