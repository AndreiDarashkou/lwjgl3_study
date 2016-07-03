package com.game.test_game.game_logic.menu.command;

import com.game.test_game.game_logic.menu.GameMenuHud;
import com.game.test_game.game_logic.menu.command.exception.CommandException;

class BackCommand implements Command {

    @Override
    public void execute() throws CommandException {
        try {
            GameMenuHud.setMainMenu(true);
        } catch (Exception e) {
            throw  new CommandException(e, "Back to main menu Command Exception");
        }
    }

}
