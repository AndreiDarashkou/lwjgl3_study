package com.game.engine.graphics.light;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class SpotLight {

    private PointLight pointLight;
    private Vector3f coneDirection;
    private float cutOff;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.getPointLight()), new Vector3f(spotLight.getConeDirection()), spotLight.getCutOff());
    }

    private final void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float) Math.cos(Math.toRadians(cutOffAngle)));
    }

}
