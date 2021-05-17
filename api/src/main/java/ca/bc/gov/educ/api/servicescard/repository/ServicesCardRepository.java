package ca.bc.gov.educ.api.servicescard.repository;

import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServicesCardRepository extends CrudRepository<ServicesCardEntity, UUID> {
	Optional<ServicesCardEntity> findByDid(String did);
}
