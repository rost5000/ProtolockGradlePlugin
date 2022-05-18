package io.github.rost5000.gradle.protolock.extensions;

import org.gradle.api.Named;


public class ExecutableLocator implements Named {

    private final String name;

    private String path;
    private String artifact;

    public ExecutableLocator(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setArtifact(String spec) {
        this.artifact = spec;
        this.path = null;
    }

    /**
     * Specifies a local path.
     */
    public void setPath(String path) {
        this.path = path;
        this.artifact = null;
    }

    public String getArtifact() {
        return artifact;
    }

    public String getPath() {
        return path;
    }
}
