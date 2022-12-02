package com.example.msmembre.controller;
import com.example.msmembre.entities.File;
import com.example.msmembre.entities.Member;
import com.example.msmembre.entities.Student;
import com.example.msmembre.entities.TeacherResearcher;
import com.example.msmembre.repositories.FileRepository;
import com.example.msmembre.repositories.MemberRepository;
import com.example.msmembre.service.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController()
@RequestMapping(path = "/api/member")
@CrossOrigin()
public class MemberController {
    @Autowired
    IMemberService iMemberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;

//    @GetMapping(path = {"/get/{imageName}"})
//    public File getImage(@PathVariable("imageName") String imageName) throws IOException {
//        final Optional<File> retrievedImage = fileRepository.findByName(imageName);
//        return new File(retrievedImage.get().getName(), retrievedImage.get().getType(),
//                decompressBytes(retrievedImage.get().getData()));
//    }

//    // compress the image bytes before storing it in the database
//    public static byte[] compressBytes(byte[] data) {
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while (!deflater.finished()) {
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer, 0, count);
//        }
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//        }
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
//        return outputStream.toByteArray();
//    }
//
//    // uncompress the image bytes before returning it to the angular application
//    public static byte[] decompressBytes(byte[] data) {
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        try {
//            while (!inflater.finished()) {
//                int count = inflater.inflate(buffer);
//                outputStream.write(buffer, 0, count);
//            }
//            outputStream.close();
//        } catch (IOException ioe) {
//        } catch (DataFormatException e) {
//        }
//        return outputStream.toByteArray();
//    }

    @PutMapping("/updatePhoto/{idMember}/{idPhoto}")
    public Member uplaodImage(@PathVariable Long idMember, @PathVariable Long idPhoto, @RequestParam("imageFile") MultipartFile file) throws IOException {
        File photo = fileRepository.findById(idPhoto).get();
        Member member = findMemberById(idMember).get();
        photo.setName(file.getOriginalFilename());
        photo.setType(file.getContentType());
        photo.setData(file.getBytes());
        fileRepository.saveAndFlush(photo);
        member.setPhoto(photo);
        return memberRepository.saveAndFlush(member);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        File databaseFile = fileRepository.findByName(fileName).get();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }

    @PostMapping(value = "/addStudent", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Member addMemberStudent(@ModelAttribute("student") Student student,
                                   @RequestPart("cvFile") MultipartFile cvFile,
                                   @RequestPart("photoFile") MultipartFile photoFile
    ) throws IOException {
        File img = new File(photoFile.getOriginalFilename(), photoFile.getContentType(), photoFile.getBytes());
        File cv = new File(cvFile.getOriginalFilename(), cvFile.getContentType(), cvFile.getBytes());
        student.setPhoto(fileRepository.save(img));
        student.setCv(fileRepository.save(cv));
        return iMemberService.addMember(student);
    }

    @PostMapping(value = "/addTeacherResearcher", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})

    public Member addMemberTeacherResearcher(@ModelAttribute("teacher") TeacherResearcher teacherResearcher,
                                             @RequestParam("cvFile") MultipartFile cvFile,
                                             @RequestParam("photoFile") MultipartFile photoFile
    ) throws IOException {
        File img = new File(photoFile.getOriginalFilename(), photoFile.getContentType(), photoFile.getBytes());
        File cv = new File(cvFile.getOriginalFilename(), cvFile.getContentType(), cvFile.getBytes());
        teacherResearcher.setPhoto(fileRepository.save(img));
        teacherResearcher.setCv(fileRepository.save(cv));
        return iMemberService.addMember(teacherResearcher);
    }

//    @PutMapping(value = "/updateStudent/{id}")
//    public Member updateMember(@PathVariable Long id, Student student, @RequestParam("cvFile") MultipartFile cvFile,
//                               @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
//        student.setId(id);
//        String cvName = StringUtils.cleanPath(Objects.requireNonNull(cvFile.getOriginalFilename()));
//        Path cvStorage = get("E:/gi3/lab-de-recherche/LabBackend/MS-Membre/cvTeachers/", cvName).toAbsolutePath().normalize();
//        copy(cvFile.getInputStream(), cvStorage, REPLACE_EXISTING);
//        String photoName = StringUtils.cleanPath(Objects.requireNonNull(photoFile.getOriginalFilename()));
//        Path photoStorage = get("E:/gi3/lab-de-recherche/LabBackend/MS-Membre/photosTeachers/", photoName).toAbsolutePath().normalize();
//        copy(photoFile.getInputStream(), photoStorage, REPLACE_EXISTING);
//        return iMemberService.updateMember(student, cvName, photoName);
//    }
//    @PutMapping(value = "/updateTeacherResearcher/{id}")
//    public Member updateMembre(@PathVariable Long id, @RequestBody TeacherResearcher teacherResearcher) {
//        teacherResearcher.setId(id);
//        return iMemberService.updateMember(teacherResearcher);
//    }

    @GetMapping(value = "/members")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Member> findAllMembers() {
        return iMemberService.findAll();
    }


    @GetMapping("/findStudentBySearch")
    public List<Student> findByFirstNameOrLastName(@RequestParam String firstName,
                                                   @RequestParam String lastName,
                                                   @RequestParam String cin,
                                                   @RequestParam String diploma) {
        return iMemberService.findByFirstNameAndLastNameAndCinAndDiploma(firstName, lastName, cin, diploma);
    }

    @GetMapping("/findTeacherBySearch")
    public List<TeacherResearcher> findByFirstNameAndLastNameAndCinAndEtablishmentAndGrade(@RequestParam String firstName,
                                                                                           @RequestParam String lastName,
                                                                                           @RequestParam String cin,
                                                                                           @RequestParam String etablishment,
                                                                                           @RequestParam String grade
    ) {
        return iMemberService.findByFirstNameAndLastNameAndCinAndEtablishmentAndGrade(firstName, lastName, cin, etablishment, grade);
    }
    @GetMapping(value = "/students")
    public List<Student> findAllStudents() {
        return iMemberService.findAllStudents();
    }
    @GetMapping(value = "/teachers")
    public List<TeacherResearcher> findAllTeachers() {
        return iMemberService.findAllTeachers();
    }
    @GetMapping(value = "/member/{id}")
    public Optional<Member> findMemberById(@PathVariable Long id) {
        return iMemberService.findMemberById(id);
    }
    @DeleteMapping(value = "/deleteMember/{id}")
    public void deleteMember(@PathVariable Long id) {
        iMemberService.deleteMember(id);

    }

    @PutMapping(value = "/affectSupervisorToStudent/{idSupervisor}")
    public Member affectSupervisorToStudent(@RequestBody Student student, @PathVariable Long idSupervisor) {
        return iMemberService.affectSupervisorToStudent(student, idSupervisor);
    }

    @GetMapping("/findByInscriptionDatePeriod")
    public List<Student> findStudentByInscriptionDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inscriptionDateGT,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date inscriptionDateLT) {
        return iMemberService.findStudentByInscriptionDateBetween(inscriptionDateGT, inscriptionDateLT);

    }

    @GetMapping("/studentsBySupervisorName")
    public List<Student> getAllStudentsBySupervisorName(@RequestParam String name) {
        return iMemberService.getAllStudentsBySupervisorName(name);
    }

}
