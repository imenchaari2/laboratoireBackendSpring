package com.example.toolservice.repositories;

import com.example.toolservice.entities.Tool;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    Tool findByCreatedDateOrderByCreatedDateAsc(@NonNull Date createdDate);
    Tool findBySourceContainingIgnoreCase(@NonNull String source);
}
