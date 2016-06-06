package com.game.engine.graphics;

import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ < 0) {
            position.x -= Math.sin(rotation.y * Math.PI / 180) * offsetZ;
            position.z += Math.cos(rotation.y * Math.PI / 180) * offsetZ;
        }
        if (offsetZ > 0) {
            position.x -= Math.sin(rotation.y * Math.PI / 180) * offsetZ;
            position.z += Math.cos(rotation.y * Math.PI / 180) * offsetZ;
        }
        if (offsetX < 0) {
            position.x -= Math.sin((rotation.y - 90) * Math.PI / 180) * offsetX;
            position.z += Math.cos((rotation.y - 90) * Math.PI / 180) * offsetX;
        }
        if (offsetX > 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.y += offsetZ;
    }
}
