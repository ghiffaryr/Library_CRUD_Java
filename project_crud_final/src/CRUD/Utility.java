package CRUD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {

    static long getEntryEachYear(String author, String year) throws IOException {
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

        bufferInput.close();

        return entry;
    }

    static boolean checkBook(String[] keywords, boolean isDisplay) throws IOException{

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

        bufferInput.close();

        return isExist;

    }

    protected static String getYear() throws IOException{
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

    public static boolean getYesorNo(String message){
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

    public static void clearScreen() {
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
