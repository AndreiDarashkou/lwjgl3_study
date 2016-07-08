package com.game.test_game.game_logic.garage;

import com.game.engine.GameEngine;
import com.game.engine.Window;
import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.hud.AbstractHud;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.hud.Hud;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.items.CompositeGameItem;
import com.game.engine.items.GameItem;
import com.game.engine.items.TextItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.game.test_game.game_logic.garage.DescriptionCarItem.CHARSET;
import static com.game.test_game.game_logic.garage.DescriptionCarItem.FONT;

@Getter
@Setter
public class GarageHud extends CompositeGameItem implements Hud {

    private DescriptionCarArea descriptionCarArea;
    private ScrollBarArea scrollBarArea;
    private GameItem backGround;

    public GarageHud(Window window) throws Exception {
        descriptionCarArea = new DescriptionCarArea();
        scrollBarArea = new ScrollBarArea(window);
        createBackGround(window);

        itemsList.addAll(descriptionCarArea.getGameItems());
        itemsList.addAll(scrollBarArea.getGameItems());
        itemsList.add(backGround);
    }

    public void updateSize(Window window) {
        descriptionCarArea.updateSize(window);
        scrollBarArea.updateSize(window);
        updateBackGround(window);
        window.setResized(false);
    }

    public void updateState() throws Exception {
        descriptionCarArea.updateScale();
    }

    private void createBackGround(Window window) throws Exception {
        Mesh backGroundMesh = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.GARAGE), 1f);
        backGroundMesh.setMaterial(material);
        backGround = new GameItem(backGroundMesh);

        updateBackGround(window);
    }

    private void updateBackGround(Window window) {
        backGround.setPosition(window.getWidth() / 2, window.getHeight() / 2, 0f);
        backGround.setScale(window.getWidth() / 2, window.getHeight() / 2, 0f);
    }
}
