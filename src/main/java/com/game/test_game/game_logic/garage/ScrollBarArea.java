package com.game.test_game.game_logic.garage;

import com.game.engine.Window;
import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.graphics.texture.Texture;
import com.game.engine.graphics.texture.TextureSetting;
import com.game.engine.input.MouseInput;
import com.game.engine.items.CompositeGameItem;
import com.game.engine.items.GameItem;
import com.game.engine.loader.obj.OBJLoader;
import com.game.test_game.common.ObjConstants;
import com.game.test_game.common.TextureConstants;
import lombok.Getter;
import org.joml.Vector2f;

@Getter
public class ScrollBarArea extends CompositeGameItem {

    private GameItem buttonUpItem;
    private GameItem buttonDownItem;
    private GameItem carsScrollItem;

    public ScrollBarArea(Window window) throws Exception {

        buttonUpItem = createButton();
        buttonUpItem.setRotation(0, 0, 180);
        buttonDownItem = createButton();
        carsScrollItem = createCarsScroll(window);

        updateSize(window);

        itemsList.add(buttonUpItem);
        itemsList.add(buttonDownItem);
        itemsList.add(carsScrollItem);
    }

    private static float delta;

    public void updateSize(Window window) {
        buttonUpItem.setPosition(window.getWidth() - 110, window.getHeight() / 12);
        buttonDownItem.setPosition(window.getWidth() - 110, 11 * window.getHeight() / 12);
        carsScrollItem.setPosition(window.getWidth() - 110, window.getHeight() / 2);

        //delta -= 0.001f;
        int numCars = (window.getHeight()/2) / 100;
        TextureSetting setting = new TextureSetting();
        setting.setOffset(new Vector2f(0f, delta));
        setting.setVisibleMin(new Vector2f(0.0f, 0.0f + delta));
        setting.setVisibleMax(new Vector2f(1.0f, ((float) numCars / 6) + delta));
        carsScrollItem.getMesh().getMaterial().getTexture().setTextureSetting(setting);
        carsScrollItem.setScale(100, numCars * 80, 0);
        System.out.println(numCars);
    }

    private void checkMouseHoverScroll(Window window, MouseInput mouseInput) {
        //TODO
        double mouseX = mouseInput.getCurrentPos().x;
        double mouseY = mouseInput.getCurrentPos().y;
    }

    private GameItem createButton() throws Exception {
        Mesh button = OBJLoader.loadMesh(ObjConstants.TRIANGLE);
        Material material = new Material(new Texture(TextureConstants.GRADIENT_RECT_PNG), 1f);
        button.setMaterial(material);

        GameItem buttonItem = new GameItem(button);
        buttonItem.setScale(80, 20, 0);

        return buttonItem;
    }

    private GameItem createCarsScroll(Window window) throws Exception {
        Mesh carsScroll = OBJLoader.loadMesh(ObjConstants.RECTANGLE);
        Material material = new Material(new Texture(TextureConstants.CARS_SCROLL), 1f);
        carsScroll.setMaterial(material);

        GameItem scrollItem = new GameItem(carsScroll);
        scrollItem.setScale(100, window.getHeight() / 3, 0);

        return scrollItem;
    }

}
