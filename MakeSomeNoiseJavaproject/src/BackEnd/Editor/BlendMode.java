package BackEnd.Editor;

/**
 * Enum indicates how a noiselayer will be blended with previous layers
 * @author Fei Triolo
 * @author Maya Malavasi
 * @package BackEnd.Editor
 */
public enum BlendMode {
    MULTIPLY(0),
    DIVIDE(1),
    ADD(2),
    SUBTRACT(3);
    private int mode;

    private BlendMode(int mode){
        this.mode = mode;
    }

    /**
     * @return the value of a given BlendMode
     */
    public int getMode() {
        return mode;
    }

    /**
     * Generates a BlendMode enum from a name String
     * @param name the String value of a BlendMode (i.e. "MULTIPLY", "DIVIDE", "ADD", "SUBTRACT")
     * @return the BlendMode enum of that name
     * @throws IllegalArgumentException if name does not match a valid BlendMode enum
     */
    public static BlendMode fromString(String name) {
        for (BlendMode bm : BlendMode.values()) {
            if (bm.name().equalsIgnoreCase(name)) {
                return bm;
            }
        }
        throw new IllegalArgumentException("Unknown blend mode: " + name);
    }

    /**
     * @return the String value of a BlendMode (i.e. "MULTIPLY", "DIVIDE", "ADD", "SUBTRACT")
     */
    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Generates a BlendMode enum from an integer value
     * @param i
     * @return a BlendMode enum corresponding to the given value (i.e. 0 -> MULTIPLY; 1 -> DIVIDE; 2 -> ADD; 3 -> SUBTRACT)
     */
    public static BlendMode fromInt(int i){
        for(BlendMode blendmode : values()){
            if(i == blendmode.mode){
                return blendmode;
            }
        }
        return MULTIPLY;
    }
}
