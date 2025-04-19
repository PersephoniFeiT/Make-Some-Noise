package BackEnd.Editor;

public class Simplex2NoiseLayer implements NoiseLayer{
    private int seed;
    private double floor;
    private double ceiling;
    private double amplitude;
    private double frequency;
    private BlendMode blendMode;
    private double gain;

    public Simplex2NoiseLayer(int seed, double floor, double ceiling, double gain, double amplitude, double frequency, BlendMode blendMode){
        this.seed = seed;
        this.floor = (floor < 0 || floor > 1 )? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling > 1 || ceiling < 0)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= 0)? 0 : amplitude;
        this.gain = (gain >= 1)? 0.999 : (gain <= -1)? -0.999 : gain;
        this.frequency = frequency;
        this.blendMode = blendMode;
    }

    @Override
    public double evaluate(int x, int y) {
        double val = (amplitude * SimplexNoise.noise(((double) x + Double.valueOf(getSeed()).hashCode())/100.0, ((double) y + Double.valueOf(getSeed()).hashCode())/100.0) + 1) / 2 + gain;
        return (val > ceiling)? ceiling : (val < floor)? floor : val;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode){
        this.blendMode = blendMode;
    }

    @Override
    public int getSeed() {
        return seed;
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
    public double getGain() {return gain;}


    @Override
    public void setGain(double newGain) {
        this.gain = (newGain > 0.999)? 0.999 : (newGain < 0.999)? -0.999 : newGain;
    }

}
