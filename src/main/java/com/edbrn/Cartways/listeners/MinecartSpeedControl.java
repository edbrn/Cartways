package com.edbrn.Cartways.listeners;

import com.edbrn.Cartways.state.MinecartState;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

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

      if (minecartState.getIsAtStation()) {
        return;
      }

      if (minecartState.isAtStation()) {
        Bukkit.broadcastMessage("At station");
        minecartState.setReachedStation();
        minecart.setVelocity(new Vector(0, 0, 0));
      } else {
        Vector direction = minecartState.getInstance().getFacing().getDirection();
        minecartState
            .getInstance()
            .setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()));
      }
    }
  }
}
