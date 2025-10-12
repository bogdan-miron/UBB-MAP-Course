package UI;
import Controller.Controller;
import model.Participant;
import model.Researcher;
import model.Student;
import model.Teacher;

import java.util.Scanner;

public class UI {
    private Controller controller;
    private Scanner scanner = new Scanner(System.in);

    public UI(Controller controller) {
        this.controller = controller;
    }

    private void displayMenu(){
        System.out.println("=======================");
        System.out.println("1. Add participant");
        System.out.println("2. Remove participant");
        System.out.println("3. View all participants");
        System.out.println("4. View participants who presented.");
        System.out.println("5. Exit application");
    }

    public void run(){
        while(true){
            displayMenu();
            int choice;
            System.out.println("Enter choice: ");
            try{
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1){
                    addFromUI();
                }
                else if (choice == 2){
                    removeFromUI();
                }
                else if (choice == 3){
                    displayAll();
                }
                else if (choice == 4){
                    displayPresented();
                }
                else if (choice == 5){
                    return;
                }
            }
            catch(Exception e){
                System.out.println("Please enter a valid choice :(");
                System.out.println(e.getMessage());
            }

        }
    }

    private void displayPresented() {
        controller.getParticipantsThatPresented();
    }

    private void displayAll() {
        controller.displayAllParticipants();
    }

    private void removeFromUI() {
        int id;

        try{
            System.out.println("Please enter the id of the participant you would like to remove:");
            id = Integer.parseInt(scanner.nextLine());
            controller.removeParticipantById(id);
        }
        catch(Exception e){
            System.out.println("Please enter a valid choice, something went wrong.");
        }
    }

    private void addFromUI() {
        int id;
        String name;
        String title;
        Boolean hasPresented;
        String typeOfEntity;

        try {
            System.out.println("Student/Teacher/Researcher: ");
            typeOfEntity = scanner.nextLine();
            System.out.println("Enter participant ID: ");
            id = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter participant name: ");
            name = scanner.nextLine();
            System.out.println("Enter participant title: ");
            title = scanner.nextLine();
            System.out.print("Has presented? (yes/no): ");
            String presentedInput = scanner.nextLine().toLowerCase().trim();
            hasPresented = presentedInput.equals("yes") || presentedInput.equals("y");

            if (typeOfEntity.equals("Student")){
                Participant tmp = new Student(id, name, title, hasPresented);
                controller.addParticipant(tmp);
            }
            else if (typeOfEntity.equals("Teacher")){
                Participant tmp = new Teacher(id, name, title, hasPresented);
                controller.addParticipant(tmp);
            }
            else if (typeOfEntity.equals("Researcher")){
                Participant tmp = new Researcher(id, name, title, hasPresented);
                controller.addParticipant(tmp);
            }
            else {
                throw new Exception("Type of participant not recognized.");
            }
        }
        catch (Exception e){
            System.out.println("Please enter a valid choice, something went wrong.");
            System.out.println(e.getMessage());
        }
    }
}
