package com.lawab1ders.nan7i.kali.shader;

/*
 * Copyright (c) 2025 EldoDebug, Nan7i.南起
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

import com.lawab1ders.nan7i.kali.InstanceAccess;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class Shader implements InstanceAccess {

    private final int id;

    protected Shader(String path) throws IOException {
        this.id = glCreateProgram();
        int fid = createShader(path, GL_FRAGMENT_SHADER), vid = createShader("vertex.vsh", GL_VERTEX_SHADER);

        glValidateProgram(id);
        checkError(id, GL_VALIDATE_STATUS);

        glLinkProgram(id);
        checkError(id, GL_LINK_STATUS);

        glDeleteShader(fid);
        glDeleteShader(vid);
        checkError(fid, GL_DELETE_STATUS);
        checkError(vid, GL_DELETE_STATUS);
    }

    private int createShader(String path, int type) throws IOException {
        int id = glCreateShader(type);

        glShaderSource(id, getShaderResource(path));
        glCompileShader(id);
        checkError(id, GL_COMPILE_STATUS);
        glAttachShader(this.id, id);

        return id;
    }

    private String getShaderResource(String path) throws IOException {
        InputStream inputStream =
                mc.getResourceManager().getResource(new ResourceLocation("k40", "shaders/" + path)).getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String source = "";
        for (String s; (s = bufferedReader.readLine()) != null; source += s + System.lineSeparator()) ;

        return source;
    }

    private void checkError(int id, int name) {
        if (glGetShaderi(id, name) == GL_FALSE) {
            throw new IllegalStateException("Failed to execute GL instruction with sequence number " + name + ", id: "
                    + this.id);
        }
    }

    public void attach() {
        glUseProgram(id);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void drawQuads(double x, double y, double width, double height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2d(x, y + height);
        glTexCoord2f(1, 0);
        glVertex2d(x + width, y + height);
        glTexCoord2f(1, 1);
        glVertex2d(x + width, y);
        glTexCoord2f(0, 1);
        glVertex2d(x, y);
        glEnd();
    }

    //---------------------------------------------------------------------------
    // #uniforms
    //---------------------------------------------------------------------------

    public void uniformFB(String name, FloatBuffer floatBuffer) {
        glUniform1(uniformLocation(name), floatBuffer);
    }

    public void uniformi(String name, int... args) {
        int loc = uniformLocation(name);
        switch (args.length) {
            case 1:
                glUniform1i(loc, args[0]);
                break;
            case 2:
                glUniform2i(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3i(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4i(loc, args[0], args[1], args[2], args[3]);
                break;
            default:
                throw new RuntimeException("Incorrect parameters length, id: " + this.id);
        }
    }

    public void uniformf(String name, float... args) {
        int loc = uniformLocation(name);
        switch (args.length) {
            case 1:
                glUniform1f(loc, args[0]);
                break;
            case 2:
                glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
            default:
                throw new RuntimeException("Incorrect parameters length, id: " + this.id);
        }
    }

    private int uniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }
}
