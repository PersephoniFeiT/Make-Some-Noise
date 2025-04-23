package BackEnd.Editor;

/**
 * Implementation of NoiseLayer using 2D Simplex Noise. Simplex noise algorithms offer a more coherent, gradient noise pattern than {@link RandomNoiseLayer} static noise. 
 * This class is dependant on a public domain general implementation of {@link SimplexNoise} by Stefan Gustavson (stegu@itn.liu.se), optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Version 2012-03-09 accessed from Steven Rombauts' Simplex noise C++ Repository at https://github.com/SRombauts/SimplexNoise/blob/master/references/SimplexNoise.java
 * @author Fei Triolo
 */
public class Simplex2NoiseLayer implements NoiseLayer{
    private int seed;
    private double floor;
    private double ceiling;
    private double amplitude;
    private double frequency;
    private BlendMode blendMode;
    private double gain;

    /**
     * Constructs a new Simplex2NoiseLayer object with initial attribute values set
     * @param seed The initial seed of the RNG
     * @param floor The initial Floor attribute value of the noise pattern
     * @param ceiling The initial Ceiling attribut value of the noise pattern
     * @param gain The initial Gain attribute value of the noise pattern
     * @param amplitude The initial Amplitude attribute value of the noise pattern
     * @param frequency The initial Frequency attribute value of the noise pattern
     * @param blendMode The initial BlendMode attribute value of the layer
     */
    public Simplex2NoiseLayer(int seed, double floor, double ceiling, double gain, double amplitude, double frequency, BlendMode blendMode){
        this.seed = seed;
        this.floor = (floor < 0 || floor > 1 )? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling > 1 || ceiling < 0)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= 0)? 0 : amplitude;
        this.gain = (gain >= 1)? 0.999 : (gain <= -1)? -0.999 : gain;
        this.frequency = frequency;
        this.blendMode = blendMode;
    }

    /**
     * {@inheritDoc}
     * @implNote Since Stefan Gustavson's Simplex Noise API returns a value between -1 and 1, return values are normalized liniarly for the required 0 to 1 range. Virtual space is also scaled down by a factor of 100 to account for the granularity of Gustavson's function and transformed by a hash of the Seed attribute to retrofit our data-driven Seed attribute.  
     */
    @Override
    public double evaluate(int x, int y) {
        double val = (amplitude * SimplexNoise.noise(((double) x + Double.valueOf(getSeed()).hashCode())/100.0, ((double) y + Double.valueOf(getSeed()).hashCode())/100.0) + 1) / 2 + gain;
        return (val > ceiling)? ceiling : (val < floor)? floor : val;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlendMode getBlendMode() {
        return blendMode;
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
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAmp(double newAmp) {
        this.amplitude = (newAmp >= 1)? 1 : (newAmp <= -1)? -1 : newAmp;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloor(double newFloor) {
        this.floor = (newFloor >= getCeiling())? getCeiling() : (newFloor <= 0)? 0 : newFloor;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setCeiling(double newCeiling) {
        this.ceiling = (newCeiling <= getFloor())? getFloor() : (newCeiling >= 1)? 1 : newCeiling;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public double getGain() {return gain;}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGain(double newGain) {
        this.gain = (newGain > 0.999)? 0.999 : (newGain < 0.999)? -0.999 : newGain;
    }

}
