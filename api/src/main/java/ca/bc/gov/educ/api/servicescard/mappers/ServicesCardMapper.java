package ca.bc.gov.educ.api.servicescard.mappers;

import ca.bc.gov.educ.api.servicescard.model.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.struct.ServicesCard;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
@SuppressWarnings("squid:S1214")
public interface ServicesCardMapper {

  ServicesCardMapper mapper = Mappers.getMapper(ServicesCardMapper.class);

  ServicesCardEntity toModel(ServicesCard servicesCard);

  ServicesCard toStructure(ServicesCardEntity servicesCardEntity);
}
