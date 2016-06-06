package com.game.test_game;

import com.game.engine.Window;
import com.game.engine.graphics.*;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.ShaderProgram;
import com.game.engine.graphics.light.PointLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.graphics.light.SpotLight;
import com.game.engine.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static com.game.test_game.Uniforms.AMBIENT_LIGHT;
import static com.game.test_game.Uniforms.CAMERA_POSITION;
import static com.game.test_game.Uniforms.DIRECTIONAL_LIGHT;
import static com.game.test_game.Uniforms.MATERIAL;
import static com.game.test_game.Uniforms.MODEL_VIEW_MATRIX;
import static com.game.test_game.Uniforms.POINT_LIGHTS;
import static com.game.test_game.Uniforms.PROJECTION_MATRIX;
import static com.game.test_game.Uniforms.SPECULAR_POWER;
import static com.game.test_game.Uniforms.SPOT_LIGHTS;
import static com.game.test_game.Uniforms.TEXTURE_SAMPLER;
import static org.lwjgl.opengl.GL11.*;

public class TestGameRenderer {

    private static final String VERTEX_GLSL = "/shaders/vertex.glsl";
    private static final String FRAGMENT_GLSL = "/shaders/fragment.glsl";

    private static final float fieldOfView = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private Transformation transformation;
    private ShaderProgram shaderProgram;
    private float specularPower = 0.2f;

    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;


    public TestGameRenderer() {
        transformation = new Transformation();
    }

    public void init() throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource(VERTEX_GLSL));
        shaderProgram.createFragmentShader(Utils.loadResource(FRAGMENT_GLSL));
        shaderProgram.link();

        shaderProgram.createUniform(PROJECTION_MATRIX);
        shaderProgram.createUniform(MODEL_VIEW_MATRIX);

        shaderProgram.createMaterialUniform(MATERIAL);
        shaderProgram.createUniform(CAMERA_POSITION);

        shaderProgram.createUniform(SPECULAR_POWER);
        shaderProgram.createUniform(AMBIENT_LIGHT);
        shaderProgram.createPointLightListUniform(POINT_LIGHTS, MAX_POINT_LIGHTS);
        shaderProgram.createSpotLightListUniform(SPOT_LIGHTS, MAX_SPOT_LIGHTS);
        shaderProgram.createDirectionalLightUniform(DIRECTIONAL_LIGHT);
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, SceneLight sceneLight) {

        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(fieldOfView, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform(PROJECTION_MATRIX, projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        shaderProgram.setUniform(CAMERA_POSITION, camera.getPosition());

        renderLights(viewMatrix, sceneLight);

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform(MODEL_VIEW_MATRIX, modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform(MATERIAL, mesh.getMaterial());
            mesh.render();
        }

        shaderProgram.unbind();
    }

    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight) {

        shaderProgram.setUniform(AMBIENT_LIGHT, sceneLight.getAmbientLight());
        shaderProgram.setUniform(SPECULAR_POWER, specularPower);

        processPointLights(viewMatrix, sceneLight.getPointLightList());
        processSpotLights(viewMatrix, sceneLight.getSpotLightList());

        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform(DIRECTIONAL_LIGHT, currDirLight);

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

            shaderProgram.setUniform(SPOT_LIGHTS, currSpotLight, i);
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
            shaderProgram.setUniform(POINT_LIGHTS, currPointLight, i);
        }
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

}
