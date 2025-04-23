package BackEnd.Editor;

/**
 * GradientFunction represents a linear gradient between 2 colors in RGB Colorspace
 * @author Fei Triolo
 */
public class GradientFunction {
    /**
     * Internal RGB Color class for Gradient interpolation
     * Values are not normalized to enable colormath outside the normal range of colors
     */
    private class Color{
        int rval;
        int gval;
        int bval;
        /**
         * Generates a Color object from a given hexcode
         * @param hexCode The 6 digit hexadecimal value of a color ranging from 0x000000 to 0xFFFFFF for real colors. 0xXX______ represent the red value. 0x__XX__ represent the green value. 0x____XX represent the blue value
         */
        private Color(int hexCode){
            rval = (hexCode >> 16) & 0xFF;
            gval = (hexCode >> 8) & 0xFF;
            bval = hexCode & 0xFF;
        }
        /**
         * Generates a Color object from RGB values
         * Values are not normalized to enable colormath outside the normal range of colors
         * @param r The red value typically ranging from 0 to 255
         * @param g The green value typically ranging from 0 to 255
         * @param b The blue value typically ranging from 0 to 255
         */
        private Color(int r, int g, int b){
            rval = r;
            gval = g;
            bval = b;
        }

        /**
         * @return the hex code of a Color object (NOTE that the values are not normalized to enable colormath outside the normal range of colors)
         */
        private int toHexCode(){
            return rval * 0x010000 + gval * 0x000100 + bval;
        }

        /**
         * Calculates the vector difference between colors in RGB Colorspace as a Color object
         * @param other the other color operand to be subtracted from this one
         * @return a Color object with values representing the difference between two Colors (NOTE that this can have RBG color range of 0 to 255 which would not represent a real color)
         */
        private Color diff(Color other){
            return new Color(this.rval - other.rval, this.gval - other.gval, this.bval - other.bval);
        }

        /**
         * Calculates the vector addition between colors in RGB Colorspace as a Color object
         * @param other the other color operand to be added to this one
         * @return a color object with values representing the sum of two Colors (NOTE that this can have RBG color range of 0 to 255 which would not represent a real color)
         */
        private Color add(Color other){
            return new Color(this.rval + other.rval, this.gval + other.gval, this.bval + other.bval);
        }

        /**
         * Scalar multiplication of this color in RGB Colorspace
         * (NOTE that this can have RBG color range of 0 to 255 which would not represent a real color)
         * @param val the scalar value to be multiplied by
         */
        private void scale(double val){
            rval = (int)(rval * val);
            gval = (int)(gval * val);
            bval = (int)(bval * val);

        }
    }

    private Color color1;
    private Color color2;

    /**
     * Constructs a Gradient between two colors based on their hexcode
     * Hexadecimal values ranging from 0x000000 to 0xFFFFFF for real colors. 0xXX______ represent the red value. 0x__XX__ represent the green value. 0x____XX represent the blue value
     * @param color1 The hexcode of the starting gradient color
     * @param color2 The hexcode of the ending gradient color
     * For best practice make sure each value is within the range of real colors.
     */
    public GradientFunction(int color1, int color2){
        this.color1 = new Color(color1);
        this.color2 = new Color(color2);
    }

    /**
     * Sets the starting gradient color to a new color of a given hexadecimal value
     * @param hexCode the hexadecimal value of the new starting color. (For best practice make sure the value is within the range of real colors)
     */
    public void SetColor1(int hexCode){
        this.color1 = new Color(hexCode);
    }

    /**
     * Sets the ending gradient color to a new color of a given hexadecimal value
     * @param hexCode the hexadecimal value of the new starting color. (For best practice make sure the value is within the range of real colors)
     */
    public void SetColor2(int hexCode){
        this.color2 = new Color(hexCode);
    }

    /**
     * Applies a linear interpolation of the gradient to a double value between 0 and 1 where 0 is mapped to the starting color and 1 is mapped to the ending color
     * (e.g if the starting color is 0xFF0000 and the ending color is 0x0000FF evaluate(0.5) would return a purple color around 0x800080) 
     * @param val the double value that is mapped accross the gradient. (For best practice keep this value between 0 and 1)
     * @return the hexcode for a color at some point between the starting and ending colors in RGB Colorspace determined by val
     */
    public int interpolate(double val){
        Color dColor = color2.diff(color1);
        dColor.scale(val);
        return (color1.add(dColor)).toHexCode();
    }
}
