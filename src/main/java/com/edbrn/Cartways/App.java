package com.edbrn.Cartways;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import com.edbrn.Cartways.listeners.DeployCartButtonListener;
import com.edbrn.Cartways.listeners.MinecartSpeedControl;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Cartways] Plugin enabled");

    Server server = this.getServer();

    server.getPluginManager().registerEvents(new DeployCartButtonListener(this.getLogger()), this);
    server.getPluginManager().registerEvents(new MinecartSpeedControl(this.getLogger()), this);
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Cartways] Plugin disabled");
  }
}
