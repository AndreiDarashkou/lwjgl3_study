package com.game.engine.graphics.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;


@Getter
@Setter
public class DirectionalLight {

    private Vector3f color;
    private Vector3f direction;
    private float intensity;

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }

}