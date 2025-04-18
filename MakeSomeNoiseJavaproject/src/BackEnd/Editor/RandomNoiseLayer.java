package BackEnd.Editor;
import java.util.Random;
public class RandomNoiseLayer implements NoiseLayer{
    int seed;
    Random rng;
    double floor;
    double ceiling;
    double amplitude;
    double frequency;

    public RandomNoiseLayer(int seed, double floor, double ceiling, double amplitude, double frequency){
        this.seed = seed;
        this.rng = new Random(seed);
        this.floor = (floor <= 0)? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling >= 1)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= 0)? 0 : amplitude;
        this.frequency = frequency;
    }

    @Override
    public double evaluate(int x, int y) {
        double val = rng.nextDouble();
        val = ((val < floor )? floor : (val > ceiling)? ceiling : val) * amplitude + (1 - amplitude)/2;
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