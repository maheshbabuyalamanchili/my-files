package appmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public  class PropertyFileReader {

    private static PropertyFileReader propertyFileReader = null;
    private final Properties reader = new Properties();

    FileInputStream fis;

    public PropertyFileReader(String file) {
        try {
            this.fis = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/"+file);
            reader.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public  String get(String name){
       return this.reader.getProperty(name);
}

    public static PropertyFileReader getInstance(String fileName) {
        if (propertyFileReader == null) {
            propertyFileReader = new PropertyFileReader(fileName);
        }
        return propertyFileReader;

    }

}




