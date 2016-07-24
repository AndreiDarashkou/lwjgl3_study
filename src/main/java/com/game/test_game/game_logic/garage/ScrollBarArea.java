package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.graphics.texture.TextureSetting;
import com.game.engine.hud.AbstractHud;
import com.game.engine.hud.HudGameItem;
import com.game.engine.hud.HudGameItemImpl;
import com.game.engine.input.MouseInput;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.joml.Vector2f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScrollBarArea extends AbstractHud {

    private static float delta;

    private HudGameItem buttonUpItem;
    private HudGameItem buttonDownItem;
    private HudGameItem carsScrollItem;
    private List<HudGameItem> scrollBorder;

    private int visibleCars = 4;
    private int allCars = 6;
    private int currentCar = 0;

    public ScrollBarArea(Window window) throws Exception {

        buttonUpItem = createButton();
        buttonUpItem.setRotation(0, 0, 180);
        buttonDownItem = createButton();
        carsScrollItem = createCarsScroll(window);
        scrollBorder = createScrollBorder(window);

        updateSize(window);

        itemsList.add(buttonUpItem);
        itemsList.add(buttonDownItem);
        itemsList.add(carsScrollItem);
        itemsList.addAll(scrollBorder);
    }

    @Override
    public void update(Window window, MouseInput mouseInput) throws Exception {
        boolean isButtonUp = buttonUpItem.isHovered(mouseInput);
        buttonUpItem.getMesh().getMaterial().setColor(isButtonUp ? Color.ORANGE : Color.GREEN);

        boolean isButtonDown = buttonDownItem.isHovered(mouseInput);
        buttonDownItem.getMesh().getMaterial().setColor(isButtonDown ? Color.ORANGE : Color.GREEN);

        if ((isButtonUp || isButtonDown) && mouseInput.isLeftButtonClicked()) {
            if (isButtonDown && currentCar < allCars - visibleCars) {
                currentCar += 1;
            } else if (isButtonUp && currentCar > 0) {
                currentCar -= 1;
            }
            setActualCar();
        }

    }

    public void updateSize(Window window) {
        buttonUpItem.setPosition(window.getWidth() - 110, window.getHeight() / 12);
        buttonDownItem.setPosition(window.getWidth() - 110, 11 * window.getHeight() / 12);
        carsScrollItem.setPosition(window.getWidth() - 110, window.getHeight() / 2, 0.01f);
        visibleCars = window.getHeight() / 200;
        setActualCar();
    }

    private void setActualCar() {
        delta = (float) currentCar / allCars;
        TextureSetting setting = new TextureSetting();
        setting.setOffset(new Vector2f(0f, delta));
        setting.setVisibleMin(new Vector2f(0.0f, 0.0f + delta));
        setting.setVisibleMax(new Vector2f(1.0f, ((float) visibleCars / allCars) + delta));
        carsScrollItem.getMesh().getMaterial().getTexture().setTextureSetting(setting);
        carsScrollItem.setScale(90, visibleCars * 80, 0);
    }

    private HudGameItem createButton() throws Exception {
        Mesh button = OBJLoader.loadMesh(ObjConstants.TRIANGLE);
        Material material = new Material(Color.blue, 1f);
        button.setMaterial(material);

        HudGameItemImpl buttonItem = new HudGameItemImpl(button);
        buttonItem.setScale(80, 20, 0);
        //TODO hmm
        buttonItem.setWidth(80);
        buttonItem.setHeight(25);

        return buttonItem;
    }

    private HudGameItem createCarsScroll(Window window) throws Exception {
        Mesh carsScroll = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.CARS_SCROLL), 1f);
        carsScroll.setMaterial(material);

        HudGameItemImpl scrollItem = new HudGameItemImpl(carsScroll);
        scrollItem.setScale(100, window.getHeight() / 3, 0);

        return scrollItem;
    }

    private List<HudGameItem> createScrollBorder(Window window) throws Exception {
        Mesh border = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.SCROLL_BORDER), 1f);
        border.setMaterial(material);

        List<HudGameItem> borders = new ArrayList<>();
        for (int i = 0; i < visibleCars; i++) {
            HudGameItemImpl borderItem = new HudGameItemImpl(border);
            borderItem.setPosition(window.getWidth() - 110, window.getHeight() / 4 + i * 160 - 20, 0.02f);
            borderItem.setScale(100, 95, 0);

            borders.add(borderItem);
        }

        return borders;
    }

}
