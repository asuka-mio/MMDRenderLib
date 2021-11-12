package moe.tarsin.mmdrenderlib.animation;

import moe.tarsin.mmdrenderlib.NativeLibraryProvider;
import moe.tarsin.mmdrenderlib.model.MMDModel;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MMDAnimManager {
    static Map<File, Long> animStatic;
    static Map<MMDModel, Map<File, Long>> animModel;

    public static void Init() {
        animStatic = new HashMap<>();
        animModel = new HashMap<>();
    }

    public static void AddModel(MMDModel model) {
        animModel.put(model, new HashMap<>());
    }

    public static void DeleteAllAnimOfModel(MMDModel model) {
        Collection<Long> arr = animModel.get(model).values();
        for (Long i : arr)
            NativeLibraryProvider.DeleteAnimation(i);
        animModel.remove(model);
    }

    //For other entity (Multiple model)
    public static long GetAnimModel(MMDModel model, File animFile) {
        Map<File, Long> sub = animModel.get(model);
        Long result = sub.get(animFile);
        if (result == null) {
            long anim = NativeLibraryProvider.LoadAnimation(model.GetModelLong(), animFile.getAbsolutePath());
            if (anim == 0)
                return 0;
            result = anim;
            sub.put(animFile, result);
        }
        return result;
    }
}