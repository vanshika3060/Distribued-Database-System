package metadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Server2 {

    public static void Server2Data() throws IOException {
        File fobj = new File("C:\\Users\\AVuser\\IdeaProjects\\csci5408-dpg13\\src\\main\\java\\sys\\Server2.txt");
        FileWriter fileWriter = new FileWriter("globalMetaData.txt");
        fileWriter.write("table_name \t \t \t date_created");
        fileWriter.close();
    }

}