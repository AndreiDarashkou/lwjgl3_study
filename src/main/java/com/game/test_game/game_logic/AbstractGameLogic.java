package com.game.test_game.game_logic;

import com.game.engine.GameLogic;
import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.graphics.Camera;
import com.game.engine.graphics.Renderer;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import com.game.test_game.GameState;
import org.joml.Vector3f;

abstract class AbstractGameLogic implements GameLogic {
    public static GameState gameState = GameState.MENU;

    protected final Renderer renderer = new Renderer();
    protected Camera camera = new Camera();
    protected Scene scene = new Scene();
    protected Window window;

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        this.window = window;
        setupLights();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
    }

    @Override
    public void render() {
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
    }

    protected void setupLights() {
        SceneLight sceneLight = new SceneLight();

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 2.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);

        scene.setSceneLight(sceneLight);
    }

}
