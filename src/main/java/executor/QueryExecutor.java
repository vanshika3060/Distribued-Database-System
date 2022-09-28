package executor;

import logs.GeneralLog;
import logs.QueryLog;
import utils.RemoteSession;

import java.io.*;
import java.net.UnknownHostException;
import java.util.Arrays;

import static utils.Constants.*;
import static utils.RemoteSession.*;
import static utils.RemoteSession.makeDatabase;

import java.util.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryExecutor {

    static QueryLog qlog = new QueryLog();
    static RemoteSession host = new RemoteSession();
    static String error = "No Error";
    static String hostName;
    static FileWriter GM;
    static GeneralLog gLog;

    {
        try {
            hostName = host.getHostName();
            System.out.println("Host Name: " + hostName);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Hostname");
        }
    }

    public static void ExecuteCreateDatabase(String query) throws Exception {
        String databaseString = query.replace(";", "");
        String[] databaseArray = databaseString.split(" ");
        String databaseName = databaseArray[databaseArray.length - 1];
        String databaseDirectory = DATABASE_DIRECTORY + databaseName;
        File database = new File(databaseDirectory);
        if (database.exists()) {
            System.out.println("The database already exists!");
        } else {
            database.mkdir();
            boolean db_result = makeDatabase(databaseName);
            System.out.println("Database created successfully!");
            qlog.writeQueryLog(query, error);
        }
    }

    public static String ExecuteUseDatabase(String query) throws IOException {
        String databaseString = query.replace(";", "");
        String[] databaseArray = databaseString.split(" ");
        String databaseName = databaseArray[1];
        String databaseDirectory = DATABASE_DIRECTORY + databaseName;
        String currentDatabase = null;
        File database = new File(databaseDirectory);
        if (database.exists()) {
            currentDatabase = database.toString() + "/";
            qlog.writeQueryLog(query, error);
        } else {
            System.out.println("No database with name " + databaseName + " found");
        }
        return currentDatabase;
    }

    public static void ExecuteCreateTable(String db, String query) throws IOException {
        query = query.replace(");", " ");
        String[] columnNames = query.split("\\(");
        if (columnNames.length >= 2) {
            String tableName = columnNames[0].split(" ")[2];
            String filePath = db + "/" + tableName + ".txt";
            String metafilePath = db + "/" + tableName + "_meta" + ".txt";
            FileWriter metawriter = new FileWriter(metafilePath);
            File f = new File(filePath);
            if (f.exists()) {
                System.out.println("Table with name " + tableName + " already exists.");

            }
            try (FileWriter writer = new FileWriter(filePath)) {
                StringBuilder fileheader = new StringBuilder();
                String[] headerList = columnNames[1].split(",");
                for (int i = 0; i < headerList.length; i++) {
                    String[] headers = headerList[i].toString().trim().split(" ");
                    metawriter.write(Arrays.toString(headers).replace("[", "").replace("]", "")
                            .replace(",", "|").replace("| key", ""));
                    metawriter.write("\n");
                    if (headers.length == 2) {
                        fileheader.append(headers[0]).append("(").append(headers[1]).append(")")
                                .append("|");
                    }
                    if (headers.length == 4 && headers[2].equalsIgnoreCase("PRIMARY")) {
                        fileheader.append(headers[0]).append("(").append(headers[1]).append("@")
                                .append("PK").append(")").append("|");
                    }
                    if (headers.length == 4 && headers[2].equalsIgnoreCase("REFERENCES")) {

                        String fkTable = headers[3].split("\\(")[0];
                        String fKcol = headers[3].split("\\(")[1].replaceAll("\\)", "");
                        fileheader.append(headers[0]).append("(").append(headers[1]).append("@")
                                .append("FK").append("@").append(fkTable).append("@")
                                .append(fKcol).append(")").append("|");
                    }
                    qlog.writeQueryLog(query, error);
                }
                writer.write(fileheader.toString());
                writer.close();

                metawriter.close();
                putFile(metafilePath);
                if (hostName.equals(VM1_HOST)) {
                    GM = new FileWriter(Global_Meta_File_Path, true);
                    GM.append(db + "||");
                    GM.append(hostName + "||");
                    java.util.Date date = new java.util.Date();
                    GM.append(date + "||");
                    GM.append(tableName + "\n");
                    FileWriter I1 = new FileWriter(Instance1_Meta_File_Path, true);
                    I1.append(db + "||");
                    I1.append(hostName + "||");
                    I1.append(date + "||");
                    I1.append(tableName + "|\n");
                    I1.close();

                    GM.close();
                    putFile(Global_Meta_File_Path);
                } else {
                    GM = new FileWriter(Global_Meta_File_Path, true);
                    GM.append(db + "||");
                    GM.append(hostName + "||");
                    java.util.Date date = new java.util.Date();
                    GM.append(date + "||");
                    GM.append(tableName + "\n");
                    FileWriter I2 = new FileWriter(Instance2_Meta_File_Path, true);
                    I2.append(db + "||");
                    I2.append(hostName + "||");
                    I2.append(date + "||");
                    I2.append(tableName + "|\n");

                    I2.close();
                    GM.close();
                    putFile(Global_Meta_File_Path);
                }
                putFile(filePath);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static void ExecuteInsertStatement(String db, String query) throws IOException {
        String q = query.replace(")", "");
        String[] q1 = q.split("\\(");
        String tableName = q1[0].split(" ")[2];
        String filePath = db + "/" + tableName + ".txt";
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("No table with name " + tableName + " exists.");
        } else {
            String[] columnNames = q1[1].split(" VALUES");
            String[] finalColumnNames = columnNames[0].split(",");
            String[] values = q1[2].replace(";", "").split(",");

            File myObj = new File(db+"/"+tableName+"_meta.txt");
            Scanner myReader = new Scanner(myObj);
            ArrayList<String> datatypes1 = new ArrayList<String>();
            String[] datatypes;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                datatypes = data.split("\\|");
                datatypes1.add(datatypes[1]);
            }
            myReader.close();
            int columnCount = 0;
            for (Object tableMetaPart : datatypes1) {
                String j = String.valueOf(tableMetaPart);
                System.out.println(j);
                if (j.equals("int")) {
                    try {
                        assert values != null;
                        Integer.parseInt(values[columnCount]);
                    } catch (Exception e) {
                        System.out.println("Invalid syntax. Please use the following syntax: insert into <tableName> values (value, value, ...)");
                        return;
                    }
                } else if (j.equals("text")) {
                    try {
                        assert values != null;
                        if (values[columnCount] == null) {
                            System.out.println("Invalid syntax. Please use the following syntax: insert into <tableName> values (value, value, ...)");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid syntax. Please use the following syntax: insert into <tableName> values (value, value, ...)");
                        return;
                    }
                }
                columnCount++;
            }
            StringBuilder rowContent = new StringBuilder();
            assert values != null;
            for (String insertValue : values) {
                rowContent.append(insertValue.trim()).append(" | ");
            }
            rowContent.deleteCharAt(rowContent.length() - 1);
            rowContent.deleteCharAt(rowContent.length() - 1);
            rowContent.append("\n");
            FileWriter writer = new FileWriter(  filePath, true);
            writer.append(rowContent.toString());
            writer.close();
            putFile(filePath);
            gLog = new GeneralLog();
            gLog.writeGeneralLog(query,db,tableName);
        }
    }

    public static void ExecuteSelectStatement(String db, String query) throws Exception {
        query = query.toLowerCase();
        if (query.contains("where")) {
            String[] q = query.split("where");
            String[] FetchtableName = q[0].split(" ");
            String tableName = FetchtableName[3];
            String[] FetchColumnName = q[1].split("=");
            String columnName = FetchColumnName[0].replace(" ", "");
            BufferedReader br = new BufferedReader(new FileReader(db + "/" + tableName + ".txt"));
            String line = br.readLine();
            int getColumns = line.indexOf(columnName);
            System.out.println(getColumns);
            System.out.println(FetchColumnName[1].replace(";", "|").replace("\"", ""));
            while ((line = br.readLine()) != null) {
                boolean record = line.contains(FetchColumnName[1].replace(";", "|").replace("\"", ""));
                if (record) {
                    System.out.println(line);
                }
            }
        } else {
            String[] q = query.split(" ");
            String tableName = q[3].replace(";", "");
            BufferedReader br = new BufferedReader(new FileReader(  db + "/" + tableName + ".txt"));
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                System.out.println(line);
            }
        }
        qlog.writeQueryLog(query, error);
    }

    public static void ExecuteUpdateStatement(String db, String query) {
        String[] tokens = query.split("set");
        String[] tableName = tokens[0].split(" ");
        try {
            BufferedReader br = new BufferedReader(new FileReader( db + "/" + tableName[1] + ".txt"));
            String line = br.readLine();
            StringBuffer buffer = new StringBuffer();
            buffer.append(line + System.lineSeparator());
            String[] headers = line.replace("(TEXT)", "").replace("(INT@PK)", "").split("\\|");
            FileWriter writeData = new FileWriter(db + "/" + tableName[1] + ".txt");
            String[] columnNames = tokens[1].split("where");
            String[] updateValues = columnNames[0].split("=");
            String key = updateValues[0].replace(" ", "");
            String value = updateValues[1].replace("\"", "");
            String[] referenceValues = columnNames[1].split("=");
            String rkey = referenceValues[0].replace(" ", "");
            String rvalue = referenceValues[1].replace(";", "");
            rvalue = rvalue.trim();
            int rKeyIndex = 0;
            int keyIndex = 0;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].contains(rkey)) {
                    rKeyIndex = i;
                }
                if (headers[i].contains(key)) {
                    keyIndex = i;

                }

            }
            String result = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values[rKeyIndex].trim().equals(rvalue)) {
                    values[keyIndex] = value;
                }
                result = String.join("|", values);
                buffer.append(result + System.lineSeparator());

            }
            String fileContents = buffer.toString();
            writeData.write(fileContents);

            writeData.close();
            putFile(db + "/" + tableName[1] + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            gLog.writeGeneralLog(query,db,tableName[1]);
        } catch (IOException e) {
            System.out.println("Error for General Log by Update Statement");
        }

    }

    public static void ExecuteDeleteStatement(String db, String query) throws FileNotFoundException {
        if (query.toLowerCase().contains("where")) {
            String[] tokens = query.split("where");
            String tableName = tokens[0].split(" ")[2];
            String[] references = tokens[1].split("=");
            String rkey = references[0].trim();
            String rvalue = references[1].replace(";", "").trim();
            try {

                BufferedReader br = new BufferedReader(new FileReader(db + "/" + tableName + ".txt"));
                String line = br.readLine();
                String[] headers = line.replace("(TEXT)", "").replace("(INT@PK)", "").split("\\|");
                StringBuffer buffer = new StringBuffer();
                buffer.append(line + System.lineSeparator());
                int rKeyIndex = 0;
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equals(rkey)) {
                        rKeyIndex = i;
                    }

                }
                FileWriter writeData = new FileWriter(db + "/" + tableName + ".txt");
                String result = null;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split("\\|");
                    if (!values[rKeyIndex].trim().equals(rvalue.trim())) {
                        result = String.join("|", values);
                        buffer.append(result + System.lineSeparator());
                    }
                }
                String fileContents = buffer.toString();
                writeData.write(fileContents);
                writeData.close();
                putFile(db + "/" + tableName + ".txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                gLog.writeGeneralLog(query,db,tableName);
            } catch (IOException e) {
                System.out.println("Error for General Log by delete Statement");
            }
        } else {
            String tableName = query.split(" ")[2];
            String deleteTablePath = db + "/" + tableName;
            PrintWriter writer = new PrintWriter(deleteTablePath);
            writer.print("");
            putFile(db + "/" + tableName + ".txt");

        }

    }

}