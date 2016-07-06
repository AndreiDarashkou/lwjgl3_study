package com.game.test_game.game_logic;

import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.test_game.game_logic.menu.GameMenuHud;
import com.game.test_game.game_logic.menu.MenuHud;

class MenuGameLogic extends AbstractGameLogic {

    private GameMenuHud gameMenuHud;

    @Override
    public void init(Window window) throws Exception {
        super.init(window);
        gameMenuHud = new GameMenuHud();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        gameMenuHud.update(window, mouseInput);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        gameMenuHud.updateSize(window);
    }

    @Override
    public void cleanup() {
        gameMenuHud.cleanup();
    }

}
