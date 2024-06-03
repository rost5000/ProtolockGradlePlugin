package io.github.rost5000.gradle.protolock.extensions;

import lombok.Data;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;

@Data
public class ProtolockPluginExtension {
    private ExecutableLocator protolock = new ExecutableLocator("protolock");
    private NamedDomainObjectContainer<ProtobufLocations> location;

    public void location(Action<? super NamedDomainObjectContainer<ProtobufLocations>> action) {
        action.execute(this.getLocation());
    }

    public void protolock(Action<? super ExecutableLocator> action) {
        action.execute(this.getProtolock());
    }
}
