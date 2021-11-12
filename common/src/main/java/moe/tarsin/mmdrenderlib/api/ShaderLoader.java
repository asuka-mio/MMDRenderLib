package moe.tarsin.mmdrenderlib.api;

import moe.tarsin.mmdrenderlib.shader.ShaderInstance;
import moe.tarsin.mmdrenderlib.shader.ShaderProvider;

import java.io.File;
import java.io.IOException;

public final class ShaderLoader{
    /**
     * Compile a shader from two glsl sources
     * @param vertexShader vertex shader source
     * @param fragShader fragment shader source
     * @return the wrapper of a shader,deliver it to render components
     * @throws IOException when shader file missing or can't be compiled
     */
    public static ShaderInstance LoadShader(File vertexShader, File fragShader) throws IOException {
        return ShaderProvider.GenerateShader(vertexShader,fragShader);
    }
}
