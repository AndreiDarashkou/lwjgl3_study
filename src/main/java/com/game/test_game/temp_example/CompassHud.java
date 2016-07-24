package com.game.test_game.temp_example;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.hud.AbstractHud;
import com.game.engine.hud.Hud;
import com.game.engine.hud.HudGameItem;
import com.game.engine.hud.HudGameItemImpl;
import com.game.engine.items.GameItem;
import com.game.engine.items.GameItemImpl;
import com.game.engine.loader.obj.OBJLoader;
import org.joml.Vector3f;

import static com.game.test_game.common.ObjConstants.COMPASS;

public class CompassHud extends AbstractHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);
    private static final String CHARSET = "ISO-8859-1";
    private final HudGameItem compassItem;

    public CompassHud(String statusText) throws Exception {
        Mesh mesh = OBJLoader.loadMesh(COMPASS);
        Material material = new Material();
        material.setColour(new Vector3f(1, 0, 0));
        mesh.setMaterial(material);
        compassItem = new HudGameItemImpl(mesh);
        compassItem.setScale(40.0f);
        compassItem.setRotation(0f, 0f, 180f);
    }

    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public List<HudGameItem> getGameItems() {
        ArrayList<HudGameItem> list = new ArrayList<>();
        list.add(compassItem);

        return list;
    }

    @Override
    public void addGameItem(HudGameItem item) {

    }

    public void updateSize(Window window) {
        this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}