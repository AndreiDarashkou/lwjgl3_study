package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.graphics.GameItem;
import com.game.engine.graphics.Mesh;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class TestGameLogic implements GameLogic {


    private int displxInc = 0;
    private int displyInc = 0;
    private int displzInc = 0;
    private int scaleInc = 0;

    private final TestGameRenderer testGameRenderer;
    private GameItem[] gameItems;

    public TestGameLogic() {
        testGameRenderer = new TestGameRenderer();
    }

    @Override
    public void init() throws Exception {
        testGameRenderer.init();

        float[] vertices = new float[]{
                -0.5f, 0.5f, -1.0f,
                -0.5f, -0.5f, -1.0f,
                0.5f, 0.5f, -1.0f,
                0.5f, -0.5f, -1.0f
        };

        int[] indexes = new int[]{
                0, 1, 2, 2, 1, 3,
        };

        float[] colour = {
                1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f
        };

        Mesh mesh = new Mesh(vertices, colour, indexes);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setPosition(0, 0, -2);

        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setPosition(3, 0, -2);
        gameItems = new GameItem[]{gameItem, gameItem2};
    }

    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        }
        if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameItem gameItem : gameItems) {
            // Update position
            Vector3f itemPos = gameItem.getPosition();
            float posx = itemPos.x + displxInc * 0.01f;
            float posy = itemPos.y + displyInc * 0.01f;
            float posz = itemPos.z + displzInc * 0.01f;
            gameItem.setPosition(posx, posy, posz);

            // Update scale
            float scale = gameItem.getScale();
            scale += scaleInc * 0.05f;
            if (scale < 0) {
                scale = 0;
            }
            gameItem.setScale(scale);

            // Update rotation angle
            float rotation = gameItem.getRotation().z + 1.5f;
            if (rotation > 360) {
                rotation = 0;
            }
            gameItem.setRotation(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        testGameRenderer.render(window, gameItems);
    }

    @Override
    public void cleanup() {
        testGameRenderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
