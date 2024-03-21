package helpers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.json.JSONObject;

public class JsonLoader {

    public JSONObject loadJson(String filename) {
        // Get the file handle for the JSON file
        FileHandle fileHandle = Gdx.files.internal(filename);

        // Check if the file exists
        if (!fileHandle.exists()) {
            Gdx.app.error("JsonLoader", "File not found: " + filename);
            return null;
        }

        try {
            // Read the JSON data from the file
            String jsonString = fileHandle.readString();

            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            return jsonObject;
        } catch (Exception e) {
            Gdx.app.error("JsonLoader", "Error loading JSON file: " + filename, e);
            return null;
        }
    }
}
