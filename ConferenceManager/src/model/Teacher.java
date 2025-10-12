package model;

public class Teacher extends Participant {
    public Teacher(int id, String name, String title, Boolean hasPresented){
        super(id, name, title, hasPresented);
    }

    @Override
    public void displayInfo() {
        System.out.println("=======================");
        System.out.println("Teacher ID: " + id + "\nName: " + name + "\nOccupation: " + title + "\nHas Presented Paper: " + hasPresented);
    }
}
