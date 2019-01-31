package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.School;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends MongoRepository<School, String> {

  public School findBySchoolId(String schoolId);

  public void deleteBySchoolId(String schoolId);

  public List<School> findAllBySchoolIdIn(List<String> schoolIdList);

  public Page<School> findAllByNameLikeAndSchoolIdIn(String schoolName, List<String> schoolIdList,
      Pageable pageable);

  public School findBySchoolCode(String schoolCode);

  public School findByTeacherCode(String teacherCode);

  public School findByStudentCode(String studentCode);
  
  public boolean existsBySchoolId(String schoolId);

}
