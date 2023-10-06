package com.yanny.ytech.network.kinetic;

import com.yanny.ytech.network.generic.client.ClientNetwork;
import org.jetbrains.annotations.NotNull;

public class KineticClientNetwork extends ClientNetwork {
    @NotNull private final RotationDirection rotationDirection;
    private final int stressCapacity;
    private final int stress;

    public KineticClientNetwork(int networkId, int stressCapacity, int stress, @NotNull RotationDirection rotationDirection) {
        super(networkId);
        this.stressCapacity = stressCapacity;
        this.stress = stress;
        this.rotationDirection = rotationDirection;
    }

    @NotNull
    public RotationDirection getRotationDirection() {
        return rotationDirection;
    }

    public int getStressCapacity() {
        return stressCapacity;
    }

    public int getStress() {
        return stress;
    }
}
