package com.game.test_game.game_logic.garage;

import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.graphics.texture.TextureSetting;
import com.game.engine.hud.HudGameItem;
import com.game.engine.hud.HudGameItemImpl;
import com.game.engine.items.GameItemImpl;
import com.game.engine.items.TextItemImpl;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.joml.Vector2f;

import java.awt.Font;

@Getter
public class DescriptionCarItem {

    public static final Font FONT = new Font("Cooper Black", Font.ITALIC, 16);
    public static final String CHARSET = "ISO-8859-1";
    private final FontTexture font;

    private TextItemImpl characteristic;
    private HudGameItem scaleRectangle;
    private double scaleFilling;

    public DescriptionCarItem(String characteristicName, float scaleFilling) throws Exception {
        this.scaleFilling = scaleFilling;

        font = new FontTexture(FONT, CHARSET);
        characteristic = new TextItemImpl(characteristicName, font);
        createScaleRectangle();
    }

    private void createScaleRectangle() throws Exception {
        Texture texture = new Texture(TextureConstants.GRADIENT_RECT_PNG);
        Mesh mesh = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        mesh.setMaterial(new Material(texture, 1f));
        scaleRectangle = new HudGameItemImpl(mesh);
        scaleRectangle.setScale(100, 5, 0);
    }

    public void updateSize(float scaleFilling) throws Exception {
        TextureSetting setting = new TextureSetting();
        setting.setOffset(new Vector2f(-(1f - scaleFilling / 100f), 0f));

        scaleRectangle.getMesh().getMaterial().getTexture().setTextureSetting(setting);
        scaleRectangle.setScale(scaleFilling, 5, 0);
        scaleRectangle.getPosition().x = 170f + scaleFilling;
    }

    public HudGameItem[] getItems() {
        return new HudGameItem[]{characteristic, scaleRectangle};
    }

    public void cleanup() {
        characteristic.getMesh().cleanup();
        scaleRectangle.getMesh().cleanup();
    }
}
