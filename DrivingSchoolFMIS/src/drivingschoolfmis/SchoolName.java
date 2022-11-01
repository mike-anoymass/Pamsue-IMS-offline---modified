package drivingschoolfmis;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SchoolName {
    String nameOfSchool;

    SchoolName(){
        schoolNameFile();
    }

    private void schoolNameFile() {
        boolean fileCreated = createSchoolNameFile();
        if(!fileCreated){
            File fileObj = new File("schoolName.txt");
            nameOfSchool ="";
            try {
                Scanner reader = new Scanner(fileObj);
                while(reader.hasNextLine()){
                    nameOfSchool = reader.nextLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            

            if(nameOfSchool.length() == 0){
                try{
                    String sName = JOptionPane.showInputDialog(null ,
                            "Enter a brief name of your Driving School",
                            "Driving School Name",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    boolean allLetters = sName.chars().allMatch(Character::isLetter);

                    if(!allLetters){
                        printError("Only letters allowed");
                    }else{
                        nameOfSchool = sName;
                        setSchoolName(sName);
                        JOptionPane.showMessageDialog(null, "Ok Done!");
                    }
                }catch(StringIndexOutOfBoundsException e){
                    printError("Name should not be empty");
                }catch(NullPointerException e){
                    System.exit(0);
                }

            }
        }
    }

    private void printError(String e) {
        JOptionPane.showMessageDialog(
                null,
                "Invalid Name: " + e,
                "Ops! ",
                JOptionPane.ERROR_MESSAGE
        );
        schoolNameFile();
    }

    private void setSchoolName(String sName) {
        try {
            FileWriter fileWriter = new FileWriter("schoolName.txt");
            fileWriter.write(capitalize(sName));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean createSchoolNameFile() {
        File fileObj = new File("schoolName.txt");
        try {
            if (fileObj.createNewFile()) {
                System.out.println("file created");
                return true;
            } else {
                System.out.println("exists");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    String getNameOfSchool(){
        return capitalize(nameOfSchool);
    }

    String capitalize(String word){
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }

}
