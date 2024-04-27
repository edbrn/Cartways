package com.edbrn.Cartways.state;

import com.edbrn.Cartways.App;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MinecartState {
  private Minecart instance;
  private boolean isStopped = false;
  private boolean hasRecentlyStopped = false;

  public MinecartState(Minecart minecart) {
    this.instance = minecart;
  }

  public Minecart getInstance() {
    return this.instance;
  }

  public boolean shouldMinecartMove() {
    return !this.isStopped;
  }

  public boolean shouldStop() {
    return !this.hasRecentlyStopped && this.isAtStation();
  }

  public void moveBackward() {
    this.instance.setVelocity(this.instance.getFacing().getOppositeFace().getDirection());
  }

  public void stopBreifly() {
    this.stopMoving();

    isStopped = true;
    MinecartState self = this;
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            JavaPlugin.getPlugin(App.class),
            new Runnable() {
              public void run() {
                self.hasRecentlyStopped = true;
                self.isStopped = false;

                if (self.isAtEndOfLine()) {
                  self.moveBackward();
                } else {
                  self.move();
                }

                Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(
                        JavaPlugin.getPlugin(App.class),
                        new Runnable() {
                          public void run() {
                            self.hasRecentlyStopped = false;
                          }
                        },
                        40);
              }
            },
            90);
  }

  public void move() {
    this.instance.setVelocity(this.instance.getFacing().getDirection());
  }

  public void stopMoving() {
    this.instance.setVelocity(new Vector(0, 0, 0));
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

    if (this.isStationBlockNear(currentBlock.getRelative(1, 2, 0))
        || this.isStationBlockNear(currentBlock.getRelative(-1, 2, 0))
        || this.isStationBlockNear(currentBlock.getRelative(0, 2, 1))
        || this.isStationBlockNear(currentBlock.getRelative(0, 2, -1))) {
      return true;
    }

    return false;
  }

  public boolean isAtEndOfLine() {
    Block currentBlock = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (this.instance.getFacing().equals(BlockFace.EAST)
        && this.isStationBlockNear(currentBlock.getRelative(1, 2, 0))) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.WEST)
        && this.isStationBlockNear(currentBlock.getRelative(-1, 2, 0))) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.NORTH)
        && this.isStationBlockNear(currentBlock.getRelative(0, 2, -1))) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.SOUTH)
        && this.isStationBlockNear(currentBlock.getRelative(0, 2, 1))) {
      return true;
    }

    return false;
  }
}
