package com.edbrn.Cartways.state;

import com.edbrn.Cartways.App;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MinecartState {
  private Minecart instance;
  private boolean isAtStation = false;

  public MinecartState(Minecart minecart) {
    this.instance = minecart;
  }

  public Minecart getInstance() {
    return this.instance;
  }

  public boolean getIsAtStation() {
    return isAtStation;
  }

  public void setLeftStation() {
    isAtStation = false;
  }

  public void setReachedStation() {
    isAtStation = true;
    MinecartState self = this;
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            JavaPlugin.getPlugin(App.class),
            new Runnable() {
              public void run() {
                Vector direction = self.instance.getFacing().getDirection();
                self.instance.setVelocity(
                    new Vector(direction.getX(), direction.getY(), direction.getZ()));

                Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(
                        JavaPlugin.getPlugin(App.class),
                        new Runnable() {
                          public void run() {
                            self.setLeftStation();
                          }
                        },
                        20);
              }
            },
            90);
  }

  public boolean isApproachingStation() {
    return false;
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

  public boolean isAtStation() {
    Block currentBlock = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (
        this.isStationBlockNear(currentBlock.getRelative(1, 2, 0))
          || this.isStationBlockNear(currentBlock.getRelative(-1, 2, 0))
          || this.isStationBlockNear(currentBlock.getRelative(0, 2, 1))
          || this.isStationBlockNear(currentBlock.getRelative(0, 2, -1))
    ) {
        return true;
    }

    return false;
  }
}
