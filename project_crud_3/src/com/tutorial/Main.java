package com.tutorial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner terminalInput = new Scanner(System.in);
        String userChoice;
        boolean isContinue = true;

        while(isContinue) {
            clearScreen();
            System.out.println("Library Database\n");
            System.out.println("1.\tList of all books");
            System.out.println("2.\tSearch book data");
            System.out.println("3.\tAdd book data");
            System.out.println("4.\tChange book data");
            System.out.println("5.\tDelete book data");

            System.out.print("\n\nYour choice: ");
            userChoice = terminalInput.next();

            switch (userChoice) {
                case "1":
                    System.out.println("\n=================");
                    System.out.println("LIST OF ALL BOOKS");
                    System.out.println("=================");
                    showData();
                    break;
                case "2":
                    System.out.println("\n================");
                    System.out.println("SEARCH BOOK DATA");
                    System.out.println("================");
                    findData();
                    break;
                case "3":
                    System.out.println("\n=============");
                    System.out.println("ADD BOOK DATA");
                    System.out.println("=============");
                    // tambah data
                    break;
                case "4":
                    System.out.println("\n================");
                    System.out.println("CHANGE BOOK DATA");
                    System.out.println("================");
                    break;
                case "5":
                    System.out.println("\n===============");
                    System.out.println("CLEAN BOOK DATA");
                    System.out.println("===============");
                    break;
                default:
                    System.err.println("\nYour input not found\nPlease choose [1-5]");
            }

            isContinue = getYesorNo("Do you want to continue");
        }
    }

    private static void findData() throws IOException{
        //read database to check exist or not
        try{
            File file = new File("database.txt");
        } catch (Exception e){
            System.err.println("Database not found");
            System.err.println("Please add the data first");
            return;
        }

        //take keyword from user
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Enter keyword to find the book: ");
        String findString = terminalInput.nextLine();
        String[] keywords = findString.split("\\s+");

        //check keyword on database
        checkBook(keywords);

    }

    private static void checkBook(String[] keywords) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.println("| No |\tYear  |\tAuthor                |\tPublisher             |\tBook Title");
        System.out.println("---------------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        boolean isExist;
        int dataNo = 0;
        while(data != null){

            //check keywords in line
            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist) {
                dataNo++;

                StringTokenizer stringToken = new StringTokenizer(data, ",");

                stringToken.nextToken();
                System.out.printf("|%2d  ", dataNo);
                System.out.printf("|\t%4s  ", stringToken.nextToken());
                System.out.printf("|\t%-20s  ", stringToken.nextToken());
                System.out.printf("|\t%-20s  ", stringToken.nextToken());
                System.out.printf("|\t%s  ", stringToken.nextToken());
                System.out.printf("\n");
            }

            data = bufferInput.readLine();
        }

        System.out.println("---------------------------------------------------------------------------------------------------");


    }

    private static void showData() throws IOException{

        FileReader fileInput;
        BufferedReader bufferInput = null;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e){
            System.err.println("Database not found");
            System.err.println("Please add data first");
            return;
        }

        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.println("| No |\tYear  |\tAuthor                |\tPublisher             |\tBook Title");
        System.out.println("---------------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int dataNo = 0;
        while(data != null){
            dataNo++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            stringToken.nextToken();
            System.out.printf("|%2d  ", dataNo);
            System.out.printf("|\t%4s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s  ", stringToken.nextToken());
            System.out.printf("|\t%s  ", stringToken.nextToken());
            System.out.printf("\n");

            data = bufferInput.readLine();
        }

        System.out.println("---------------------------------------------------------------------------------------------------");

    }

    private static boolean getYesorNo(String message){
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n)? ");
        String userChoice = terminalInput.next();

        while(!userChoice.equalsIgnoreCase("y") && !userChoice.equalsIgnoreCase("n")) {
            System.err.println("Your choice is not y nor n");
            System.out.print("\n"+message+" (y/n)? ");
            userChoice = terminalInput.next();
        }

        return userChoice.equalsIgnoreCase("y");

    }

    private static void clearScreen() {
        try {
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception e) {
            System.err.println("cannot clear the screen");
        }
    }
}
