package jewellery.jewellery.Repositories;

import jewellery.jewellery.Models.Participants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsRepository extends JpaRepository<Participants, Long> {
}