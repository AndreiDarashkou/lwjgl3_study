package com.game.test_game.game_logic.menu.command.exception;

import lombok.Getter;
import org.lwjgl.system.CallbackI;

@Getter
public class CommandException extends Exception {
    private String message;

    public CommandException(Exception e, String message) {
        this.message = message;
    }

}
