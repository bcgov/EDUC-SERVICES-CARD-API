package ca.bc.gov.educ.api.student.mappers;

import ca.bc.gov.educ.api.student.model.StudentEntity;
import ca.bc.gov.educ.api.student.struct.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UUIDMapper.class)
public interface StudentMapper {

  StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

  StudentEntity toModel(Student student);

  Student toStructure(StudentEntity studentEntity);
}
