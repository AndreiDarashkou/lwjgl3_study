package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.Texture;
import com.game.engine.items.GameItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class ScrollBarArea {

    private GameItem[] allScrollItems;

    private GameItem buttonUpItem;
    private GameItem buttonDownItem;

    public ScrollBarArea(Window window) throws Exception {
        buttonUpItem = createButton();

        buttonDownItem = createButton();
        buttonDownItem.setRotation(0, 0, 180);

        updateSize(window);

        allScrollItems = new GameItem[]{buttonUpItem, buttonDownItem};
    }

    public void updateSize(Window window) {
        buttonUpItem.setPosition(window.getWidth() - 110, 200, 0);
        buttonDownItem.setPosition(window.getWidth() - 110, 70, 0);
    }

    private GameItem createButton() throws Exception {
        Mesh button = OBJLoader.loadMesh(ObjConstants.TRIANGLE);
        Material material = new Material(new Texture(TextureConstants.GRADIENT_RECT_PNG), 1f);
        button.setMaterial(material);

        GameItem buttonItem = new GameItem(button);
        buttonItem.setScale(100, 20, 0);

        return buttonItem;
    }

}
