package com.game.engine.hud;

import com.game.engine.items.GameItem;

import java.util.List;

public interface Hud {

    List<HudGameItem> getGameItems();

    void addGameItem(HudGameItem item);

    default void cleanup() {
        List<HudGameItem> gameItems = getGameItems();
        gameItems.forEach(item -> item.getMesh().cleanup());
    }

}