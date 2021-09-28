package com.tutorial;

import java.io.*;
import java.time.Year;
import java.util.Arrays;
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
                    addData();
                    showData();
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
                    deleteData();
                    break;
                default:
                    System.err.println("\nYour input not found\nPlease choose [1-5]");
            }

            isContinue = getYesorNo("Do you want to continue");
        }
    }

    private static void deleteData() throws IOException{
        //get original database
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        //make temporary database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        //show data
        System.out.println("Book List");
        showData();

        //get user input to delete data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nEnter book number to delete: ");
        int deleteNum = terminalInput.nextInt();

        //looping to read each data line and skip deleted data
        boolean isFound = false;
        int entryCount = 0;

        String data = bufferedInput.readLine();

        while(data != null){
            entryCount++;
            boolean isDelete = false;

            StringTokenizer st = new StringTokenizer(data,",");

            //show data that will be deleted
            if(deleteNum == entryCount){
                System.out.println("\nData that will be deleted is: ");
                System.out.println("-----------------------------------");
                System.out.println("Reference       : " + st.nextToken());
                System.out.println("Year            : " + st.nextToken());
                System.out.println("Author          : " + st.nextToken());
                System.out.println("Publisher       : " + st.nextToken());
                System.out.println("Book Title      : " + st.nextToken());

                isDelete = getYesorNo("Are you sure to delete this data?");
                isFound = true;
            }

            if(isDelete){
                System.out.println("Data successfully deleted");
            } else {
                //write data to temporary database
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        if(!isFound){
            System.err.println("Book not found");
        }

        //write data to file
        bufferedOutput.flush();
        //delete original file
        bufferedInput.close();
        database.delete();
        //rename temporary database to original
        bufferedOutput.close();
        tempDB.renameTo(database);
    }

    private static void addData() throws IOException{
        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        //take input from user
        Scanner terminalInput = new Scanner(System.in);
        String author, title, publisher, year;

        System.out.print("Enter author name: ");
        author = terminalInput.nextLine();
        System.out.print("Enter book title: ");
        title = terminalInput.nextLine();
        System.out.print("Enter publisher name: ");
        publisher = terminalInput.nextLine();
        System.out.print("Enter release year, format=(YYYY): ");
        year = getYear();

        //check the book in database
        String[] keywords = {year+","+author+","+publisher+","+title};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = checkBook(keywords,false);

        //write book to database
        if(!isExist){
            //name_year_no,year,name,publisher,title
            System.out.println(getEntryEachYear(author, year));
            long entryNumber = getEntryEachYear(author, year) + 1;

            String authorWithoutSpace = author.replaceAll("\\s+","");
            String primaryKey = authorWithoutSpace+"_"+year+"_"+entryNumber;
            System.out.println("\nData you will input is");
            System.out.println("----------------------------------------");
            System.out.println("Primary Key  : " + primaryKey);
            System.out.println("Year         : " + year);
            System.out.println("Author       : " + author);
            System.out.println("Publisher    : " + publisher);
            System.out.println("Book Title   : " + title);

            boolean isAdd = getYesorNo("Do you want to add that data? ");

            if(isAdd){
                bufferOutput.write(primaryKey+","+year+","+author+","+publisher+","+title);
                bufferOutput.newLine();
                bufferOutput.flush();
            }
        } else {
            System.out.println("The book you are going to add is already in database with data below");
            checkBook(keywords,true);
        }

        bufferOutput.close();

    }

    private static long getEntryEachYear(String author, String year) throws IOException{
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner dataScanner;
        String primaryKey;

        while(data != null){
            dataScanner = new Scanner(data);
            dataScanner.useDelimiter(",");
            primaryKey = dataScanner.next();
            dataScanner = new Scanner(primaryKey);
            dataScanner.useDelimiter("_");

            author = author.replaceAll("\\s+","");

            if(author.equalsIgnoreCase(dataScanner.next()) && year.equalsIgnoreCase(dataScanner.next())){
                entry = dataScanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        return entry;
    }

    private static String getYear() throws IOException{
        boolean yearValid = false;
        Scanner terminalInput = new Scanner(System.in);
        String yearInput = terminalInput.nextLine();
        while(!yearValid){
            try{
                Year.parse(yearInput);
                yearValid = true;
            } catch (Exception e){
                System.out.println("Year format you entered was wrong, format=(YYYY");
                System.out.print("Please enter the published year again: ");
                yearValid = false;
                yearInput = terminalInput.nextLine();
            }
        }

        return yearInput;
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
        checkBook(keywords,true);

    }

    private static boolean checkBook(String[] keywords, boolean isDisplay) throws IOException{

        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        if(isDisplay) {
            System.out.println("\n---------------------------------------------------------------------------------------------------");
            System.out.println("| No |\tYear  |\tAuthor                |\tPublisher             |\tBook Title");
            System.out.println("---------------------------------------------------------------------------------------------------");
        }

        String data = bufferInput.readLine();
        boolean isExist = false;
        int dataNo = 0;
        while(data != null){

            //check keywords in line
            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist) {
                if(isDisplay) {
                    dataNo++;

                    StringTokenizer stringToken = new StringTokenizer(data, ",");

                    stringToken.nextToken();
                    System.out.printf("|%2d  ", dataNo);
                    System.out.printf("|\t%4s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s  ", stringToken.nextToken());
                    System.out.printf("|\t%-20s  ", stringToken.nextToken());
                    System.out.printf("|\t%s  ", stringToken.nextToken());
                    System.out.printf("\n");
                } else {
                    break;
                }
            }

            data = bufferInput.readLine();
        }

        if(isDisplay) {
            System.out.println("---------------------------------------------------------------------------------------------------");
        }

        return isExist;

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
            addData();
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

        bufferInput.close();
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
