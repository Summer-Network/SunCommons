package com.summer.commons.game;

public enum GameState {
    WAITING, STARTING, INGAME, CLOSED;

    public boolean canJoin() {
        return this == WAITING;
    }
}