package com.game.engine.items;


import com.game.engine.graphics.HeightMapMesh;
import lombok.Getter;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

@Getter
public class Terrain {

    private final int terrainSize;
    private final int verticesPerCol;
    private final int verticesPerRow;

    private final GameItemImpl[] gameItems;
    private final HeightMapMesh heightMapMesh;
    private final Rectangle2D.Float[][] boundingBoxes;

    /**
     * A Terrain is composed by blocks, each block is a GameItemImpl constructed
     * from a HeightMap.
     *
     * @param terrainSize The number of blocks will be terrainSize * terrainSize
     * @param scale The scale to be applied to each terrain block
     * @param minY The minimum y value, before scaling, of each terrain block
     * @param maxY The maximum y value, before scaling, of each terrain block
     * @param heightMapFile
     * @param textureFile
     * @param textInc
     * @throws Exception
     */
    public Terrain(int terrainSize, float scale, float minY, float maxY, String heightMapFile, String textureFile, int textInc) throws Exception {
        this.terrainSize = terrainSize;
        gameItems = new GameItemImpl[terrainSize * terrainSize];

        BufferedImage heightMapImage = ImageIO.read(getClass().getResourceAsStream(heightMapFile));
        verticesPerCol = heightMapImage.getWidth() - 1;
        verticesPerRow = heightMapImage.getHeight() - 1;

        heightMapMesh = new HeightMapMesh(minY, maxY, heightMapImage, textureFile, textInc);
        boundingBoxes = new Rectangle2D.Float[terrainSize][terrainSize];
        for (int row = 0; row < terrainSize; row++) {
            for (int col = 0; col < terrainSize; col++) {
                float xDisplacement = (col - ((float) terrainSize - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) terrainSize - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

                GameItemImpl terrainBlock = new GameItemImpl(heightMapMesh.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPosition(xDisplacement, 0, zDisplacement);
                gameItems[row * terrainSize + col] = terrainBlock;

                boundingBoxes[row][col] = getBoundingBox(terrainBlock);
            }
        }
    }

    public float getHeight(Vector3f position) {
        // For each terrain block we get the bounding box, translate it to view coodinates
        // and check if the position is contained in that bounding box
        Rectangle2D.Float boundingBox = null;
        boolean found = false;
        GameItemImpl terrainBlock = null;
        for (int row = 0; row < terrainSize && !found; row++) {
            for (int col = 0; col < terrainSize && !found; col++) {
                terrainBlock = gameItems[row * terrainSize + col];
                boundingBox = boundingBoxes[row][col];
                found = boundingBox.contains(position.x, position.z);
            }
        }

        float result = Float.MIN_VALUE;
        // If we have found a terrain block that contains the position we need
        // to calculate the height of the terrain on that position
        if (found) {
            Vector3f[] triangle = getTriangle(position, boundingBox, terrainBlock);
            result = interpolateHeight(triangle[0], triangle[1], triangle[2], position.x, position.z);
        }

        return result;
    }

    protected Vector3f[] getTriangle(Vector3f position, Rectangle2D.Float boundingBox, GameItemImpl terrainBlock) {
        // Get the column and row of the heightmap associated to the current position
        float cellWidth = boundingBox.width / (float) verticesPerCol;
        float cellHeight = boundingBox.height / (float) verticesPerRow;
        int col = (int) ((position.x - boundingBox.x) / cellWidth);
        int row = (int) ((position.z - boundingBox.y) / cellHeight);

        Vector3f[] triangle = new Vector3f[3];
        triangle[1] = new Vector3f(
                boundingBox.x + col * cellWidth,
                getWorldHeight(row + 1, col, terrainBlock),
                boundingBox.y + (row + 1) * cellHeight);

        triangle[2] = new Vector3f(
                boundingBox.x + (col + 1) * cellWidth,
                getWorldHeight(row, col + 1, terrainBlock),
                boundingBox.y + row * cellHeight);

        if (position.z < getDiagonalZCoord(triangle[1].x, triangle[1].z, triangle[2].x, triangle[2].z, position.x)) {
            triangle[0] = new Vector3f(
                    boundingBox.x + col * cellWidth,
                    getWorldHeight(row, col, terrainBlock),
                    boundingBox.y + row * cellHeight);
        } else {
            triangle[0] = new Vector3f(
                    boundingBox.x + (col + 1) * cellWidth,
                    getWorldHeight(row + 2, col + 1, terrainBlock),
                    boundingBox.y + (row + 1) * cellHeight);
        }

        return triangle;
    }

    protected float getDiagonalZCoord(float x1, float z1, float x2, float z2, float x) {
        float z = ((z1 - z2) / (x1 - x2)) * (x - x1) + z1;
        return z;
    }

    protected float getWorldHeight(int row, int col, GameItemImpl gameItem) {
        float y = heightMapMesh.getHeight(row, col);
        return y * gameItem.getScale().y + gameItem.getPosition().y;
    }

    protected float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z) {
        // Plane equation ax+by+cz+d=0
        float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
        float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
        float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
        float d = -(a * pA.x + b * pA.y + c * pA.z);
        // y = (-d -ax -cz) / b
        float y = (-d - a * x - c * z) / b;
        return y;
    }

    /**
     * Gets the bounding box of a terrain block
     *
     * @param terrainBlock A GameItemImpl instance that defines the terrain block
     * @return The boundingg box of the terrain block
     */
    private Rectangle2D.Float getBoundingBox(GameItemImpl terrainBlock) {
        float scale = terrainBlock.getScale().x;
        Vector3f position = terrainBlock.getPosition();

        float topLeftX = HeightMapMesh.STARTX * scale + position.x;
        float topLeftZ = HeightMapMesh.STARTZ * scale + position.z;
        float width = Math.abs(HeightMapMesh.STARTX * 2) * scale;
        float height = Math.abs(HeightMapMesh.STARTZ * 2) * scale;
        Rectangle2D.Float boundingBox = new Rectangle2D.Float(topLeftX, topLeftZ, width, height);
        return boundingBox;
    }
}
