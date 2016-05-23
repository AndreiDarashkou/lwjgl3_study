package com.game;

import com.game.logic.DummyGame;
import com.game.logic.GameLogic;

public class Main {

    private static long windowHandler;

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            GameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }

//    public static void main2(String[] args) {
//        Window window = new Window();
//        windowHandler = window.init();
//
//        try {
//            loop();
//
//            glfwDestroyWindow(windowHandler);
//            glfwFreeCallbacks(windowHandler);
//        } finally {
//            glfwTerminate();
//            glfwSetErrorCallback(null).free();
//        }
//    }

}
