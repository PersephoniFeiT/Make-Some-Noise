package BackEnd.Accounts;
import BackEnd.Editor.*;
import BackEnd.Editor.PerlinNoiseLayer;
import Exceptions.ExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maya Malavasi
 * Project represents a single Project instance in the editor. It can translate itself to and from JSON to be stored in
 * the database, and stored a list of {@link NoiseLayer}s, as well as metadata like title, username of creator, and status for
 * sharing purposes.
 */
public class Project {
    private Integer ID;

    public String title = "New Project";
    public final LocalDate dateCreated;
    public String status = "private";
    public String thumbnail;
    public List<String> tags = new ArrayList<>();
    private final List<NoiseLayer> layers = new ArrayList<>();
    public int color1 = 0x000000;
    public int color2 = 0xFFFFFF;
    private Integer accID;
    private BlendMode bm = BlendMode.MULTIPLY;

    /**
     * Constructs a new Project object with initial attribute values set
     * @param ID Account ID of creator
     * @param title {@link String} title of project
     * @param dateCreated The date the project was created
     */
    public Project(Integer ID, String title, LocalDate dateCreated){
        this.ID = ID;
        this.thumbnail = "MakeSomeNoiseJavaproject/src/ImageSources/stockThumbnail.png";
        this.title = title;
        this.dateCreated = dateCreated;
    }

    /**
     * Constructs a new Project object with only the title set. For use when working as a guest with no account ID
     * @param title {@link String} title of project
     */
    public Project(String title){
        this.ID = null;
        this.thumbnail = "MakeSomeNoiseJavaproject/src/ImageSources/stockThumbnail.png";
        this.title = title;
        this.dateCreated = LocalDate.now();
    }

    /** Returns the unique project ID as an {@link Integer}.
     * @return the {@link Integer} project ID -- null if has not been saved to data base (i.e. editing as guest)
     */
    public Integer getID(){return this.ID;}

    /** Sets the unique project ID.
     * @param ID the {@link Integer} ID of the project
     */
    public void setID(Integer ID){this.ID = ID;}

    /** Removes a {@link NoiseLayer} from the project's List of {@link NoiseLayer}s
     * @param index the index position of the {@link NoiseLayer} to remove
     */
    public void removeLayer(int index){
        this.layers.remove(index);
    }

    /** Removes a {@link NoiseLayer} from the project's List of {@link NoiseLayer}s
     * @param nl the {@link NoiseLayer} object to find and remove from List
     * */
    public void removeLayer(NoiseLayer nl) {
        this.layers.remove(nl);
    }

    /** Returns a safe copy of the {@link NoiseLayer} List that nonetheless contains the actual {@link NoiseLayer} objects
     * @return a new ArrayList that contains the project's {@link NoiseLayer} objects
     */
    public ArrayList<NoiseLayer> getLayerList(){
        return new ArrayList<>(this.layers);
    }

    /** Returns the {@link NoiseLayer} object at an index in the {@link NoiseLayer} List.
     * @param index the int index of the {@link NoiseLayer}
     * @return the requested {@link NoiseLayer}
     */
    public NoiseLayer getLayer(int index){
        return this.layers.get(index);
    }

    /** Adds {@link NoiseLayer} object to {@link NoiseLayer} List.
     * @param layer the {@link NoiseLayer} to add
     */
    public void addLayer(NoiseLayer layer){
        this.layers.add(layer);
    }

    /** Clears {@link NoiseLayer} List of all objects
     */
    public void clearLayers(){
        this.layers.clear();
    }

    /** Get first color of gradient
     * @return int for hex code of color1
     */
    public int getColor1(){
        return color1;
    }

    /** Get second color of gradient
     * @return int for hex code of color2
     */
    public int getColor2(){
        return color2;
    }

    /** Set first color of gradient
     * @param hexCode int for hex code of color1
     */
    public void setColor1(int hexCode){
        this.color1 = hexCode;
    }

    /** Set second color of gradient
     * @param hexCode int for hex code of color2
     */
    public void setColor2(int hexCode){
        this.color2 = hexCode;
    }

    /** Get account ID associated with the project
     * @return account ID of project creator
     */
    public Integer getAccID(){return this.accID;}

    /** Set account ID associated with project
     * @param accID {@link Integer} account ID associated with project
     */
    public void setAccID(Integer accID){this.accID = accID;}

    /** Get blend mode of project
     * @return BlendMode blendmode
     */
    public BlendMode getBlendMode(){return this.bm;}

    /** Set blend mode of project
     * @param bmString {@link String} of blend mode (i.e. "MULTIPLY", "DIVIDE", etc.)
     */
    public void setBlendMode(String bmString){this.bm = BlendMode.fromString(bmString);}
    /** Set blend mode of project
     * @param bm BlendMode object (i.e. BlendMode.MULTIPLY, BlendMode.DIVIDE, etc.)
     */
    public void setBlendMode(BlendMode bm){this.bm = bm;}


    /** Creates, constructs, and returns a new Project instance from a given JSON {@link String}.
     * @param JSON JSON {@link String} containing all project metadata and {@link NoiseLayer} parameters
     * @return a new Project instance constructed from JSON {@link String}
     */
    public static Project fromJSONtoProject(String JSON){
        try {
            // mapper to map {@link String} names to object fields
            ObjectMapper objectMapper = new ObjectMapper();
            //root node is start of project tree
            JsonNode rootNode = objectMapper.readTree(JSON);

            // Extract the first key (which is the project ID)
            Iterator<String> fieldNames = rootNode.fieldNames();
            // if it's an empty JSON file, we have a problem. Just return an empty project
            if (!fieldNames.hasNext()) return new Project("");
            //otherwise the Project ID is the first {@link String}
            String idString = fieldNames.next();

            Integer projectId;
            if (idString.isEmpty()) projectId = null;
            else projectId = Integer.parseInt(idString);

            //now go in a layer, starting from the project ID
            JsonNode projectNode = rootNode.get(idString);

            // Extract primitive fields
            String title;
            if (!projectNode.has("title")) title = ""; else title = projectNode.get("title").asText();
            LocalDate dateCreated = LocalDate.parse(projectNode.get("dateCreated").asText());
            String status = projectNode.get("status").asText();
            String thumbnail = projectNode.get("thumbnail").asText();
            // Extract tags from array in JSON
            JsonNode tagsNode = projectNode.get("tags");
            List<String> tagString = new ArrayList<>();
            if (tagsNode != null && tagsNode.isArray()) {
                for (JsonNode tag : tagsNode) {
                    tagString.add(tag.asText());
                }
            }
            int c1 = projectNode.get("color1").asInt();
            int c2 = projectNode.get("color2").asInt();
            int accID = projectNode.get("accountID").asInt();

            // Create a new Project instance
            Project project = new Project(projectId, title, dateCreated);
            project.status = status;
            project.thumbnail = thumbnail;
            project.tags.addAll(tagString);
            project.setColor1(c1);
            project.setColor2(c2);
            project.setAccID(accID);

            // Extract {@link NoiseLayer} objects
            int layerIndex = 1;
            while (projectNode.has("layer" + layerIndex)) {
                //set root to layer name
                JsonNode layerNode = projectNode.get("layer" + layerIndex);

                // Assuming {@link NoiseLayer} has a constructor or a fromJson method
                NoiseLayer layer;

                int seed = layerNode.get("seed").asInt();
                double freq = layerNode.get("freq").asDouble();
                double amp = layerNode.get("amp").asDouble();
                double floor = layerNode.get("floor").asDouble();
                double ceiling = layerNode.get("ceiling").asDouble();
                double gain = layerNode.get("gain").asDouble();
                String bmString = layerNode.get("blendmode").asText();
                BlendMode bm = BlendMode.fromString(bmString);

                String type = layerNode.get("type").asText();
                switch (type) {
                    case "PerlinNoiseLayer": 
                        layer = new PerlinNoiseLayer();
                        break;
                    case "RandomNoiseLayer":
                        //int seed, double floor, double ceiling, double amplitude, double frequency
                        layer =  new RandomNoiseLayer(seed, floor, ceiling, gain, amp, freq, bm);
                        break;
                    case "Simplex2NoiseLayer":
                        //int seed, double floor, double ceiling, double amplitude, double frequency
                        layer =  new Simplex2NoiseLayer(seed, floor, ceiling, gain, amp, freq, bm);
                        break;
                    case "Simplex3NoiseLayer":
                        layer =  new Simplex3NoiseLayer(seed, floor, ceiling, gain, amp, freq, bm);
                        break;
                    case "SimplexNoise":
                        layer =  null;//new SimplexNoise();
                        break;
                    default: 
                        layer = new RandomNoiseLayer(seed, floor, ceiling, gain, amp, freq, bm); //not a known type?
                        break;
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

    /** Reads a Project instance and creates a JSON {@link String} from the project data and metadata
     * @return a {@link String} JSON containing all project data and metadata */
    public String toJSONString(){
        StringBuilder s = new StringBuilder();


        s.append("{");
        if (this.getID() == null) s.append("\"\""); else s.append("\"").append(this.getID()).append("\"");
        s.append(": {");

        s.append("\"title\": ");
        if (this.title == null) s.append("\"New Project\","); else s.append("\"").append(this.title).append("\",");

        s.append("\"dateCreated\": \"").append(this.dateCreated.toString()).append("\",");
        s.append("\"status\": \"").append(this.status).append("\",");
        s.append("\"thumbnail\": \"").append(this.thumbnail).append("\",");
        s.append("\"color1\": \"").append(this.getColor1()).append("\",");
        s.append("\"color2\": \"").append(this.getColor2()).append("\",");
        s.append("\"accountID\": \"").append(this.accID).append("\",");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonTags = objectMapper.writeValueAsString(this.tags);
            s.append("\"tags\": ").append(jsonTags);
        } catch (Exception e){
            s.append("\"tags\": []");
        }
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
            s.append("\"gain\": ").append(l.getGain()).append(",");
            s.append("\"blendmode\": ").append(l.getBlendMode().toString());
            s.append("}");
            if (i<this.layers.size()) s.append(",");
            i++;
        }
        s.append("}}");
        return s.toString();
    }
}
