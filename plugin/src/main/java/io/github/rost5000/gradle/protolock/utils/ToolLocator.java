package io.github.rost5000.gradle.protolock.utils;

import io.github.rost5000.gradle.protolock.extensions.ExecutableLocator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.FileCollection;
import org.gradle.internal.impldep.com.amazonaws.services.kms.model.UnsupportedOperationException;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToolLocator {
    Configuration configuration;
    ExecutableLocator executableLocator;
    Project project;

    public ToolLocator(Project project, ExecutableLocator executableLocator) {
        configuration = project.getConfigurations().create("protolockToolsLocator_" + executableLocator.getName(),
                config -> {
                    config.setVisible(false);
                    config.setTransitive(false);
                    config.setExtendsFrom(Collections.emptyList());
                }
        );
        this.executableLocator = executableLocator;
        this.project = project;
    }

    public File getExecutableFileProtolockFromDependency() {
        if (this.executableLocator.getArtifact() == null) {
            if(this.executableLocator.getPath() == null) {
                return null;
            }
            return new File(this.executableLocator.getPath());
        }
        DependencyInfo dependencyInfo = getDependencyInfo(this.executableLocator.getArtifact());
        Map<String, String> notation = getNotation(dependencyInfo);
        Dependency dependency = project.getDependencies().add(configuration.getName(), notation);
        FileCollection artifactFiles = configuration.fileCollection(dependency);
        return artifactFiles.getSingleFile();

    }

    private Map<String, String> getNotation(DependencyInfo dependencyInfo) {
        Map<String, String> notation = new HashMap<>();
        notation.put("group", dependencyInfo.getGroup());
        notation.put("name", dependencyInfo.getArtifact());
        notation.put("version", dependencyInfo.getVersion());
        notation.put("classifier", dependencyInfo.getClassifier());
        notation.put("ext", dependencyInfo.getExtension());
        return notation;
    }

    private DependencyInfo getDependencyInfo(String artifact) {
        String[] splitted = artifact.split(":");
        if (splitted.length != 3) {
            throw new GradleException("Artifact must be in the form of group:name:version");
        }
        return new DependencyInfo(splitted[0], splitted[1], splitted[2]);
    }

    @Data
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    protected static class DependencyInfo {
        String group;
        String artifact;
        String version;
        String classifier = this.generateClassifier();
        String extension = this.generateExtension();

        private String generateExtension() {
            return "exe";
        }

        private String generateClassifier() {
            String os, osProperty = System.getProperty("os.name").toLowerCase();
            String processor = System.getProperty("os.arch").toLowerCase();
            if (osProperty.contains("win")) {
                os = "win";
            } else if (osProperty.contains("linux")) {
                os = "linux";
            } else {
                throw new RuntimeException("Unsupported OS: " + osProperty);
            }
            return os + "-" + processor;
        }

    }
}
