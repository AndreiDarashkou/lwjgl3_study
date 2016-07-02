package com.game.engine;

import com.game.engine.input.MouseInput;

public class GameEngine implements Runnable {

    private static final int TARGET_FPS = 60;
    private static final int TARGET_UPS = 60;

    private final Thread gameLoopThread;
    private final GameLogic gameLogic;
    private final Window window;
    private final Timer timer;
    private MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, GameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        gameLogic.init(window);
        mouseInput.init(window);
    }


    private void gameLoop() throws Exception {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        while (!window.windowShouldClose()) {
          // System.gc();
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            //System.out.println(1/elapsedTime);

            if (!window.isVSync()) {
                sync();
            }
        }
    }

    private void input() throws Exception {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    private void update(float interval) throws Exception {
        gameLogic.update(interval, mouseInput);
    }


    private void render() {
        gameLogic.render();
        window.update();
    }

    private void cleanup() {
        gameLogic.cleanup();
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

}
