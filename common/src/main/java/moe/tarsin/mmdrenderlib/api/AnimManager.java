package moe.tarsin.mmdrenderlib.api;

import moe.tarsin.mmdrenderlib.animation.MMDAnimManager;
import moe.tarsin.mmdrenderlib.model.MMDModel;

import java.io.File;

public final class AnimManager {
    public static void setModelWithAnim(MMDModel model, File animFile,int layer){
        long anim = MMDAnimManager.GetAnimModel(model,animFile);
        model.ChangeAnim(anim,layer);
    }
}
