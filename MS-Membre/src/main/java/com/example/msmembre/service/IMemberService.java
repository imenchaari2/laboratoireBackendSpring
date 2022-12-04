package com.example.msmembre.service;

import com.example.msmembre.entities.Member;
import com.example.msmembre.entities.Student;
import com.example.msmembre.entities.TeacherResearcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMemberService {
    Member addMember(Member m);
//    Member addMember(Member m,String cv, String photo);
    void deleteMember(Long id);

    Member updateMember(Member p);

    Optional<Member> findMemberById(Long id);

    List<Member> findAll();

    List<Student>findByFirstNameAndLastNameAndCinAndDiploma(String firstName,String lastName,String cin,String diploma);
    List<TeacherResearcher>findByFirstNameAndLastNameAndCinAndEtablishmentAndGrade(String firstName, String lastName,String cin,String etablishment,String grade);


    //recherche spécifique des étudiants
    List<Student> findAllStudents();
    List<Student> findAllStudentsBySupervisor(Long id);
    List<Student> findStudentByInscriptionDateBetween(Date inscriptionDateGT, Date inscriptionDateLT);
    List<Student> getAllStudentsBySupervisorName(String name);


    //recherche spécifique des enseignants
    List<TeacherResearcher> findAllTeachers();

    //other ...

    Student affectSupervisorToStudent(Student student , Long idSupervisor);


}
