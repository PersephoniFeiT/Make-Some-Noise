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

    public int getMode() {
        return mode;
    }

    public static BlendMode fromString(String name) {
        for (BlendMode bm : BlendMode.values()) {
            if (bm.name().equalsIgnoreCase(name)) {
                return bm;
            }
        }
        throw new IllegalArgumentException("Unknown blend mode: " + name);
    }

    @Override
    public String toString() {
        return this.name();
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
