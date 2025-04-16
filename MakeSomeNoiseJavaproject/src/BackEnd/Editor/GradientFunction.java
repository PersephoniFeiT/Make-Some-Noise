package BackEnd.Editor;

public class GradientFunction {
    private class Color{
        int rval;
        int gval;
        int bval;
        private Color(int hexCode){
            rval = (hexCode >> 16) & 0xFF;
            gval = (hexCode >> 8) & 0xFF;
            bval = hexCode & 0xFF;
        }
        private Color(int r, int g, int b){
            rval = r;
            gval = g;
            bval = b;
        }
        private int toHexCode(){
            return rval * 0x010000 + gval * 0x000100 + bval;
        }

        private Color diff(Color other){
            return new Color(this.rval - other.rval, this.gval - other.gval, this.bval - other.bval);
        }

        private Color add(Color other){
            return new Color(this.rval + other.rval, this.gval + other.gval, this.bval + other.bval);
        }

        private void scale(double val){
            rval = (int)(rval * val);
            gval = (int)(gval * val);
            bval = (int)(bval * val);

        }
    }

    private Color color1;
    private Color color2;

    public GradientFunction(int color1, int color2){
        this.color1 = new Color(color1);
        this.color2 = new Color(color2);
    }

    public void SetColor1(int hexCode){
        this.color1 = new Color(hexCode);
    }

    public void SetColor2(int hexCode){
        this.color2 = new Color(hexCode);
    }

    public int interpolate(double val){
        Color dColor = color2.diff(color1);
        dColor.scale(val);
        return (color1.add(dColor)).toHexCode();
    }
}
