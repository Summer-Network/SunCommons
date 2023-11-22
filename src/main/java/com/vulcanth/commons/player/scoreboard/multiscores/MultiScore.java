package com.vulcanth.commons.player.scoreboard.multiscores;

import java.util.List;

public abstract class MultiScore {

    private final int updateTime;
    private int index;
    private final int maxIndex;

    public MultiScore(int updateTime, int maxIndex) {
        this.updateTime = updateTime;
        this.index = 0;
        this.maxIndex = maxIndex;
    }

    public int getUpdateTime() {
        return this.updateTime;
    }

    public void changeScoreboard() {
        index++;
        if (maxIndex < index) {
            this.index = 0;
        }
    }

    public List<String> getScoreboardNow() {
        return getScoreboardLines(index);
    }

    public int getIndex() {
        return this.index;
    }

    public abstract List<String> getScoreboardLines(int index);
}
