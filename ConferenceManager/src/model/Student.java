package model;

public class Student extends Participant {
    public Student(int id, String name, String title, Boolean hasPresented){
        super(id, name, title, hasPresented);
    }

    @Override
    public void displayInfo(){
        System.out.println("=======================");
        System.out.println("Student ID: " + id + "\nName: " + name + "\nOccupation: " + title + "\nHas Presented Paper: " + hasPresented);
    }
}
