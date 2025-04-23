package BackEnd.Editor;
import java.util.*;

/**
 * LayerManager is responsible for combining NoiseLayers into a single raster
 * @author Fei Triolo
*/
public class LayerManager {

    /**
     * Primary LayerManager function that combines each NoiseLayer across a certain raster matrix size.
     * For each "real-space" position in the raster matrix, a NoiseLayer is evaluated based on a "virtual-space" mapping determined by its frequency.
     * The evaluation is combined with the previous value of that position defaulting to 1.0 depending on the layer's BlendMode.
     * @param width The real-space width of the raster matrix
     * @param height The real-space height of the raster matrix
     * @param layerList a list of NoiseLayers to be combined
     * @return a [width][height] matrix representing each pixel value of the combined NoiseLayers
     */
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
                    double blendVal =  blend(raster[x][y], l.evaluate(virtualx, virtualy), l.getBlendMode());
                    raster[x][y] = blendVal < 0? 0 : (blendVal > 1? 1 : blendVal);
                }
            }
        });
        return raster;
    }

    /**
     * Helper function responsible for determining how each layer evaluation is blended to the raster matrix based on the layer's BlendMode
     * @param rasterVal the existing value of the raster matrix at a certain real-space position
     * @param layerVal the value returned by a NoiseLayer when evaluated at a corresponding certain virtual-space position
     * @param layerBlendMode the BlendMode of the NoiseLayer being combined
     * @return the updated raster value after being combined with the current layer value using its BlendMode
     */
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
