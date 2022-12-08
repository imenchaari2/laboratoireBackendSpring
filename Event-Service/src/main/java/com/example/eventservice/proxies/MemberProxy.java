package com.example.eventservice.proxies;

import com.example.eventservice.beans.MemberBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

//@FeignClient(name = "MEMBER-SERVICE" ,url = "http://localhost:8087")
@FeignClient(name = "MEMBER-SERVICE")

public interface MemberProxy {
    @GetMapping("/api/member/members")
    List<MemberBean> getAllMembers();

    @GetMapping("/api/member/member/{id}")
    Optional<MemberBean> getMemberById(@PathVariable Long id);

}
