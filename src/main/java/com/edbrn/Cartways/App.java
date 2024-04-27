package com.edbrn.Cartways;

import com.edbrn.Cartways.listeners.DeployCartButtonListener;
import com.edbrn.Cartways.listeners.MinecartSpeedControlListener;
import com.edbrn.Cartways.state.SpeedManagedMinecarts;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Cartways] Plugin enabled");

    Server server = this.getServer();

    server
        .getPluginManager()
        .registerEvents(
            new DeployCartButtonListener(this.getLogger(), SpeedManagedMinecarts.getShared()),
            this);
    server
        .getPluginManager()
        .registerEvents(
            new MinecartSpeedControlListener(this.getLogger(), SpeedManagedMinecarts.getShared()),
            this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Cartways] Plugin disabled");
  }
}
