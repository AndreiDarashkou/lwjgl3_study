package com.game.engine.graphics;

import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
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
 * Part of a model. Defines the vertices of a object.
 */
@Getter
public class Mesh {
    private final int vertexArrayId;
    private final int positionBufferId;
    private final int indexBufferId;
    private final int colourBufferId;
    private final int vertexCount;

    public Mesh(float[] vertices, float[] colour, int[] indices) {
        vertexCount = indices.length;

        vertexArrayId = glGenVertexArrays();
        glBindVertexArray(vertexArrayId);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();
        positionBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, positionBufferId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);
        intBuffer.put(indices).flip();
        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW);

        FloatBuffer colourBuffer = BufferUtils.createFloatBuffer(colour.length);
        colourBuffer.put(colour).flip();
        colourBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, colourBufferId);
        glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Unbind the VBO and VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionBufferId);
        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(colourBufferId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayId);
    }

    public void render() {
        // Bind to the VAO
        glBindVertexArray(getVertexArrayId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
