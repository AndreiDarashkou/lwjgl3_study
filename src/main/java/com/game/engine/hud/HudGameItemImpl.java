package com.game.engine.hud;

import com.game.engine.graphics.Mesh;
import com.game.engine.items.GameItemImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HudGameItemImpl extends GameItemImpl implements HudGameItem {

    protected float width;
    protected float height;

    public HudGameItemImpl(){}

    public HudGameItemImpl(Mesh mesh) {
        super(mesh);
    }

}
