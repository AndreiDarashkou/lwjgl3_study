package com.game.test_game.game_logic.garage;

import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.graphics.texture.TextureSetting;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.joml.Vector2f;

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

        TextureSetting setting = new TextureSetting();
        setting.setOffset(new Vector2f(-(1f - scaleFilling/100f), 0f));

        Texture texture = new Texture(TextureConstants.GRADIENT_RECT_PNG);
        texture.setTextureSetting(setting);

        Mesh mesh = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        mesh.setMaterial(new Material(texture, 1f));

        scaleRectangle.setMesh(mesh);
        scaleRectangle.setScale(scaleFilling, 5, 1);
    }

    public GameItem[] getItems() {
        return new GameItem[]{characteristic, scaleRectangle};
    }

    public void cleanup() {
        characteristic.getMesh().cleanUp();
        scaleRectangle.getMesh().cleanUp();
    }
}
