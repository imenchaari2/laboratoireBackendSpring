package com.example.toolservice.controllers;

import com.example.toolservice.entities.Tool;
import com.example.toolservice.proxies.MemberProxy;
import com.example.toolservice.services.ImpToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/tool")
public class ToolController {
    @Autowired
    ImpToolService toolService;
    @Autowired
    MemberProxy memberProxy;
    @PostMapping(value = "/addTool/{idMember}")
    public Tool addTool(@PathVariable Long idMember ,@RequestBody Tool tool){
        return toolService.addTool(tool,idMember);
    }
    @PutMapping(value="/updateTool/{id}")
    public Tool updateTool(@PathVariable Long id, @RequestBody Tool tool)
    {
        tool.setId(id);
        return toolService.updateTool(id,tool);
    }
    @GetMapping(value = "/findTool/{id}")
    public Tool findOneTool(@PathVariable Long id)
    {
        return toolService.findToolById(id);
    }
    @GetMapping(value = "/tools")
    public List<Tool> findAll()
    {
        return toolService.findAllTools();
    }
    @GetMapping(value = "/toolBySource")
    public Tool findBySourceContainingIgnoreCase(@RequestParam String source)
    {
        return toolService.findBySourceContainingIgnoreCase(source);
    }
    @GetMapping(value = "/toolByCreatedDate")
    public Tool findByLocationLikeIgnoreCase(@RequestParam Date createdDate)
    {
        return toolService.findByCreatedDateOrderByCreatedDateAsc(createdDate);
    }
    @DeleteMapping(value = "/deleteTool/{id}")
    public void deleteTool(@PathVariable Long id)
    {
        toolService.deleteTool(id);
    }
    @GetMapping("/toolsByMember/{idMember}")
    public List<Tool> getAllToolsByMember(@PathVariable Long idMember)
    {
        return toolService.getAllToolsByMember(idMember);
    }
    @PutMapping(value = "/affectMember/{idMember}/{idTool}")
    public Tool affectAuthorToTool(@PathVariable Long idMember, @PathVariable Long idTool) {
        return toolService.affectMemberToTool(idMember, idTool);
    }

    @GetMapping("/toolsByAuthorName")
    public List<Tool> getAllToolsByAuthorName(@RequestParam String name) {
        return toolService.getAllToolsByAuthorName(name);
    }

    @GetMapping("/findByCreatedDatePeriod")
    public List<Tool> findToolByCreatedDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateGT,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDateLT) {
        return toolService.findToolByCreatedDateBetween(createdDateGT, createdDateLT);

    }
}
