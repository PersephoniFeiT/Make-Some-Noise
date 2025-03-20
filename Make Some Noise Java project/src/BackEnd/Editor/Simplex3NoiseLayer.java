package BackEnd.Editor;

public class Simplex3NoiseLayer implements NoiseLayer{
    double seed;
    double floor;
    double ceiling;
    double amplitude;
    double frequency;

    public Simplex3NoiseLayer(int seed, double floor, double ceiling, double amplitude, double frequency){
        this.seed = seed;
        this.floor = (floor < 0 || floor > 1 )? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling > 1 || ceiling < 0)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= -1)? -1 : amplitude;
        this.frequency = frequency;
    }

    @Override
    public double evaluate(int x, int y) {
        double val = (frequency * SimplexNoise.noise(x, y, seed) + 1) / 2;
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
    public double getGain() {return -1;}

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
        this.floor = (newFloor >= getCeiling())? getCeiling() : (newFloor <= 0)? 0 : floor;
    }

    @Override
    public void setCeiling(double newCeiling) {
        this.ceiling = (newCeiling >= 1)? 1 : (newCeiling <= getFloor())? floor : ceiling;
    }

    @Override
    public void setGain(double newGain) {;}
}
