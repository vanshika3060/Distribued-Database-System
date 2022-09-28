package parser;

import logs.QueryLog;

import java.util.regex.Pattern;

public class QueryParser {

    public static boolean parser(String query) {
        boolean isSyntaxCorrect=false;
        String lowercaseQuery = query.toLowerCase();
        if (query == null || query.isEmpty()) {
            System.out.println("The query syntax is invalid.");
        } else if (lowercaseQuery.contains("create database")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(CREATE\\sDATABASE\\s[a-zA-Z\\d]+;)$", lowercaseQuery);
        } else if (lowercaseQuery.split(" ")[0].contains("use")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(USE\\s[a-zA-Z\\d]+;)$", lowercaseQuery);
        } else if (lowercaseQuery.contains("create table")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(CREATE\\sTABLE\\s[a-zA-Z\\d]+\\s\\(([a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?(,\\s[a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?)*)\\);)$", lowercaseQuery);
        } else if (lowercaseQuery.contains("insert into")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(INSERT INTO (\\S+).*\\s+\\((.*?)\\).*\\s+VALUES.*\\s+\\((.*?)\\).*\\;)$", lowercaseQuery);
        } else if (lowercaseQuery.contains("select")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(SELECT\\s+(.+?)\\s*\\s+FROM\\s+(.*?)\\s*(WHERE\\s+(.*?)\\s*)?;)$", lowercaseQuery);
        } else if (lowercaseQuery.contains("update")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(UPDATE\\s+(\\S+)\\s*SET\\s+(.*?)\\s*(WHERE\\s+(.*?))?;)$", lowercaseQuery);
        } else if (lowercaseQuery.contains("delete")) {
            isSyntaxCorrect = Pattern.matches("^(?i)(DELETE FROM\\s+(\\S+)\\s*(WHERE\\s(.*?)\\s*)?;)$", lowercaseQuery);
        }else{
            System.out.println("No such query parse exists.");
        }
            return isSyntaxCorrect;
        }
    }
