package metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MetadataGenerator {

    public static void createMetaData() throws IOException {

        File fobj = new File("globalMetaData.txt");
        FileWriter fileWriter = new FileWriter("globalMetaData.txt");
        fileWriter.write("database_id \t \t \t database_name \t \t \t server_name \t \t \t time_created \t \t \t number_of_tables");
        fileWriter.close();

    }
}
