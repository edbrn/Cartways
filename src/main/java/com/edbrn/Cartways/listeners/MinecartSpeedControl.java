package com.edbrn.Cartways.listeners;

import com.edbrn.Cartways.state.MinecartState;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;
import java.util.logging.Logger;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MinecartSpeedControl implements Listener {
  Logger logger;

  public MinecartSpeedControl(Logger logger) {
    this.logger = logger;
  }

  @EventHandler
  public void onVehicleMove(VehicleMoveEvent event) {
    if (event.getVehicle().getName().equals("Minecart")
        && event.getVehicle().getTicksLived() > 20) {
      Minecart minecart = (Minecart) event.getVehicle();
      MinecartState minecartState = SpeedManagedMinecarts.getShared().getMinecartState(minecart);

      if (minecartState.shouldStop()) {
        minecartState.stopBreifly();
      }

      minecartState.move();
    }
  }
}
