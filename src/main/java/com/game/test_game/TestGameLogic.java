package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.items.GameItem;
import com.game.engine.items.SkyBox;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import com.game.engine.items.Terrain;
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
    private GameItem cubeGameItem;
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

        // Setup  GameItems
        float reflectance = 1f;
        Mesh cubeMesh = OBJLoader.loadMesh("/models/cube.obj");
        Material cubeMaterial = new Material(new Vector3f(0, 1, 0), reflectance);
        cubeMesh.setMaterial(cubeMaterial);
        cubeGameItem = new GameItem(cubeMesh);
        cubeGameItem.setPosition(0, 0, 0);
        cubeGameItem.setScale(0.5f);

        Mesh quadMesh = OBJLoader.loadMesh("/models/plane.obj");
        Material quadMaterial = new Material(new Vector3f(0.0f, 0.0f, 1.0f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -1, 0);
        quadGameItem.setScale(2.5f);

        scene.setGameItems(new GameItem[]{cubeGameItem, quadGameItem});

        // Setup Lights
        setupLights();

        camera.getPosition().z = 2;
        testHud = new TestHud("");
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
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
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            angleInc -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            angleInc += 0.05f;
        } else {
            angleInc = 0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
// Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain != null ? terrain.getHeight(camera.getPosition()) : -Float.MAX_VALUE;
        if (camera.getPosition().y <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        float rotY = cubeGameItem.getRotation().y;
        rotY += 0.5f;
        if ( rotY >= 360 ) {
            rotY -= 360;
        }
        cubeGameItem.getRotation().y = rotY;

        lightAngle += angleInc;
        if ( lightAngle < 0 ) {
            lightAngle = 0;
        } else if (lightAngle > 180 ) {
            lightAngle = 180;
        }
        float zValue = (float)Math.cos(Math.toRadians(lightAngle));
        float yValue = (float)Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();
        float lightAngle = (float)Math.toDegrees(Math.acos(lightDirection.z));
        testHud.setStatusText("LightAngle: " + lightAngle);
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
