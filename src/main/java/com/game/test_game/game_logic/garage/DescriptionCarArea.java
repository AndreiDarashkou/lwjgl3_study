package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.items.GameItem;
import lombok.Getter;
import lombok.Setter;

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
        int heightIndent = 20;
        int widthIndentText = 20;

        for (int i = 0; i < descriptionCarItems.length; i++) {
            DescriptionCarItem item = descriptionCarItems[i];
            item.getCharacteristic().setPosition(widthIndentText, heightIndent + indentText * i);

            float scaleX = item.getScaleRectangle().getScale().x;
            item.getScaleRectangle().setPosition(widthIndentText + 150f + scaleX, heightIndent + indentText * i + 10, 0);
        }
    }

    public void updateScale() throws Exception {
        if (maxSpeedItem.getScaleFilling() != maxSpeed) {
            maxSpeedItem.updateMesh(controllability);
        }
        if (accelerationItem.getScaleFilling() != acceleration) {
            accelerationItem.updateMesh(controllability);
        }
        if (controllabilityItem.getScaleFilling() != controllability) {
            controllabilityItem.updateMesh(controllability);
        }
    }

    public void cleanup() {
        GameItem[] gameItems = getAllDescriptionItems();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
