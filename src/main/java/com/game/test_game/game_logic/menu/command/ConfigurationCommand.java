package com.game.test_game.game_logic.menu.command;

import com.game.test_game.game_logic.menu.GameMenuHud;
import com.game.test_game.game_logic.menu.command.exception.CommandException;

class ConfigurationCommand implements Command{

    @Override
    public void execute() throws CommandException {
        try {
            GameMenuHud.setMainMenu(false);
        } catch (Exception e) {
            throw  new CommandException(e, "Configuration Command Exception");
        }
    }

}
