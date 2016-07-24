package com.game.engine.items;

import com.game.engine.hud.HudGameItem;
import org.joml.Vector3f;

import java.awt.Color;

public interface TextItem extends HudGameItem {

    public void setText(String text);

    public void setColor(Color color);

}
