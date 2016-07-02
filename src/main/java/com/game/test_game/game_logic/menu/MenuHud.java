package com.game.test_game.game_logic.menu;

import com.game.engine.Window;
import com.game.engine.graphics.Hud;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.test_game.MainGameLogic;

public class MenuHud implements Hud {

    MenuTextItem[] menuItems;

    protected MenuHud(MenuTextItem[] menuItems) throws Exception {
        this.menuItems = menuItems;
    }

    @Override
    public GameItem[] getGameItems() {
        return menuItems;
    }

    public void updateSize(com.game.engine.Window window) {
        int width = window.getWidth();
        int height = window.getHeight();

        for (int i = 0; i < menuItems.length; i++) {
            MenuTextItem item = menuItems[i];
            item.setPosition(width / 2 - item.getWidth() / 2, height / 3 + item.getHeight() * i, 0);
        }
    }

    public void update(Window window, MouseInput mouseInput) throws Exception {
        checkMouseHoverMenu(window, mouseInput);
        checkMouseClickMenu(window, mouseInput);
    }

    private void checkMouseClickMenu(Window window, MouseInput mouseInput) throws Exception {
        if (mouseInput.isLeftButtonClicked()) {
            for (MenuTextItem item : menuItems) {
                if (item.isHover()) {
                    item.execute();
                    MainGameLogic.changeGameLogic(window);
                    return;
                }
            }
        }
    }

    private void checkMouseHoverMenu(Window window, MouseInput mouseInput) {
        double mouseX = mouseInput.getCurrentPos().x;
        double mouseY = mouseInput.getCurrentPos().y;

        for (MenuTextItem item : menuItems) {
            double itemX = item.getPosition().x;
            double itemY = item.getPosition().y;

            if (mouseX > itemX && mouseX < window.getWidth() / 2 + item.getWidth()) {
                if (mouseY < itemY + item.getHeight() && mouseY > itemY) {
                    item.setHover(true);
                } else {
                    item.setHover(false);
                }
            } else {
                item.setHover(false);
            }
        }
    }

    private MenuTextItem getSelected() {
        for (MenuTextItem menuItem : menuItems) {
            if (menuItem.isSelected()) {
                return menuItem;
            }
        }
        return null;
    }

    private void resetSelected() {
        for (MenuTextItem menuItem : menuItems) {
            menuItem.setHover(false);
            menuItem.setSelected(false);
        }
    }
}
