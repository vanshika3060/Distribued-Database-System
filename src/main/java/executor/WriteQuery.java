package executor;

import parser.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static utils.Constants.DATABASE_DIRECTORY;

public class WriteQuery {
    static String database = "";

    static boolean transactionBegin = false;
    static List<String> transactionList = new ArrayList<>();

    public static void tableExist(String lcQuery, String tableName){
        File databaseInfo = new File(DATABASE_DIRECTORY + database);
        String[] fileList = databaseInfo.list();
        if(Arrays.asList(fileList).contains(tableName));
            transactionList.add(lcQuery);
    }

    public static void sqlQueries() throws Exception {

        System.out.println("Please enter the query you want to execute");
        Scanner sc = new Scanner(System.in);
        String query = sc.nextLine();
        String lcQuery = query.toLowerCase();
        boolean validFlag = true;

        if(lcQuery.contains("start transaction")){
            transactionBegin = true;
        }
        String[] arrOfStr;
        if(transactionBegin){
            if(lcQuery.contains("select")){
                arrOfStr = lcQuery.split("from");
                tableExist(lcQuery, arrOfStr[1]);
                validFlag = true;
            }else if(lcQuery.contains("update")){
                validFlag = ValidatorTransaction.updateValidator(lcQuery, database);
                if(validFlag){
                    transactionList.add(lcQuery);
                }
            }else if(lcQuery.contains("delete")){
                arrOfStr = lcQuery.split("from");
                tableExist(lcQuery, arrOfStr[1]);
                validFlag = true;
            }else if(lcQuery.contains("insert")){
                arrOfStr = lcQuery.split("into");
                tableExist(lcQuery, arrOfStr[1]);
                validFlag = ValidatorTransaction.insertValidator(lcQuery, database);
            }
        }

        if(lcQuery.contains("commit") || lcQuery.contains("rollback") || !validFlag){
            transactionBegin = false;
            if(lcQuery.contains("rollback") || !validFlag){
                transactionList = new ArrayList<>();
            }
        }

        if(!transactionBegin) {
            if(transactionList.isEmpty() && validFlag)
                sqlQuery(lcQuery, query);
            else{
                for(String q : transactionList){
                    sqlQuery(q, q);
                }
                transactionList = new ArrayList<>();
            }
        }



    }

    public static void sqlQuery(String lcQuery, String query) throws Exception {


        boolean result;
        QueryExecutor qe = new QueryExecutor();
        if (lcQuery.contains("create database")) {
            result = QueryParser.parser(query);
            if (result) {
                qe.ExecuteCreateDatabase(query);
            } else {
                System.out.println("Invalid query for creating database!");
            }

        } else if (lcQuery.split(" ")[0].contains("use")) {
            result = QueryParser.parser(query);
            if (result) {
                database = qe.ExecuteUseDatabase(query);
            } else {
                System.out.println("Invalid query for using database!");
            }


        } else if (lcQuery.contains("create table")) {
            result = QueryParser.parser(query);
            if (database == null) {
                System.out.println("Please select the database");
            }
            if (result) {
                qe.ExecuteCreateTable(database, lcQuery);
            } else {
                System.out.println("Invalid query for creating table");
            }

        } else if (lcQuery.contains("insert into")) {
            result = QueryParser.parser(query);
            if (database == null) {
                System.out.println("Please select the database");
            }
            if (result) {
                qe.ExecuteInsertStatement(database, lcQuery);
            } else {
                System.out.println("Invalid query for inserting data");
            }
        } else if (lcQuery.contains("select")) {
            result = QueryParser.parser(query);
            if (result) {
                qe.ExecuteSelectStatement(database, lcQuery);
            } else {
                System.out.println("Invalid query for selecting data");
            }
            if (database == null) {
                System.out.println("please select the database");
            }
        } else if (lcQuery.contains("update")) {
            result = QueryParser.parser(query);
            if (result) {
                qe.ExecuteUpdateStatement(database, lcQuery);
            } else {
                System.out.println("Invalid query for updating data");
            }
            if (database == null) {
                System.out.println("please select the database");
            }
        } else if (lcQuery.contains("delete")) {
            result = QueryParser.parser(query);
            if (result) {
                qe.ExecuteDeleteStatement(database, lcQuery);
            } else {
                System.out.println("Invalid query for delete data");
            }
            if (database == null) {
                System.out.println("please select the database");
            }
        } else {
            System.out.println("Invalid query");
        }

    }
}
