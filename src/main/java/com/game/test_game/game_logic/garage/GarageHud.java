package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Hud;
import com.game.engine.items.GameItem;
import lombok.Getter;

@Getter
public class GarageHud implements Hud {

    private DescriptionCarArea descriptionCarArea;

    public GarageHud() throws Exception {
        descriptionCarArea = new DescriptionCarArea();
    }

    @Override
    public GameItem[] getGameItems() {
        return descriptionCarArea.getAllDescriptionItems();
    }


    public void updateSize(Window window) {
        descriptionCarArea.updateSize(window);
    }

    public void updateState() throws Exception {
        descriptionCarArea.updateScale();
    }

}
