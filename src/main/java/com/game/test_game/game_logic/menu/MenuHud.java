package com.game.test_game.game_logic.menu;

import com.game.engine.Window;
import com.game.engine.hud.AbstractHud;
import com.game.engine.hud.Hud;
import com.game.engine.input.MouseInput;
import com.game.engine.items.CompositeGameItem;

public class MenuHud extends AbstractHud {

    MenuTextItem[] menuItems;

    protected MenuHud(MenuTextItem[] menuItems) throws Exception {
        this.menuItems = menuItems;
        for (MenuTextItem item : menuItems) {
            itemsList.add(item);
        }
    }

    public void updateSize(Window window) {
        float width = window.getWidth();
        float height = window.getHeight();

        for (int i = 0; i < menuItems.length; i++) {
            MenuTextItem item = menuItems[i];
            item.setPosition(width / 2 - item.getWidth() / 2, height / 3 + item.getHeight() * i);
        }
    }

    public void update(Window window, MouseInput mouseInput) throws Exception {
        checkMouseHoverMenu(mouseInput);
        checkMouseClickMenu(mouseInput);
    }

    protected void checkMouseClickMenu(MouseInput mouseInput) throws Exception {
        if (mouseInput.isLeftButtonClicked()) {
            for (MenuTextItem item : menuItems) {
                if (item.isHover()) {
                    item.execute();
                    return;
                }
            }
        }
    }

    protected void checkMouseHoverMenu(MouseInput mouseInput) {
        double mouseX = mouseInput.getCurrentPos().x;
        double mouseY = mouseInput.getCurrentPos().y;

        for (MenuTextItem item : menuItems) {
            item.setHover(item.isHovered(mouseInput));
        }
    }

}
