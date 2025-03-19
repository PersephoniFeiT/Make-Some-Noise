package BackEnd.Accounts;

import BackEnd.Editor.NoiseLayer;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private int ID;
    public String title;
    public List<NoiseLayer> layers;

    public Project(int ID, int[][] layerParameters){
        this.ID = ID;
        this.layers = new ArrayList<NoiseLayer>();
    }

    public Project(){
        this.layers = new ArrayList<NoiseLayer>();
    }

    public int getID(){return this.ID;}

    public String toJSONString(){
        return "";
    }
}
