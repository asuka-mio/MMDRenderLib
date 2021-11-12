package moe.tarsin.mmdrenderlib.api;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.model.MMDModel;
import moe.tarsin.mmdrenderlib.render.ModelRenderer;
import moe.tarsin.mmdrenderlib.shader.ShaderInstance;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class RenderControler {
    /**
     *
     * @param model the model to render once
     * @param stack a matrix stack,contains model view matrix
     * @param entityYaw the towards to render
     * You can only call this when the thread is a render thread
     * or opengl context will throw an error
     */
    public void render(MMDModel model, PoseStack stack, float entityYaw){
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        ModelRenderer.RenderModel(model,entityYaw,stack);
    }

    /**
     *
     * @param shader bind this shader to MMD rendering,it will be used until you bind another shader
     */
    public void bindShader(ShaderInstance shader){
        if(shader==null){
            MMDRenderLib.logger.error("Attempt to bind a uninitialized shader!");
            return;
        }
        ModelRenderer.bindShader(shader);
    }
}
