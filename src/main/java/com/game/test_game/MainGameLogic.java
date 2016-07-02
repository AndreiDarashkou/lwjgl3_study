package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.test_game.game_logic.GameLogicType;

public class MainGameLogic implements GameLogic {

    public static GameState gameState = GameState.MENU;
    public static GameLogic currentGameLogic = GameLogicType.MENU;

    @Override
    public void init(Window window) throws Exception {
        currentGameLogic.init(window);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        currentGameLogic.input(window, mouseInput);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        currentGameLogic.update(interval, mouseInput);
    }

    @Override
    public void render() {
        currentGameLogic.render();
    }

    @Override
    public void cleanup() {
        currentGameLogic.cleanup();
    }

    public static void changeGameLogic(Window window) throws Exception {
        switch (gameState) {
            case MENU:
                setGameLogic(GameLogicType.MENU, window);
                break;
            case RACE:
                setGameLogic(GameLogicType.RACE, window);
                break;
            case GARAGE:
                setGameLogic(GameLogicType.GARAGE, window);
                break;
            case EXIT:
                window.close();
                break;
        }
    }

    private static void setGameLogic(GameLogicType logic, Window window) throws Exception {
        if (currentGameLogic != logic) {
            currentGameLogic.cleanup();
            currentGameLogic = logic;
            currentGameLogic.init(window);
        }
    }


}
