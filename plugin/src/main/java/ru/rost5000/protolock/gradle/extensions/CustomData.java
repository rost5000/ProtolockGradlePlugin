package ru.rost5000.protolock.gradle.extensions;

import org.gradle.api.provider.Property;

abstract public class CustomData {

    abstract public Property<String> getWebsiteUrl();

    abstract public Property<String> getVcsUrl();
}