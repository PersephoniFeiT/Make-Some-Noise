package BackEnd.Editor;

public class Simplex2NoiseLayer implements NoiseLayer{
    double floor;
    double ceiling;
    double amplitude;
    double frequency;

    public Simplex2NoiseLayer(double floor, double ceiling, double amplitude, double frequency){
        this.floor = (floor < 0 || floor > 1 )? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling > 1 || ceiling < 0)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= 0)? 0 : amplitude;
        this.frequency = frequency;
    }

    @Override
    public double evaluate(int x, int y) {
        double val = (amplitude * SimplexNoise.noise(x, y) + 1) / 2;
        return (val > ceiling)? ceiling : (val < floor)? floor : val;
    }

    @Override
    public int getSeed() {
        return -1;
    }

    @Override
    public double getFreq() {
        return frequency;
    }

    @Override
    public double getAmp() {
        return amplitude;
    }

    @Override
    public double getFloor() {
        return floor;
    }

    @Override
    public double getCeiling() {
        return ceiling;
    }

    @Override
    public void setFreq(double newFreq) {
        this.frequency = newFreq;
    }

    @Override
    public void setAmp(double newAmp) {
        this.amplitude = (newAmp >= 1)? 1 : (newAmp <= -1)? -1 : newAmp;
    }

    @Override
    public void setFloor(double newFloor) {
        this.floor = (newFloor >= getCeiling())? getCeiling() : (newFloor <= 0)? 0 : newFloor;
    }

    @Override
    public void setCeiling(double newCeiling) {
        this.ceiling = (newCeiling <= getFloor())? getFloor() : (newCeiling >= 1)? 1 : newCeiling;
    }

    @Override
    public double getGain() {return -1;}

    @Override
    public void setGain(double newGain) {;}

}
