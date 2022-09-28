package logs;

import authentication.UserSession;
import utils.Constants;
import utils.RemoteSession;

import java.io.*;

public class GeneralLog {

    public static void writeGeneralLog(String query,String db,String tableName) throws IOException {
        UserSession s = new UserSession();
        RemoteSession host =new RemoteSession();
        File Log = new File(Constants.Log_Folder);
        if(!Log.exists()) {
            Log.mkdir();
        }
        FileWriter generalLog = new FileWriter(Constants.General_Log_Path,true);
        String hostName = host.getHostName();
        generalLog.append("\n"+hostName+" || ");
        generalLog.append(s.getUser()+" || ");
        generalLog.append(db+" || ");
        generalLog.append(tableName+" || ");
        BufferedReader br = new BufferedReader(new FileReader(db + "/" + tableName + ".txt"));
        int count =1;
        try {
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                count++;
            }
        } finally {
            br.close();
        }
        generalLog.append(count-2+" || ");
        java.util.Date date = new java.util.Date();
        generalLog.append(date+" || ");
        generalLog.append(query+" || ");
        generalLog.close();
    }
}