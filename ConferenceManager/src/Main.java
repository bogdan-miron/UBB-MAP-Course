import Repository.InMemoryRepository;
import UI.UI;
import Repository.*;
import Controller.*;

public class Main {
    public static void main(String[] args) {

        IRepository repo = new InMemoryRepository();
        Controller controller = new Controller(repo);
        UI ui = new UI(controller);
        ui.run();
    }
}