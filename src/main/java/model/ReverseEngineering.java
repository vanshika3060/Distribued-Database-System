package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.Constants.DATABASE_DIRECTORY;
import static utils.Constants.MODELLING_DIRECTORY;

public class ReverseEngineering {


    public void selectDatabase() throws IOException {

        System.out.println("Enter database Name: ");
        Scanner input = new Scanner(System.in);
        String databaseName = input.next();

        String pathString = DATABASE_DIRECTORY + databaseName;
        Path path = Path.of(pathString);
        if (Files.notExists(path)) {
            System.out.println("Database does not exist");
        } else {

            //Create model folder
            File modelPath = new File(MODELLING_DIRECTORY + databaseName);
            if (modelPath.mkdirs()) {
                System.out.println("Directory is created");
            }
            else {
                System.out.println("Directory cannot be created");
            }

            File modelFile = new File(MODELLING_DIRECTORY + databaseName + "/" + databaseName + "_model.txt");
            if (modelFile.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }

            //Get all the tables in a database
            File readFile = new File(pathString);
            String[] fileList = readFile.list();

            FileWriter modelWriter = new FileWriter(modelFile);
            List<String> cardinality = new ArrayList<>();

            //Create model
            assert fileList != null;
            for (String filename : fileList) {
                if(filename.contains("meta")){
                    System.out.println("Files: " + filename);

                    try {
                        File tableInfo = new File(pathString + "/" + filename);
                        Scanner getInfo = new Scanner(tableInfo);
                        String[] parts = filename.split("_");
                        String tableName = parts[0];
                        modelWriter.write("Table Name: " + tableName + "\n");
                        modelWriter.write("Column info: \n");
                        while (getInfo.hasNextLine()) {
                            String data = getInfo.nextLine();
                            if(data.contains("Foreign")){
                                String relation = tableName + " | " + data;
                                cardinality.add(relation);
                            }
                            System.out.println("Column info: " + data);
                            modelWriter.write( "\t"+ data + "\n");
                        }
                        modelWriter.write("\n\n");
                        getInfo.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }

                }
            }
            String writeData = "";
            if(cardinality.size() > 0){
                modelWriter.write("Cardinality: \n");
            }
            for(String card : cardinality){
                String[] parts = card.split(" | ");
                if(card.contains("unique")){
                    writeData = parts[0] + " one-to-one " + parts[parts.length - 1];
                }else{
                    writeData = parts[0] + " many-to-one " + parts[parts.length - 1];
                }
                modelWriter.write(writeData + "\n");
            }
            modelWriter.close();
        }
    }

}
