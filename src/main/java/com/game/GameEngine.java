package com.game;

import com.game.input.KeyboardHandler;
import com.game.input.MouseHandler;
import com.game.logic.GameLogic;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.opengl.GL11.*;

public class GameEngine implements Runnable {

    private static final double MS_PER_UPDATE = 1000f/50;

    private final Thread gameLoopThread;
    private GameLogic gameLogic;
    Window window;

    public GameEngine(String windowTitle, int width, int height, boolean vsSync, GameLogic gameLogic) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vsSync);
        this.gameLogic = gameLogic;
    }

    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            //init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }


    private void gameLoop() {
        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        double previous = System.currentTimeMillis();
        double steps = 0;
        while (!glfwWindowShouldClose(window.getWindowHandler())) {
            double current = System.currentTimeMillis();
            double elapsed = current - previous;
            previous = current;
            steps += elapsed;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window.getWindowHandler()); //смена кадров (текущего и готового в буфере за прошлый пробег)

            glfwPollEvents();
            while (steps >= MS_PER_UPDATE) {
                updateGameState();
                steps -= MS_PER_UPDATE;
            }

            //render();
            sync(current);
        }

    }
    public static void updateGameState(){
        if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE))
            System.out.println("Space Key Pressed");
        if(KeyboardHandler.isKeyDown(GLFW_KEY_A))
            System.out.println("A Key Pressed");
        System.out.println(MouseHandler.getPosition());
    }


    //если быстрее чем надо притормаживаем (заменить на v_sync)
    private static void sync(double loopStartTime) {
        float loopSlot = 1000f / 50;
        double endTime = loopStartTime + loopSlot;
        while(System.currentTimeMillis() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {}
        }
    }

}
