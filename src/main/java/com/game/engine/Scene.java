package com.game.engine;

import com.game.engine.graphics.Fog;
import com.game.engine.graphics.Mesh;
import com.game.engine.items.GameItemImpl;
import com.game.engine.items.SkyBox;
import com.game.engine.graphics.light.SceneLight;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Scene {
    private Map<Mesh, List<GameItemImpl>> meshMap = new HashMap<>();
    private GameItemImpl[] gameItems;
    private SkyBox skyBox;
    private SceneLight sceneLight;
    private Fog fog = Fog.NO_FOG;

    public void setGameItems(GameItemImpl[] gameItems) {
        meshMap.clear();
        int numGameItems = gameItems != null ? gameItems.length : 0;

        for (int i = 0; i < numGameItems; i++) {
            GameItemImpl gameItem = gameItems[i];
            Mesh mesh = gameItem.getMesh();
            List<GameItemImpl> list = meshMap.get(mesh);
            if (list == null) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public void cleanup() {
        meshMap.keySet().forEach(Mesh::cleanup);
    }
}
