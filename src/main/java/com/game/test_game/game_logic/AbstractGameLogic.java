package com.game.test_game.game_logic;

import com.game.engine.GameEngine;
import com.game.engine.GameLogic;
import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.graphics.Camera;
import com.game.engine.graphics.FontTexture;
import com.game.engine.hud.Hud;
import com.game.engine.graphics.Renderer;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.hud.HudGameItem;
import com.game.engine.input.MouseInput;
import com.game.engine.items.TextItem;
import com.game.engine.items.TextItemImpl;
import com.game.test_game.game_logic.menu.MenuHud;
import lombok.Getter;
import org.joml.Vector3f;

import static com.game.test_game.game_logic.garage.DescriptionCarItem.CHARSET;
import static com.game.test_game.game_logic.garage.DescriptionCarItem.FONT;

@Getter
public abstract class AbstractGameLogic implements GameLogic {

    protected final Renderer renderer = new Renderer();
    protected Camera camera = new Camera();
    protected Scene scene = new Scene();
    protected Hud hud;
    protected Window window;

    private TextItem fpsItem;
    private static int fpsCount = 0;

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        this.window = window;
        setupLights();
        fpsItem = new TextItemImpl("FPS: 0", new FontTexture(FONT, CHARSET));
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        fpsCount++;
        if (fpsCount % 10 == 0) {
            updateFpsItem(window);
            fpsCount = 0;
        }
    }

    @Override
    public final void render() {
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }

    protected void setupLights() {
        SceneLight sceneLight = new SceneLight();

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 2.0f;
        Vector3f lightDirection = new Vector3f(0, 0, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);

        scene.setSceneLight(sceneLight);
    }

    protected void updateFpsItem(Window window) {
        fpsItem.setText("FPS: " + GameEngine.currentFPS);
        fpsItem.getPosition().y = window.getHeight() - 50;
        fpsItem.getPosition().x = 50;
        fpsItem.getPosition().z = 0.1f;
    }

    public void setHud(MenuHud hud) {
        this.hud = hud;
    }
}
