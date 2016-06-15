package com.game.engine.graphics;


import com.game.engine.Scene;
import com.game.engine.Window;
import com.game.engine.items.GameItem;
import com.game.engine.items.SkyBox;
import com.game.engine.graphics.light.DirectionalLight;
import com.game.engine.graphics.light.PointLight;
import com.game.engine.graphics.light.SceneLight;
import com.game.engine.graphics.light.SpotLight;
import com.game.engine.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;

import static com.game.engine.graphics.Uniforms.*;
import static com.game.engine.graphics.Uniforms.DIRECTIONAL_LIGHT;
import static com.game.engine.graphics.Uniforms.SPOT_LIGHTS;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class Renderer {
    private static final String SCENE_VERTEX = "/shaders/vertex.glsl";
    private static final String SCENE_FRAGMENT = "/shaders/fragment.glsl";

    private static final String HUD_VERTEX = "/shaders/hud_vertex.glsl";
    private static final String HUD_FRAGMENT = "/shaders/hud_fragment.glsl";

    private static final String SKY_BOX_VERTEX = "/shaders/sb_vertex.glsl";
    private static final String SKY_BOX_FRAGMENT = "/shaders/sb_fragment.glsl";

    private static final float FOV = (float) Math.toRadians(90.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation = new Transformation();
    private ShadowMap shadowMap;

    private ShaderProgram depthShaderProgram;
    private ShaderProgram sceneShaderProgram;
    private ShaderProgram hudShaderProgram;
    private ShaderProgram skyBoxShaderProgram;

    private final float specularPower = 10f;

    public void init() throws Exception {
        shadowMap = new ShadowMap();

        setupDepthShader();
        setupSceneShader();
        setupHudShader();
        setupSkyBoxShader();
    }

    private void setupDepthShader() throws Exception {
        depthShaderProgram = new ShaderProgram();
        depthShaderProgram.createVertexShader(Utils.loadResource("/shaders/depth_vertex.glsl"));
        depthShaderProgram.createFragmentShader(Utils.loadResource("/shaders/depth_fragment.glsl"));
        depthShaderProgram.link();

        depthShaderProgram.createUniform("orthoProjectionMatrix");
        depthShaderProgram.createUniform("modelLightViewMatrix");
    }

    private void setupSceneShader() throws Exception {
        sceneShaderProgram = createShaderProgram(SCENE_VERTEX, SCENE_FRAGMENT);

        sceneShaderProgram.createUniform(PROJECTION_MATRIX);
        sceneShaderProgram.createUniform(MODEL_VIEW_MATRIX);
        sceneShaderProgram.createUniform(TEXTURE_SAMPLER);
        sceneShaderProgram.createUniform("normalMap");

        sceneShaderProgram.createMaterialUniform(MATERIAL);
        sceneShaderProgram.createUniform(CAMERA_POSITION);

        sceneShaderProgram.createUniform(SPECULAR_POWER);
        sceneShaderProgram.createUniform(AMBIENT_LIGHT);
        sceneShaderProgram.createPointLightListUniform(POINT_LIGHTS, MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform(SPOT_LIGHTS, MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform(DIRECTIONAL_LIGHT);
        sceneShaderProgram.createFogUniform("fog");
        // Create uniforms for shadow mapping
        sceneShaderProgram.createUniform("shadowMap");
        sceneShaderProgram.createUniform("orthoProjectionMatrix");
        sceneShaderProgram.createUniform("modelLightViewMatrix");
    }

    private void setupHudShader() throws Exception {
        hudShaderProgram = createShaderProgram(HUD_VERTEX, HUD_FRAGMENT);

        hudShaderProgram.createUniform(PROJECTION_MODEL_MATRIX);
        hudShaderProgram.createUniform(COLOUR);
        hudShaderProgram.createUniform(HAS_TEXTURE);
    }

    private void setupSkyBoxShader() throws Exception {
        skyBoxShaderProgram = createShaderProgram(SKY_BOX_VERTEX, SKY_BOX_FRAGMENT);

        skyBoxShaderProgram.createUniform(PROJECTION_MATRIX);
        skyBoxShaderProgram.createUniform(MODEL_VIEW_MATRIX);
        skyBoxShaderProgram.createUniform(TEXTURE_SAMPLER);
        skyBoxShaderProgram.createUniform(AMBIENT_LIGHT);
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

    public void render(Window window, Camera camera, Scene scene, Hud hud) {
        clear();

        // Render depth map before view ports has been set up
        renderDepthMap(window, camera, scene);

        glViewport(0, 0, window.getWidth(), window.getHeight());

        // Update projection and view atrices once per render cycle
        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateViewMatrix(camera);

        renderScene(camera, scene);

        renderSkyBox(window, camera, scene);

        renderHud(window, hud);

        //renderAxes(camera);
    }

    private void renderDepthMap(Window window, Camera camera, Scene scene) {
        // Setup view port to match the texture size
        glBindFramebuffer(GL_FRAMEBUFFER, shadowMap.getDepthMapFBO());
        glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);

        depthShaderProgram.bind();

        DirectionalLight light = scene.getSceneLight().getDirectionalLight();
        Vector3f lightDirection = light.getDirection();

        float lightAngleX = (float)Math.toDegrees(Math.acos(lightDirection.z));
        float lightAngleY = (float)Math.toDegrees(Math.asin(lightDirection.x));
        float lightAngleZ = 0;
        Matrix4f lightViewMatrix = transformation.updateLightViewMatrix(new Vector3f(lightDirection).mul(light.getShadowPosMult()), new Vector3f(lightAngleX, lightAngleY, lightAngleZ));
        DirectionalLight.OrthoCoords orthCoords = light.getOrthoCords();
        Matrix4f orthoProjMatrix = transformation.updateOrthoProjectionMatrix(orthCoords.left, orthCoords.right, orthCoords.bottom, orthCoords.top, orthCoords.near, orthCoords.far);

        depthShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);
        Map<Mesh, List<GameItem>> mapMeshes = scene.getMeshMap();
        for (Mesh mesh : mapMeshes.keySet()) {
            mesh.renderList(mapMeshes.get(mesh), (GameItem gameItem) -> {
                        Matrix4f modelLightViewMatrix = transformation.buildModelViewMatrix(gameItem, lightViewMatrix);
                        depthShaderProgram.setUniform("modelLightViewMatrix", modelLightViewMatrix);
                    }
            );
        }

        // Unbind
        depthShaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void renderScene(Camera camera, Scene scene) {
        sceneShaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        sceneShaderProgram.setUniform(PROJECTION_MATRIX, projectionMatrix);
        Matrix4f orthoProjMatrix = transformation.getOrthoProjectionMatrix();
        sceneShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);
        sceneShaderProgram.setUniform(CAMERA_POSITION, camera.getPosition());
        Matrix4f lightViewMatrix = transformation.getLightViewMatrix();

        Matrix4f viewMatrix = transformation.getViewMatrix();
        renderLights(viewMatrix, scene.getSceneLight());

        sceneShaderProgram.setUniform(TEXTURE_SAMPLER, 0);
        sceneShaderProgram.setUniform("fog", scene.getFog());
        sceneShaderProgram.setUniform("normalMap", 1);
        sceneShaderProgram.setUniform("shadowMap", 2);

        Map<Mesh, List<GameItem>> mapMeshes = scene.getMeshMap();
        for (Mesh mesh : mapMeshes.keySet()) {
            sceneShaderProgram.setUniform(MATERIAL, mesh.getMaterial());
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getId());
            mesh.renderList(mapMeshes.get(mesh), (GameItem gameItem) -> {
                        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
                        sceneShaderProgram.setUniform(MODEL_VIEW_MATRIX, modelViewMatrix);
                        Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(gameItem, lightViewMatrix);
                        sceneShaderProgram.setUniform("modelLightViewMatrix", modelLightViewMatrix);
                    }
            );
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

    private void renderHud(Window window, Hud hud) {
        hudShaderProgram.bind();

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
        for (GameItem gameItem : hud.getGameItems()) {
            Mesh mesh = gameItem.getMesh();

            Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(gameItem, ortho);
            hudShaderProgram.setUniform(PROJECTION_MODEL_MATRIX, projModelMatrix);
            hudShaderProgram.setUniform(COLOUR, gameItem.getMesh().getMaterial().getColour());
            hudShaderProgram.setUniform(HAS_TEXTURE, gameItem.getMesh().getMaterial().isTextured() ? 1 : 0);

            mesh.render();
        }

        hudShaderProgram.unbind();
    }

    private void renderSkyBox(Window window, Camera camera, Scene scene) {
        SkyBox skyBox = scene.getSkyBox();
        if (skyBox != null) {
            skyBoxShaderProgram.bind();

            skyBoxShaderProgram.setUniform(TEXTURE_SAMPLER, 0);

            Matrix4f projectionMatrix = transformation.getProjectionMatrix();
            skyBoxShaderProgram.setUniform(PROJECTION_MATRIX, projectionMatrix);

            skyBox = scene.getSkyBox();
            Matrix4f viewMatrix = transformation.getViewMatrix();
            viewMatrix.m30 = 0;
            viewMatrix.m31 = 0;
            viewMatrix.m32 = 0;
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(skyBox, viewMatrix);
            skyBoxShaderProgram.setUniform(MODEL_VIEW_MATRIX, modelViewMatrix);
            skyBoxShaderProgram.setUniform(AMBIENT_LIGHT, scene.getSceneLight().getAmbientLight());

            scene.getSkyBox().getMesh().render();

            skyBoxShaderProgram.unbind();
        }
    }


    public void cleanup() {
        if (shadowMap != null) {
            shadowMap.cleanup();
        }
        if (depthShaderProgram != null) {
            depthShaderProgram.cleanup();
        }
        if (skyBoxShaderProgram != null) {
            skyBoxShaderProgram.cleanup();
        }
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
        if (hudShaderProgram != null) {
            hudShaderProgram.cleanup();
        }
    }
}
