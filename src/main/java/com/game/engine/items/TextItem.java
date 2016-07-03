package com.game.engine.items;


import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.util.Utils;
import lombok.Getter;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TextItem extends GameItem {

    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;
    private final FontTexture fontTexture;

    private String text;

    public TextItem(String text, FontTexture fontTexture) throws Exception {
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        setMesh(buildMesh());
    }

    private Mesh buildMesh() {
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        List<Float> positions = new ArrayList<>(numChars * 12);
        List<Float> textCoords = new ArrayList<>(numChars * 8);
        List<Integer> indices = new ArrayList<>(numChars * 6);
        float[] normals = new float[0];

        float startX = 0;
        for (int i = 0; i < numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);
            float leftTextureCoord = (float) charInfo.getStartX() / (float) fontTexture.getWidth();
            float rightTextureCoord = (float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth();
            // Left Top vertex
            setPosition(positions, startX, 0.0f);
            textCoords.add(leftTextureCoord);
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD);

            // Left Bottom vertex
            setPosition(positions, startX, (float) fontTexture.getHeight());
            textCoords.add(leftTextureCoord);
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            setPosition(positions, startX + charInfo.getWidth(), (float) fontTexture.getHeight());
            textCoords.add(rightTextureCoord);
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 2);

            // Right Top vertex
            setPosition(positions, startX + charInfo.getWidth(), 0.0f);
            textCoords.add(rightTextureCoord);
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);

            startX += charInfo.getWidth();
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));

        return mesh;
    }

    private static void setPosition(List<Float> positions, float startX, float height) {
        positions.add(startX); //x
        positions.add(height); //y
        positions.add(ZPOS);   //z
    }

    public void setText(String text) {
        this.text = text;
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh());
    }

    public void setColor(Color color) {
        this.getMesh().getMaterial().setColour(new Vector3f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255));
    }
}