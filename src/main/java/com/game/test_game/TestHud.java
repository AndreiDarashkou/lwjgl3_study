package com.game.test_game;

import java.awt.Font;

import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import org.joml.Vector3f;

import static com.game.test_game.TextureConstants.COMPASS;

public class TestHud implements Hud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);
    private static final String CHARSET = "ISO-8859-1";
    private final GameItem[] gameItems;
    private final TextItem statusTextItem;
    private final GameItem compassItem;

    public TestHud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        this.statusTextItem = new TextItem(statusText, fontTexture);
        this.statusTextItem.getMesh().getMaterial().setColour(new Vector3f(1, 1, 1));

        // Create compass
        Mesh mesh = OBJLoader.loadMesh(COMPASS);
        Material material = new Material();
        material.setColour(new Vector3f(1, 0, 0));
        mesh.setMaterial(material);
        compassItem = new GameItem(mesh);
        compassItem.setScale(40.0f);
        // Rotate to transform it to screen coordinates
        compassItem.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
        gameItems = new GameItem[]{statusTextItem, compassItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
        this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}