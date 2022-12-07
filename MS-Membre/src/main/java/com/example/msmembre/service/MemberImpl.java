package com.example.msmembre.service;


import com.example.msmembre.entities.*;
import com.example.msmembre.repositories.MemberRepository;
import com.example.msmembre.repositories.StudentRepository;
import com.example.msmembre.repositories.TeacherResearcherRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class MemberImpl implements IMemberService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherResearcherRepository teacherResearcherRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired
    PasswordEncoder encoder;

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member addMember(Member m) {
        m.setCreatedDate(new Date());
        m.setPassword(encoder.encode(m.getPassword()));
//        String role = m.getRole();
//        switch (role) {
//            case "admin" -> {
//                role = String.valueOf(ERole.ROLE_ADMIN);
//            }
//            case "student" -> {
//                role = String.valueOf(ERole.ROLE_STUDENT);
//            }
//            case "teacher" -> {
//                role = String.valueOf(ERole.ROLE_TEACHER);
//            }
//            default -> role = null;
//        }
//        m.setRole(role);
        memberRepository.save(m);
        return m;
    }

    public void deleteMember(Long id) {
        List<Student> students = findAllStudentsBySupervisor(id);
        if (students != null) {
            students.forEach(student -> student.setSupervisor(null));
        }
        memberRepository.deleteById(id);
    }

    @Override
    public TeacherResearcher updateTeacher(TeacherResearcher m, MultipartFile cvFile, MultipartFile photoFile) {
        Member member = memberRepository.findById(m.getId()).get();
        if (!m.getPassword().equals(member.getPassword())) {
            m.setPassword(encoder.encode(m.getPassword()));
        }
        if (cvFile == null) {
            m.setCv(member.getCv());
        }
        if (photoFile == null) {
            m.setPhoto(member.getPhoto());
        }
        m.setCreatedDate(member.getCreatedDate());
        return memberRepository.saveAndFlush(m);
    }

    @Override
    public Student updateStudent(Student m, MultipartFile cvFile, MultipartFile photoFile) {
        Student member = studentRepository.findById(m.getId()).get();
        m.setCreatedDate(member.getCreatedDate());
        if (!m.getPassword().equals(member.getPassword())) {
            m.setPassword(encoder.encode(m.getPassword()));

        }
        if (cvFile == null) {
            m.setCv(member.getCv());
        }
        if (photoFile == null) {
            m.setPhoto(member.getPhoto());
        }
        m.setSupervisor(member.getSupervisor());
        return memberRepository.saveAndFlush(m);
    }

    public List<Member> findAll() {
        List<Member> membersList = memberRepository.findAll();
        var filtered = membersList.stream().filter(member ->
                !Objects.equals(member.getRole(), ERole.ROLE_ADMIN.name())).toList();
        return new ArrayList<>(filtered);
    }

    @Override
    public List<Student> findByFirstNameAndLastNameAndCinAndType(String firstName, String lastName, String cin, String type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> teacher = cq.from(Student.class);

        if (firstName != null) {
            predicates.add(cb.like(teacher.get("firstName"), "%" + firstName + "%"));
        }
        if (lastName != null) {
            predicates.add(cb.like(teacher.get("lastName"), "%" + lastName + "%"));
        }
        if (cin != null) {
            predicates.add(cb.like(teacher.get("cin"), "%" + cin + "%"));
        }
        if (type != null) {
            predicates.add(cb.like(teacher.get("type"), "%" + type + "%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<TeacherResearcher> findByFirstNameAndLastNameAndCinAndEtablishmentAndGrade(String firstName, String lastName, String cin, String etablishment, String grade) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<TeacherResearcher> cq = cb.createQuery(TeacherResearcher.class);
        Root<TeacherResearcher> teacher = cq.from(TeacherResearcher.class);
        if (firstName != null) {
            predicates.add(cb.like(teacher.get("firstName"), "%" + firstName + "%"));
        }
        if (lastName != null) {
            predicates.add(cb.like(teacher.get("lastName"), "%" + lastName + "%"));
        }
        if (cin != null) {
            predicates.add(cb.like(teacher.get("cin"), "%" + cin + "%"));
        }
        if (etablishment != null) {
            predicates.add(cb.like(teacher.get("etablishment"), "%" + etablishment + "%"));
        }
        if (grade != null) {
            predicates.add(cb.like(teacher.get("grade"), "%" + grade + "%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    public List<Student> findByDiploma(String diploma) {
        return studentRepository.findByDiploma(diploma);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findAllStudentsBySupervisor(Long id) {
        List<Student> studentsByMember = Lists.newArrayList();
        List<Student> students = findAllStudents();
        students.forEach(student -> {
            if (student.getSupervisor() != null && student.getSupervisor().getId().equals(id)) {
                studentsByMember.add(student);
            }
        });
        return studentsByMember;
    }

    @Override
    public List<Student> findStudentByInscriptionDateBetween(Date inscriptionDateGT, Date inscriptionDateLT) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> student = cq.from(Student.class);

        if (inscriptionDateGT != null && inscriptionDateLT != null) {
            predicates.add(cb.greaterThanOrEqualTo(student.get("inscriptionDate"), inscriptionDateGT));
            predicates.add(cb.lessThanOrEqualTo(student.get("inscriptionDate"), inscriptionDateLT));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }


    @Override
    public List<TeacherResearcher> findAllTeachers() {
        return teacherResearcherRepository.findAll();
    }

    @Override
    public Student affectSupervisorToStudent(Student student, Long idSupervisor) {
        TeacherResearcher supervisor = teacherResearcherRepository.findById(idSupervisor).get();
        student.setSupervisor(supervisor);
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudentsBySupervisorName(String name) {
        return studentRepository.findStudentBySupervisor_FirstNameContainingIgnoreCase(name);
    }


}
