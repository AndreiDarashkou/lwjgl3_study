package com.game.engine.graphics.hud;

import com.game.engine.items.GameItem;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHud implements Hud {

    protected List<GameItem> additionalItems = new ArrayList();

    public GameItem[] getAllGameItems(GameItem[] items) {
        return (GameItem[]) ArrayUtils.addAll(items, getAdditionalItems());
    }

    @Override
    public void addGameItem(GameItem item) {
        additionalItems.add(item);
    }

    protected GameItem[] getAdditionalItems() {
        GameItem[] additional = new GameItem[additionalItems.size()];
        for (int i = 0; i < additional.length; i++) {
            additional[i] = additionalItems.get(i);
        }
        return additional;
    }

}
