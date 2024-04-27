package com.edbrn.Cartways;

import com.edbrn.Cartways.listeners.DeployCartButtonListener;
import com.edbrn.Cartways.listeners.MinecartSpeedControl;
import org.bukkit.Bukkit;
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
            new DeployCartButtonListener(this.getLogger(), Bukkit.getScheduler()), this);
    server.getPluginManager().registerEvents(new MinecartSpeedControl(this.getLogger()), this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Cartways] Plugin disabled");
  }
}
