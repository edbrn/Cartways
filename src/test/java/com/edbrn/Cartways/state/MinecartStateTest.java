package com.edbrn.Cartways.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Minecart;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MinecartStateTest {
  private Minecart setUpMinecartAndRailwayWithSpeedSign(
      BlockFace minecartDirection, Shape railShape) {
    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Block blockUnderRail = Mockito.mock(Block.class);
    Material material = Material.RAIL;

    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    Rail railBlockData = Mockito.mock(Rail.class);
    Mockito.when(blockUnderMinecart.getBlockData()).thenReturn(railBlockData);

    Mockito.when(railBlockData.getShape()).thenReturn(railShape);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);
    Mockito.when(blockUnderMinecart.getRelative(0, -2, 0)).thenReturn(blockUnderRail);

    Sign sign = Mockito.mock(Sign.class);
    Mockito.when(blockUnderRail.getType()).thenReturn(Material.OAK_SIGN);
    Mockito.when(blockUnderRail.getState()).thenReturn(sign);

    SignSide frontOfSign = Mockito.mock(SignSide.class);
    Mockito.when(sign.getSide(Side.FRONT)).thenReturn(frontOfSign);
    Mockito.when(frontOfSign.getLine(0)).thenReturn("[SPEED SIGNAL]");
    Mockito.when(frontOfSign.getLine(1)).thenReturn("0.42");

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);
    Mockito.when(minecart.getFacing()).thenReturn(minecartDirection);

    return minecart;
  }

  @Test
  public void testGetInstance() {
    Minecart minecart = Mockito.mock(Minecart.class);
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);
    MinecartState minecartState = new MinecartState(minecart, scheduler);
    assertEquals(minecartState.getInstance(), minecart);
  }

  @Test
  public void testMoveStopsIfBlockBelowIsNotRail() {
    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Material material = Material.GRASS_BLOCK;
    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(0, 0, 0));
  }

  @Test
  public void testHeadingNorthOnSouthEastTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.NORTH, Shape.SOUTH_EAST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, -1));
  }

  @Test
  public void testHeadingNorthOnSouthWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.NORTH, Shape.SOUTH_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(-1, 0, -1));
  }

  @Test
  public void testHeadingNorthOnNorthSouthTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.NORTH, Shape.NORTH_SOUTH);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(0, 0, -1));
  }

  @Test
  public void testHeadingSouthOnSouthWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.SOUTH, Shape.SOUTH_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, 1));
  }

  @Test
  public void testHeadingSouthOnNorthWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.SOUTH, Shape.NORTH_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(-1, 0, 1));
  }

  @Test
  public void testHeadingSouthOnNorthEastTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.SOUTH, Shape.NORTH_EAST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, 1));
  }

  @Test
  public void testHeadingSouthOnNorthSouthTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart =
        this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.SOUTH, Shape.NORTH_SOUTH);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(0, 0, 1));
  }

  @Test
  public void testHeadingEastOnSouthEastTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.EAST, Shape.SOUTH_EAST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, -1));
  }

  @Test
  public void testHeadingEastOnSouthWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.EAST, Shape.SOUTH_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, 1));
  }

  @Test
  public void testHeadingEastOnNorthWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.EAST, Shape.NORTH_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, -1));
  }

  @Test
  public void testHeadingEastOnEastWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.EAST, Shape.EAST_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(1, 0, 0));
  }

  @Test
  public void testHeadingWestOnNorthEastTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.WEST, Shape.NORTH_EAST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(-1, 0, -1));
  }

  @Test
  public void testHeadingWestOnSouthEastTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.WEST, Shape.SOUTH_EAST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(-1, 0, 1));
  }

  @Test
  public void testHeadingWestOnEastWestTrack() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.WEST, Shape.EAST_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setVelocity(new Vector(-1, 0, 0));
  }

  @Test
  public void testMaxSpeedUpdatedWhenPassingSignOnLeft() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = this.setUpMinecartAndRailwayWithSpeedSign(BlockFace.WEST, Shape.EAST_WEST);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    minecartState.move();

    Mockito.verify(minecart).setMaxSpeed(0.42);
  }

  @Test
  public void testMaxSpeedRemainsTheSameWhenNoSignPresent() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Block blockUnderRail = Mockito.mock(Block.class);
    Material material = Material.RAIL;

    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    Rail railBlockData = Mockito.mock(Rail.class);
    Mockito.when(blockUnderMinecart.getBlockData()).thenReturn(railBlockData);

    Mockito.when(railBlockData.getShape()).thenReturn(Shape.SOUTH_EAST);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);
    Mockito.when(blockUnderMinecart.getRelative(0, -2, 0)).thenReturn(blockUnderRail);

    Sign sign = Mockito.mock(Sign.class);
    Mockito.when(blockUnderRail.getType()).thenReturn(Material.OAK_WALL_SIGN);
    Mockito.when(blockUnderRail.getState()).thenReturn(sign);

    SignSide frontOfSign = Mockito.mock(SignSide.class);
    Mockito.when(sign.getSide(Side.FRONT)).thenReturn(frontOfSign);
    Mockito.when(frontOfSign.getLine(0)).thenReturn("[OTHER]");

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);
    Mockito.when(minecart.getFacing()).thenReturn(BlockFace.NORTH);
    Mockito.when(minecart.getMaxSpeed()).thenReturn(0.2);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    Double speed = minecartState.getSpeed();

    assertEquals(speed, 0.2);
  }

  @Test
  public void testMaxSpeedRemainsSameWhenSignHasInvalidDouble() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Block blockUnderRail = Mockito.mock(Block.class);
    Material material = Material.RAIL;

    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    Rail railBlockData = Mockito.mock(Rail.class);
    Mockito.when(blockUnderMinecart.getBlockData()).thenReturn(railBlockData);

    Mockito.when(railBlockData.getShape()).thenReturn(Shape.SOUTH_EAST);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);
    Mockito.when(blockUnderMinecart.getRelative(0, -2, 0)).thenReturn(blockUnderRail);

    Sign sign = Mockito.mock(Sign.class);
    Mockito.when(blockUnderRail.getType()).thenReturn(Material.OAK_WALL_SIGN);
    Mockito.when(blockUnderRail.getState()).thenReturn(sign);

    SignSide frontOfSign = Mockito.mock(SignSide.class);
    Mockito.when(sign.getSide(Side.FRONT)).thenReturn(frontOfSign);
    Mockito.when(frontOfSign.getLine(0)).thenReturn("[SPEED SIGNAL]");
    Mockito.when(frontOfSign.getLine(1)).thenReturn("invalid");

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);
    Mockito.when(minecart.getFacing()).thenReturn(BlockFace.NORTH);
    Mockito.when(minecart.getMaxSpeed()).thenReturn(0.2);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    Double speed = minecartState.getSpeed();

    assertEquals(speed, 0.2);
  }

  @Test
  public void testMaxSpeedRemainsSameWhenSignHasNumberHigherThanOne() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Block blockUnderRail = Mockito.mock(Block.class);
    Material material = Material.RAIL;

    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    Rail railBlockData = Mockito.mock(Rail.class);
    Mockito.when(blockUnderMinecart.getBlockData()).thenReturn(railBlockData);

    Mockito.when(railBlockData.getShape()).thenReturn(Shape.SOUTH_EAST);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);
    Mockito.when(blockUnderMinecart.getRelative(0, -2, 0)).thenReturn(blockUnderRail);

    Sign sign = Mockito.mock(Sign.class);
    Mockito.when(blockUnderRail.getType()).thenReturn(Material.OAK_WALL_SIGN);
    Mockito.when(blockUnderRail.getState()).thenReturn(sign);

    SignSide frontOfSign = Mockito.mock(SignSide.class);
    Mockito.when(sign.getSide(Side.FRONT)).thenReturn(frontOfSign);
    Mockito.when(frontOfSign.getLine(0)).thenReturn("[SPEED SIGNAL]");
    Mockito.when(frontOfSign.getLine(1)).thenReturn("2");

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);
    Mockito.when(minecart.getFacing()).thenReturn(BlockFace.NORTH);
    Mockito.when(minecart.getMaxSpeed()).thenReturn(0.2);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    Double speed = minecartState.getSpeed();

    assertEquals(speed, 0.2);
  }

  @Test
  public void testMaxSpeedReturnedWhenValid() {
    BukkitScheduler scheduler = Mockito.mock(BukkitScheduler.class);

    Minecart minecart = Mockito.mock(Minecart.class);
    Location minecartLocation = Mockito.mock(Location.class);

    Block blockUnderMinecart = Mockito.mock(Block.class);
    Block blockUnderRail = Mockito.mock(Block.class);
    Material material = Material.RAIL;

    Mockito.when(blockUnderMinecart.getType()).thenReturn(material);

    Rail railBlockData = Mockito.mock(Rail.class);
    Mockito.when(blockUnderMinecart.getBlockData()).thenReturn(railBlockData);

    Mockito.when(railBlockData.getShape()).thenReturn(Shape.SOUTH_EAST);

    World world = Mockito.mock(World.class);
    Mockito.when(world.getBlockAt(minecartLocation)).thenReturn(blockUnderMinecart);
    Mockito.when(blockUnderMinecart.getRelative(0, -2, 0)).thenReturn(blockUnderRail);

    Sign sign = Mockito.mock(Sign.class);
    Mockito.when(blockUnderRail.getType()).thenReturn(Material.OAK_SIGN);
    Mockito.when(blockUnderRail.getState()).thenReturn(sign);

    SignSide frontOfSign = Mockito.mock(SignSide.class);
    Mockito.when(sign.getSide(Side.FRONT)).thenReturn(frontOfSign);
    Mockito.when(frontOfSign.getLine(0)).thenReturn("[SPEED SIGNAL]");
    Mockito.when(frontOfSign.getLine(1)).thenReturn("0.5");

    Mockito.when(minecart.getWorld()).thenReturn(world);
    Mockito.when(minecart.getLocation()).thenReturn(minecartLocation);
    Mockito.when(minecart.getFacing()).thenReturn(BlockFace.NORTH);
    Mockito.when(minecart.getMaxSpeed()).thenReturn(0.2);

    MinecartState minecartState = new MinecartState(minecart, scheduler);
    Double speed = minecartState.getSpeed();

    assertEquals(speed, 0.5);
  }
}
