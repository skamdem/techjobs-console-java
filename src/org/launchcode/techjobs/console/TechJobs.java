package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVRecord;

import java.util.*;

/**
 * Created by LaunchCode
 */
public class TechJobs {
    private static Scanner in = new Scanner(System.in);
    public static void main (String[] args) {
        // Initialize our field map with key/name pairs
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        // Top-level menu options
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        // Allow the user to search until they manually quit
        while (true) {
            String actionChoice = getUserSelection("View jobs by:", actionChoices);
            if (actionChoice.equals("list")) {
                String columnChoice = getUserSelection("List", columnChoices);
                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());//return allJobs in the database
                } else {
                    //list of all values from loaded data without duplicates for a given column
                    ArrayList<String> results = JobData.findAll(columnChoice);
                    Collections.sort(results);//Bonus Missions 1. Sorting list results
                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");
                    // Print list of skills, employers, etc
                    for (String item : results) {
                        System.out.println(item);
                    }
                }
            } else { // choice is "search"
                // How does the user want to search (e.g. by skill or employer)
                String searchField = getUserSelection("Search by:", columnChoices);
                // What is their search term?
                System.out.println("\nSearch term: ");
                String searchTerm = in.nextLine();
                if (searchField.equals("all")) {
                    //Search "ALL" the database for "searchTerm"
                    printJobs(JobData.findByValue(searchTerm));
                } else {//Search Only the column "searchField" in the database for "searchTerm"
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }
            }
        }
    }

    // Prints a sub-menu and ﻿Returns the key of the selected item from the choices Dictionary
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {
        Integer choiceIdx;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        // Put the choices in an ordered structure so we can
        // associate an integer with each one
        Integer i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }
        do {
            System.out.println("\n" + menuHeader);
            // Print available choices
            for (Integer j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }
            choiceIdx = in.nextInt();
            in.nextLine();
            // Validate user's input
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }
        } while(!validChoice);
        return choiceKeys[choiceIdx];
    }

    // Print a list of jobs
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        //Bonus Missions 1. Sorting list results
        String comparisonKey = "position type";
        Collections.sort(someJobs, new Comparator<HashMap< String,String >>() {
            @Override
            public int compare(HashMap<String, String> lhs,
                               HashMap<String, String> rhs) {
                // Do your comparison logic here and return accordingly.
                return lhs.get(comparisonKey).compareTo(rhs.get(comparisonKey));
            }
        });

        if (someJobs.isEmpty()){
            System.out.println("No matching jobs were found.");
        } else{
            /*Integer numberOfColumns = someJobs.get(0).size();
            String[] headers = someJobs.get(0).keySet().toArray(new String[numberOfColumns]);*/
            System.out.println(someJobs.size() + " jobs founds");
            for (HashMap<String,String> entry : someJobs){
                System.out.println("*****");
                for (Map.Entry<String,String> subEntry : entry.entrySet()) {
                    System.out.println(subEntry.getKey() + ": " + subEntry.getValue());
                }
                System.out.println("*****");
                System.out.println();
            }
        }
    }
}
