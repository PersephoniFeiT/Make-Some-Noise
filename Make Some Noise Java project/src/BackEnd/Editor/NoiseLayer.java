package BackEnd.Editor;

//import org.apache.commons.math4.*;

public interface NoiseLayer {
    public double evaluate(int x, int y);
    public int getSeed();
    public double getFreq();
    public double getAmp();
    public double getFloor();
    public double getCeiling();
    public void setFreq(double newFreq);
    public void setAmp(double newAmp);
    public void setFloor(double newFloor);
    public void setCeiling(double newCeiling);
}
