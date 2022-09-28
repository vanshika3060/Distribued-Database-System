package metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Server1 {

    public static void Server1Data() throws IOException {
        File fobj = new File("Server1.txt");
        FileWriter fileWriter = new FileWriter("globalMetaData.txt");
        fileWriter.write("table_name \t \t \t date_created");
        fileWriter.close();
    }

}
