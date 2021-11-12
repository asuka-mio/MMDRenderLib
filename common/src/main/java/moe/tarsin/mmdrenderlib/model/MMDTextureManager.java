package moe.tarsin.mmdrenderlib.model;
import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.NativeLibraryProvider;
import org.lwjgl.opengl.GL46C;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MMDTextureManager {
    static Map<String, Texture> textures;

    public static void Init() {
        textures = new HashMap<>();
    }

    public static Texture GetTexture(String filename) {
        Texture result = textures.get(filename);
        if (result == null) {
            long nfTex = NativeLibraryProvider.LoadTexture(filename);
            if (nfTex == 0) {
                MMDRenderLib.logger.warn(String.format("Cannot find texture: %s", filename));
                return null;
            }
            int x = NativeLibraryProvider.GetTextureX(nfTex);
            int y = NativeLibraryProvider.GetTextureY(nfTex);
            long texData = NativeLibraryProvider.GetTextureData(nfTex);
            boolean hasAlpha = NativeLibraryProvider.TextureHasAlpha(nfTex);

            int tex = GL46C.glGenTextures();
            GL46C.glBindTexture(GL46C.GL_TEXTURE_2D, tex);
            int texSize = x * y * (hasAlpha ? 4 : 3);
            ByteBuffer texBuffer = ByteBuffer.allocateDirect(texSize);
            NativeLibraryProvider.CopyDataToByteBuffer(texBuffer, texData, texSize);
            if (hasAlpha) {
                GL46C.glPixelStorei(GL46C.GL_UNPACK_ALIGNMENT, 4);
                GL46C.glTexImage2D(GL46C.GL_TEXTURE_2D, 0, GL46C.GL_RGBA, x, y, 0, GL46C.GL_RGBA, GL46C.GL_UNSIGNED_BYTE, texBuffer);
            } else {
                GL46C.glPixelStorei(GL46C.GL_UNPACK_ALIGNMENT, 1);
                GL46C.glTexImage2D(GL46C.GL_TEXTURE_2D, 0, GL46C.GL_RGB, x, y, 0, GL46C.GL_RGB, GL46C.GL_UNSIGNED_BYTE, texBuffer);
            }
            NativeLibraryProvider.DeleteTexture(nfTex);

            GL46C.glTexParameteri(GL46C.GL_TEXTURE_2D, GL46C.GL_TEXTURE_MAX_LEVEL, 0);
            GL46C.glTexParameteri(GL46C.GL_TEXTURE_2D, GL46C.GL_TEXTURE_MIN_FILTER, GL46C.GL_LINEAR);
            GL46C.glTexParameteri(GL46C.GL_TEXTURE_2D, GL46C.GL_TEXTURE_MAG_FILTER, GL46C.GL_LINEAR);
            GL46C.glBindTexture(GL46C.GL_TEXTURE_2D,0);

            result = new Texture();
            result.tex = tex;
            result.hasAlpha = hasAlpha;
            textures.put(filename, result);
        }
        return result;
    }

    public static class Texture {
        public int tex;
        public boolean hasAlpha;
    }
}