package com.game.test_game.game_logic;

import com.game.engine.GameEngine;
import com.game.engine.GameLogic;
import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.graphics.Camera;
import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.hud.Hud;
import com.game.engine.graphics.Renderer;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.awt.Font;

@Getter
public abstract class AbstractGameLogic implements GameLogic {

    protected final Renderer renderer = new Renderer();
    protected Camera camera = new Camera();
    protected Scene scene = new Scene();
    @Setter
    protected Hud hud;
    protected Window window;
    //private TextItem fpsItem;

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        this.window = window;
        setupLights();
        //font = new FontTexture(FONT, CHARSET);
        //updateFpsItem();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
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

//    Font FONT = new Font("Cooper Black", Font.ITALIC, 16);
//    String CHARSET = "ISO-8859-1";
//
//    FontTexture font;
//
//    public void updateFpsItem() throws Exception {
//        fpsItem = new TextItem("FPS: " + GameEngine.currentFPS, font);
//        fpsItem.getPosition().y = window.getHeight() - 50;
//        fpsItem.getPosition().x = 50;
//        fpsItem.getPosition().z = 0.1f;
//    }

}
