package moe.tarsin.mmdrenderlib.model;

import moe.tarsin.mmdrenderlib.MMDRenderLib;
import moe.tarsin.mmdrenderlib.NativeLibraryProvider;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class MMDModel{
    long model;
    String modelDir;
    int vertexCount;
    ByteBuffer posBuffer, norBuffer, uvBuffer;

    int vertexArrayObject;
    int indexBufferObject;
    int positionBufferObject;
    int normalBufferObject;
    int uvBufferObject;

    int indexElementSize;
    int indexType;

    boolean isModelLoaded;

    Material[] mats;
    public boolean isModelLoaded(){
        return this.isModelLoaded;
    }

    public int getVertexArrayObject() {
        return vertexArrayObject;
    }

    public int getIndexBufferObject() {
        return indexBufferObject;
    }

    public int getIndexType() {
        return indexType;
    }

    public long getSubMeshCount() {
        return NativeLibraryProvider.GetSubMeshCount(model);
    }

    public void uploadModelData(int positionLocation,int uvLocation){
        int posAndNorSize = vertexCount * 12; //float * 3
        long posData = NativeLibraryProvider.GetPoss(model);
        NativeLibraryProvider.CopyDataToByteBuffer(posBuffer, posData, posAndNorSize);
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, positionBufferObject);
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER,posBuffer,GL46C.GL_STATIC_DRAW);
        GL46C.glVertexAttribPointer(positionLocation,3,GL46C.GL_FLOAT,false,0, 0);


        int uvSize = vertexCount * 8; //float * 2
        long uvData = NativeLibraryProvider.GetUVs(model);
        NativeLibraryProvider.CopyDataToByteBuffer(uvBuffer, uvData, uvSize);
        GL46C.glBindBuffer(GL46C.GL_ARRAY_BUFFER, uvBufferObject);
        GL46C.glBufferData(GL46C.GL_ARRAY_BUFFER,uvBuffer,GL46C.GL_STATIC_DRAW);
        GL46C.glVertexAttribPointer(uvLocation,2,GL46C.GL_FLOAT,false,0, 0);
    }
    public MMDModel(String modelFilename, String modelDir, boolean isPMD, long layerCount){
        long model;
        if (isPMD)
            model = NativeLibraryProvider.LoadModelPMD(modelFilename, modelDir, layerCount);
        else
            model = NativeLibraryProvider.LoadModelPMX(modelFilename, modelDir, layerCount);
        isModelLoaded = true;
        if (model == 0) {
            MMDRenderLib.logger.error(String.format("Cannot open model: '%s'.", modelFilename));
            isModelLoaded = false;
        }
        //Model exists,now we prepare data for OpenGL
        vertexArrayObject = GL46C.glGenVertexArrays();
        indexBufferObject = GL46C.glGenBuffers();
        positionBufferObject = GL46C.glGenBuffers();
        normalBufferObject = GL46C.glGenBuffers();
        uvBufferObject = GL46C.glGenBuffers();

        int vertexCount = (int) NativeLibraryProvider.GetVertexCount(model);
        posBuffer = ByteBuffer.allocateDirect(vertexCount * 12); //float * 3
        norBuffer = ByteBuffer.allocateDirect(vertexCount * 12);
        uvBuffer = ByteBuffer.allocateDirect(vertexCount * 8); //float * 2

        GL46C.glBindVertexArray(vertexArrayObject);
        //Init indexBufferObject
        int indexElementSize = (int) NativeLibraryProvider.GetIndexElementSize(model);
        int indexCount = (int) NativeLibraryProvider.GetIndexCount(model);
        int indexSize = indexCount * indexElementSize;
        long indexData = NativeLibraryProvider.GetIndices(model);
        ByteBuffer indexBuffer = ByteBuffer.allocateDirect(indexSize);
        for (int i = 0; i < indexSize; ++i)
            indexBuffer.put(NativeLibraryProvider.ReadByte(indexData, i));
        indexBuffer.position(0);
        GL46C.glBindBuffer(GL46C.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
        GL46C.glBufferData(GL46C.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL46C.GL_STATIC_DRAW);

        indexType = switch (indexElementSize) {
            case 1 -> GL46C.GL_UNSIGNED_BYTE;
            case 2 -> GL46C.GL_UNSIGNED_SHORT;
            case 4 -> GL46C.GL_UNSIGNED_INT;
            default -> 0;
        };

        //Material
        MMDModel.Material[] mats = new MMDModel.Material[(int) NativeLibraryProvider.GetMaterialCount(model)];
        for (int i = 0; i < mats.length; ++i) {
            mats[i] = new MMDModel.Material();
            String texFilename = NativeLibraryProvider.GetMaterialTex(model, i);
            if (!texFilename.isEmpty()) {
                MMDTextureManager.Texture mgrTex = MMDTextureManager.GetTexture(texFilename);
                if (mgrTex != null) {
                    mats[i].tex = mgrTex.tex;
                    mats[i].hasAlpha = mgrTex.hasAlpha;
                }
            }
        }
    }

    public static void Delete(MMDModel model) {
        NativeLibraryProvider.DeleteModel(model.model);
    }

    public void ChangeAnim(long anim, long layer) {
        NativeLibraryProvider.ChangeModelAnim(model, anim, layer);
    }

    public void ResetPhysics() {
        NativeLibraryProvider.ResetModelPhysics(model);
    }

    public long GetModelLong() {
        return model;
    }

    public String GetModelDir() {
        return modelDir;
    }

    public void Update() {
        NativeLibraryProvider.UpdateModel(model);
    }
    public int getSubMeshMaterialID(long index){
        return NativeLibraryProvider.GetSubMeshMaterialID(model, index);
    }
    public boolean getMaterialBothFace(int materialID){
        return NativeLibraryProvider.GetMaterialBothFace(model, materialID);
    }
    public float getMaterialAlpha(int materialID){
        return NativeLibraryProvider.GetMaterialAlpha(model, materialID);
    }
    public int getSubMeshVertexCount(long index){
        return NativeLibraryProvider.GetSubMeshVertexCount(model, index);
    }


    static class Material {
        int tex;
        boolean hasAlpha;

        Material() {
            tex = 0;
            hasAlpha = false;
        }
    }
    public boolean isMatMissingTex(int index){
        return mats[index].tex == 0;
    }
    public int getMaterialTex(int index){
        return mats[index].tex;
    }
    public long getTexStartPos(long count){
        return (long) NativeLibraryProvider.GetSubMeshBeginIndex(model, count) * indexElementSize;
    }
}