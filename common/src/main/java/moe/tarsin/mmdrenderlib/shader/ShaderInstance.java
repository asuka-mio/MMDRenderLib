package moe.tarsin.mmdrenderlib.shader;

import org.lwjgl.opengl.GL46C;

public class ShaderInstance {
    private int programId;
    private int positionLocation;
    private int uvLocation;
    private int projMatLocation;
    private int modelViewLocation;
    private int samplerLocation;

    public ShaderInstance(int programId){
        this.programId = programId;
        positionLocation = GL46C.glGetAttribLocation(programId,"Position");
        uvLocation = GL46C.glGetAttribLocation(programId,"UV0");
        projMatLocation = GL46C.glGetUniformLocation(programId,"ProjMat");
        modelViewLocation = GL46C.glGetUniformLocation(programId,"ModelViewMat");
        samplerLocation = GL46C.glGetUniformLocation(programId,"Sampler0");
    }
    public int getProgramId(){
        return programId;
    }
    public int getPositionLocation(){
        return positionLocation;
    }

    public int getModelViewLocation() {
        return modelViewLocation;
    }

    public int getProjMatLocation() {
        return projMatLocation;
    }

    public int getUvLocation() {
        return uvLocation;
    }

    public int getSamplerLocation() {
        return samplerLocation;
    }
}
