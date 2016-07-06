package com.game.engine.graphics.hud;

import com.game.engine.items.GameItem;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHud implements Hud {

    protected List<GameItem> itemsList = new ArrayList();

    @Override
    public void addGameItem(GameItem item) {
        itemsList.add(item);
    }

    @Override
    public List<GameItem> getGameItems(){
        return itemsList;
    }
}
