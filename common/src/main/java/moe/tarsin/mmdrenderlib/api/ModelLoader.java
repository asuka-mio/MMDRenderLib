package moe.tarsin.mmdrenderlib.api;

import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.animation.MMDAnimManager;
import moe.tarsin.mmdrenderlib.model.MMDModel;

import java.io.File;
import java.io.FileNotFoundException;

public final class ModelLoader {
    public static MMDModel LoadModel(File modelFile,File modelDir,long layerCount) throws Exception{
        boolean isPMD;
        if(!modelFile.exists())
            throw new FileNotFoundException(modelFile.getAbsolutePath()+"not exists");
        if(modelFile.getName().endsWith(".pmx"))
            isPMD = false;
        else if(modelFile.getName().endsWith(".pmd"))
            isPMD = true;
        else {
            MMDRenderLib.logger.error(modelFile.getAbsolutePath()+"can't be loaded!");
            throw new Exception("Unsupport MMD File");
        }
        MMDModel model = new MMDModel(modelFile.getAbsolutePath(),modelDir.getAbsolutePath(),isPMD,layerCount);
        MMDAnimManager.AddModel(model);
        return model;
    }
    public static void DeleteModel(MMDModel model){
        MMDAnimManager.DeleteAllAnimOfModel(model);
        MMDModel.Delete(model);
    }
}
