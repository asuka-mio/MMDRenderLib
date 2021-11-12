package moe.tarsin.mmdrenderlib.api;

import com.mojang.blaze3d.vertex.PoseStack;
import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.model.MMDModel;
import moe.tarsin.mmdrenderlib.render.ModelRenderer;
import moe.tarsin.mmdrenderlib.shader.ShaderInstance;

public final class RenderControler {
    public void render(MMDModel model, PoseStack stack, float entityYaw){
        ModelRenderer.RenderModel(model,entityYaw,stack);
    }
    public void bindShader(ShaderInstance shader){
        if(shader==null){
            MMDRenderLib.logger.error("Attempt to bind a uninitialized shader!");
            return;
        }
        ModelRenderer.bindShader(shader);
    }
}
