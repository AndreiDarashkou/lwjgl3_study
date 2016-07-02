package com.game.test_game.game_logic.menu;

import com.game.engine.graphics.FontTexture;
import com.game.engine.items.TextItem;
import com.game.test_game.game_logic.menu.command.Command;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.awt.*;

@Getter
@Setter
public class MenuTextItem extends TextItem {

    private int width;
    private int height;
    private boolean isHover;
    private boolean isSelected;
    private Command command;

    public MenuTextItem(String text, FontTexture fontTexture, Command command) throws Exception {
        super(text, fontTexture);
        this.command = command;
        height = fontTexture.getFont().getSize() * 2;
        width = text.length() * fontTexture.getFont().getSize() / 2;
    }

    void setColor(int r, int g, int b) {
        getMesh().getMaterial().setColour(new Vector3f(r, g, b));
    }

    public void setColor(Color color) {
        getMesh().getMaterial().setColour(new Vector3f(color.getRed(), color.getGreen(), color.getBlue()));
    }

    public void setHover(boolean isHover) {
        this.isHover = isHover;
        setColor(isHover ? Color.GREEN : Color.YELLOW);
        setScale(isHover ? 1.05f : 1.0f);
    }

    public void execute() {
        command.execute();
    }
}
