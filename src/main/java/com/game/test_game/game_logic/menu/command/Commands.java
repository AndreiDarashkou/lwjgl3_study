package com.game.test_game.game_logic.menu.command;

import com.game.test_game.game_logic.menu.command.exception.CommandException;

public enum Commands implements Command{
    NEW_GAME(new NewGameCommand()),
    LOAD_GAME(new LoadGameCommand()),
    CONFIGURATION(new ConfigurationCommand()),
    EXIT(new ExitCommand()),
    SCREEN_RESOLUTION(new ResolutionCommand()),
    SOUND(new SoundCommand()),
    BACK(new BackCommand());

    private Command command;

    Commands(Command command) {
        this.command = command;
    }

    @Override
    public void execute() throws CommandException {
        command.execute();
    }
}
