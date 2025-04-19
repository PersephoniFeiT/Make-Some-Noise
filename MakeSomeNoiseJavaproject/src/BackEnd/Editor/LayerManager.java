package BackEnd.Editor;
import java.util.*;

/* Class responsible for multiplying noise layers for fractal adding
 * (potentially other leyer types in the future) 
 * so they can be rendered*/
public class LayerManager {


    public static double[][] multiplyLayers(int width, int height, ArrayList<NoiseLayer> layerList){
        double[][] raster = new double[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                raster[x][y] = 1;
            }
        }
        layerList.forEach(l -> {
            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    int virtualx = (int) ((int) x/l.getFreq());
                    int virtualy = (int) ((int) y/l.getFreq());
                    double blendVal =  blend(raster[x][y], l.evaluate(virtualx, virtualy), l.getBlendMode())
                    raster[x][y] = blendVal < 0? 0 : blendVal > 1? 1 : blendVal;
                }
            }
        });
        return raster;
    }

    private static double blend(double rasterVal, double layerVal, BlendMode layerBlendMode){
        switch(layerBlendMode){
            case MULTIPLY:
                return rasterVal *= layerVal;
            case ADD:
                return rasterVal += layerVal;
            case SUBTRACT:
                return rasterVal -= layerVal;
            case DIVIDE:
                return rasterVal /= layerVal;
            default:
                return rasterVal;
        }
    }
}
