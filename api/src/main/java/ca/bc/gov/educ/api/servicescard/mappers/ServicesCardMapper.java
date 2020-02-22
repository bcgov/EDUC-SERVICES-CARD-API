package ca.bc.gov.educ.api.servicescard.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;

@Mapper(uses = UUIDMapper.class)
@SuppressWarnings("squid:S1214")
public interface ServicesCardMapper {

  ServicesCardMapper mapper = Mappers.getMapper(ServicesCardMapper.class);

  ServicesCardEntity toModel(ServicesCard servicesCard);

  ServicesCard toStructure(ServicesCardEntity servicesCardEntity);
}
