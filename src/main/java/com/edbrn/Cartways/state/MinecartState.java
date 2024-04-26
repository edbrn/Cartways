package com.edbrn.Cartways.state;

import org.bukkit.entity.Minecart;

public class MinecartState {
    private Minecart instance;

    public MinecartState(Minecart minecart) {
        this.instance = minecart;
    }

    public Minecart getInstance() {
        return this.instance;
    }
}
