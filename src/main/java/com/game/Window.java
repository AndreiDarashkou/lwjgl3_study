package com.game;

import com.game.input.KeyboardHandler;
import com.game.input.MouseHandler;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Getter
@Setter
public class Window {

    private static final double MS_PER_UPDATE = 1000f/50;

    private int width = 500;
    private int height = 500;
    private long windowHandler;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback mouseCallback;
    private GLFWWindowSizeCallback windowSizeCallback;
    private boolean resized;

    public Window(String windowTitle, int width, int height, boolean vsSync) {
        this.width = width;
        this.height = height;
        windowHandler = init(windowTitle, vsSync);
    }

    public long init(String windowTitle, boolean vsSync) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandler = glfwCreateWindow(width, height, windowTitle, NULL, NULL);
        if (windowHandler == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(windowHandler, keyCallback = new KeyboardHandler());
        glfwSetCursorPosCallback(windowHandler, mouseCallback = new MouseHandler());

        glfwSetWindowSizeCallback(windowHandler, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowHandler, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        glfwMakeContextCurrent(windowHandler);
        glfwSwapInterval(1);

        glfwShowWindow(windowHandler);

        return windowHandler;
    }

    public void setClearColor(float red, float blue, float green, float alpha) {
        
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindowHandler() {
        return windowHandler;
    }
}
