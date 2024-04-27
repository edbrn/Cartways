package com.edbrn.Cartways.listeners;

import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DeployCartButtonListenerTest {
  @Test
  public void testDeployCartWithButton() {
    Logger logger = Mockito.mock(Logger.class);

    PlayerInteractEvent event = Mockito.mock(PlayerInteractEvent.class);

    Block clickedBlock = Mockito.mock(Block.class);
    Mockito.when(event.getClickedBlock()).thenReturn(clickedBlock);
    Mockito.when(clickedBlock.getType()).thenReturn(Material.OAK_BUTTON);

    Block signBlock = Mockito.mock(Block.class);
    Mockito.when(clickedBlock.getRelative(BlockFace.UP)).thenReturn(signBlock);
    Mockito.when(signBlock.getType()).thenReturn(Material.OAK_WALL_SIGN);

    Sign blockState = Mockito.mock(Sign.class);

    SignSide signFront = Mockito.mock(SignSide.class);
    Mockito.when(signBlock.getState()).thenReturn(blockState);
    Mockito.when(blockState.getSide(Side.FRONT)).thenReturn(signFront);
    Mockito.when(signFront.getLine(0)).thenReturn("[STATION]");

    Location blockLocation = Mockito.mock(Location.class);
    Mockito.when(clickedBlock.getLocation()).thenReturn(blockLocation);

    Location railLocation = Mockito.mock(Location.class);
    Mockito.when(blockLocation.add(0, -1, 0)).thenReturn(railLocation);

    Player player = Mockito.mock(Player.class);
    Mockito.when(event.getPlayer()).thenReturn(player);

    World world = Mockito.mock(World.class);
    Mockito.when(player.getWorld()).thenReturn(world);

    Minecart minecart = Mockito.mock(Minecart.class);
    Mockito.when(world.spawn(Mockito.any(Location.class), Mockito.any())).thenReturn(minecart);

    DeployCartButtonListener listener =
        new DeployCartButtonListener(logger, Mockito.mock(BukkitScheduler.class));
    listener.onPlayerInteract(event);

    Mockito.verify(minecart).addPassenger(player);
    Mockito.verify(minecart).setVelocity(new Vector(0, 0, -2));
    Mockito.verify(minecart).setMaxSpeed(0.3);
  }

  @Test
  public void testDeployCartDoesNothingIfSignNotPresent() {
    Logger logger = Mockito.mock(Logger.class);

    PlayerInteractEvent event = Mockito.mock(PlayerInteractEvent.class);

    Block clickedBlock = Mockito.mock(Block.class);
    Mockito.when(event.getClickedBlock()).thenReturn(clickedBlock);
    Mockito.when(clickedBlock.getType()).thenReturn(Material.OAK_BUTTON);

    Block signBlock = Mockito.mock(Block.class);
    Mockito.when(clickedBlock.getRelative(BlockFace.UP)).thenReturn(signBlock);
    Mockito.when(signBlock.getType()).thenReturn(Material.STONE);

    Sign blockState = Mockito.mock(Sign.class);

    SignSide signFront = Mockito.mock(SignSide.class);
    Mockito.when(signBlock.getState()).thenReturn(blockState);
    Mockito.when(blockState.getSide(Side.FRONT)).thenReturn(signFront);
    Mockito.when(signFront.getLine(0)).thenReturn("[STATION]");

    Location blockLocation = Mockito.mock(Location.class);
    Mockito.when(clickedBlock.getLocation()).thenReturn(blockLocation);

    Location railLocation = Mockito.mock(Location.class);
    Mockito.when(blockLocation.add(0, -1, 0)).thenReturn(railLocation);

    Player player = Mockito.mock(Player.class);
    Mockito.when(event.getPlayer()).thenReturn(player);

    World world = Mockito.mock(World.class);
    Mockito.when(player.getWorld()).thenReturn(world);

    Minecart minecart = Mockito.mock(Minecart.class);
    Mockito.when(world.spawn(Mockito.any(Location.class), Mockito.any())).thenReturn(minecart);

    DeployCartButtonListener listener =
        new DeployCartButtonListener(logger, Mockito.mock(BukkitScheduler.class));
    listener.onPlayerInteract(event);

    Mockito.verify(world, Mockito.times(0)).spawn(Mockito.any(Location.class), Mockito.any());
  }

  @Test
  public void testDeployCartDoesNothingIfSignMisconfigured() {
    Logger logger = Mockito.mock(Logger.class);

    PlayerInteractEvent event = Mockito.mock(PlayerInteractEvent.class);

    Block clickedBlock = Mockito.mock(Block.class);
    Mockito.when(event.getClickedBlock()).thenReturn(clickedBlock);
    Mockito.when(clickedBlock.getType()).thenReturn(Material.OAK_BUTTON);

    Block signBlock = Mockito.mock(Block.class);
    Mockito.when(clickedBlock.getRelative(BlockFace.UP)).thenReturn(signBlock);
    Mockito.when(signBlock.getType()).thenReturn(Material.OAK_WALL_SIGN);

    Sign blockState = Mockito.mock(Sign.class);

    SignSide signFront = Mockito.mock(SignSide.class);
    Mockito.when(signBlock.getState()).thenReturn(blockState);
    Mockito.when(blockState.getSide(Side.FRONT)).thenReturn(signFront);
    Mockito.when(signFront.getLine(0)).thenReturn("[OTHER]");

    Location blockLocation = Mockito.mock(Location.class);
    Mockito.when(clickedBlock.getLocation()).thenReturn(blockLocation);

    Location railLocation = Mockito.mock(Location.class);
    Mockito.when(blockLocation.add(0, -1, 0)).thenReturn(railLocation);

    Player player = Mockito.mock(Player.class);
    Mockito.when(event.getPlayer()).thenReturn(player);

    World world = Mockito.mock(World.class);
    Mockito.when(player.getWorld()).thenReturn(world);

    Minecart minecart = Mockito.mock(Minecart.class);
    Mockito.when(world.spawn(Mockito.any(Location.class), Mockito.any())).thenReturn(minecart);

    DeployCartButtonListener listener =
        new DeployCartButtonListener(logger, Mockito.mock(BukkitScheduler.class));
    listener.onPlayerInteract(event);

    Mockito.verify(world, Mockito.times(0)).spawn(Mockito.any(Location.class), Mockito.any());
  }
}
