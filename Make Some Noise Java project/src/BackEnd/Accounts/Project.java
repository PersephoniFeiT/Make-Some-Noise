package BackEnd.Accounts;
import BackEnd.Editor.*;
import BackEnd.Editor.PerlinNoiseLayer;
import Exceptions.Accounts.ExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Project {
    private final Integer ID;

    public String title = "New Project";
    public String username;
    public final LocalDate dateCreated;
    public String status;
    public String thumbnail;
    public final List<String> tags = new ArrayList<>();
    private final List<NoiseLayer> layers = new ArrayList<>();

    public Project(Integer ID, String title, String username, LocalDate dateCreated){
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

    public Integer getID(){return this.ID;}

    public void removeLayer(int index){
        this.layers.remove(index);
    }

    public void removeLayer(NoiseLayer nl) {
        this.layers.remove(nl);
    }

    public ArrayList<NoiseLayer> getLayerList(){
        return new ArrayList<>(this.layers);
    }

    public NoiseLayer getLayer(int index){
        return this.layers.get(index);
    }

    public void addLayer(NoiseLayer layer){
        this.layers.add(layer);
    }

    public void clearLayers(){
        this.layers.clear();
    }

    public static Project fromJSONtoProject(String JSON){
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
            if (idString.isEmpty()) projectId = null;
            else projectId = Integer.parseInt(idString);

            //now go in a layer, starting from the project ID
            JsonNode projectNode = rootNode.get(idString);

            // Extract primitive fields
            String title;
            if (!projectNode.has("title")) title = null; else title = projectNode.get("title").asText();
            String username;
            if (!projectNode.has("username")) username = null; else username = projectNode.get("username").asText();
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

                int seed = layerNode.get("seed").asInt();
                double freq = layerNode.get("freq").asDouble();
                double amp = layerNode.get("amp").asDouble();
                double floor = layerNode.get("floor").asDouble();
                double ceiling = layerNode.get("ceiling").asDouble();
                double gain = layerNode.get("gain").asDouble();

                String type = layerNode.get("type").asText();
                layer = switch (type) {
                    case "PerlinNoiseLayer" -> new PerlinNoiseLayer();
                    case "RandomNoiseLayer" -> new RandomNoiseLayer(seed, freq, amp, floor, ceiling);
                    case "Simplex2NoiseLayer" -> new Simplex2NoiseLayer(seed, freq, amp, floor, ceiling);
                    case "Simplex3NoiseLayer" -> new Simplex3NoiseLayer(seed, freq, amp, floor, ceiling);
                    case "SimplexNoise" -> null;//new SimplexNoise();
                    default -> new RandomNoiseLayer(seed, freq, amp, floor, ceiling); //not a known type?
                };

                project.layers.add(layer);
                layerIndex++;
            }
            return project;
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
        // if we've gotten through all of this and there was a problem, return blank project.
        return new Project("");
    }

    public String toJSONString(){
        StringBuilder s = new StringBuilder();


        s.append("{");
        if (this.getID() == null) s.append("\"\""); else s.append("\"").append(this.getID()).append("\"");
        s.append(": {");

        s.append("\"title\": ");
        if (this.title == null) s.append("\"New Project\","); else s.append("\"").append(this.title).append("\",");

        s.append("\"username\": ");
        if (this.username == null) s.append("null,"); else s.append("\"").append(this.username).append("\",");

        s.append("\"dateCreated\": \"").append(this.dateCreated.toString()).append("\",");
        s.append("\"status\": \"").append(this.status).append("\",");
        s.append("\"thumbnail\": \"").append(this.thumbnail).append("\",");
        s.append("\"tags\": ").append(this.tags.toString());
        if (!this.layers.isEmpty()) s.append(",");

        int i = 1;
        for (NoiseLayer l : this.layers){
            s.append("\"layer").append(i).append("\": {");
            s.append("\"type\": \"").append(l.getClass().getSimpleName()).append("\",");
            s.append("\"seed\": ").append(l.getSeed()).append(",");
            s.append("\"freq\": ").append(l.getFreq()).append(",");
            s.append("\"amp\": ").append(l.getAmp()).append(",");
            s.append("\"floor\": ").append(l.getFloor()).append(",");
            s.append("\"ceiling\": ").append(l.getCeiling()).append(",");
            s.append("\"gain\": ").append(l.getGain());
            s.append("}");
            if (i<this.layers.size()) s.append(",");
            i++;
        }
        s.append("}}");
        return s.toString();
    }
}
