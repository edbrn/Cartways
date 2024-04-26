package com.edbrn.Cartways.listeners;

import java.util.logging.Logger;

import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import com.edbrn.Cartways.state.MinecartState;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;

public class MinecartSpeedControl implements Listener {
    Logger logger;

    public MinecartSpeedControl(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle().getName().equals("Minecart") && event.getVehicle().getTicksLived() > 20) {
            Minecart minecart = (Minecart) event.getVehicle();
            MinecartState minecartState = SpeedManagedMinecarts.getShared().getMinecartState(minecart);
            Vector direction = minecartState.getInstance().getFacing().getDirection();
            minecartState.getInstance().setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()));
        }
    }
}
