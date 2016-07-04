package com.game.test_game.game_logic.garage;

import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.Texture;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;

import java.awt.Font;

@Getter
public class DescriptionCarItem {

    public static final Font FONT = new Font("Cooper Black", Font.ITALIC, 16);
    private static final String CHARSET = "ISO-8859-1";
    private final FontTexture font;

    private TextItem characteristic;
    private GameItem scaleRectangle;
    private double scaleFilling;

    public DescriptionCarItem(String characteristicName, float scaleFilling) throws Exception {
        this.scaleFilling = scaleFilling;

        font = new FontTexture(FONT, CHARSET);
        characteristic = new TextItem(characteristicName, font);
        scaleRectangle = new GameItem();

        updateMesh(scaleFilling);
    }

    public void updateMesh(float scaleFilling) throws Exception {
        //TODO use color instead of texture
        Material material = new Material(new Texture(TextureConstants.GRADIENT_RECT_PNG), 1f);
        OBJLoader.setOffset(-(100f - scaleFilling)/100f, 0);
        Mesh mesh = OBJLoader.loadMesh(ObjConstants.FILLING_RECTANGLE);
        mesh.setMaterial(material);

        scaleRectangle.setMesh(mesh);
        scaleRectangle.setScale(scaleFilling, 80, 1);
    }

    public GameItem[] getItems() {
        return new GameItem[]{characteristic, scaleRectangle};
    }

    public void cleanup() {
        characteristic.getMesh().cleanUp();
        scaleRectangle.getMesh().cleanUp();
    }
}
