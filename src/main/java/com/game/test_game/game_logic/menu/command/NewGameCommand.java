package com.game.test_game.game_logic.menu.command;

import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.menu.command.exception.CommandException;

class NewGameCommand implements Command {

    @Override
    public void execute() throws CommandException {
        try {
            MainGameLogic.INSTANCE.updateGameState(GameState.GARAGE);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new CommandException(e, "New game Command Exception");
        }
    }

}
