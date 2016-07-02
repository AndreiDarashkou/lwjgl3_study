package com.game.test_game.game_logic;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.input.MouseInput;

public enum GameLogicType implements GameLogic{
    MENU(new MenuGameLogic()),
    GARAGE(new GarageGameLogic()),
    RACE(new RaceGameLogic());

    GameLogic gameLogic;

    GameLogicType(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    @Override
    public void init(Window window) throws Exception {
        gameLogic.init(window);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        gameLogic.input(window, mouseInput);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        gameLogic.update(interval, mouseInput);
    }

    @Override
    public void render() {
        gameLogic.render();
    }

    @Override
    public void cleanup() {
        gameLogic.cleanup();
    }
}
