package Repository;
import model.Participant;

public interface IRepository {

    Boolean addParticipant(Participant participant);
    Boolean removeParticipantById(int id);

    Participant[] getParticipants();
}
