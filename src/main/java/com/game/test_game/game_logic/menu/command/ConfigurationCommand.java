package com.game.test_game.game_logic.menu.command;

import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.menu.GameMenuHud;

class ConfigurationCommand implements Command{

    @Override
    public void execute() {
        GameMenuHud.setMainMenu(false);
    }

}
