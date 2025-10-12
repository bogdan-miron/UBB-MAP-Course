package Controller;
import Repository.IRepository;
import model.Participant;

public class Controller {
    private IRepository repo;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public Boolean addParticipant(Participant participant){
        return repo.addParticipant(participant);
    }

    public Boolean removeParticipantById(int id){
        return repo.removeParticipantById(id);
    }

    public void displayAllParticipants(){
        for (Participant p : repo.getParticipants()){
            if (p != null) {
                p.displayInfo();
            }
        }
    }

    public void getParticipantsThatPresented(){
        for(Participant p : repo.getParticipants()){
            if (p != null && p.getHasPresented()){
                p.displayInfo();
            }
        }
    }

    public Participant[] getAllParticipants(){
        return repo.getParticipants();
    }
}
