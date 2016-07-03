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
import lombok.Setter;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class DescriptionCarArea {

    private DescriptionCarItem maxSpeedItem;
    private DescriptionCarItem accelerationItem;
    private DescriptionCarItem controllabilityItem;

    private GameItem[] allDescriptionItems;
    private DescriptionCarItem[] descriptionCarItems;

    @Setter
    private float maxSpeed = 100f;
    @Setter
    private float acceleration = 100f;
    @Setter
    private float controllability = 1f;

    public DescriptionCarArea() throws Exception {
        maxSpeedItem = new DescriptionCarItem("Max speed", maxSpeed);
        accelerationItem = new DescriptionCarItem("Acceleration", acceleration);
        controllabilityItem = new DescriptionCarItem("Controllability", controllability);

        descriptionCarItems = new DescriptionCarItem[]{maxSpeedItem, accelerationItem, controllabilityItem};

        initItems();
    }

    private void initItems() {
        List<GameItem> allItems = new ArrayList();

        for (DescriptionCarItem item : descriptionCarItems) {
            allItems.addAll(Arrays.asList(item.getItems()));
        }
        GameItem[] stockArr = new GameItem[allItems.size()];
        allDescriptionItems = allItems.toArray(stockArr);
    }


    public void updateSize(Window window) {
        int indentText = DescriptionCarItem.FONT.getSize() + 5;
        int heightIndent = window.getHeight() / 12;
        int widthIndentText = window.getWidth()/20;

        for (int i = 0; i < descriptionCarItems.length; i++) {
            DescriptionCarItem item = descriptionCarItems[i];
            item.getCharacteristic().setPosition(widthIndentText, heightIndent + indentText * i);

            float scaleX = item.getScaleRectangle().getScale().x;
            item.getScaleRectangle().setPosition(200f + scaleX, heightIndent + indentText * i + 10, 0);
        }
    }

    public void updateScale() throws Exception {
        if (maxSpeedItem.getScaleFilling() != maxSpeed) {
            Vector3f oldScale = maxSpeedItem.getScaleRectangle().getScale();
            oldScale.x = maxSpeed;
            maxSpeedItem.getScaleRectangle().setScale(oldScale.x, oldScale.y, oldScale.z);
        }
        if (accelerationItem.getScaleFilling() != acceleration) {
            Vector3f oldScale = accelerationItem.getScaleRectangle().getScale();
            oldScale.x = acceleration;
            accelerationItem.getScaleRectangle().setScale(oldScale.x, oldScale.y, oldScale.z);
        }
        if (controllabilityItem.getScaleFilling() != controllability) {
            //TODO terribly
            Vector3f oldScale = controllabilityItem.getScaleRectangle().getScale();
            oldScale.x = controllability;

            Material material = new Material(new Texture(TextureConstants.GRADIENT_RECT_PNG), 1f);

            OBJLoader.setOffset(-(100f - controllability)/100f, 0);
            Mesh mesh = OBJLoader.loadMesh(ObjConstants.FILLING_RECTANGLE);
            mesh.setMaterial(material);
            controllabilityItem.getScaleRectangle().setMesh(mesh);
            controllabilityItem.getScaleRectangle().setScale(oldScale.x, oldScale.y, oldScale.z);
        }
    }

    public void cleanup() {
        GameItem[] gameItems = getAllDescriptionItems();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
