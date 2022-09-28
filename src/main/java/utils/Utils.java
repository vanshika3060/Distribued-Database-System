package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static utils.Constants.GET_USER_INFORMATION_FILE;
import static utils.RemoteSession.putFile;

public class Utils {


//sftpClient.put(localFile, remoteDir + "sshjFile.txt");
//
//sftpClient.close();
//sshClient.disconnect();
	public static void writeFile(String data, String fileName) throws IOException {
		try {
			File f = new File(fileName);
			System.out.println(fileName);
			FileWriter file;
			if(f.exists()){
				file = new FileWriter(fileName, true);

			}else{
				file = new FileWriter(fileName);
			}
			file.write(data);
			file.append("\n");
			file.flush();
			file.close();


		} catch (Exception e) {
			e.printStackTrace();

		}



	}

	public static List<String> readFile(String fileName)  {
		List<String> lines = new ArrayList<>();
		try {
			File file = new File(fileName);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;

	}


}
