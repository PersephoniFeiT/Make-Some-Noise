package BackEnd.Editor;

/**
 * Implementation of NoiseLayer using 2D slices of 3D Simplex Noise. Simplex noise algorithms offer a more coherent, gradient noise pattern than purely random static noise. 
 * @apiNote This class is dependant on a public domain general implementation of Simplex Noise fby Stefan Gustavson (stegu@itn.liu.se), optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * @apiNote Version 2012-03-09 accessed from Steven Rombauts' Simplex noise C++ Repository at https://github.com/SRombauts/SimplexNoise/blob/master/references/SimplexNoise.java
 * This class uses Gustavson's API to generate 3D slices with the x coordinates in virtual space and the z coordinate determined by the seed value
 * @author Fei Triolo
 */
public class Simplex3NoiseLayer implements NoiseLayer{
    private int seed;
    private double floor;
    private double ceiling;
    private double amplitude;
    private double frequency;
    private BlendMode blendMode;
    private double gain;
 

    /**
     * Constructs a new Simplex3Noise object with initial attribute values set
     * @param seed The initial Seed attribute of the noise virtual space
     * @param floor The initial Floor attribute value of the noise pattern
     * @param ceiling The initial Ceiling attribut value of the noise pattern
     * @param gain The initial Gain attribute value of the noise pattern
     * @param amplitude The initial Amplitude attribute value of the noise pattern
     * @param frequency The initial Frequency attribute value of the noise pattern
     * @param blendMode The initial BlendMode attribute value of the layer
     */
    public Simplex3NoiseLayer(int seed, double floor, double ceiling, double gain, double amplitude, double frequency, BlendMode blendMode){
        this.seed = seed;
        this.floor = (floor < 0 || floor > 1 )? 0 : (floor > ceiling)? ceiling : floor;
        this.ceiling = (ceiling > 1 || ceiling < 0)? 1 : (ceiling < floor)? floor : ceiling;
        this.amplitude = (amplitude >= 1)? 1 : (amplitude <= -1)? -1 : amplitude;
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
     * @implNote Like {@link Simplex2Noise}, virtual space is scaled by a factor of 100 to account for the granularity of Gustavson's implementation and results are normalized from -1 to 1 to the expected range of values between 0 and 1.
     */
    @Override
    public double evaluate(int x, int y) {
        double val = (amplitude * SimplexNoise.noise(((double)x)/100.0, ((double)y)/100.0, (seed/100.0)) + 1) / 2 + gain;
        return (val > ceiling)? ceiling : (val < floor)? floor : val;
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
    public double getGain() {return gain;}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGain(double newGain) {
        this.gain = (newGain > 0.999)? 0.999 : (newGain < 0.999)? -0.999 : newGain;
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
}
