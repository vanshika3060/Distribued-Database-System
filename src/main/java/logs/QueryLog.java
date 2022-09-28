package logs;


import authentication.UserSession;
import utils.Constants;

import java.io.*;

import static utils.Constants.Global_Meta_File_Path;
import static utils.RemoteSession.putFile;

public class QueryLog {
    public static void writeQueryLog(String query,String error) throws IOException {
        UserSession s = new UserSession();
        File queryLog = new File(Constants.Log_Folder);
        if(!queryLog.exists()) {
            queryLog.mkdir();
        }
        FileWriter fw = new FileWriter(Constants.Query_Log_Path,true);
        fw.append("\n"+s.getUser()+" || ");
        java.util.Date date = new java.util.Date();
        putFile(Constants.Query_Log_Path);
        fw.append(date+" || ");
        fw.append(query+" || ");
        fw.append(error);
        fw.close();
    }
}
