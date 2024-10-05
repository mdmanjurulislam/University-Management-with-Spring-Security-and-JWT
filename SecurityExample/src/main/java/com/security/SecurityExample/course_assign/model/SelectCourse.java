package com.security.SecurityExample.course_assign.model;

import com.security.SecurityExample.auth.model.Users;
import com.security.SecurityExample.subject.model.Subject;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "Select_Course",schema = "Man")
public class SelectCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int selectCourseId;

    @ManyToOne
    @JoinColumn(name = "course_assigned_id")
    private CourseAssign selectCourse;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users student;


}
