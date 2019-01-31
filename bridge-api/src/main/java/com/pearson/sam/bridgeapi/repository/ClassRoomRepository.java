package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.Classroom;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends MongoRepository<Classroom, String> {

  public Classroom findByClassroomId(String classroomId);

  public List<Classroom> findAllByName(String name);

  public void deleteByClassroomId(String classroomId);

  public List<Classroom> findAllByClassroomIdIn(List<String> classroomIdList);

  public Classroom findByValidationCode(String validationCode);

  public Classroom findTopByOrderByIdDesc();
  
  public Long countBySchoolId(String schoolId);
  
  List<Classroom> findAllByProductIn(List<String> productIds);
  
  public boolean existsByValidationCode(String validationCode);

}
