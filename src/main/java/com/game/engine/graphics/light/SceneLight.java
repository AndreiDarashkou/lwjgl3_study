package com.game.engine.graphics.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class SceneLight {
    private Vector3f ambientLight;
    private DirectionalLight directionalLight;
    private PointLight[] pointLightList;
    private SpotLight[] spotLightList;
    private Vector3f skyBoxLight;
}
