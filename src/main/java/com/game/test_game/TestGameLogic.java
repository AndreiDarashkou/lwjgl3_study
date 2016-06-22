package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.items.GameItem;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import com.game.engine.items.Terrain;
import com.game.engine.loader.md5.MD5Loader;
import com.game.engine.loader.md5.MD5Model;
import com.game.engine.loader.obj.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class TestGameLogic implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.05f;

    private final Vector3f cameraInc;
    private final Renderer renderer;
    private Scene scene = new Scene();
    private Camera camera = new Camera();

    private TestHud testHud;
    private float lightAngle;
    private Terrain terrain;
    private GameItem monster;
    private float angleInc = 0;

    public TestGameLogic() {
        renderer = new Renderer();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = 45;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        float reflectance = 1f;

        Mesh quadMesh = OBJLoader.loadMesh("/models/plane.obj");
        Material quadMaterial = new Material(new Vector3f(0.0f, 0.0f, 1.0f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, 0, 0);
        quadGameItem.setScale(2.5f);

        // Setup  GameItems
        MD5Model md5Meshodel = MD5Model.parse("/models/monster.md5mesh");
        monster = MD5Loader.process(md5Meshodel, new Vector3f(1, 1, 1));
        monster.setScale(0.05f);
        monster.setRotation(90, 0, 0);

        scene.setGameItems(new GameItem[] { quadGameItem, monster} );

        // Setup Lights
        setupLights();

        camera.getPosition().x = 0.25f;
        camera.getPosition().y = 6.5f;
        camera.getPosition().z = 6.5f;
        camera.getRotation().x = 25;
        camera.getRotation().y = -1;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

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
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to

        float rotZ = monster.getRotation().z;
        rotZ += 0.5f;
        if (rotZ >= 360) {
            rotZ -= 360;
        }
        monster.getRotation().z = rotZ;

//        testHud.setStatusText("LightAngle: " + lightAngle);
    }

    @Override
    public void render(Window window) {
        if (testHud != null) {
            testHud.updateSize(window);
        }
        renderer.render(window, camera, scene, testHud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (testHud != null) {
            testHud.cleanup();
        }
    }
}
