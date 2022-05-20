package io.github.rost5000.gradle.protolock;

import com.google.gradle.osdetector.OsDetectorPlugin;
import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;
import io.github.rost5000.gradle.protolock.extensions.ProtobufLocations;
import io.github.rost5000.gradle.protolock.extensions.ProtolockPluginExtension;
import io.github.rost5000.gradle.protolock.service.ProtolockService;
import io.github.rost5000.gradle.protolock.service.ProtolockServiceDecorator;
import io.github.rost5000.gradle.protolock.utils.ToolLocator;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.File;
import java.util.Collections;

public class ProtolockPlugin implements Plugin<Project> {
    private static final String GRADLE_PROTOC_STATUS_NAME = "protolockStatus";
    private static final String GRADLE_PROTOC_COMMIT_NAME = "protolockCommit";
    private static final String GRADLE_PROTOC_INIT_NAME = "protolockInit";
    private ToolLocator toolLocator;
    private ExecutableLocator protocLocation;

    @Override
    public void apply(Project project) {
        project.apply(Collections.singletonMap("plugin", OsDetectorPlugin.class));

        ProtolockPluginExtension extension = project.getExtensions()
                .create("protolock", ProtolockPluginExtension.class);
        NamedDomainObjectContainer<ProtobufLocations> container = project.container(ProtobufLocations.class);
        extension.setLocation(container);

        final ProtolockServiceDecorator protolockService = new ProtolockServiceDecorator(extension);

        this.toolLocator = new ToolLocator(project, extension.getProtolock());
        this.protocLocation = extension.getProtolock();

        project.getTasks().register(GRADLE_PROTOC_STATUS_NAME,
                task -> registerTaskStatus(task, protolockService));
        project.getTasks().register(GRADLE_PROTOC_COMMIT_NAME,
                task -> registerTaskCommit(task, protolockService));
        project.getTasks().register(GRADLE_PROTOC_INIT_NAME,
                task -> registerTaskInit(task, protolockService));

        Task testTask = project.getTasks().findByName("test");
        if (testTask != null) {
            testTask.dependsOn(GRADLE_PROTOC_STATUS_NAME);
        }

        if (project.getPluginManager().hasPlugin("com.google.protobuf") &&
                project.getPluginManager().hasPlugin("java")) {
            container.register(
                    "defProto",
                    addLocation(
                            new File(project.getProjectDir().getAbsolutePath() +
                                    File.separator +
                                    "src" + File.separator + "main" + File.separator + "proto"),
                            container
                    )
            );
        }
    }

    private Action<? super ProtobufLocations> addLocation(File dir, NamedDomainObjectContainer<ProtobufLocations> container) {
        return (Action<ProtobufLocations>) protobufLocations -> {
            protobufLocations.setLockDir(dir.getAbsolutePath());
            protobufLocations.setProtorootDir(dir.getAbsolutePath());
        };
    }

    private void registerTaskInit(Task task, ProtolockService protolockService) {
        prepeare();
        protolockService.init();
    }

    private void prepeare() {
        File executableFileFromDependency = toolLocator.getExecutableFileProtolockFromDependency();
        if(executableFileFromDependency == null) {
            return;
        }
        this.protocLocation.setPath(executableFileFromDependency.getAbsolutePath());
    }

    private void registerTaskCommit(Task task, ProtolockService protolockService) {
        prepeare();
        protolockService.commit();
    }

    private void registerTaskStatus(Task task, ProtolockService protolockService) {
        prepeare();
        protolockService.status();
    }
}
