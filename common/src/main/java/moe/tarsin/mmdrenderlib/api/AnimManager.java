package moe.tarsin.mmdrenderlib.api;

import moe.tarsin.mmdrenderlib.animation.MMDAnimManager;
import moe.tarsin.mmdrenderlib.model.MMDModel;

import java.io.File;

public final class AnimManager {
    /**
     *Load an animation for a MMD model instance
     * @param model the MMD model instance
     * @param animFile this point to an VMD file
     * @param layer the target layer to set animation for model
     */
    public static void setModelWithAnim(MMDModel model, File animFile,int layer){
        long anim = MMDAnimManager.GetAnimModel(model,animFile);
        model.ChangeAnim(anim,layer);
    }
}
