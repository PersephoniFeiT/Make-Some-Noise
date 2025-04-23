package BackEnd.Editor;

public enum BlendMode {
    MULTIPLY(0),
    DIVIDE(1),
    ADD(2),
    SUBTRACT(3);
    private int mode;
    private BlendMode(int mode){
        this.mode = mode;
    }

    public static BlendMode fromInt(int i){
        for(BlendMode blendmode : values()){
            if(i == blendmode.mode){
                return blendmode;
            }
        }
        return MULTIPLY;
    }
}
