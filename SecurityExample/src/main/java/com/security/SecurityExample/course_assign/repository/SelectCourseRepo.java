package com.security.SecurityExample.course_assign.repository;

import com.security.SecurityExample.course_assign.model.SelectCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectCourseRepo extends JpaRepository<SelectCourse,Integer> {

    @Query(value = "select * from Man.select_course where user_id = :userId",nativeQuery = true)
    List<SelectCourse> findAllSelectedCourseByUserID(@Param("userId") int userId);

    @Query(value = "select * from Man.select_course as sc " +
            "join Man.course_assign as cs " +
            "on sc.course_assigned_id = cs.course_assign_id " +
            "where cs.faculty_id = :facultyId",
            nativeQuery = true)
    List<SelectCourse> findAllCourseSelectedByStudent(@Param("facultyId") int facultyId);
}
