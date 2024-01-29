package com.summer.commons.player.scoreboard.multiscores.model;

import java.util.Arrays;
import java.util.List;

public class ScoreboardModel {

    private final List<String> lines;

    public ScoreboardModel(String... lines) {
        this.lines = Arrays.asList(lines);
    }

    public List<String> getLines() {
        return this.lines;
    }
}
