package com.game.test_game.game_logic;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.test_game.MainGameLogic;
import com.game.test_game.menu.MenuHud;

class MenuGameLogic extends AbstractGameLogic {
    private MenuHud menuHud;

    @Override
    public void init(Window window) throws Exception {
        super.init(window);
        menuHud = new MenuHud();
        setupLights();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        menuHud.update(window, mouseInput);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        menuHud.update(window, mouseInput);
    }

    @Override
    public void render() {
        menuHud.updateSize(window);
        renderer.render(window, camera, scene, menuHud);
    }

    @Override
    public void cleanup() {
        menuHud.cleanup();
    }

}
