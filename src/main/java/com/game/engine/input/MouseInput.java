package com.game.engine.input;

import com.game.engine.Window;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

@Getter
public class MouseInput {

    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private final Vector2f displayVector;
    private boolean inWindow = false;
    private boolean leftButtonReleased = false;
    private boolean rightButtonReleased = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    @Setter
    private boolean leftButtonClicked = false;

    //we should keep links or collector will destroy them
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWCursorEnterCallback cursorEnterCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;

    public MouseInput() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displayVector = new Vector2f();
    }

    public void init(Window window) {
        //moved
        glfwSetCursorPosCallback(window.getWindowHandle(), cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                currentPos.x = xpos;
                currentPos.y = ypos;
            }
        });
        //track position in our window
        glfwSetCursorEnterCallback(window.getWindowHandle(), cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                inWindow = entered;
            }
        });
        //button pressed
        glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                leftButtonReleased = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE;
                rightButtonReleased = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_RELEASE;

                leftButtonClicked = leftButtonPressed && leftButtonReleased;

                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            }
        });
    }

    public void input(Window window) {
        displayVector.x = 0;
        displayVector.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displayVector.y = (float) deltax;
            }
            if (rotateY) {
                displayVector.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isLeftButtonClicked() {
        if (leftButtonClicked) {
            leftButtonClicked = false;
            return true;
        }
        return false;
    }

}
