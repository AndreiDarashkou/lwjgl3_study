package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static com.game.test_game.TextureConstants.*;
import static org.lwjgl.glfw.GLFW.*;

public class TestGameLogic implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.15f;

    private final Vector3f cameraInc = new Vector3f();
    private final Renderer renderer;
    private Scene scene = new Scene();
    private Camera camera = new Camera();

    private TestHud testHud;
    private float lightAngle;

    public TestGameLogic() {
        renderer = new Renderer();
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        float reflectance = 1f;

        Mesh mesh = OBJLoader.loadMesh(CUBE);
        Texture texture = new Texture(GRASS_BLOCK);
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        float blockScale = 0.5f;
        float skyBoxScale = 20.0f;
        float extension = 2.0f;

        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy;
        int NUM_ROWS = (int)(extension * skyBoxScale * 2 / inc);
        int NUM_COLS = (int)(extension * skyBoxScale * 2/ inc);
        GameItem[] gameItems  = new GameItem[NUM_ROWS * NUM_COLS];
        for(int i=0; i<NUM_ROWS; i++) {
            for(int j=0; j<NUM_COLS; j++) {
                GameItem gameItem = new GameItem(mesh);
                gameItem.setScale(blockScale);
                incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
                gameItem.setPosition(posx, starty + incy, posz);
                gameItems[i*NUM_COLS + j] = gameItem;

                posx += inc;
            }
            posx = startx;
            posz -= inc;
        }
        scene.setGameItems(gameItems);

        // Setup  SkyBox
        SkyBox skyBox = new SkyBox(SKY_BOX, SKY_BOX_PNG);
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        testHud = new TestHud("DEMO");

        camera.getPosition().x = 0.65f;
        camera.getPosition().y = 1.15f;
        camera.getPosition().y = 4.34f;

    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();

        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        float lightIntensity = 1.0f;
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));

        scene.setSceneLight(sceneLight);
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
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            testHud.rotateCompass(camera.getRotation().y);
        }

        lightAngle += 0.5f;
        DirectionalLight directionalLight = scene.getSceneLight().getDirectionalLight();
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -90 || lightAngle >= 90) {
            float factor = 1 - (Math.abs(lightAngle) - 90) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        testHud.updateSize(window);
        renderer.render(window, camera, scene, testHud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        Map<Mesh, List<GameItem>> mapMeshes = scene.getMeshMap();
        mapMeshes.keySet().forEach(Mesh::cleanUp);
        testHud.cleanup();
    }
}
