package com.game.engine.graphics;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Part of a test_game. Defines the vertices of a object.
 */
@Getter
@Setter
public class Mesh {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private int vertexArrayId;
    private int vertexCount;

    private List<Integer> bufferIdList = new ArrayList<>();
    private Texture texture;
    private Vector3f colour;

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
        colour = DEFAULT_COLOUR;
        vertexCount = indices.length;
        vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);

        initPositionBuffer(positions);
        initTextureBuffer(textCoords);
        initNormalsBuffer(normals);
        initIndexBuffer(indices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(float[] vertices, float[] colour, int[] indices) {
        vertexCount = indices.length;
        vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);

        initPositionBuffer(vertices);
        initIndexBuffer(indices);
        initColourBuffer(colour);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public Mesh(float[] positions, float[] textureCoordinates, int[] indices, Texture texture) {
        this.texture = texture;

        vertexCount = indices.length;
        vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);

        initPositionBuffer(positions);
        initTextureBuffer(textureCoordinates);
        initIndexBuffer(indices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    private void initPositionBuffer(float[] positions) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    private void initIndexBuffer(int[] indices) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);
    }

    private void initColourBuffer(float[] colour) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(colour), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    }

    private void initTextureBuffer(float[] textureCoordinates) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    }

    private void initNormalsBuffer(float[] normals) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
    }

    private FloatBuffer createFloatBuffer(float[] colour) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(colour.length);
        floatBuffer.put(colour).flip();

        return floatBuffer;
    }

    private IntBuffer createIntBuffer(int[] indices) {
        IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);
        intBuffer.put(indices).flip();
        return intBuffer;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        bufferIdList.forEach(bufferId -> glDeleteBuffers(bufferId));

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayId);
    }

    public void render() {
        // Activate firs texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, texture.getId());

        // Bind to the VAO
        glBindVertexArray(getVertexArrayId());
        //draw mesh
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
