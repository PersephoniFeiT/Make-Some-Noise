package BackEnd.Editor;
import java.util.*;

/* Class responsible for multiplying noise layers for fractal adding
 * (potentially other leyer types in the future) 
 * so they can be rendered*/
public class LayerManger {
    
    private ArrayList<BackEnd.Editor.NoiseLayer> layerList;
    
    protected LayerManger(){
        layerList = new ArrayList<>();
    }
    public void addLayer(NoiseLayer layer){
        layerList.add(layer);
    }

    public double[][] multiplyLayers(int width, int height){
        double[][] raster = new double[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                raster[x][y] = 1;
            }
        }
        layerList.stream().forEach(l -> {
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    int virtualx = (int) ((int) x/l.getFreq());
                    int virtualy = (int) ((int) y/l.getFreq());
                    raster[x][y] *= l.evaluate(virtualx, virtualy);
                }
            }
        });
        return raster;
    }
}
