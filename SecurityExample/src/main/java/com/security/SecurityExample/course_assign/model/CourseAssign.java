package com.security.SecurityExample.course_assign.model;

import com.security.SecurityExample.faculty.model.Faculty;
import com.security.SecurityExample.subject.model.Subject;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CourseAssign",schema = "Man")
public class CourseAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseAssignId;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject selectSubject;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty selectFaculty;

    @Column(name = "assigned_date")
    private LocalDateTime courseAssignedDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "modified_date")
    private LocalDateTime courseModifiedDate;


}
