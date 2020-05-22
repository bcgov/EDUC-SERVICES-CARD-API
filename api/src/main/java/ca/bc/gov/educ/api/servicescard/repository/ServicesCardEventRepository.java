package ca.bc.gov.educ.api.servicescard.repository;

import ca.bc.gov.educ.api.servicescard.model.ServicesCardEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicesCardEventRepository extends CrudRepository<ServicesCardEvent, UUID> {
  Optional<ServicesCardEvent> findBySagaIdAndEventType(UUID sagaId, String eventType);

  List<ServicesCardEvent> findByEventStatus(String toString);

  List<ServicesCardEvent> findAll();
}
