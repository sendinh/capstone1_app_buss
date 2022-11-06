package com.team03.dtuevent.callbacks;

@FunctionalInterface
public interface Observer<T> {
    void valueChanged(T newValue);
}