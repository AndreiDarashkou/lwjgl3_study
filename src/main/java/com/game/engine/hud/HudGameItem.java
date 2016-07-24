package com.game.engine.hud;

import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;

public interface HudGameItem extends GameItem {

    float getWidth();

    float getHeight();

    default boolean isHovered(MouseInput mouseInput) {
        double mouseX = mouseInput.getCurrentPos().x;
        double mouseY = mouseInput.getCurrentPos().y;

        double itemX = getPosition().x;
        double itemY = getPosition().y;

        if (mouseX > itemX - getWidth() && mouseX < itemX + getWidth()) {
            if (mouseY > itemY - getHeight() && mouseY < itemY + getHeight() ) {
               return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}


