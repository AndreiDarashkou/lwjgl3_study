package com.game.test_game;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.PointLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.graphics.light.SpotLight;
import com.game.engine.input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.game.test_game.TextureConstants.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

public class TestGameLogic implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private static final float CAMERA_POS_STEP = 0.15f;

    private final Vector3f cameraInc = new Vector3f();
    private final Renderer renderer;
    private GameItem[] gameItems;
    private Camera camera = new Camera();

    private TestHud testHud;

    private SceneLight sceneLight = new SceneLight();

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
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, 0, -2);
        gameItems = new GameItem[]{gameItem};

        sceneLight = new SceneLight();

        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        PointLight pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);
        sceneLight.setPointLightList(new PointLight[]{pointLight});

        lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity);
        att = new PointLight.Attenuation(0.0f, 0.0f, 0.02f);
        pointLight.setAttenuation(att);
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        sceneLight.setSpotLightList(new SpotLight[]{spotLight, new SpotLight(spotLight)});

        lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));

        testHud = new TestHud("DEMO");
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
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
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
        renderer.render(window, camera, gameItems, sceneLight, testHud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
        testHud.cleanup();
    }
}
