package com.game.test_game.game_logic;

import com.game.engine.Window;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.test_game.GameState;
import com.game.test_game.MainGameLogic;
import com.game.test_game.game_logic.garage.DescriptionCarArea;
import com.game.test_game.game_logic.garage.GarageHud;
import org.apache.commons.lang.ArrayUtils;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

class GarageGameLogic extends AbstractGameLogic {

    private final Vector3f cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    private GameItem quadGameItem;
    private float rotY = 0.05f;

    @Override
    public void init(Window window) throws Exception {
        super.init(window);
        hud = new GarageHud(window);
//        Vector3f vector3f = new Vector3f(12f, 0f, 0f).normalize(); // axis
//
//        float reflectance = 1f;
//        Material quadMaterial = new Material(new Vector3f(0.0f, 0.0f, 1.0f), reflectance);
//        quadMaterial.setTexture(new Texture(GRASS_BLOCK));
//
//        Mesh quadMesh = OBJLoader.loadMesh(CUBE);
//        quadMesh.setMaterial(quadMaterial);
//
//        quadGameItem = new GameItem(quadMesh);
//
//        float angle = 20;
//        float vectorOffset = (float) Math.sin(Math.toRadians(angle / 2));
//        vector3f = vector3f.mul(vectorOffset);
//        Quaternionf quaternion = new Quaternionf(vector3f.x, vector3f.y, vector3f.z, (float) Math.cos(Math.toRadians(angle / 2)));
//        quadGameItem.setQuaternion(quaternion);
//
//
//        scene.setGameItems(new GameItem[]{quadGameItem});
//
//        camera.getPosition().y = 1f;
//        camera.getPosition().z = 10f;
    }

    @Override
    public void input(Window window, MouseInput mouseInput) throws Exception {
        if (window.isKeyPressed(GLFW_KEY_ESCAPE)) {
            MainGameLogic.INSTANCE.updateGameState(GameState.MENU);
        }
    }

    private static boolean testReverse = true;

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception {
        super.update(interval, mouseInput);

        GarageHud garageHud = (GarageHud) hud;
        garageHud.updateSize(window);

        DescriptionCarArea area = garageHud.getDescriptionCarArea();
        float controllability = area.getControllability();
        if (controllability <= 0 || controllability >= 100) {
            testReverse = !testReverse;
        }
        if (testReverse) {
            area.setControllability(controllability + 1f);
        } else {
            area.setControllability(controllability - 1f);
        }
        try {
            garageHud.updateState();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (mouseInput.isRightButtonPressed()) {
//            Vector2f rotVec = mouseInput.getDisplayVector();
//            camera.moveRotation(rotVec.x * GameConfiguration.MOUSE_SENSITIVITY, rotVec.y * GameConfiguration.MOUSE_SENSITIVITY, 0);
//        }
//        Vector3f prevPos = new Vector3f(camera.getPosition());
//        camera.movePosition(cameraInc.x * GameConfiguration.CAMERA_POS_STEP, cameraInc.y * GameConfiguration.CAMERA_POS_STEP, cameraInc.z * GameConfiguration.CAMERA_POS_STEP);
//
//        quadGameItem.getQuaternion().rotateY(rotY);
    }

}
