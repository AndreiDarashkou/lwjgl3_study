package com.game.test_game.game_logic;

import com.game.engine.GameLogic;
import com.game.engine.Window;
import com.game.engine.graphics.Camera;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.Texture;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;
import com.game.test_game.configuration.GameConfiguration;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static com.game.test_game.common.ObjConstants.CUBE;
import static com.game.test_game.common.TextureConstants.GRASS_BLOCK;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

class GarageGameLogic extends AbstractGameLogic {

    private final Vector3f cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    private GameItem quadGameItem;
    private float rotY = 0.05f;

    @Override
    public void init(Window window) throws Exception {
        super.init(window);

        Vector3f vector3f = new Vector3f(12f, 0f, 0f).normalize(); // axis

        float reflectance = 1f;
        Material quadMaterial = new Material(new Vector3f(0.0f, 0.0f, 1.0f), reflectance);
        quadMaterial.setTexture(new Texture(GRASS_BLOCK));

        Mesh quadMesh = OBJLoader.loadMesh(CUBE);
        quadMesh.setMaterial(quadMaterial);

        quadGameItem = new GameItem(quadMesh);

        float angle = 20;
        float vectorOffset = (float) Math.sin(Math.toRadians(angle / 2));
        vector3f = vector3f.mul(vectorOffset);
        Quaternionf quaternion = new Quaternionf(vector3f.x, vector3f.y, vector3f.z, (float) Math.cos(Math.toRadians(angle / 2)));
        quadGameItem.setQuaternion(quaternion);


        scene.setGameItems(new GameItem[]{quadGameItem});

        setupLights();

        camera.getPosition().y = 1f;
        camera.getPosition().z = 10f;
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
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
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            MainGameLogic.gameState = GameState.MENU;
            MainGameLogic.changeGameLogic(window);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplayVector();
            camera.moveRotation(rotVec.x * GameConfiguration.MOUSE_SENSITIVITY, rotVec.y * GameConfiguration.MOUSE_SENSITIVITY, 0);
        }
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * GameConfiguration.CAMERA_POS_STEP, cameraInc.y * GameConfiguration.CAMERA_POS_STEP, cameraInc.z * GameConfiguration.CAMERA_POS_STEP);

        quadGameItem.getQuaternion().rotateY(rotY);
    }

    @Override
    public void render() {
        renderer.render(window, camera, scene);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        scene.getGameItems();
    }

}
