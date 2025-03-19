package BackEnd.Accounts;

import BackEnd.Editor.NoiseLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private Integer ID;

    public String title;
    public String username;
    public final LocalDate dateCreated = LocalDate.now();
    public String status;
    public String thumbnail;
    public final List<String> tags = new ArrayList<>();
    public final List<NoiseLayer> layers = new ArrayList<>();

    public Project(int ID, String title, String username){
        this.ID = ID;
        this.status = "private";
        this.thumbnail = "";
        this.username = username;
        this.title = title;
    }

    public Project(String title){
        this.ID = null;
        this.status = null;
        this.thumbnail = "";
        this.username = null;
        this.title = title;
    }

    public static Project fromJSONtoProject(String JSON){
        return new Project("");
    }

    public Integer getID(){return this.ID;}

    public String toJSONString(){
        return "";
    }
}
