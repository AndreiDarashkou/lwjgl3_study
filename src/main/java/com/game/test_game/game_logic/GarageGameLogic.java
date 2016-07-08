package com.game.test_game.game_logic;

import com.game.engine.GameEngine;
import com.game.engine.Window;
import com.game.engine.graphics.FontTexture;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.garage.DescriptionCarArea;
import com.game.test_game.game_logic.garage.GarageHud;
import org.apache.commons.lang.ArrayUtils;
import org.joml.Vector3f;

import java.awt.Font;

import static org.lwjgl.glfw.GLFW.*;

class GarageGameLogic extends AbstractGameLogic {

    private final Vector3f cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    private GameItem quadGameItem;
    private float rotY = 0.05f;

    @Override
    public void init(Window window) throws Exception {
        super.init(window);
        hud = new GarageHud(window);
        hud.addGameItem(getFpsItem());
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            MainGameLogic.INSTANCE.updateGameState(GameState.MENU);
        }
    }

    private static boolean testReverse = true;

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        super.update(interval, mouseInput);

        GarageHud garageHud = (GarageHud) hud;
        if (window.isResized()) {
            window.setResized(false);
            garageHud.updateSize(window);
        }

        DescriptionCarArea area = garageHud.getDescriptionCarArea();
        float controllability = area.getControllability();
        if (controllability <= 0 || controllability >= 100) {
            testReverse = !testReverse;
        }
        if (testReverse) {
            area.setControllability(controllability + 1f);
        } else {
            area.setControllability(controllability - 1f);
        }
        try {
            garageHud.updateState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
