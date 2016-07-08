package com.game.engine.graphics.hud;

import com.game.engine.items.GameItem;

import java.util.List;

public interface Hud {

    List<GameItem> getGameItems();

    void addGameItem(GameItem item);

    default void cleanup() {
        List<GameItem> gameItems = getGameItems();
        gameItems.forEach(item -> item.getMesh().cleanup());
    }
    
}