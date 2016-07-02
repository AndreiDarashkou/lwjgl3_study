package com.game.test_game.menu;

import com.game.engine.Window;
import com.game.engine.graphics.FontTexture;
import com.game.engine.graphics.Hud;
import com.game.engine.input.MouseInput;
import com.game.engine.items.GameItem;
import com.game.test_game.MainGameLogic;
import com.game.test_game.menu.command.Commands;

import java.awt.*;

public class MenuHud implements Hud {

    private static final Font FONT = new Font("Cooper Black", Font.PLAIN, 24);
    private static final String CHARSET = "ISO-8859-1";
    private final MenuTextItem[] menuItems;

    public MenuHud() throws Exception {
        FontTexture font = new FontTexture(FONT, CHARSET);

        MenuTextItem newGame = new MenuTextItem(" New Game  ", font, Commands.NEW_GAME);
        MenuTextItem loadGame = new MenuTextItem(" Load Game ", font, Commands.LOAD_GAME);
        MenuTextItem configuration = new MenuTextItem(" Configuration ", font, Commands.CONFIGURATION);
        MenuTextItem exit = new MenuTextItem("Exit", font, Commands.EXIT);

        menuItems = new MenuTextItem[]{newGame, loadGame, configuration, exit};
    }

    public MenuHud(MenuTextItem[] menuItems) throws Exception {
        this.menuItems = menuItems;
    }

    @Override
    public GameItem[] getGameItems() {
        return menuItems;
    }

    public void updateSize(Window window) {
        int width = window.getWidth();
        int height = window.getHeight();

        for (int i = 0; i < menuItems.length; i++) {
            MenuTextItem item = menuItems[i];
            item.setPosition(width / 2 - item.getWidth() / 2, height / 4 + item.getHeight() * i, 0);
        }
    }

    public void update(Window window, MouseInput mouseInput) throws Exception {
        checkMouseHoverMenu(window, mouseInput);
        checkMouseClickMenu(window, mouseInput);
    }

    public MenuTextItem getSelected() {
        for (MenuTextItem menuItem : menuItems) {
            if (menuItem.isSelected()) {
                return menuItem;
            }
        }
        return null;
    }

    private void checkMouseClickMenu(Window window, MouseInput mouseInput) throws Exception {
        if (mouseInput.isLeftButtonPressed()) {
            for (MenuTextItem item : menuItems) {
                if (item.isHover() && !item.isSelected()) {
                    item.execute();
                    System.out.println(item.getText().trim());
                    MainGameLogic.changeGameLogic(window);
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

    private void resetSelected() {
        for (MenuTextItem menuItem : menuItems) {
            if (menuItem.isSelected()) {
                menuItem.setSelected(false);
                break;
            }
        }
    }

}
