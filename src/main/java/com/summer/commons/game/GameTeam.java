package com.summer.commons.game;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class GameTeam {

    private List<Player> members;

    public GameTeam() {
        this.members = new ArrayList<>();
    }

}
