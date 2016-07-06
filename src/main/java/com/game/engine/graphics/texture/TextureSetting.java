package com.game.engine.graphics.texture;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

@Getter
@Setter
public class TextureSetting {
    private Vector2f offset = new Vector2f();
    private Vector2f visibleMin = new Vector2f();
    private Vector2f visibleMax = new Vector2f(1.0f, 1.0f);
    private boolean isUsed;

    public boolean isUsed() {
        return offset.x != 0 || offset.y != 0 || visibleMin.x != 0 || visibleMin.y != 0 || visibleMax.x != 1f || visibleMax.y != 1f;
    }
}
