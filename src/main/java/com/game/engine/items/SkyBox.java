package com.game.engine.items;

import com.game.engine.graphics.Material;
import com.game.engine.graphics.Mesh;
import com.game.engine.loader.obj.OBJLoader;
import com.game.engine.graphics.texture.Texture;

public class SkyBox extends GameItemImpl {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxTexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }

}