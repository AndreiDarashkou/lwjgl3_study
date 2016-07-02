package com.game;

import com.game.engine.GameEngine;
import com.game.engine.GameLogic;
import com.game.test_game.MainGameLogic;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new MainGameLogic();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            GameEngine gameEngine = new GameEngine("GAME", 1024, 768, vSync, gameLogic);
            gameEngine.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }

}
