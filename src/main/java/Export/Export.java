package Export;

import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.DATABASE_DIRECTORY;
import static utils.Constants.DUMPS_DIRECTORY;

public class Export {

		public void getDump(String databaseName) throws IOException {
				String pathString = DATABASE_DIRECTORY+ databaseName;
				Path path = Path.of(pathString);
				if (Files.notExists(path)) {
						System.out.println("Database does not exist");
				} else {
						//Create dumps folder
						File theDir = new File(DUMPS_DIRECTORY + databaseName);
						if (!theDir.exists()) {
								theDir.mkdirs();
						}

						//Get all the tables in a database
						File file = new File(pathString);
						String[] fileList = file.list();

						//Create dumps
						for (String filename : fileList) {
								String[] parts = filename.split("_");
								String tableName = parts[0];
								String dumpFilePath = DUMPS_DIRECTORY+ databaseName + "/" + tableName + ".sql";

								if (filename.contains("meta")) {
										String filePath = pathString + "/" + filename;
										String query = createTableQuery(filename, filePath);
										File dumpFile = new File(dumpFilePath);
										if (!dumpFile.exists()) {
												dumpFile.createNewFile();
										} else {
												PrintWriter writer = new PrintWriter(dumpFilePath);
												writer.print("");
										}
										Utils.writeFile(
												"--\n" + "-- Table structure for table `" + tableName + "`\n" + "--",
												dumpFilePath);
										Utils.writeFile(query, dumpFilePath);

								}
						}

						for (String filename : fileList) {

								if (!filename.contains("meta")) {
										String[] parts = filename.split("\\.");
										String tableName = parts[0];
										String dumpFilePath = DUMPS_DIRECTORY + databaseName + "/" + tableName + ".sql";
										String filePath = pathString + "/" + filename;
										Utils.writeFile(
												"--\n" + "-- Dumping data for table `" + tableName + "`\n" + "--",
												dumpFilePath);
										Utils.writeFile(
												"[SET SQL_MODE = \"NO_AUTO_VALUE_ON_ZERO\";\n" + "START TRANSACTION;\n"
														+ "SET time_zone = \"+00:00\";\n", dumpFilePath);
										String query = insertIntoTableQuery(filename, filePath);
										Utils.writeFile(query, dumpFilePath);
										Utils.writeFile("COMMIT;", dumpFilePath);

								}
						}
						System.out.println("Exported successfully to the directory dumps");

				}
		}

		public String createTableQuery(String fileName, String filePath) throws IOException {
				String[] parts = fileName.split("_");
				String tableName = parts[0];
				String query = "DROP TABLE IF EXISTS `" + tableName + "`;" + "\n\n";

				query += "CREATE TABLE `" + tableName + "` ( \n";
				List<String> metaData = Utils.readFile(filePath);

				for (String columnData : metaData) {
						String[] rowData = columnData.split("\\|");
						String keyInfo = "";
						String referencesInfo = "";
						String nullInfo = "NULL";
						if (columnData.toLowerCase().contains("Primary".toLowerCase())) {
								nullInfo = "NOT NULL";
						}
						if (rowData.length > 2) {
								keyInfo = rowData[2] + " KEY";
								if (rowData.length > 3) {
										referencesInfo = rowData[3];
								}
						}
						query += rowData[0].trim() + " " + rowData[1].trim() + " " + nullInfo.trim() + " " + keyInfo.trim() + " " + referencesInfo.trim();
						if (columnData != metaData.get(metaData.size() - 1)) {
								query += ",";
						}
						query += "\n";
				}
				query += ");";
				return query;
		}

		public static String insertIntoTableQuery(String fileName, String filePath) throws IOException {
				String[] parts = fileName.split("\\.");
				String tableName = parts[0];
				String query = "INSERT INTO `" + tableName + "` VALUES";

				List<String> rowData = Utils.readFile(filePath);

				for (String columnData : rowData) {
						if (columnData != rowData.get(0)) {
								String[] cellData = columnData.split("\\|");
								query += "(";
								for (int i = 0; i < cellData.length; i++) {

										if (isNumeric(cellData[i])) {
												query += cellData[i].trim();
										} else {
												query += "'" + cellData[i].trim() + "'";
										}
										if (i != cellData.length - 1) {
												query += ",";
										}
								}
								query += ")";
								if (columnData != rowData.get(rowData.size() - 1)) {
										query += ",";
								} else {
										query += ";";
								}
						}
				}

				return query;
		}

		//Reference taken from https://stackabuse.com/java-check-if-string-is-a-number/
		public static boolean isNumeric(String string) {
				int intValue;
				Double DoubleValue;
				Float FloatValue;
				Long LongValue;

				if (string == null || string.equals("")) {

						return false;
				}

				try {
						intValue = Integer.parseInt(string);
						return true;
				} catch (NumberFormatException e) {
				}
				try {
						DoubleValue = Double.parseDouble(string);
						return true;
				} catch (NumberFormatException e) {
				}
				try {
						FloatValue = Float.parseFloat(string);
						return true;
				} catch (NumberFormatException e) {
				}
				try {
						LongValue = Long.parseLong(string);
						return true;
				} catch (NumberFormatException e) {
				}
				return false;
		}
}