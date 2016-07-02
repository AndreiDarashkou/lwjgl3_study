package com.game.test_game.game_logic.menu;

import com.game.engine.graphics.FontTexture;
import com.game.test_game.game_logic.menu.command.Commands;
import lombok.Getter;

import java.awt.*;

@Getter
public class GameMenuHud {

    private static final Font FONT = new Font("Cooper Black", Font.PLAIN, 32);
    private static final String CHARSET = "ISO-8859-1";
    private final FontTexture font;

    private static MenuHud mainMenu;
    private static MenuHud configurationMenu;
    private static MenuHud currentMenu;

    public GameMenuHud() throws Exception {
        font = new FontTexture(FONT, CHARSET);
        initMainMenu();
        initConfigurationMenu();
        setMainMenu(true);
    }

    private void initMainMenu() throws Exception {
        MenuTextItem newGame = new MenuTextItem(" New Game  ", font, Commands.NEW_GAME);
        MenuTextItem loadGame = new MenuTextItem(" Load Game ", font, Commands.LOAD_GAME);
        MenuTextItem configuration = new MenuTextItem(" Configuration ", font, Commands.CONFIGURATION);
        MenuTextItem exit = new MenuTextItem("Exit", font, Commands.EXIT);

        mainMenu = new MenuHud(new MenuTextItem[]{newGame, loadGame, configuration, exit});
    }

    private void initConfigurationMenu() throws Exception {
        MenuTextItem resolution = new MenuTextItem("Screen Resolution", font, Commands.SCREEN_RESOLUTION);
        MenuTextItem sound = new MenuTextItem("Sound", font, Commands.SOUND);
        MenuTextItem back = new MenuTextItem("Back", font, Commands.BACK);

        configurationMenu = new MenuHud(new MenuTextItem[]{sound, resolution, back});
    }

    public void cleanup() {
        mainMenu.cleanup();
        configurationMenu.cleanup();
    }

    public MenuHud getCurrentMenu() {
        return currentMenu;
    }

    public static void setMainMenu(boolean isMainMenu) {
        if (isMainMenu) {
            currentMenu = mainMenu;
        } else {
            currentMenu = configurationMenu;
        }
    }
}
