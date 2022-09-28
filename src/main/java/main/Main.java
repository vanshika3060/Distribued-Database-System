package main;

import Export.Export;
import analysis.Analysis;
import authentication.Login;
import authentication.Register;
import executor.WriteQuery;
import model.ReverseEngineering;


import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws Exception {

        boolean flag = true;

            while (flag) {
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("                          Database Management System                   ");
                System.out.println("-----------------------------------------------------------------------");
                System.out.println("1. Registeration");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        Register r = new Register();
                        r.userRegisteration();
                        break;
                    case 2:
                        Login l = new Login();
                        boolean result = l.userLogin();
                        if (result) {
                            boolean check = true;
                            while (check) {
                                System.out.println("--------------------MAIN MENU---------------------");
                                System.out.println("1. Write Queries");
                                System.out.println("2. Export");
                                System.out.println("3. Data Model");
                                System.out.println("4. Analytics");
                                System.out.println("5. Logout");
                                Scanner input = new Scanner(System.in);

                                int choices = input.nextInt();
                                switch (choices) {
                                    case 1:
                                        WriteQuery executeQuery = new WriteQuery();
                                        executeQuery.sqlQueries();
                                        break;

                                    case 3:
                                        ReverseEngineering reverseEngineering = new ReverseEngineering();
                                        reverseEngineering.selectDatabase();
                                        break;

                                    case 2:
                                        System.out.println(
                                                "Please enter the database name that you want to export:");
                                        String db = input.next();
                                        Export export = new Export();
                                        export.getDump(db);
                                        break;
                                    case 4:
                                        String response;
                                        String active_userName = "shivam";
                                        Analysis analysis = new Analysis();
                                        System.out.println("-----------------------------------------------------------------------");
                                        System.out.println("Enter your command for Analysis");
                                        System.out.println("-----------------------------------------------------------------------");
                                        Scanner scanner = new Scanner(System.in);
                                        response = scanner.nextLine();
                                        switch(response)
                                        {
                                            case "queries":
                                                analysis.chooseinstance();
                                            case "create":
                                                analysis.QueryAnalysis(response);
                                            case "update":
                                                analysis.QueryAnalysis(response);
                                        }

                                    case 5:
                                        check = false;
                                        break;

                                    default:
                                        System.out.println("Invalid Choice!");
                                }
                            }

                        }
                        break;
                    case 3:
                        flag=false;
                        break;
                    default:
                        System.out.println("Invalid Choice!");
                }

            }

        }
    }


