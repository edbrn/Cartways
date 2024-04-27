package com.edbrn.Cartways.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.scheduler.BukkitScheduler;

public class SpeedManagedMinecarts {
  private Map<UUID, MinecartState> trackedCarts = new HashMap<>();
  private BukkitScheduler scheduler;

  private static SpeedManagedMinecarts instance;

  private SpeedManagedMinecarts(BukkitScheduler scheduler) {
    this.scheduler = scheduler;
  }

  public static SpeedManagedMinecarts getShared() {
    if (instance == null) {
      instance = new SpeedManagedMinecarts(Bukkit.getScheduler());
    }

    return instance;
  }

  public static SpeedManagedMinecarts getShared(BukkitScheduler scheduler) {
    if (instance == null) {
      instance = new SpeedManagedMinecarts(scheduler);
    }

    return instance;
  }

  public void addMinecart(Minecart minecart) {
    this.trackedCarts.put(minecart.getUniqueId(), new MinecartState(minecart, this.scheduler));
  }

  public MinecartState getMinecartState(Minecart minecart) {
    return this.trackedCarts.get(minecart.getUniqueId());
  }
}
