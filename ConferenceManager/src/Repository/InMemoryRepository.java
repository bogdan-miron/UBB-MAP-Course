package Repository;

import model.Participant;

public class InMemoryRepository implements IRepository {

    private Participant[] participants = new Participant[100];
    private int size = 0;

    @Override
    public Boolean addParticipant(Participant participant) {
        for(Participant p : participants){
            if(p != null && p.getId() == participant.getId()){
                return false;
            }
        }

        participants[size] = participant;
        size++;
        return true;
    }

    @Override
    public Boolean removeParticipantById(int id) {
        
        int indexToRemove = -1;
        for (int i = 0; i < size; i++) {
            if (participants[i] != null &&
                    participants[i].getId() == id) {
                indexToRemove = i;  // Save the INDEX, not the ID!
                break;
            }
        }

        
        if (indexToRemove == -1) {
            return false;
        }

        
        for (int i = indexToRemove; i < size - 1; i++) {
            participants[i] = participants[i + 1];
        }

        
        participants[size - 1] = null;
        size--;

        return true;
    }

    @Override
    public Participant[] getParticipants() {
        return participants;
    }
}
