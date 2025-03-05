package backend.Editor;
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
        return 0;
    }

    @Override
    public int getSeed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSeed'");
    }

    @Override
    public double getFreq() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFreq'");
    }

    @Override
    public double getAmp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAmp'");
    }

    @Override
    public double getGain() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGain'");
    }

    @Override
    public double getFloor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFloor'");
    }

    @Override
    public double getCeiling() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCeiling'");
    }
    
}