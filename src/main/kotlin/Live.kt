package org.example

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.lang.Thread.sleep


class Live(private var window: Long = 0) {

    fun run(){
        init()
        loop()

        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun init(){

        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)


        window = glfwCreateWindow(800, 800, "Hello World!", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create the GLFW window")

        glfwSetKeyCallback(window) { window, key, scancode, action, mods  ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true)
        }

        glfwMakeContextCurrent(window)
        glfwSwapInterval(1)
        glfwShowWindow(window)
    }


    private fun loop(){
        GL.createCapabilities()

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glOrtho(0.0, 40.0, 40.0, 0.0, -1.0, 1.0)
        val grid = Array(40) { Array(40) { false } }

        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            drawGrid(grid)
            // grid = nextGeneration(grid)
            sleep(200)

            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    private fun drawGrid(grid: Array<Array<Boolean>>) {
        for (x in grid.indices){
            for (y in grid[x].indices){
                glColor3f(
                    0f,
                    if (grid[x][y]) 1.0f else 0f,
                    0f
                )
                glBegin(GL_QUADS)
                glVertex2f(x.toFloat(), y.toFloat())
                glVertex2f(x + 1f, y.toFloat())
                glVertex2f(x + 1f, y + 1f)
                glVertex2f(x.toFloat(), y + 1f)
                glEnd()
            }
        }
    }

}