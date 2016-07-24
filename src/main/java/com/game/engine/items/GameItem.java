package com.game.engine.items;

import com.game.engine.graphics.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface GameItem {

    Vector3f getPosition();

    void setPosition(float x, float y, float z);

    void setPosition(float x, float y);

    void setRotation(float x, float y, float z);

    Mesh getMesh();

    void setMesh(Mesh mesh);

    void setScale(float x, float y, float z);

    void setScale(float scale);

    Vector3f getRotation();

    void setRotation(Vector3f rotation);

    Vector3f getScale();

    Quaternionf getQuaternion();

    void setQuaternion(Quaternionf quaternion);

}
