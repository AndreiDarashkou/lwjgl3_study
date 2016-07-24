package com.game.engine.hud;

import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.test_game.game_logic.menu.MenuTextItem;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHud implements Hud {

    protected List<HudGameItem> itemsList = new ArrayList();

    @Override
    public void addGameItem(HudGameItem item) {
        itemsList.add(item);
    }

    @Override
    public List<HudGameItem> getGameItems() {
        return itemsList;
    }

    public abstract void update(Window window, MouseInput mouseInput) throws Exception;

}
