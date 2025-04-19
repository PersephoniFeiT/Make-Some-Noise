package BackEnd.Editor;


//import org.apache.commons.math4.*;

public interface NoiseLayer {
    public enum BlendMode{
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }
    public double evaluate(int x, int y);
    public int getSeed();
    public double getFreq();
    public double getAmp();
    public double getFloor();
    public double getCeiling();
    public double getGain();
    public BlendMode getBlendMode();
    public void setBlendMode(BlendMode blendMode);
    public void setFreq(double newFreq);
    public void setAmp(double newAmp);
    public void setFloor(double newFloor);
    public void setCeiling(double newCeiling);
    public void setGain(double newGain);
}
