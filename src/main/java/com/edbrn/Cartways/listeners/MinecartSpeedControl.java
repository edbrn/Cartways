package com.edbrn.Cartways.listeners;

import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
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

    private boolean isStationBlockNear(Block block) {
        if (block.getType().name().equals("OAK_WALL_SIGN")) {
            Sign sign = (Sign) block.getState();
            if (sign.getSide(Side.FRONT).getLine(0).equals("[STATION]")) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (event.getVehicle().getName().equals("Minecart") && event.getVehicle().getTicksLived() > 20) {
            Minecart minecart = (Minecart) event.getVehicle();
            MinecartState minecartState = SpeedManagedMinecarts.getShared().getMinecartState(minecart);

            if (minecartState.getIsAtStation()) {
                return;
            }

            Block nextBlock = event.getFrom().getBlock();
            if (
                this.isStationBlockNear(nextBlock.getRelative(1, 2, 0))
                || this.isStationBlockNear(nextBlock.getRelative(-1, 2, 0))
                || this.isStationBlockNear(nextBlock.getRelative(0, 2, 1))
                || this.isStationBlockNear(nextBlock.getRelative(0, 2, -1))
            ) {
                minecartState.setReachedStation();
                minecart.setVelocity(new Vector(0, 0, 0));
            } else {
                Vector direction = minecartState.getInstance().getFacing().getDirection();
                minecartState.getInstance().setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()));
            }
        }
    }
}
