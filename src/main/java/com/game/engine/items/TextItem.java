package com.game.engine.items;

import com.game.engine.hud.HudGameItem;
import com.game.engine.input.MouseInput;
import org.joml.Vector3f;

import java.awt.Color;

public interface TextItem extends HudGameItem {

    public void setText(String text);

    public void setColor(Color color);

    default boolean isHovered(MouseInput mouseInput) {
        double mouseX = mouseInput.getCurrentPos().x;
        double mouseY = mouseInput.getCurrentPos().y;

        double itemX = getPosition().x;
        double itemY = getPosition().y;

        if (mouseX > itemX && mouseX < itemX + getWidth()) {
            if (mouseY > itemY && mouseY < itemY + getHeight() ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
