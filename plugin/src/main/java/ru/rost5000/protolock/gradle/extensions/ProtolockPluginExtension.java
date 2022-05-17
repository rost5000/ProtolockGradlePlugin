package ru.rost5000.protolock.gradle.extensions;

import lombok.Data;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;

@Data
public class ProtolockPluginExtension {
    private ExecutableLocator protolock = new ExecutableLocator("protoc");
    private NamedDomainObjectContainer<ProtobufLocations> location;

    public void location(Action<? super NamedDomainObjectContainer<ProtobufLocations>> action) {
        action.execute(this.getLocation());
    }

    public void protoc(Action<? super ExecutableLocator> action) {
        action.execute(this.getProtolock());
    }
}
