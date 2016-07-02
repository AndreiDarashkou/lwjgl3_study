package com.game.test_game.menu.command;

public enum Commands implements Command{
    NEW_GAME(new NewGameCommand()),
    LOAD_GAME(new LoadGameCommand()),
    CONFIGURATION(new ConfigurationCommand()),
    EXIT(new ExitCommand());

    private Command command;

    Commands(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
