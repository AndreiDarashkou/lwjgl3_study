package com.game.engine.graphics.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class PointLight {

    private Vector3f color;
    private Vector3f position;
    private Attenuation attenuation;
    protected float intensity;

    public PointLight(Vector3f color, Vector3f position, float intensity) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        attenuation = new Attenuation(1, 0, 0);
    }

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    @Getter
    @Setter
    public static class Attenuation {

        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }
    }

}
