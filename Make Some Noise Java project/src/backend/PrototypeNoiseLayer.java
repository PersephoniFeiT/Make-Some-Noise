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
}
