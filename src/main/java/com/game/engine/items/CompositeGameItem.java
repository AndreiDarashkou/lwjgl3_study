package com.game.engine.items;

import java.util.ArrayList;
import java.util.List;

public class CompositeGameItem {

    protected List<GameItem> itemsList = new ArrayList();

    public void addGameItem(GameItem item) {
        itemsList.add(item);
    }

    public List<GameItem> getGameItems(){
        return itemsList;
    }

    public void cleanup() {
        itemsList.forEach(item -> item.getMesh().cleanUp());
    }

}
