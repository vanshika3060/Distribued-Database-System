package analysis;

import java.io.*;
import java.util.*;

public class Analysis {

    public void QueryAnalysis(String search) throws IOException {

        File file = new File("Logs/QueryLog.txt");
        String[] words = null;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str;
        int count =0;
        while((str = br.readLine()) != null)
        {
            words = str.split(" ");
            for (String word : words)
            {
                if (word.equals(search))
                {
                    count++;
                }
            }
        }
        FileInputStream fr1 = new FileInputStream("AnalysisData\\createUpdateData.txt");
        FileOutputStream fr2 = new FileOutputStream("AnalysisData\\createUpdatefile.txt");
        try {
            int n;
            while ((n = fr1.read()) != -1) {
                fr2.write(n);
            }
        }
        finally {
            if (fr1 != null) {

                fr1.close();
            }
            if (fr2 != null) {
                fr2.close();
            }

        }

    }
    public void totalQueries( String active_userName,String instanceName, HashMap<String, MyData> db_name) throws IOException {

        String sCurrentLine = new String();
        String user;
        BufferedReader reader = new BufferedReader(new
                FileReader("Logs/QueryLog.txt"));

        String words[] = null;
        int count=1;

        while ((sCurrentLine = reader.readLine() )!= null)
        {
            words = sCurrentLine.split(" ");
            String current_user = words[0];
            if (words[11].equals("use")) {
                String databaseName = words[12];
                if (db_name.containsKey(databaseName)) {
                    if (current_user.equals(active_userName)) {
                        user = words[0];
                        for (String word : words) {
                            if (word.equals(user)) {
                                count++;
                                db_name.put(databaseName, new MyData(instanceName, count));
                            }
                        }
                    }
                } else {
                    db_name.put(databaseName, new MyData(instanceName, count));
                }
            }
        }
    }
    public void chooseinstance() throws IOException {
        String sCurrentLine2 = new String();
        BufferedReader reader = new BufferedReader(new
                FileReader("Logs/QueryLog.txt"));
        String[] words2 = null;
        String active_userName = "shivam";
        HashMap<String, MyData> db_name = new HashMap<>();
        while ((sCurrentLine2 = reader.readLine() )!= null) {

            words2 = sCurrentLine2.split(" ");
            if(words2[2].equals("distributed-database-1"))
            {
                totalQueries(active_userName,"distributed-database-1", db_name);
            }
            else
            {
                totalQueries(active_userName,"distributed-database-2", db_name);
            }

            FileInputStream fr1 = new FileInputStream("AnalysisData\\analysisData.txt");
            FileOutputStream fr2 = new FileOutputStream("AnalysisData\\analysisfile.txt");
            try {
                int n;
                while ((n = fr1.read()) != -1) {
                    fr2.write(n);
                }
            }
            finally {
                if (fr1 != null) {

                    fr1.close();
                }
                if (fr2 != null) {
                    fr2.close();
                }
            }
        }
    }
}