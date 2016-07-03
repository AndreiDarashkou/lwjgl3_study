package com.game.test_game.game_logic.menu.command;

import com.game.engine.Window;
import com.game.test_game.game_logic.menu.command.exception.CommandException;

public interface Command {
    void execute() throws CommandException;
}
