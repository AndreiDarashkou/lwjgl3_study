package com.game.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseHandler extends GLFWCursorPosCallback {

    private static final MousePosition mousePosition = new MousePosition();

    @Override
    public void invoke(long window, double xPos, double yPos) {
        mousePosition.update(xPos, yPos);
    }

    public static MousePosition getPosition() {
        return mousePosition;
    }
}