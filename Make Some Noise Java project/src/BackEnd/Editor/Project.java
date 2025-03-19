package BackEnd.Editor;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public final int ID;
    public List<NoiseLayer> layers;

    public Project(int ID, int[][] layerParameters){
        this.ID = ID;
        this.layers = new ArrayList<NoiseLayer>();
    }

    public String toJSONString(){
        return "";
    }
}
