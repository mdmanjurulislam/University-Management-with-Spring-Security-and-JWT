package com.security.SecurityExample.course_assign.repository;

import com.security.SecurityExample.course_assign.model.CourseAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAssignRepo extends JpaRepository<CourseAssign,Integer> {
}
