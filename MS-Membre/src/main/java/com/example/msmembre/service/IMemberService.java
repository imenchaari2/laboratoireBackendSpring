package com.example.msmembre.service;

import com.example.msmembre.entities.Member;
import com.example.msmembre.entities.Student;
import com.example.msmembre.entities.TeacherResearcher;
import com.example.msmembre.payload.request.UpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMemberService {
    Member addMember(Member m);
//    Member addMember(Member m,String cv, String photo);
    void deleteMember(Long id);
    UpdateRequest updateMemberInfos(UpdateRequest m);
    TeacherResearcher updateTeacher(TeacherResearcher p,MultipartFile cvFile, MultipartFile photoFile);
    Optional<Member> findMemberById(Long id);

    List<Member> findAll();
    List<Member> findAllAuthors();

    List<Student>findByFirstNameAndLastNameAndCinAndType(String firstName,String lastName,String cin,String type);
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


    Student updateStudent(Student student, MultipartFile cvFile, MultipartFile photoFile);
    String changePassword(Long id,String currentPass , String NewPass);
}
