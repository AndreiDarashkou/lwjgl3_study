package com.game.logic;

import com.game.Window;

public interface GameLogic {

    void init() throws Exception;
    void input();
    void update(float interval);
    void render(Window window);

}
