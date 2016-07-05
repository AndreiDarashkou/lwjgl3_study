package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Hud;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.Texture;
import com.game.engine.items.GameItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class GarageHud implements Hud {

    private DescriptionCarArea descriptionCarArea;
    private ScrollBarArea scrollBarArea;

    private GameItem backGround;

    public GarageHud(Window window) throws Exception {
        descriptionCarArea = new DescriptionCarArea();
        scrollBarArea = new ScrollBarArea(window);

        Mesh backGroundMesh = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.GARAGE), 1f);
        backGroundMesh.setMaterial(material);
        backGround = new GameItem(backGroundMesh);

        updateBackGround(window);
    }

    @Override
    public GameItem[] getGameItems() {
        GameItem[] desc = descriptionCarArea.getAllDescriptionItems();
        GameItem[] scroll = scrollBarArea.getAllScrollItems();

        return (GameItem[]) ArrayUtils.add(ArrayUtils.addAll(desc, scroll), backGround);
    }


    public void updateSize(Window window) {
        descriptionCarArea.updateSize(window);
        scrollBarArea.updateSize(window);
        updateBackGround(window);
    }

    public void updateState() throws Exception {
        descriptionCarArea.updateScale();
    }

    private void updateBackGround(Window window) {
        backGround.setPosition(window.getWidth() / 2, window.getHeight() / 2, 0f);
        backGround.setScale(window.getWidth()/2, window.getHeight() / 2, 0f);
    }


}
