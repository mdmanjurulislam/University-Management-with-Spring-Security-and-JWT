package com.security.SecurityExample.subject.repository;

import com.security.SecurityExample.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepo extends JpaRepository<Subject,Integer> {
}
