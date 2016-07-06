package com.game.engine.graphics.hud;

import com.game.engine.items.GameItem;

public interface Hud {

    GameItem[] getGameItems();

    void addGameItem(GameItem item);

    default void cleanup() {
        GameItem[] gameItems = getGameItems();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}