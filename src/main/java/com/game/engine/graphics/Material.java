package com.game.engine.graphics;

import com.game.engine.graphics.texture.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Material {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f colour;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    public Material() {
        colour = DEFAULT_COLOUR;
        reflectance = 0;
    }

    public Material(Vector3f colour, float reflectance) {
        this();
        this.colour = colour;
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

}
