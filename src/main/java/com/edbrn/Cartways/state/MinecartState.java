package com.edbrn.Cartways.state;

import org.bukkit.Bukkit;
import org.bukkit.entity.Minecart;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.edbrn.Cartways.App;

public class MinecartState {
    private Minecart instance;
    private boolean isAtStation = false;

    public MinecartState(Minecart minecart) {
        this.instance = minecart;
    }

    public Minecart getInstance() {
        return this.instance;
    }

    public boolean getIsAtStation() {
        return isAtStation;
    }

    public void setLeftStation() {
        isAtStation = false;
    }

    public void setReachedStation() {
        isAtStation = true;
        MinecartState self = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(App.class), new Runnable() {
            public void run() {
                Vector direction = self.instance.getFacing().getDirection();
                self.instance.setVelocity(new Vector(direction.getX(), direction.getY(), direction.getZ()));
                
                Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(App.class), new Runnable() {
                    public void run() {
                        self.setLeftStation();
                    }
                }, 20);
            }
        }, 90);
    }
}
