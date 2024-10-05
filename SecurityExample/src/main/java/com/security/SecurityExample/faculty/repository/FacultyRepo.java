package com.security.SecurityExample.faculty.repository;

import com.security.SecurityExample.faculty.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepo extends JpaRepository<Faculty,Integer> {
}
