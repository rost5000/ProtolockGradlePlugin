package io.github.rost5000.gradle.protolock;

import io.github.rost5000.gradle.protolock.extensions.ProtobufLocations;
import io.github.rost5000.gradle.protolock.extensions.ProtolockPluginExtension;
import io.github.rost5000.gradle.protolock.service.ProtolockService;
import io.github.rost5000.gradle.protolock.service.ProtolockServiceDecorator;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.File;

public class ProtolockPlugin implements Plugin<Project> {
    ProtolockService protolockService;

    @Override
    public void apply(Project project) {
        ProtolockPluginExtension extension = project.getExtensions()
                .create("protolock", ProtolockPluginExtension.class);
        NamedDomainObjectContainer<ProtobufLocations> container = project.container(ProtobufLocations.class);
        extension.setLocation(container);

        this.protolockService = new ProtolockServiceDecorator(extension);


        project.getTasks().register("protolockStatus", this::registerTaskStatus);
        project.getTasks().register("protolockCommit", this::registerTaskCommit);
        project.getTasks().register("protolockInit", this::registerTaskInit);

        Task testTask = project.getTasks().findByName("test");
        if (testTask != null) {
            testTask.dependsOn("protolockStatus");
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

    private void registerTaskInit(Task task) {
        protolockService.init();
    }

    private void registerTaskCommit(Task task) {
        protolockService.commit();
    }

    private void registerTaskStatus(Task task) {
        protolockService.status();
    }
}
