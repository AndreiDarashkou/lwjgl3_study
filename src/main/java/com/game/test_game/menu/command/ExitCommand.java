package com.game.test_game.menu.command;

import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;

class ExitCommand implements Command{

    @Override
    public void execute() {
        MainGameLogic.gameState = GameState.EXIT;
    }
}
