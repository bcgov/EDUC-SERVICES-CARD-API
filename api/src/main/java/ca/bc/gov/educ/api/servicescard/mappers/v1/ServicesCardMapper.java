package ca.bc.gov.educ.api.servicescard.mappers.v1;

import ca.bc.gov.educ.api.servicescard.mappers.LocalDateTimeMapper;
import ca.bc.gov.educ.api.servicescard.mappers.UUIDMapper;
import ca.bc.gov.educ.api.servicescard.model.v1.ServicesCardEntity;
import ca.bc.gov.educ.api.servicescard.struct.v1.ServicesCard;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
@SuppressWarnings("squid:S1214")
public interface ServicesCardMapper {

  ServicesCardMapper mapper = Mappers.getMapper(ServicesCardMapper.class);

  ServicesCardEntity toModel(ServicesCard servicesCard);

  ServicesCard toStructure(ServicesCardEntity servicesCardEntity);
}
