package com.game.model;

public enum GameStatus {
    MENU,
    PLAY,
    PAUSE,
    MOVIE;

    private static GameStatus gameState = GameStatus.MENU;
}
