package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Hud;
import com.game.engine.items.GameItem;
import lombok.Getter;

@Getter
public class GarageHud implements Hud {

    private DescriptionCarArea descriptionCarArea;
    private ScrollBarArea scrollBarArea;

    public GarageHud() throws Exception {
        descriptionCarArea = new DescriptionCarArea();
        scrollBarArea = new ScrollBarArea();
    }

    @Override
    public GameItem[] getGameItems() {
        GameItem[] desc = descriptionCarArea.getAllDescriptionItems();
        GameItem[] scroll = scrollBarArea.getAllScrollItems();
        //TODO
        return descriptionCarArea.getAllDescriptionItems();
    }


    public void updateSize(Window window) {
        descriptionCarArea.updateSize(window);
    }

    public void updateState() throws Exception {
        descriptionCarArea.updateScale();
    }

}
