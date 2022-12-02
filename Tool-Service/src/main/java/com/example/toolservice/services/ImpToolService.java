package com.example.toolservice.services;

import com.example.toolservice.entities.Tool;
import com.example.toolservice.repositories.ToolRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ImpToolService implements IToolService {
    @Autowired
    ToolRepository toolRepository;
    @Override
    public Tool addTool(Tool tool) {
        return this.toolRepository.save(tool);
    }

    @Override
    public void deleteTool(Long id) {
        this.toolRepository.deleteById(id);
    }

    @Override
    public Tool updateTool(Tool tool) {
        return this.toolRepository.saveAndFlush(tool);
    }

    @Override
    public Tool findToolById(long id) {
        return this.toolRepository.findById(id).get();
    }

    @Override
    public List<Tool> findAllTools() {
        return this.toolRepository.findAll();
    }

    @Override
    public Tool findByCreatedDateOrderByCreatedDateAsc(@NonNull Date createdDate) {
        return toolRepository.findByCreatedDateOrderByCreatedDateAsc(createdDate);
    }

    @Override
    public Tool findBySourceContainingIgnoreCase(@NonNull String source) {
        return toolRepository.findBySourceContainingIgnoreCase(source);
    }

    @Override
    public List<Tool> getAllToolsByMember(Long idMember) {
        List<Tool> toolsByMember = Lists.newArrayList();
        List<Tool> tools = toolRepository.findAll();
        var toolList = tools.stream().filter(tool -> tool.getMemberId().equals(idMember)).toList();
        toolsByMember.addAll(toolList);
        return toolsByMember;
    }
}
