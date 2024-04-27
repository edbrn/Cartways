package com.edbrn.Cartways.listeners;

import com.edbrn.Cartways.state.MinecartState;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MinecartSpeedControlTest {
  @Test
  public void testStopsWhenShouldStop() {
    Logger logger = Mockito.mock(Logger.class);
    SpeedManagedMinecarts speedManagedMinecarts = Mockito.mock(SpeedManagedMinecarts.class);

    VehicleMoveEvent event = Mockito.mock(VehicleMoveEvent.class);

    Minecart vehicle = Mockito.mock(Minecart.class);
    Mockito.when(event.getVehicle()).thenReturn(vehicle);
    Mockito.when(vehicle.getType()).thenReturn(EntityType.MINECART);
    Mockito.when(vehicle.getTicksLived()).thenReturn(25);

    UUID vehicleUuid = UUID.randomUUID();
    Mockito.when(vehicle.getUniqueId()).thenReturn(vehicleUuid);

    MinecartState minecartState = Mockito.mock(MinecartState.class);
    Mockito.when(speedManagedMinecarts.getMinecartState(vehicle)).thenReturn(minecartState);
    Mockito.when(minecartState.shouldStop()).thenReturn(true);

    MinecartSpeedControlListener listener =
        new MinecartSpeedControlListener(logger, speedManagedMinecarts);
    listener.onVehicleMove(event);

    Mockito.verify(minecartState, Mockito.times(1)).stopBreifly();
    Mockito.verify(minecartState, Mockito.times(1)).move();
  }

  @Test
  public void testDoesNotStopWhenShouldNot() {
    Logger logger = Mockito.mock(Logger.class);
    SpeedManagedMinecarts speedManagedMinecarts = Mockito.mock(SpeedManagedMinecarts.class);

    VehicleMoveEvent event = Mockito.mock(VehicleMoveEvent.class);

    Minecart vehicle = Mockito.mock(Minecart.class);
    Mockito.when(event.getVehicle()).thenReturn(vehicle);
    Mockito.when(vehicle.getType()).thenReturn(EntityType.MINECART);
    Mockito.when(vehicle.getTicksLived()).thenReturn(25);

    UUID vehicleUuid = UUID.randomUUID();
    Mockito.when(vehicle.getUniqueId()).thenReturn(vehicleUuid);

    MinecartState minecartState = Mockito.mock(MinecartState.class);
    Mockito.when(speedManagedMinecarts.getMinecartState(vehicle)).thenReturn(minecartState);
    Mockito.when(minecartState.shouldStop()).thenReturn(false);

    MinecartSpeedControlListener listener =
        new MinecartSpeedControlListener(logger, speedManagedMinecarts);
    listener.onVehicleMove(event);

    Mockito.verify(minecartState, Mockito.times(0)).stopBreifly();
    Mockito.verify(minecartState, Mockito.times(1)).move();
  }

  @Test
  public void testDoesNothingWhenMinecartIsVeryNew() {
    Logger logger = Mockito.mock(Logger.class);
    SpeedManagedMinecarts speedManagedMinecarts = Mockito.mock(SpeedManagedMinecarts.class);

    VehicleMoveEvent event = Mockito.mock(VehicleMoveEvent.class);

    Minecart vehicle = Mockito.mock(Minecart.class);
    Mockito.when(event.getVehicle()).thenReturn(vehicle);
    Mockito.when(vehicle.getType()).thenReturn(EntityType.MINECART);
    Mockito.when(vehicle.getTicksLived()).thenReturn(10);

    UUID vehicleUuid = UUID.randomUUID();
    Mockito.when(vehicle.getUniqueId()).thenReturn(vehicleUuid);

    MinecartState minecartState = Mockito.mock(MinecartState.class);
    Mockito.when(speedManagedMinecarts.getMinecartState(vehicle)).thenReturn(minecartState);
    Mockito.when(minecartState.shouldStop()).thenReturn(true);

    MinecartSpeedControlListener listener =
        new MinecartSpeedControlListener(logger, speedManagedMinecarts);
    listener.onVehicleMove(event);

    Mockito.verify(minecartState, Mockito.times(0)).stopBreifly();
    Mockito.verify(minecartState, Mockito.times(0)).move();
  }

  @Test
  public void testDoesNothingWhenVehicleIsNotPlainMinecart() {
    Logger logger = Mockito.mock(Logger.class);
    SpeedManagedMinecarts speedManagedMinecarts = Mockito.mock(SpeedManagedMinecarts.class);

    VehicleMoveEvent event = Mockito.mock(VehicleMoveEvent.class);

    Minecart vehicle = Mockito.mock(Minecart.class);
    Mockito.when(event.getVehicle()).thenReturn(vehicle);
    Mockito.when(vehicle.getType()).thenReturn(EntityType.MINECART_CHEST);
    Mockito.when(vehicle.getTicksLived()).thenReturn(25);

    UUID vehicleUuid = UUID.randomUUID();
    Mockito.when(vehicle.getUniqueId()).thenReturn(vehicleUuid);

    MinecartState minecartState = Mockito.mock(MinecartState.class);
    Mockito.when(speedManagedMinecarts.getMinecartState(vehicle)).thenReturn(minecartState);
    Mockito.when(minecartState.shouldStop()).thenReturn(true);

    MinecartSpeedControlListener listener =
        new MinecartSpeedControlListener(logger, speedManagedMinecarts);
    listener.onVehicleMove(event);

    Mockito.verify(minecartState, Mockito.times(0)).stopBreifly();
    Mockito.verify(minecartState, Mockito.times(0)).move();
  }
}
