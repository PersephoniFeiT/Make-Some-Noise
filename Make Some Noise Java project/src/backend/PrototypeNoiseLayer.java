package backend;

public class PrototypeNoiseLayer implements NoiseLayer{
    public double[][] getValues(){
        double[][] mat = {  {1,0,0,0,0,0,0},
                            {1,1,0,0,0,0,0},
                            {1,1,1,0,0,0,0},
                            {1,1,1,1,0,0,0},
                            {1,1,1,1,1,0,0},
                            {1,1,1,1,1,1,0},
                            {1,1,1,1,1,1,1}};
        return mat;
    }

    @Override
    public double evaluate(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluate'");
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

    @Override
    public void regenerate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'regenerate'");
    }
}
