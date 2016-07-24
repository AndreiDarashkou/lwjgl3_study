package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.hud.AbstractHud;
import com.game.engine.items.CompositeGameItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class DescriptionCarArea extends AbstractHud {

    private DescriptionCarItem maxSpeedItem;
    private DescriptionCarItem accelerationItem;
    private DescriptionCarItem controllabilityItem;

    private List<DescriptionCarItem> descriptionCarItems = new ArrayList<>();

    @Setter
    private float maxSpeed = 100f;
    @Setter
    private float acceleration = 100f;
    @Setter
    private float controllability = 1f;

    public DescriptionCarArea() throws Exception {
        initDescriptionCarItems();
        initItemList();
    }

    private void initDescriptionCarItems() throws Exception {
        maxSpeedItem = new DescriptionCarItem("Max speed", maxSpeed);
        accelerationItem = new DescriptionCarItem("Acceleration", acceleration);
        controllabilityItem = new DescriptionCarItem("Controllability", controllability);

        descriptionCarItems.add(maxSpeedItem);
        descriptionCarItems.add(accelerationItem);
        descriptionCarItems.add(controllabilityItem);
    }

    private void initItemList() {
        for (DescriptionCarItem item : descriptionCarItems) {
            itemsList.addAll(Arrays.asList(item.getItems()));
        }
    }


    public void updateSize(Window window) {
        int indentText = DescriptionCarItem.FONT.getSize() + 5;
        int heightIndent = 20;
        int widthIndentText = 20;

        for (int i = 0; i < descriptionCarItems.size(); i++) {
            DescriptionCarItem item = descriptionCarItems.get(i);
            item.getCharacteristic().setPosition(widthIndentText, heightIndent + indentText * i);

            float scaleX = item.getScaleRectangle().getScale().x;
            item.getScaleRectangle().setPosition(170f + scaleX, heightIndent + indentText * i + 10, 0);
        }
    }

    public void updateScale() throws Exception {
        if (maxSpeedItem.getScaleFilling() != maxSpeed) {
            maxSpeedItem.updateSize(maxSpeed);
        }
        if (accelerationItem.getScaleFilling() != acceleration) {
            accelerationItem.updateSize(acceleration);
        }
        if (controllabilityItem.getScaleFilling() != controllability) {
            controllabilityItem.updateSize(controllability);
        }
    }

}
