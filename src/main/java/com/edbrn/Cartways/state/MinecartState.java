package com.edbrn.Cartways.state;

import com.edbrn.Cartways.App;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Minecart;
import org.bukkit.material.Rails;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class MinecartState {
  private Minecart instance;
  private boolean isStopped = false;
  private boolean hasRecentlyStopped = false;

  private final static double DEFAULT_SPEED = 0.4;
  private final static double DEFAULT_STARTUP_SPEED = 0.25;

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
    return !this.isStopped && !this.hasRecentlyStopped && this.isAtStation();
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
                    self.instance.setMaxSpeed(MinecartState.DEFAULT_STARTUP_SPEED);
                  self.move();
                }

                Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(
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
    if (block.getType().equals(Material.OAK_WALL_SIGN)) {
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

    if (this.instance.getFacing().equals(BlockFace.WEST)) {
      return this.getNewSpeedFromSignalSignOrCurrentSpeed(
          currentBlock.getRelative(BlockFace.SOUTH));
    } else if (this.instance.getFacing().equals(BlockFace.NORTH)) {
      return this.getNewSpeedFromSignalSignOrCurrentSpeed(currentBlock.getRelative(BlockFace.WEST));
    } else if (this.instance.getFacing().equals(BlockFace.SOUTH)) {
      return this.getNewSpeedFromSignalSignOrCurrentSpeed(currentBlock.getRelative(BlockFace.EAST));
    } else if (this.instance.getFacing().equals(BlockFace.EAST)) {
      return this.getNewSpeedFromSignalSignOrCurrentSpeed(
          currentBlock.getRelative(BlockFace.NORTH));
    }

    return this.instance.getMaxSpeed();
  }

  public void move() {
    Block railUnder = this.instance.getWorld().getBlockAt(this.instance.getLocation());

    if (this.isStopped || !railUnder.getType().equals(Material.RAIL)) {
        this.instance.setVelocity(new Vector(0, 0, 0));
        return;
    }

    Rail rail = (Rail) railUnder.getBlockData();
    Shape railShape = rail.getShape();

    BlockFace facing = this.instance.getFacing();

    this.instance.setMaxSpeed(0.1);

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
    
    // this.instance.setMaxSpeed(getSpeed());
    // this.instance.setVelocity(this.instance.getFacing().getDirection());
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
