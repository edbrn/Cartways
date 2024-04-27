package com.edbrn.Cartways.listeners;

import com.edbrn.Cartways.state.MinecartState;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;
import java.util.logging.Logger;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MinecartSpeedControlListener implements Listener {
  Logger logger;
  SpeedManagedMinecarts speedManagedMinecarts;

  public MinecartSpeedControlListener(Logger logger, SpeedManagedMinecarts speedManagedMinecarts) {
    this.logger = logger;
    this.speedManagedMinecarts = speedManagedMinecarts;
  }

  @EventHandler
  public void onVehicleMove(VehicleMoveEvent event) {
    if (event.getVehicle().getType().equals(EntityType.MINECART)
        && event.getVehicle().getTicksLived() > 20) {
      Minecart minecart = (Minecart) event.getVehicle();
      MinecartState minecartState = this.speedManagedMinecarts.getMinecartState(minecart);

      if (minecartState.shouldStop()) {
        minecartState.stopBreifly();
      }

      minecartState.move();
    }
  }
}
