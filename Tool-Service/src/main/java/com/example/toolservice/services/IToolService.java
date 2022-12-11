package com.example.toolservice.services;

import com.example.toolservice.entities.Tool;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public interface IToolService {
    Tool addTool(Tool tool,Long idMember);
    void deleteTool(Long id);
    Tool updateTool(Long idTool ,Tool tool);
    Tool findToolById(long id);
    List<Tool> findAllTools();
    Tool findByCreatedDateOrderByCreatedDateAsc(@NonNull Date createdDate);
    Tool findBySourceContainingIgnoreCase(@NonNull String source);
    List<Tool> getAllToolsByMember(Long idMember);
    Tool affectMemberToTool(Long idAuthor, Long idTool);
    List<Tool> getAllToolsByAuthorName(String name);
    List<Tool> findToolByCreatedDateBetween(Date createdDateGT, Date createdDateLT);
}
