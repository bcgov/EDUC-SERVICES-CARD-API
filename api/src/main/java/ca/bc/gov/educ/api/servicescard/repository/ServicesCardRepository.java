package ca.bc.gov.educ.api.servicescard.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;

public interface ServicesCardRepository extends CrudRepository<ServicesCardEntity, UUID> {
	Optional<ServicesCardEntity> findByDid(String did);
}
