package com.game.engine.graphics;

import com.game.engine.graphics.light.SceneLight;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scene {
    private GameItem[] gameItems;
    private SkyBox skyBox;
    private SceneLight sceneLight;
}
