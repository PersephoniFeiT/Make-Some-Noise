package BackEnd.Editor;
import java.util.*;

/* Class responsible for multiplying noise layers for fractal adding
 * (potentially other leyer types in the future) 
 * so they can be rendered*/
public class LayerManager {
    
    private static ArrayList<BackEnd.Editor.NoiseLayer> layerList;
    
    protected LayerManager(){
        layerList = new ArrayList<>();
    }
    public LayerManager(ArrayList<NoiseLayer> noiseLayers){
        layerList = noiseLayers;
    }

    public static void removeLayer(int index){
        layerList.remove(index);
    }

    public static NoiseLayer getLayer(int index){
        return layerList.get(index);
    }

    public static void addLayer(NoiseLayer layer){
        layerList.add(layer);
    }

    public static void clearLayers(){
        layerList = new ArrayList<>();
    }

    public static void updateLayer(int index, NoiseLayer updatedLayer){
        layerList.remove(index);
        layerList.add(index, updatedLayer);
    }

    public static double[][] multiplyLayers(int width, int height){
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
