package org.example

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.lang.Thread.sleep



class Live(private var window: Long = 0) {

    private var cameraX = 0f
    private var cameraY = 0f
    private var zoom = 1f
    private val cellSize = 10.0

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

        glClearColor(0.3f, 0.3f, 0.3f, 0.0f)
        glOrtho(0.0, 80.0, 80.0, 0.0, -1.0, 1.0)
        var grid = Array(200) { Array(200) { false } }

        grid[5][5] = true
        grid[5][7] = true
        grid[4][6] = true
        grid[6][6] = true


        var isPlay = true

        glfwSetKeyCallback(window) { window, key, scancode, action, mods  ->
            if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
                isPlay = !isPlay

            if (key == GLFW_KEY_W && action == GLFW_RELEASE)
                cameraY -= 2f / zoom
            if (key == GLFW_KEY_S && action == GLFW_RELEASE)
                cameraY += 2f / zoom
            if (key == GLFW_KEY_A && action == GLFW_RELEASE)
                cameraX -= 2f / zoom
            if (key == GLFW_KEY_D && action == GLFW_RELEASE)
                cameraX += 2f / zoom

            if (key == GLFW_KEY_UP && action == GLFW_RELEASE)
                zoom *= 1.05f
            if (key == GLFW_KEY_DOWN && action == GLFW_RELEASE)
                zoom *= 0.95f
        }

        glfwSetMouseButtonCallback(window) { _, button, action, _ ->
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                val xpos = DoubleArray(1)
                val ypos = DoubleArray(1)
                glfwGetCursorPos(window, xpos, ypos)

                val mouseX = (xpos[0] / zoom + cameraX * cellSize).toFloat()
                val mouseY = (ypos[0] / zoom + cameraY * cellSize).toFloat()

                //println("${xpos[0]} ${cameraX} = ${mouseX}")

                val gridX = (mouseX / cellSize).toInt()
                val gridY = (mouseY / cellSize).toInt()

                println("${mouseX} / ${cellSize} = ${gridX}   ${mouseY} / ${cellSize} = ${gridY}")

                if (gridX in grid.indices && gridY in grid[0].indices) {
                    grid[gridX][gridY] = !grid[gridX][gridY]
                }
            }
        }

        while(!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            glPushMatrix()
            glScalef(zoom, zoom, 1f)
            glTranslatef(-cameraX, -cameraY, 0f)

            drawGrid(grid)

            glPopMatrix()

            if (isPlay){
                grid = nextGeneration(grid)
                sleep(100)
            }

            sleep(20)

            glfwSwapBuffers(window)
            glfwPollEvents()
        }
    }

    private fun drawGrid(grid: Array<Array<Boolean>>) {
        for (x in grid.indices){
            for (y in grid[x].indices){
                glColor3f(
                    if (grid[x][y]) 0.1f else 0f,
                    if (grid[x][y]) 0.9f else 0f,
                    if (grid[x][y]) 0.1f else 0f
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