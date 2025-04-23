package BackEnd.Editor;

/**
 * NoiseLayer interface representing an abstract Noise function that is driven by Seed, Frequency, Amplitude, Floor, Ceiling, Gain and BlendMode attributes and evaluates to a value between 0 and 1 in its "virtual-space"
 * @author Fei Triolo
 * @package BackEnd.Editor
 */
public interface NoiseLayer {
    /**
     * Evaluates the noise function at a given (x,y) position in virtual-space
     * @param x The x position where this function is evaluated in virtual-space
     * @param y The y position where this function is evaluated in virtual-space
     * @return
     */
    public double evaluate(int x, int y);
    /**
     * Gets the Seed attribute value of this layer
     * @return the integer value representing an RNG Seed for the initial conditions of the noise function
     */
    public int getSeed();
    /**
     * Gets the Frequency attribute value of this layer
     * @return the double value representing the real-space Frequency scaler of the noise function
     */
    public double getFreq();
    /**
     * Gets the Amplitude attribute value of this layer
     * @return the double value representing the amplitude range of that the noise function evaluates accross between 0 and 1
     */
    public double getAmp();
    /**
     * Gets the Floor attribute value of this layer
     * @return the double value from 0 to 1 representing the minimum possible output of the noise function that lower values are normalized to. This is restricted from above by the ceiling
     */
    public double getFloor();
    /**
     * Gets the Ceiling attribute value of this layer
     * @return the double value from 0 to 1 representing the maximum possible output of the noise function that higher values are normalized to. This is restricted from below by the floor
     */
    public double getCeiling();
    /**
     * Gets the Ceiling attribute value of this layer
     * @return the double value between -1 and 1 exclusive which determines the midpoint which the amplitude is centered around
     */
    public double getGain();
    /**
     * Gets the BlendMode attribute value of this layer
     * @return the BlendMode enum determining how the values of this layer are blended between previous layers (i.e. MULTIPLY, DIVIDE, ADD, SUBTRACT)
     */
    public BlendMode getBlendMode();
    /**
     * Sets this BlendMode enum value to a new BlendMode
     * @param blendMode the BlendMode that this layer will be set to. Defaults to MULTIPLY
     */
    public void setBlendMode(BlendMode blendMode);
    /**
     * Sets the Frequency attribute of this layer to a new value
     * @param newFreq the Frequency value that this layer will be set to. Must be greater than 0.
     */
    public void setFreq(double newFreq);
    /**
     * Sets the Amplitude attribute of this layer to a new value
     * @param newAmp the Amplitude value that this layer will be set to. Must be greater > 0 or <= 1. 
     */
    public void setAmp(double newAmp);
    /**
     * Sets the new Floor attribute of this layer to a new value
     * @param newFloor the Floor value that this layer will be set to. Must be >= 0, < 1, and < getCeiling()
     */
    public void setFloor(double newFloor);
    /**
     * Sets the new Ceiling attribute of this layer to a new value
     * @param newCeiling the Ceiling value that this layer will be set to. Must be > 0, <= 1 and > getFloor()
     */
    public void setCeiling(double newCeiling);
    /**
     * Sets the Gaina attribute of this layer to a new value
     * @param newGain the Gain value that this layer will be set to. Must be > -1 and < 1
     */
    public void setGain(double newGain);
}
