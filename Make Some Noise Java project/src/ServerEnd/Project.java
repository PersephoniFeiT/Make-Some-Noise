package ServerEnd;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public List<Layer> layers;

    public Project(int[][] layerParameters){
        layers = new ArrayList<Layer>();
    }

    public class Layer{
        public int paramter1;
    }
}
