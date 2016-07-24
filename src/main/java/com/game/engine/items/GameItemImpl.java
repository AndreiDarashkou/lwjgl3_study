package com.game.engine.items;

import com.game.engine.graphics.Mesh;
import lombok.Getter;
import lombok.Setter;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Getter
@Setter
public class GameItemImpl implements GameItem {

    private Mesh[] meshes;
    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Quaternionf quaternion;
    private Vector3f scale = new Vector3f(1, 1, 1);

    public GameItemImpl() {
    }

    public GameItemImpl(Mesh mesh) {
        setMesh(mesh);
    }

    public GameItemImpl(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = 0f;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Mesh getMesh() {
        return meshes == null ? null : meshes[0];
    }

    public void setMesh(Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    public void setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
    }

    public void setScale(float scale) {
        this.scale.set(scale, scale, scale);
    }
}
