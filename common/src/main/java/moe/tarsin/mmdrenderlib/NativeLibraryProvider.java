package moe.tarsin.mmdrenderlib;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class NativeLibraryProvider {
    private static final String RuntimePath = new File(System.getProperty("java.home")).getParent();
    private static final boolean isAndroid = new File("/system/build.prop").exists();
    private static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
    private static final boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    private static final HashMap<runtimeUrlRes, String> urlMap = new HashMap<runtimeUrlRes, String>() {
        {
            put(runtimeUrlRes.android_arch64, "https://github.com.cnpmjs.org/asuka-mio/KAIMyEntitySaba/releases/download/crossplatform/KAIMyEntitySaba.so");
            put(runtimeUrlRes.android_arch64_libc, "https://github.com.cnpmjs.org/asuka-mio/KAIMyEntitySaba/releases/download/crossplatform/libc++_shared.so");
        }
    };

    private static void DownloadSingleFile(URL url, File file) throws IOException {
        if (file.exists()) {
            try {
                System.load(file.getAbsolutePath());
                return; //File exist and loadable
            } catch (Error e) {
                MMDRenderLib.logger.info(file.getAbsolutePath() + "broken!Trying recover it!");
            }
        }
        try {
            file.delete();
            file.createNewFile();
            FileUtils.copyURLToFile(url, file, 30000, 30000);
            System.load(file.getAbsolutePath());
        } catch (IOException e) {
            file.delete();
            MMDRenderLib.logger.info("Download" + url.getPath() + "failed!");
            MMDRenderLib.logger.info("Cannot download runtime!");
            MMDRenderLib.logger.info("Check you internet connection and restart game!");
            e.printStackTrace();
            throw e;
        }
    }

    private static void DownloadRuntime() throws Exception {
        if (isWindows) {
            MMDRenderLib.logger.info("Not support!");
            throw new Error();
        }
        if (isLinux && !isAndroid) {
            MMDRenderLib.logger.info("Not support!");
            throw new Error();
        }
        if (isLinux && isAndroid) {
            DownloadSingleFile(new URL(urlMap.get(runtimeUrlRes.android_arch64_libc)), new File(RuntimePath, "libc++_shared.so"));
            DownloadSingleFile(new URL(urlMap.get(runtimeUrlRes.android_arch64)), new File(RuntimePath, "KAIMyEntitySaba.so"));
        }
    }

    private static void LoadLibrary(File file) {
        try {
            System.load(file.getAbsolutePath());
        } catch (Error e) {
            MMDRenderLib.logger.info("Runtime" + file.getAbsolutePath() + "not found,try download from github!");
            throw e;
        }
    }

    public static void Init() {
        try {
            if (isWindows) {
                MMDRenderLib.logger.info("Win32 Env Detected!");
                LoadLibrary(new File(Minecraft.getInstance().gameDirectory.getAbsolutePath(), "KAIMyEntitySaba.dll"));//WIN32
            }
            if (isLinux && !isAndroid) {
                MMDRenderLib.logger.info("Linux Env Detected!");
                LoadLibrary(new File(Minecraft.getInstance().gameDirectory.getAbsolutePath(), "KAIMyEntitySaba.so"));//Linux
            }
            if (isLinux && isAndroid) {
                MMDRenderLib.logger.info("Android Env Detected!");
                LoadLibrary(new File(RuntimePath, "libc++_shared.so"));
                LoadLibrary(new File(RuntimePath, "KAIMyEntitySaba.so"));//Android
            }
        } catch (Error e) {
            try {
                DownloadRuntime();
            } catch (Exception ex) {
                throw e;
            }
        }
    }

    public static native String GetVersion();

    public static native byte ReadByte(long data, long pos);

    public static native void CopyDataToByteBuffer(ByteBuffer buffer, long data, long pos);

    public static native long LoadModelPMX(String filename, String dir, long layerCount);

    public static native long LoadModelPMD(String filename, String dir, long layerCount);

    public static native void DeleteModel(long model);

    public static native void UpdateModel(long model);

    public static native long GetVertexCount(long model);

    public static native long GetPoss(long model);

    public static native long GetNormals(long model);

    public static native long GetUVs(long model);

    public static native long GetIndexElementSize(long model);

    public static native long GetIndexCount(long model);

    public static native long GetIndices(long model);

    public static native long GetMaterialCount(long model);

    public static native String GetMaterialTex(long model, long pos);

    public static native String GetMaterialSpTex(long model, long pos);

    public static native String GetMaterialToonTex(long model, long pos);

    public static native long GetMaterialAmbient(long model, long pos);

    public static native long GetMaterialDiffuse(long model, long pos);

    public static native long GetMaterialSpecular(long model, long pos);

    public static native float GetMaterialSpecularPower(long model, long pos);

    public static native float GetMaterialAlpha(long model, long pos);

    public static native long GetMaterialTextureMulFactor(long model, long pos);

    public static native long GetMaterialTextureAddFactor(long model, long pos);

    public static native int GetMaterialSpTextureMode(long model, long pos);

    public static native long GetMaterialSpTextureMulFactor(long model, long pos);

    public static native long GetMaterialSpTextureAddFactor(long model, long pos);

    public static native long GetMaterialToonTextureMulFactor(long model, long pos);

    public static native long GetMaterialToonTextureAddFactor(long model, long pos);

    public static native boolean GetMaterialBothFace(long model, long pos);

    public static native long GetSubMeshCount(long model);

    public static native int GetSubMeshMaterialID(long model, long pos);

    public static native int GetSubMeshBeginIndex(long model, long pos);

    public static native int GetSubMeshVertexCount(long model, long pos);

    public static native void ChangeModelAnim(long model, long anim, long layer);

    public static native void ResetModelPhysics(long model);

    public static native long CreateMat();

    public static native void DeleteMat(long mat);

    public static native void GetRightHandMat(long model, long mat);

    public static native void GetLeftHandMat(long model, long mat);

    public static native long LoadTexture(String filename);

    public static native void DeleteTexture(long tex);

    public static native int GetTextureX(long tex);

    public static native int GetTextureY(long tex);

    public static native long GetTextureData(long tex);

    public static native boolean TextureHasAlpha(long tex);

    public static native long LoadAnimation(long model, String filename);

    public static native void DeleteAnimation(long anim);

    enum runtimeUrlRes {
        android_arch64, android_arch64_libc
    }
}
