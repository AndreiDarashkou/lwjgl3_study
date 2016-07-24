package com.game.test_game.game_logic.menu;

import com.game.engine.graphics.FontTexture;
import com.game.engine.hud.HudGameItem;
import com.game.engine.items.TextItemImpl;
import com.game.test_game.game_logic.menu.command.Command;
import com.game.test_game.game_logic.menu.command.exception.CommandException;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.awt.*;

@Getter
@Setter
public class MenuTextItem extends TextItemImpl implements HudGameItem {

    private float width;
    private float height;
    private boolean isHover;
    private boolean isSelected;
    private Command command;

    public MenuTextItem(String text, FontTexture fontTexture, Command command) throws Exception {
        super(text, fontTexture);
        this.command = command;
        height = fontTexture.getFont().getSize() * 2;
        width = text.length() * fontTexture.getFont().getSize() / 2;
    }

    void setColor(float r, float g, float b) {
        getMesh().getMaterial().setColour(new Vector3f(r, g, b));
    }

    public void setHover(boolean isHover) {
        this.isHover = isHover;
        setColor(isHover ? Color.ORANGE : Color.YELLOW);
        setScale(isHover ? 1.05f : 1.0f);
    }

    public void execute() throws Exception {
        try {
            command.execute();
        } catch (CommandException e) {
            throw new Exception(e);
        }
    }

}
