package moe.tarsin.mmdrenderlib.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.model.MMDModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL46C;

import java.nio.FloatBuffer;

public class ModelRenderer {
    private static boolean isShaderBinded = false;
    private static moe.tarsin.mmdrenderlib.shader.ShaderInstance activateShader;
    public static void bindShader(moe.tarsin.mmdrenderlib.shader.ShaderInstance shader){
        activateShader = shader;
    }
    public static void RenderModel(MMDModel model, float entityYaw, PoseStack deliverStack ) {
        model.Update();
        if (!isShaderBinded) {
            MMDRenderLib.logger.error("Shader Not Binded");
            return;
        }
        if(!model.isModelLoaded()){
            MMDRenderLib.logger.error("Attempt to render a unloaded model");
            return;
        }
        ShaderInstance shader = RenderSystem.getShader();

        //UnbindAll?
        GL46C.glBindVertexArray(model.getVertexArrayObject());
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        deliverStack.mulPose(Vector3f.YP.rotationDegrees(-entityYaw));
        deliverStack.scale(0.09f,0.09f,0.09f);
        shader.MODEL_VIEW_MATRIX.set(deliverStack.last().pose());
        FloatBuffer modelViewMatBuff = shader.MODEL_VIEW_MATRIX.getFloatBuffer();
        FloatBuffer projViewMatBuff = shader.PROJECTION_MATRIX.getFloatBuffer();


        GL46C.glEnableVertexAttribArray(activateShader.getPositionLocation());
        RenderSystem.activeTexture(GL46C.GL_TEXTURE0);
        GL46C.glEnableVertexAttribArray(activateShader.getUvLocation());

        model.uploadModelData(activateShader.getPositionLocation(), activateShader.getUvLocation());


        GL46C.glBindBuffer(GL46C.GL_ELEMENT_ARRAY_BUFFER, model.getIndexBufferObject());
        GL46C.glUseProgram(activateShader.getProgramId());
        GL46C.glUniformMatrix4fv(activateShader.getModelViewLocation(),false,modelViewMatBuff);
        GL46C.glUniformMatrix4fv(activateShader.getProjMatLocation(),false,projViewMatBuff);
        long subMeshCount = model.getSubMeshCount();
        for (long i = 0; i < subMeshCount; ++i) {
            int materialID = model.getSubMeshMaterialID(i);
            float alpha = model.getMaterialAlpha(materialID);
            if (alpha == 0.0f)
                continue;

            if (model.getMaterialBothFace(materialID)) {
                RenderSystem.disableCull();
            } else {
                RenderSystem.enableCull();
            }
            if (model.isMatMissingTex(materialID))
                Minecraft.getInstance().getEntityRenderDispatcher().textureManager.bindForSetup(TextureManager.INTENTIONAL_MISSING_TEXTURE);
            else
                GL46C.glBindTexture(GL46C.GL_TEXTURE_2D,model.getMaterialTex(materialID));
            long startPos = model.getTexStartPos(i);
            int count = model.getSubMeshVertexCount(i);

            GL46C.glUniform1i(activateShader.getSamplerLocation(),0);
            GL46C.glDrawElements(GL46C.GL_TRIANGLES, count, model.getIndexType(), startPos);
        }
        GL46C.glUseProgram(0);
    }
}
