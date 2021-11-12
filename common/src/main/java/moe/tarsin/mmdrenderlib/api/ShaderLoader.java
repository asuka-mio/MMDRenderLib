package moe.tarsin.mmdrenderlib.api;

import moe.tarsin.mmdrenderlib.shader.ShaderInstance;
import moe.tarsin.mmdrenderlib.shader.ShaderProvider;

import java.io.File;
import java.io.IOException;

public final class ShaderLoader{
    public static ShaderInstance LoadShader(File vertexShader, File fragShader) throws IOException {
        return ShaderProvider.GenerateShader(vertexShader,fragShader);
    }
}
