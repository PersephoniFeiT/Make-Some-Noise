package BackEnd.Editor;
import java.util.Random;
public class RandomNoiseLayer implements NoiseLayer{
    private int seed;
    private Random rng;
    private double floor;
    private double ceiling;
    private double amplitude;
    private double frequency;
    private BlendMode blendMode;
    private double gain;

    public RandomNoiseLayer(int seed, double floor, double ceiling, double gain, double amplitude, double frequency, BlendMode blendMode){
        this.seed = seed;
        this.rng = new Random(seed);
        this.floor = (floor <= 0)? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling >= 1)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= 0)? 0 : amplitude;
        this.gain = (gain >= 1)? 0.999 : (gain <= -1)?  -0.999: gain;
        this.frequency = frequency;
        this.blendMode = blendMode;
    }

    @Override
    public BlendMode getBlendMode(){
        return this.blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode){
        this.blendMode = blendMode;
    }
    
    @Override
    public double evaluate(int x, int y) {
        double val = (rng.nextDouble() * amplitude + (1 - amplitude)/2) + gain;
        val = ((val < floor )? floor : (val > ceiling)? ceiling : val);
        return val;
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
    public double getGain() {
        return -1;
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
        rng = new Random(seed);
    }

    @Override
    public void setAmp(double newAmp) {
       this.amplitude = newAmp;
       rng = new Random(seed);
    }

    @Override
    public void setFloor(double newFloor) {
        this.floor = newFloor;
        rng = new Random(seed);
    }

    @Override
    public void setCeiling(double newCeiling) {
        this.ceiling = newCeiling;
        rng = new Random(seed);
    }


    @Override
    public void setGain(double newGain) {;}
}