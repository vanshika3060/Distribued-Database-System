package authentication;

import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.RemoteSession.putFile;


public class Register {

    private static String hash(String data) {
        return DigestUtils.md5Hex(data);
    }

    private static boolean ifUserExists(String username) throws IOException {
        String userhash = hash(username);
        List<String> lines = Utils.readFile(Constants.GET_USER_INFORMATION_FILE);

        for (String l : lines) {
            if (l.contains(userhash)) {
                return true;
            }

        }


        return false;
    }

    private static String buildInput(String name, String pass, ArrayList answers) {
        StringBuilder str = new StringBuilder();
        str.append(name);
        str.append(",");
        str.append(pass);
        str.append(",");
        for (Object a : answers) {
            str.append(a.toString());
            str.append(",");
        }
        return str.toString();
    }


    private static boolean isValidUsername(String name) {
        String regex = "^[A-Za-z]\\w{4,10}$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {
            return false;
        }
        Matcher m = p.matcher(name);
        return m.matches();
    }

    private static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private static boolean isValidSecurityAnswer(String answer) {
        String regex = "^[a-zA-Z0-9_.-]\\w{3,10}$$";
        Pattern p = Pattern.compile(regex);
        if (answer == null) {
            return false;
        }
        Matcher m = p.matcher(answer);
        return m.matches();
    }


    public void userRegisteration() throws IOException {
        String username;
        String password;
        String security_answer1;
        String security_answer2;
        ArrayList<String> security_answers = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter username between 5 to 10 characters.");
        username = sc.nextLine();
        username = username.toLowerCase();
        if (ifUserExists(username)) {
            System.out.println("Username already exists.");
            return;
        }
        if (!isValidUsername(username)) {
            return;
        }

        System.out.println("Password should be Contain atleast: \n Minimum 8 Character and Maximum 20 Character \n 1 Special Character \n 1 Capital Alphabet \n 1 Small Alphabet");
        System.out.println("Enter Password: ");
        password = sc.nextLine();
        if (!isValidPassword(password)) {
            return;
        }
        System.out.println("Minimum 4 letters required.");
        System.out.println("Question 1: What city were you born in?");
        security_answer1 = sc.nextLine();
        if (!isValidSecurityAnswer(security_answer1)) {
            return;
        }
        System.out.println("Question 2: What is the name of your pet?");
        security_answer2 = sc.nextLine();
        if (!isValidSecurityAnswer(security_answer2)) {
            return;
        }
        security_answers.add(security_answer1);
        security_answers.add(security_answer2);
        username = username.toLowerCase();
        username = hash(username);
        password = hash(password);

        String input = buildInput(username, password, security_answers);
        Utils.writeFile(input.substring(0, input.length() - 1), Constants.GET_USER_INFORMATION_FILE);
        putFile(Constants.GET_USER_INFORMATION_FILE);

    }


}
