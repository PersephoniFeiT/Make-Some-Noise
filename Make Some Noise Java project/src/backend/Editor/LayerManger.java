package BackEnd.Editor;
import java.util.*;

/* Class responsible for multiplying noise layers for fractal adding
 * (potentially other leyer types in the future) 
 * so they can be rendered*/
public class LayerManger {
    
    private ArrayList<NoiseLayer> layerList;
    
    protected LayerManger(){
        layerList = new ArrayList<>();
    }
    
    public double[][] multiplyLayers(){
        return new PrototypeNoiseLayer().getValues();
    }
}
