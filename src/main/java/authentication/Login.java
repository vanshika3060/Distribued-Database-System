package authentication;


import logs.EventLog;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static utils.Constants.*;


public class Login {
    private static String hash(String data) {
        return DigestUtils.md5Hex(data);
    }

    private static boolean checkUserCredentials(String user, String password, ArrayList<String> answers) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(GET_USER_INFORMATION_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(hash(user))) {
                if (userDetails[1].equals(hash(password))) {
                    if (userDetails[2].equals(answers.get(0)) && userDetails[3].equals(answers.get(1))) {
                        return true;

                    }
                }
            }
        }
        return false;
    }

    public static boolean userLogin() throws Exception {
        UserSession s = new UserSession();
        ArrayList<String> answers = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter you userID");
        String user = sc.nextLine();
        user=user.toLowerCase();
        if (user.isEmpty() || user == null) {
            System.out.println("Please enter valid username");
        }
        System.out.println("Please enter your password");
        String password = sc.nextLine();
        if (password.isEmpty() || password == null) {
            System.out.println("Password can't be blank");
        }
        System.out.println("Please answer few security questions");
        System.out.println("Question 1: What city were you born in?");
        String answer1 = sc.nextLine();
        if (answer1.isEmpty() || answer1 == null) {
            System.out.println("The given field can't be blank");
        }
        answers.add(answer1);
        System.out.println("Question 2: What is the name of your pet?");
        String answer2 = sc.nextLine();
        if (answer2.isEmpty() || answer2 == null) {
            System.out.println("The given field can't be blank");
        }
        answers.add(answer2);
        boolean loginResult = checkUserCredentials(user, password, answers);
        if (!loginResult) {
            throw new Exception("One of the entered details is incorrect!");
        }
        s.setUser(user);
        System.out.println("User login successfully!");
        return loginResult;
    }
}
