package BackEnd.Accounts;

import BackEnd.Editor.*;

import BackEnd.Editor.PerlinNoiseLayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Project {
    private final Integer ID;

    public String title;
    public String username;
    public final LocalDate dateCreated;
    public String status;
    public String thumbnail;
    public final List<String> tags = new ArrayList<>();
    public final List<NoiseLayer> layers = new ArrayList<>();

    public Project(int ID, String title, String username, LocalDate dateCreated){
        this.ID = ID;
        this.status = "private";
        this.thumbnail = "";
        this.username = username;
        this.title = title;
        this.dateCreated = dateCreated;
    }

    public Project(String title){
        this.ID = null;
        this.status = "private";
        this.thumbnail = "";
        this.username = null;
        this.title = title;
        this.dateCreated = LocalDate.now();
    }

    public static Project fromJSONtoProject(String JSON){
        ///TODO translate JSON

        try {
            // mapper to map string names to object fields
            ObjectMapper objectMapper = new ObjectMapper();
            //root node is start of project tree
            JsonNode rootNode = objectMapper.readTree(JSON);

            // Extract the first key (which is the project ID)
            Iterator<String> fieldNames = rootNode.fieldNames();
            // if it's an empty JSON file, we have a problem. Just return an empty project
            if (!fieldNames.hasNext()) return new Project("");
            //otherwise the Project ID is the first string
            String idString = fieldNames.next();

            Integer projectId;
            if (idString.equals("null")) return new Project("");
            else projectId = Integer.parseInt(idString);

            //now go in a layer, starting from the project ID
            JsonNode projectNode = rootNode.get(idString);

            // Extract primitive fields
            String title = projectNode.get("title").asText();
            String username = projectNode.get("username").asText();
            LocalDate dateCreated = LocalDate.parse(projectNode.get("dateCreated").asText());
            String status = projectNode.get("status").asText();
            String thumbnail = projectNode.get("thumbnail").asText();
            // Extract tags from array in JSON
            List<String> tags = objectMapper.convertValue(projectNode.get("tags"), new TypeReference<List<String>>() {});

            // Create a new project instance
            Project project = new Project(projectId, title, username, dateCreated);
            project.status = status;
            project.thumbnail = thumbnail;
            project.tags.addAll(tags);

            // Extract NoiseLayer objects
            int layerIndex = 1;
            while (projectNode.has("layer" + layerIndex)) {
                //set root to layer name
                JsonNode layerNode = projectNode.get("layer" + layerIndex);

                // Assuming NoiseLayer has a constructor or a fromJson method
                NoiseLayer layer;

                String type = layerNode.get("seed").asText();
                if (type.equals("PerlinNoiseLayer")) layer = new PerlinNoiseLayer();
                else if (type.equals("RandomNoiseLayer")) layer = new RandomNoiseLayer();
                else if (type.equals("Simplex2NoiseLayer")) layer = new Simplex2NoiseLayer();
                else if (type.equals("Simplex3NoiseLayer")) layer = new Simplex3NoiseLayer();
                else if (type.equals("SimplexNoise")) layer = new SimplexNoise();



                = new NoiseLayer(
                        layerNode.get("seed").asLong(),
                        layerNode.get("freq").asDouble(),
                        layerNode.get("amp").asDouble(),
                        layerNode.get("floor").asDouble(),
                        layerNode.get("ceiling").asDouble()
                );
                project.layers.add(layer);
                layerIndex++;
            }

            return project;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return new Project("");
    }

    public Integer getID(){return this.ID;}

    public String toJSONString(){
        StringBuilder s = new StringBuilder();
        s.append("{").append(this.getID()).append(": {");

        s.append("title: '").append(this.title).append("',");
        s.append("username: '").append(this.username).append("',");
        s.append("dateCreated: '").append(this.dateCreated.toString()).append("',");
        s.append("status: '").append(this.status).append("',");
        s.append("thumbnail: '").append(this.thumbnail).append("',");
        s.append("tags: ").append(this.tags.toString());
        if (!this.layers.isEmpty()) s.append(",");

        int i = 1;
        for (NoiseLayer l : this.layers){
            s.append("layer").append(i).append(": {");
            s.append("type: ").append(l.getClass()).append(",");
            s.append("seed: ").append(l.getSeed()).append(",");
            s.append("freq: ").append(l.getFreq()).append(",");
            s.append("amp: ").append(l.getAmp()).append(",");
            s.append("floor: ").append(l.getFloor()).append(",");
            s.append("ceiling: ").append(l.getCeiling()).append(",");
            s.append("}");
            i++;
            if (i<this.layers.size()) s.append(",");
        }
        s.append("}");
        return s.toString();
    }
}
