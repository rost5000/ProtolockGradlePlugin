package ru.rost5000.protolock.gradle.service;

public interface ProtolockService {
    public void init();

    public void commit();

    public void status();
}
