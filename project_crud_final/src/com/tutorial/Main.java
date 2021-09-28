package com.tutorial;

import java.io.*;
import java.time.Year;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import static CRUD.Operation.*;
import static CRUD.Utility.*;

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
                    updateData();
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




}
