package backend.Editor;

//import org.apache.commons.math4.*;

public interface NoiseLayer {
    public double evaluate(int x, int y);
    public int getSeed();
    public double getFreq();
    public double getAmp();
    public double getGain();
    public double getFloor();
    public double getCeiling();
}
