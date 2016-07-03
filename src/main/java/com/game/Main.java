package com.game;

import com.game.engine.GameEngine;
import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.GameLogicType;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameEngine gameEngine = new GameEngine("TEST GAME", 1024, 768, vSync, MainGameLogic.INSTANCE);
            gameEngine.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }

}
