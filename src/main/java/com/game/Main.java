package com.game;

import com.game.engine.GameEngine;
import com.game.engine.GameLogic;
import com.game.model.DummyGame;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEngine.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }

}
