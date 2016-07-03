package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.test_game.game_logic.GameLogicType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class MainGameLogic implements GameLogic {

    public static final MainGameLogic INSTANCE = new MainGameLogic();

    private GameState gameState = GameState.MENU;
    private GameLogic currentGameLogic = GameLogicType.MENU;

    private Window window;

    private MainGameLogic() {
    }

    @Override
    public void init(Window window) throws Exception {
        this.window = window;
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

    public void updateGameState(GameState state) throws Exception {
        gameState = state;
        updateGameLogic();
    }

    private void updateGameLogic() throws Exception {
        switch (gameState) {
            case MENU:
                setGameLogic(GameLogicType.MENU);
                break;
            case RACE:
                setGameLogic(GameLogicType.RACE);
                break;
            case GARAGE:
                setGameLogic(GameLogicType.GARAGE);
                break;
            case EXIT:
                window.close();
                break;
        }
    }

    private void setGameLogic(GameLogicType logic) throws Exception {
        if (currentGameLogic != logic) {
            currentGameLogic.cleanup();
            currentGameLogic = logic;
            currentGameLogic.init(window);
        }
    }


}
