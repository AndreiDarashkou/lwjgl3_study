package com.game.test_game.game_logic.menu.command;

import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.menu.command.exception.CommandException;

class ExitCommand implements Command{

    @Override
    public void execute() throws CommandException {
        try {
            MainGameLogic.INSTANCE.updateGameState(GameState.EXIT);
        } catch (Exception e) {
            throw  new CommandException(e, "Exit Command Exception");
        }
    }
}