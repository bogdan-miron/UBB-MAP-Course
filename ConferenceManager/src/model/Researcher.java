package model;

public class Researcher extends Participant {
    public Researcher(int id, String name, String title, Boolean hasPresented) {
        super(id, name, title, hasPresented);
    }

    @Override
    public void displayInfo() {
        System.out.println("=======================");
        System.out.println("Researcher ID: " + id + "\nName: " + name + "\nOccupation: " + title + "\nHas Presented Paper: " + hasPresented);
    }
}
