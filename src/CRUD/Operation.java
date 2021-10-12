package CRUD;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import static CRUD.Utility.*;

public class Operation {

    public static void showData() throws IOException {

        FileReader fileInput;
        BufferedReader bufferInput = null;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e) {
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
        while (data != null) {
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

    public static void findData() throws IOException {
        //read database to check exist or not
        try {
            File file = new File("database.txt");
        } catch (Exception e) {
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
        checkBook(keywords, true);

    }

    public static void addData() throws IOException {
        FileWriter fileOutput = new FileWriter("database.txt", true);
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
        year = Utility.getYear();

        //check the book in database
        String[] keywords = {year + "," + author + "," + publisher + "," + title};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = checkBook(keywords, false);

        //write book to database
        if (!isExist) {
            //name_year_no,year,name,publisher,title
            System.out.println(getEntryEachYear(author, year));
            long entryNumber = getEntryEachYear(author, year) + 1;

            String authorWithoutSpace = author.replaceAll("\\s+", "");
            String primaryKey = authorWithoutSpace + "_" + year + "_" + entryNumber;
            System.out.println("\nData you will input is");
            System.out.println("----------------------------------------");
            System.out.println("Primary Key  : " + primaryKey);
            System.out.println("Year         : " + year);
            System.out.println("Author       : " + author);
            System.out.println("Publisher    : " + publisher);
            System.out.println("Book Title   : " + title);

            boolean isAdd = getYesorNo("Do you want to add that data? ");

            if (isAdd) {
                bufferOutput.write(primaryKey + "," + year + "," + author + "," + publisher + "," + title);
                bufferOutput.newLine();
                bufferOutput.flush();
            }
        } else {
            System.out.println("The book you are going to add is already in database with data below");
            checkBook(keywords, true);
        }

        bufferOutput.close();

    }

    public static void updateData() throws IOException {
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

        //get user input to update data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nEnter book number to update: ");
        int updateNum = terminalInput.nextInt();

        //read data
        String data = bufferedInput.readLine();
        int entryCount = 0;

        while (data != null) {
            entryCount++;

            StringTokenizer st = new StringTokenizer(data, ",");

            //show data that will be updated
            if (updateNum == entryCount) {
                System.out.println("\nData that will be updated is: ");
                System.out.println("-----------------------------------");
                System.out.println("Reference       : " + st.nextToken());
                System.out.println("Year            : " + st.nextToken());
                System.out.println("Author          : " + st.nextToken());
                System.out.println("Publisher       : " + st.nextToken());
                System.out.println("Book Title      : " + st.nextToken());

                //update data
                //get input from user
                String[] fieldData = {"year", "author", "publisher", "book title"};
                String[] tempData = new String[4];

                st = new StringTokenizer(data, ",");
                String originalData = st.nextToken();

                for (int i = 0; i < fieldData.length; i++) {
                    boolean isUpdate = getYesorNo("Do you want to update " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate) {
                        //user input
                        if (fieldData[i].equalsIgnoreCase("year")) {
                            System.out.print("Enter published year, format=(YYYY): ");
                            tempData[i] = getYear();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nEnter new " + fieldData[i] + ": ");
                            tempData[i] = terminalInput.nextLine();
                        }

                    } else {
                        tempData[i] = originalData;
                    }
                }

                //show new data to screen
                st = new StringTokenizer(data, ",");
                st.nextToken();
                System.out.println("\nYour new data is ");
                System.out.println("---------------------------------------");
                System.out.println("Year               : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("Author             : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("Publisher          : " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("Book Title         : " + st.nextToken() + " --> " + tempData[3]);


                boolean isUpdate = getYesorNo("Are you sure to update the data");

                if (isUpdate) {

                    //check the data in database
                    boolean isExist = checkBook(tempData, false);

                    if (isExist) {
                        System.err.println("Book data is already in database, update process cancelled, \nplease delete the data first");
                        //copy data
                        bufferedOutput.write(data);

                    } else {
                        //format new data to database
                        String year = tempData[0];
                        String author = tempData[1];
                        String publisher = tempData[2];
                        String title = tempData[3];

                        //create the primary key
                        long nomorEntry = getEntryEachYear(author, year) + 1;

                        String authorWithoutSpace = author.replaceAll("\\s+", "");
                        String primaryKey = authorWithoutSpace + "_" + year + "_" + nomorEntry;

                        //write data into database
                        bufferedOutput.write(primaryKey + "," + year + "," + author + "," + publisher + "," + title);
                    }
                } else {
                    //copy data
                    bufferedOutput.write(data);
                }
            } else {
                //copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
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

    public static void deleteData() throws IOException {
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

        while (data != null) {
            entryCount++;
            boolean isDelete = false;

            StringTokenizer st = new StringTokenizer(data, ",");

            //show data that will be deleted
            if (deleteNum == entryCount) {
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

            if (isDelete) {
                System.out.println("Data successfully deleted");
            } else {
                //write data to temporary database
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }

        if (!isFound) {
            System.err.println("Book not found");
        }

        //write data into file
        bufferedOutput.flush();
        //delete original file
        bufferedInput.close();
        database.delete();
        //rename temporary database to original
        bufferedOutput.close();
        tempDB.renameTo(database);
    }

}
