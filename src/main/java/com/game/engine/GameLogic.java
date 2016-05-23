package com.game.engine;

import com.game.engine.Window;

public interface GameLogic {

    void init() throws Exception;
    void input(Window window);
    void update(float interval);
    void render(Window window);

}
