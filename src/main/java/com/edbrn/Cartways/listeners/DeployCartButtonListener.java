package com.edbrn.Cartways.listeners;

import com.edbrn.Cartways.state.SpeedManagedMinecarts;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class DeployCartButtonListener implements Listener {
  Logger logger;

  public DeployCartButtonListener(Logger logger) {
    this.logger = logger;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    Block blockAbove = block.getRelative(BlockFace.UP);
    if (block.getType().name().equals("OAK_BUTTON")
        && blockAbove.getType().name() == "OAK_WALL_SIGN") {
      Sign sign = (Sign) blockAbove.getState();
      if (sign.getSide(Side.FRONT).getLine(0).equals("[STATION]")) {
        Location location = block.getLocation();
        location.add(0, -1, 0);

        World world = event.getPlayer().getWorld();
        Minecart minecart = world.spawn(location, Minecart.class);
        minecart.addPassenger(event.getPlayer());
        minecart.setVelocity(new Vector(0, 0, -2));
        minecart.setMaxSpeed(0.3);

        SpeedManagedMinecarts.getShared().addMinecart(minecart);

        event.getPlayer().sendMessage(minecart.getUniqueId().toString());
      }
    }
  }
}
