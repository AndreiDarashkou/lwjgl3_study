package com.game.test_game.game_logic.garage;

import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.Texture;
import com.game.engine.items.GameItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;

@Getter
public class ScrollBarArea {

    private GameItem[] allScrollItems;

    public ScrollBarArea() throws Exception {
        Texture buttonUpTexture = new Texture(TextureConstants.ARROW_UP);
        Material material = new Material(buttonUpTexture, 1.0f);

        Mesh buttonUp = OBJLoader.loadMesh(ObjConstants.COMPASS);
    }

}
