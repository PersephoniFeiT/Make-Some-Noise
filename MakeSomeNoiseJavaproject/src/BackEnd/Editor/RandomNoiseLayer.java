package BackEnd.Editor;
import java.util.Random;
/**
 * RandomNoiseLayer represents a static noise function which generates a completely random value on each pixel
 * @author Fei Triolo
 * @package BackEnd.Editor
 */
public class RandomNoiseLayer implements NoiseLayer{
    private int seed;
    private Random rng;
    private double floor;
    private double ceiling;
    private double amplitude;
    private double frequency;
    private BlendMode blendMode;
    private double gain;

    /**
     * Constructs a new RandomNoiseLayer object with initial attribute values set
     * @param seed The initial seed of the RNG
     * @param floor The initial Floor attribute value of the noise pattern
     * @param ceiling The initial Ceiling attribut value of the noise pattern
     * @param gain The initial Gain attribute value of the noise pattern
     * @param amplitude The initial Amplitude attribute value of the noise pattern
     * @param frequency The initial Frequency attribute value of the noise pattern
     * @param blendMode The initial BlendMode attribute value of the layer
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public BlendMode getBlendMode(){
        return this.blendMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBlendMode(BlendMode blendMode){
        this.blendMode = blendMode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double evaluate(int x, int y) {
        double val = (rng.nextDouble() * amplitude + (1 - amplitude)/2) + gain;
        val = ((val < floor )? floor : (val > ceiling)? ceiling : val);
        return val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSeed() {
        return seed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getFreq() {
        return frequency;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getAmp() {
        return amplitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGain() {
        return gain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getFloor() {
        return floor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCeiling() {
        return ceiling;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFreq(double newFreq) {
        this.frequency = newFreq;
        rng = new Random(seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAmp(double newAmp) {
       this.amplitude = newAmp;
       rng = new Random(seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloor(double newFloor) {
        this.floor = newFloor;
        rng = new Random(seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCeiling(double newCeiling) {
        this.ceiling = newCeiling;
        rng = new Random(seed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGain(double newGain) {;}
}