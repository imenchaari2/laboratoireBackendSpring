package com.example.msmembre.repositories;

import com.example.msmembre.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student>findByDiploma(String diploma);
    List<Student>findByDiplomaOrderByInscriptionDateDesc(String diploma);
    List<Student> findStudentBySupervisor_FirstNameContainingIgnoreCase(String supervisor_fullName);

}
