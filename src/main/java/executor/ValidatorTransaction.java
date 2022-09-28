package executor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static utils.Constants.DATABASE_DIRECTORY;

public class ValidatorTransaction {

    public static boolean updateValidator(String query, String db) throws IOException {

        boolean validator = false;

        String[] tokens = query.split("set");
        String[] tableName = tokens[0].split(" ");

        BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIRECTORY + db + "/" + tableName[1] + ".txt"));
        String line = br.readLine();
        String[] headers = line.replace("(TEXT)", "").replace("(INT@PK)", "")
                .split("\\|");
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
                validator = true;
            }
        }

        return validator;
    }

    public static boolean insertValidator(String query, String db) throws IOException {

        boolean validator = false;

        String[] tokens = query.split(" ");
        String tableName = tokens[2];

        String[] subarray = Arrays.copyOfRange(tokens, 3, Arrays.asList(tokens).indexOf("values"));


        BufferedReader br = new BufferedReader(new FileReader(DATABASE_DIRECTORY + db + "/" + tableName + ".txt"));
        String line = br.readLine();
        String[] headers = line.split(" | ");
        int le = (headers.length) - (headers.length)/2;
        if (le == subarray.length) {
            validator = true;
        }

        return validator;
    }
}
