package com.example.toolservice.services;

import com.example.toolservice.entities.Tool;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public interface IToolService {
    Tool addTool(Tool tool);
    void deleteTool(Long id);
    Tool updateTool(Tool tool);
    Tool findToolById(long id);
    List<Tool> findAllTools();
    Tool findByCreatedDateOrderByCreatedDateAsc(@NonNull Date createdDate);
    Tool findBySourceContainingIgnoreCase(@NonNull String source);
    List<Tool> getAllToolsByMember(Long idMember);
}
