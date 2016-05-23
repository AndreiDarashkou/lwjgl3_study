package com.game.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseHandler extends GLFWCursorPosCallback {

    private final MousePosition mousePosition = new MousePosition();

    @Override
    public void invoke(long window, double xPos, double yPos) {
        mousePosition.update(xPos, yPos);
    }

    public MousePosition getPosition() {
        return mousePosition;
    }
}