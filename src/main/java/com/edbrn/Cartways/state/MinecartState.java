package com.edbrn.Cartways.state;

import com.edbrn.Cartways.App;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class MinecartState {
  private Minecart instance;
  private BukkitScheduler scheduler;

  private boolean isStopped = false;
  private boolean hasRecentlyStopped = false;

  private static final double DEFAULT_SPEED = 0.4;
  private static final double DEFAULT_STARTUP_SPEED = 0.25;

  public MinecartState(Minecart minecart) {
    this.instance = minecart;
    this.scheduler = Bukkit.getScheduler();
  }

  public MinecartState(Minecart minecart, BukkitScheduler scheduler) {
    this.instance = minecart;
    this.scheduler = scheduler;
  }

  public Minecart getInstance() {
    return this.instance;
  }

  public boolean shouldMinecartMove() {
    return !this.isStopped;
  }

  public boolean shouldStop() {
    return !this.isStopped && !this.hasRecentlyStopped && this.isAtStation();
  }

  public void moveBackward() {
    this.instance.setVelocity(this.instance.getFacing().getOppositeFace().getDirection());
  }

  public void stopBreifly() {
    this.stopMoving();

    isStopped = true;
    MinecartState self = this;
    self.scheduler.scheduleSyncDelayedTask(
        JavaPlugin.getPlugin(App.class),
        new Runnable() {
          public void run() {
            self.hasRecentlyStopped = true;
            self.isStopped = false;

            if (self.isAtEndOfLine()) {
              self.moveBackward();
            } else {
              self.instance.setMaxSpeed(MinecartState.DEFAULT_STARTUP_SPEED);
              self.move();
            }

            self.scheduler.scheduleSyncDelayedTask(
                JavaPlugin.getPlugin(App.class),
                new Runnable() {
                  public void run() {
                    self.instance.setMaxSpeed(MinecartState.DEFAULT_SPEED);
                    self.hasRecentlyStopped = false;
                  }
                },
                40);
          }
        },
        90);
  }

  private double getNewSpeedFromSignalSignOrCurrentSpeed(Block block) {
    if (block.getType().equals(Material.OAK_SIGN)) {
      Sign sign = (Sign) block.getState();
      SignSide side = sign.getSide(Side.FRONT);
      if (side.getLine(0).equals("[SPEED SIGNAL]")) {
        String newSpeed = side.getLine(1);
        try {
          Double newSpeedDouble = Double.parseDouble(newSpeed);
          if (newSpeedDouble > 1) {
            return this.instance.getMaxSpeed();
          }

          return newSpeedDouble;
        } catch (NumberFormatException e) {
          return this.instance.getMaxSpeed();
        }
      }
    }

    return this.instance.getMaxSpeed();
  }

  public double getSpeed() {
    Block currentBlock = this.instance.getWorld().getBlockAt(this.instance.getLocation());
    return this.getNewSpeedFromSignalSignOrCurrentSpeed(currentBlock.getRelative(0, -2, 0));
  }

  public void move() {
    Block railUnder = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (this.isStopped || !railUnder.getType().equals(Material.RAIL)) {
      this.instance.setVelocity(new Vector(0, 0, 0));
      return;
    }

    this.instance.setMaxSpeed(getSpeed());

    Rail rail = (Rail) railUnder.getBlockData();
    Shape railShape = rail.getShape();
    BlockFace facing = this.instance.getFacing();

    if (facing.equals(BlockFace.NORTH)) {
      if (railShape.equals(Shape.SOUTH_EAST)) {
        this.instance.setVelocity(new Vector(1, 0, -1));
      } else if (railShape.equals(Shape.SOUTH_WEST)) {
        this.instance.setVelocity(new Vector(-1, 0, -1));
      } else if (railShape.equals(Shape.NORTH_SOUTH)) {
        this.instance.setVelocity(new Vector(0, 0, -1));
      }
    } else if (facing.equals(BlockFace.SOUTH)) {
      if (railShape.equals(Shape.SOUTH_WEST)) {
        this.instance.setVelocity(new Vector(1, 0, 1));
      } else if (railShape.equals(Shape.NORTH_WEST)) {
        this.instance.setVelocity(new Vector(-1, 0, 1));
      } else if (railShape.equals(Shape.NORTH_EAST)) {
        this.instance.setVelocity(new Vector(1, 0, 1));
      } else if (railShape.equals(Shape.NORTH_SOUTH)) {
        this.instance.setVelocity(new Vector(0, 0, 1));
      }
    } else if (facing.equals(BlockFace.EAST)) {
      if (railShape.equals(Shape.SOUTH_EAST)) {
        this.instance.setVelocity(new Vector(1, 0, -1));
      } else if (railShape.equals(Shape.SOUTH_WEST)) {
        this.instance.setVelocity(new Vector(1, 0, 1));
      } else if (railShape.equals(Shape.NORTH_WEST)) {
        this.instance.setVelocity(new Vector(1, 0, -1));
      } else if (railShape.equals(Shape.EAST_WEST)) {
        this.instance.setVelocity(new Vector(1, 0, 0));
      }
    } else if (facing.equals(BlockFace.WEST)) {
      if (railShape.equals(Shape.NORTH_EAST)) {
        this.instance.setVelocity(new Vector(-1, 0, -1));
      } else if (railShape.equals(Shape.SOUTH_EAST)) {
        this.instance.setVelocity(new Vector(-1, 0, 1));
      } else if (railShape.equals(Shape.EAST_WEST)) {
        this.instance.setVelocity(new Vector(-1, 0, 0));
      }
    }
  }

  public void stopMoving() {
    this.instance.setVelocity(new Vector(0, 0, 0));
  }

  private boolean isStationSign(Block block) {
    if (block.getType().equals(Material.OAK_SIGN)) {
      Sign sign = (Sign) block.getState();
      if (sign.getSide(Side.FRONT).getLine(0).equals("[STATION]")) {
        return true;
      }
    }

    return false;
  }

  public boolean isAtStation() {
    Block currentBlock = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (this.isStationSign(currentBlock.getRelative(0, -2, 0))) {
      return true;
    }

    return false;
  }

  public boolean isAtEndOfLine() {
    Block currentBlock = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (this.instance.getFacing().equals(BlockFace.EAST)
        && !currentBlock.getRelative(2, 0, 0).getType().equals(Material.RAIL)) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.WEST)
        && !currentBlock.getRelative(-2, 0, 0).getType().equals(Material.RAIL)) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.NORTH)
        && !currentBlock.getRelative(0, 0, -2).getType().equals(Material.RAIL)) {
      return true;
    }

    if (this.instance.getFacing().equals(BlockFace.SOUTH)
        && !currentBlock.getRelative(0, 0, 2).getType().equals(Material.RAIL)) {
      return true;
    }

    return false;
  }
}
