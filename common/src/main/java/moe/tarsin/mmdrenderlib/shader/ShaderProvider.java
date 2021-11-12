package moe.tarsin.mmdrenderlib.shader;

import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.shader.ShaderInstance;
import org.lwjgl.opengl.GL46C;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ShaderProvider {
    public static ShaderInstance GenerateShader(File vertexPath, File fragPath) throws IOException {
        int vertexShader = GL46C.glCreateShader(GL46C.GL_VERTEX_SHADER);
        FileInputStream vertexSource = new FileInputStream(vertexPath);
        GL46C.glShaderSource(vertexShader,new String(vertexSource.readAllBytes()));

        int fragShader = GL46C.glCreateShader(GL46C.GL_FRAGMENT_SHADER);
        FileInputStream fragSource = new FileInputStream(fragPath);
        GL46C.glShaderSource(fragShader,new String(fragSource.readAllBytes()));

        GL46C.glCompileShader(vertexShader);
        if (GL46C.glGetShaderi(vertexShader, GL46C.GL_COMPILE_STATUS) == GL46C.GL_FALSE) {
            String log = GL46C.glGetShaderInfoLog(vertexShader, 8192).trim();
            MMDRenderLib.logger.error("Failed to compile shader {}", log);
            GL46C.glDeleteShader(vertexShader);
        }

        GL46C.glCompileShader(fragShader);
        if (GL46C.glGetShaderi(fragShader, GL46C.GL_COMPILE_STATUS) == GL46C.GL_FALSE) {
            String log = GL46C.glGetShaderInfoLog(fragShader, 8192).trim();
            MMDRenderLib.logger.error("Failed to compile shader {}", log);
            GL46C.glDeleteShader(fragShader);
        }
        int program = GL46C.glCreateProgram();
        GL46C.glAttachShader(program,vertexShader);
        GL46C.glAttachShader(program,fragShader);
        GL46C.glLinkProgram(program);
        if (GL46C.glGetProgrami(program, GL46C.GL_LINK_STATUS) == GL46C.GL_FALSE) {
            String log = GL46C.glGetProgramInfoLog(program, 8192);
            MMDRenderLib.logger.error("Failed to link shader program\n{}", log);
            GL46C.glDeleteProgram(program);
            program = 0;
        }
        ShaderInstance shader = new ShaderInstance(program);
        MMDRenderLib.logger.info("MMD Shader Initialize finished");
        return shader;
    }
}