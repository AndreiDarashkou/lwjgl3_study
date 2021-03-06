package com.game.engine.graphics;

import com.game.engine.graphics.texture.Texture;
import com.game.engine.items.GameItemImpl;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
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
 * Part of a model. Defines the vertices of a object.
 */
@Getter
@Setter
public class Mesh {

    private int vaoId;
    private int vertexCount;

    private List<Integer> vboIdList = new ArrayList<>();
    private Material material = new Material();

    public Mesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indices) {
        vertexCount = indices.length;
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        initPositionBuffer(positions);
        initTextureBuffer(textureCoordinates);
        initNormalsBuffer(normals);
        initIndexBuffer(indices);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void initPositionBuffer(float[] positions) {
        int bufferId = glGenBuffers();
        vboIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(positions), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }

    private void initTextureBuffer(float[] textureCoordinates) {
        int bufferId = glGenBuffers();
        vboIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    }

    private void initNormalsBuffer(float[] normals) {
        int bufferId = glGenBuffers();
        vboIdList.add(bufferId);
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
    }

    private void initIndexBuffer(int[] indices) {
        int bufferId = glGenBuffers();
        vboIdList.add(bufferId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);
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


    public void renderList(List<GameItemImpl> gameItems, Consumer<GameItemImpl> consumer) {
        initRender();

        for (GameItemImpl gameItem : gameItems) {
            consumer.accept(gameItem);
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }

        endRender();
    }


    public void render() {
        initRender();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        endRender();
    }

    private void initRender() {
        Texture texture = material.getTexture();
        if (texture != null) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        Texture normalMap = material.getNormalMap();
        if ( normalMap != null ) {
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE1);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, normalMap.getId());
        }
        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    private void endRender() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vboIdList.forEach(bufferId -> glDeleteBuffers(bufferId));

        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public void deleteBuffers() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vboIdList.forEach(bufferId -> glDeleteBuffers(bufferId));

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}
