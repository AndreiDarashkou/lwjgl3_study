package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.hud.AbstractHud;
import com.game.engine.hud.Hud;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.hud.HudGameItem;
import com.game.engine.hud.HudGameItemImpl;
import com.game.engine.items.CompositeGameItem;
import com.game.engine.items.GameItemImpl;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GarageHud extends AbstractHud {

    private DescriptionCarArea descriptionCarArea;
    private ScrollBarArea scrollBarArea;
    private HudGameItem backGround;

    public GarageHud(Window window) throws Exception {
        descriptionCarArea = new DescriptionCarArea();
        scrollBarArea = new ScrollBarArea(window);
        createBackGround(window);

        itemsList.addAll(descriptionCarArea.getGameItems());
        itemsList.addAll(scrollBarArea.getGameItems());
        itemsList.add(backGround);
    }

    public void updateSize(Window window) {
        updateBackGround(window);
        descriptionCarArea.updateSize(window);
        scrollBarArea.updateSize(window);
    }

    public void updateState() throws Exception {
        descriptionCarArea.updateScale();
    }

    private void createBackGround(Window window) throws Exception {
        Mesh backGroundMesh = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.GARAGE), 1f);
        backGroundMesh.setMaterial(material);
        backGround = new HudGameItemImpl(backGroundMesh);

        updateBackGround(window);
    }

    private void updateBackGround(Window window) {
        backGround.setPosition(window.getWidth() / 2, window.getHeight() / 2, 0.0f);
        backGround.setScale(window.getWidth() / 2, window.getHeight() / 2, 0.0f);
    }
}
