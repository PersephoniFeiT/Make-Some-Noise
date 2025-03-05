package backend.Editor;

public class Simplex3NoiseLayer implements NoiseLayer{

    @Override
    public double evaluate(int x, int y) {
        return SimplexNoise.noise(x, y, getSeed());
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
