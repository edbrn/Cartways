package com.edbrn.Cartways.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Minecart;

public class SpeedManagedMinecarts {
    private Map<UUID, MinecartState> trackedCarts = new HashMap<>();

    private static SpeedManagedMinecarts instance;
    private SpeedManagedMinecarts() {}

    public static SpeedManagedMinecarts getShared() {
        if (instance == null) {
            instance = new SpeedManagedMinecarts();
        }

        return instance;
    }

    public void addMinecart(Minecart minecart) {
        this.trackedCarts.put(minecart.getUniqueId(), new MinecartState(minecart));
    }

    public MinecartState getMinecartState(Minecart minecart) {
        return this.trackedCarts.get(minecart.getUniqueId());
    }
}
