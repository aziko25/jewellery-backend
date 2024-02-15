package jewellery.jewellery.Repositories;

import jewellery.jewellery.Models.ParticipantsMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsMessagesRepository extends JpaRepository<ParticipantsMessages, Long> {
}