package appmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

public  class PropertyFileWriter {

    private static PropertyFileWriter propertyFileWriter = null;
    private final Properties write = new Properties();
    private final Properties writeUpdate = new Properties();
    private String fileName;
    FileOutputStream fos;

    private PropertyFileWriter(String file) {
        try {
            FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" + file);
            write.load(in);
            writeUpdate.load(in);
            in.close();
            this.fileName = file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPropertyValue(String propertyName, String propertyValue) {
        try {
            this.fos = new FileOutputStream(System.getProperty("user.dir") + "/src/test/resources/" + fileName);
            if (write.containsKey(propertyName)) {
                write.setProperty(propertyName, propertyValue);
            } else {
                write.put(propertyName, propertyValue);
            }
            writeUpdate.putAll(write);
            Iterator keyIterator = writeUpdate.keySet().iterator();

            while(keyIterator.hasNext()){
                String key   = (String) keyIterator.next();
                write.remove(key);
            }
            write.store(fos, null);
            writeUpdate.store(fos, null);
            write.putAll(writeUpdate);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyFileWriter getInstance(String fileName) {
        if (propertyFileWriter == null) {
            propertyFileWriter = new PropertyFileWriter(fileName);
        }
        return propertyFileWriter;

    }
}



