package com.game.engine;

import com.game.engine.input.MouseInput;

public interface GameLogic {

    void init(Window window) throws Exception;
    void input(Window window, MouseInput mouseInput) throws Exception;
    void update(float interval, MouseInput mouseInput) throws Exception;
    void render();
    void cleanup();
}
