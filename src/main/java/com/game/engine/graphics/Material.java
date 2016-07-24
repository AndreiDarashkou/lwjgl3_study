package com.game.engine.graphics;

import com.game.engine.graphics.texture.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.awt.Color;

@Getter
@Setter
public class Material {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f color;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    public Material() {
        color = DEFAULT_COLOUR;
        reflectance = 0;
    }

    public Material(Vector3f color, float reflectance) {
        this();
        this.color = color;
        this.reflectance = reflectance;
    }

    public Material(Color color, float reflectance) {
        this();
        this.color = new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        this.reflectance = reflectance;
    }


    public Material(Texture texture) {
        this();
        this.texture = texture;
    }

    public Material(Texture texture, float reflectance) {
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }
}
