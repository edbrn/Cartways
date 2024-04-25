package com.edbrn.Cartways;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
  @Override
  public void onEnable() {
    this.getLogger().info("[Cartways] Plugin enabled");
  }

  @Override
  public void onDisable() {
    this.getLogger().info("[Cartways] Plugin disabled");
  }
}
