package logs;

import utils.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventLog {
    public static void writeEventLog(String query) throws IOException {
        File log = new File(Constants.Log_Folder);
        if(!log.exists()) {
            log.mkdir();
        }
        FileWriter fw = new FileWriter(Constants.Query_Log_Path,true);
        java.util.Date date = new java.util.Date();
        fw.append(date+"||");
        fw.append("\n");
        fw.close();
    }
}