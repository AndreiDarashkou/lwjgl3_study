package com.game.model;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.shader.Mesh;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class DummyGame implements GameLogic {

    private int direction = 0;
    private float color = 0.0f;
    private final Renderer renderer;
    private Mesh mesh;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                 0.5f,  0.5f, 0.0f,
                 0.5f, -0.5f, 0.0f
        };

        int[] indexes = new int[] {
            0, 1, 2, 2, 1 , 3,
        };

        mesh = new Mesh(vertices, indexes);
    }

    @Override
    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanUp();
    }
}
