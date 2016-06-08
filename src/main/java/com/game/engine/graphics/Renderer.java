package com.game.engine.graphics;


import com.game.engine.Window;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.PointLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.graphics.light.SpotLight;
import com.game.engine.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.game.engine.graphics.Uniforms.*;
import static com.game.engine.graphics.Uniforms.DIRECTIONAL_LIGHT;
import static com.game.engine.graphics.Uniforms.SPOT_LIGHTS;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    private static final String SCENE_VERTEX = "/shaders/vertex.glsl";
    private static final String SCENE_FRAGMENT = "/shaders/fragment.glsl";

    private static final String HUD_VERTEX = "/shaders/hud_vertex.glsl";
    private static final String HUD_FRAGMENT = "/shaders/hud_fragment.glsl";

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation = new Transformation();
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram hudShaderProgram;
    private final float specularPower = 10f;

    public void init() throws Exception {
        setupSceneShader();
        setupHudShader();
    }

    private void setupSceneShader() throws Exception {
        sceneShaderProgram = createShaderProgram(SCENE_VERTEX, SCENE_FRAGMENT);

        sceneShaderProgram.createUniform(PROJECTION_MATRIX);
        sceneShaderProgram.createUniform(MODEL_VIEW_MATRIX);
        sceneShaderProgram.createUniform(TEXTURE_SAMPLER);

        sceneShaderProgram.createMaterialUniform(MATERIAL);
        sceneShaderProgram.createUniform(CAMERA_POSITION);

        sceneShaderProgram.createUniform(SPECULAR_POWER);
        sceneShaderProgram.createUniform(AMBIENT_LIGHT);
        sceneShaderProgram.createPointLightListUniform(POINT_LIGHTS, MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform(SPOT_LIGHTS, MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform(DIRECTIONAL_LIGHT);
    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = createShaderProgram(HUD_VERTEX, HUD_FRAGMENT);

        hudShaderProgram.createUniform(PROJECTION_MODEL_MATRIX);
        hudShaderProgram.createUniform(COLOUR);
        hudShaderProgram.createUniform(HAS_TEXTURE);
    }

    private ShaderProgram createShaderProgram(String vertex, String fragment) throws Exception {
        ShaderProgram shader = new ShaderProgram();
        shader.createVertexShader(Utils.loadResource(vertex));
        shader.createFragmentShader(Utils.loadResource(fragment));
        shader.link();

        return shader;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, SceneLight sceneLight, IHud hud) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        renderScene(window, camera, gameItems, sceneLight);
        renderHud(window, hud);
    }

    public void renderScene(Window window, Camera camera, GameItem[] gameItems, SceneLight sceneLight) {
        sceneShaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShaderProgram.setUniform(PROJECTION_MATRIX, projectionMatrix);
        sceneShaderProgram.setUniform(CAMERA_POSITION, camera.getPosition());

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        renderLights(viewMatrix, sceneLight);

        sceneShaderProgram.setUniform(TEXTURE_SAMPLER, 0);

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            sceneShaderProgram.setUniform(MODEL_VIEW_MATRIX, modelViewMatrix);
            sceneShaderProgram.setUniform(MATERIAL, mesh.getMaterial());

            mesh.render();
        }

        sceneShaderProgram.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

        sceneShaderProgram.setUniform(AMBIENT_LIGHT, sceneLight.getAmbientLight());
        sceneShaderProgram.setUniform(SPECULAR_POWER, specularPower);

        processPointLights(viewMatrix, sceneLight.getPointLightList());
        processSpotLights(viewMatrix, sceneLight.getSpotLightList());
        processDirectionLight(viewMatrix, sceneLight.getDirectionalLight());
    }

    private void processDirectionLight(Matrix4f viewMatrix, DirectionalLight directionalLight) {
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform(DIRECTIONAL_LIGHT, currDirLight);
    }

    private void processSpotLights(Matrix4f viewMatrix, SpotLight[] spotLightList) {
        int numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sceneShaderProgram.setUniform(SPOT_LIGHTS, currSpotLight, i);
        }
    }

    private void processPointLights(Matrix4f viewMatrix, PointLight[] pointLightList) {
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform(POINT_LIGHTS, currPointLight, i);
        }
    }

    private void renderHud(Window window, IHud hud) {
        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (GameItem gameItem : hud.getGameItems()) {
            Mesh mesh = gameItem.getMesh();

            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
            hudShaderProgram.setUniform(PROJECTION_MODEL_MATRIX, projModelMatrix);
            hudShaderProgram.setUniform(COLOUR, gameItem.getMesh().getMaterial().getColour());
            hudShaderProgram.setUniform(HAS_TEXTURE, gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);

            mesh.render();
        }

        hudShaderProgram.unbind();
    }

    public void cleanup() {
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
            hudShaderProgram.cleanup();
        }
    }
}
