package appmanager.util.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parboiled.errors.ParsingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
@Configuration
@PropertySource(value={"classpath:local.properties"})
public class JSONHelper {
    @Value("${jsonPath}")
    private String location;
    private String fileName;
    private JSONArray jsonArray = new JSONArray();

    public void init(String fileName){
        this.fileName=fileName;
        jsonArray = new JSONArray();
        JSONParser jsonParser = new JSONParser();
        try
        {
            FileReader reader = getFileReader();
            Object obj = jsonParser.parse(reader);
            jsonArray = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private FileReader getFileReader() throws FileNotFoundException {
        //location = location!=null?location:"/src/test/resources/InputFiles/json/";
        String cPath = System.getProperty("user.dir") + location + this.fileName + ".json";
        System.out.println(cPath);
        return new FileReader(cPath.replace("jsonPath=",""));
    }

    public Object getJSONObject(int index){
        if(jsonArray==null){
            throwException(null);
        }
        return jsonArray.get(index);
    }

    private void throwException(String s) {
        throw new RuntimeException(s==null||s.length()==0?"Initialize the library with file:\nUsage: jsonHelper.init(\"fileName\")":s);
    }

    public Object getJSONObject(int index, String name) {
        JSONObject jsonObject = (JSONObject) getJSONObject(index);
        if(jsonObject!=null){
            return (JSONObject) jsonObject.get(name);
        }
        return null;
    }

    public Object getJSONObjectByName(String name) {
        JSONObject jsonObject = (JSONObject) getJSONObject(0);
        if(jsonObject!=null){
            return (JSONObject) jsonObject.get(name);
        }
        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int size(){
        if (jsonArray==null) {
            throwException(null);
        }
        return jsonArray.size();
    }

}