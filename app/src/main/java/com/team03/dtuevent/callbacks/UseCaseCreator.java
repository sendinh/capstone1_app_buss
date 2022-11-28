package com.team03.dtuevent.callbacks;

import androidx.camera.core.UseCase;

@FunctionalInterface
public interface UseCaseCreator {
    UseCase[] create();
}

