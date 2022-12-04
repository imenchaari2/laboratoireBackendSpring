package com.example.articleservice.proxies;

import com.example.articleservice.beans.MemberBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "MEMBER-SERVICE" ,url = "http://localhost:8087")
@FeignClient(name = "MEMBER-SERVICE")

public interface MemberProxy {
    @GetMapping("/api/member/members")
    List<MemberBean> getAllMembers();

    @GetMapping("/api/member/member/{id}")
    MemberBean getMemberById(@PathVariable Long id);


}
