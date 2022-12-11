package com.example.toolservice.services;

import com.example.toolservice.beans.MemberBean;
import com.example.toolservice.entities.Tool;
import com.example.toolservice.proxies.MemberProxy;
import com.example.toolservice.repositories.ToolRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ImpToolService implements IToolService {
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    MemberProxy memberProxy;
    @PersistenceContext
    EntityManager em;
    @Override
    public Tool addTool(Tool tool,Long idMember) {
        tool.setMemberId(idMember);
        return this.toolRepository.saveAndFlush(tool);
    }

    @Override
    public void deleteTool(Long id) {
        this.toolRepository.deleteById(id);
    }

    @Override
    public Tool updateTool(Long idTool , Tool tool) {
        Tool tool1 = toolRepository.findById(idTool).get();
        tool.setMemberId(tool1.getMemberId());
        tool.setMemberName(tool1.getMemberName());
        return this.toolRepository.saveAndFlush(tool);
    }

    @Override
    public Tool findToolById(long id) {
        return this.toolRepository.findById(id).get();
    }
    @Override
    public List<Tool> findAllTools() {

        List<Tool> tools = toolRepository.findAll();
        List<MemberBean> members = memberProxy.getAllMembers();
        List<Long> presentMembersIds = new ArrayList<>();
        members.forEach(member -> {
            presentMembersIds.add(member.getId());
        });
        tools.forEach(tool -> {
            if (!presentMembersIds.contains(tool.getMemberId())) {
                deleteTool(tool.getId());
            } else {
                MemberBean memberBean1 = memberProxy.getMemberById(tool.getMemberId());
                if (!Objects.equals(memberBean1.getRole(), "ROLE_ADMIN")) {
                    tool.setMemberName(memberBean1.getFirstName() + " " + memberBean1.getLastName());
                    toolRepository.saveAndFlush(tool);
                } else {
                    tool.setMemberName("ADMIN");
                    toolRepository.saveAndFlush(tool);
                }
            }
        });
        return tools;
    }



    @Override
    public Tool affectMemberToTool(Long idAuthor, Long idTool) {
        MemberBean memberBean = memberProxy.getMemberById(idAuthor);
        Tool Tool = findToolById(idTool);
        Tool.setMemberId(idAuthor);
        Tool.setMemberName(memberBean.getFirstName() + " " + memberBean.getLastName());
        return toolRepository.saveAndFlush(Tool);
    }

    @Override
    public List<Tool> getAllToolsByMember(Long idMember) {
        List<Tool> ToolsByMember = Lists.newArrayList();
        List<Tool> Tools = toolRepository.findAll();
        var ToolList = Tools.stream().filter(Tool -> Tool.getMemberId().equals(idMember)).toList();
        ToolsByMember.addAll(ToolList);
        return ToolsByMember;
    }

    @Override
    public List<Tool> getAllToolsByAuthorName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Tool> cq = cb.createQuery(Tool.class);
        Root<Tool> Tool = cq.from(Tool.class);

        if (name != null) {
            predicates.add(cb.like(Tool.get("memberName"), "%" + name + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Tool> findToolByCreatedDateBetween(Date createdDateGT, Date createdDateLT) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        CriteriaQuery<Tool> cq = cb.createQuery(Tool.class);
        Root<Tool> Tool = cq.from(Tool.class);

        if (createdDateGT != null && createdDateLT != null) {
            predicates.add(cb.greaterThanOrEqualTo(Tool.get("createdDate"), createdDateGT));
            predicates.add(cb.lessThanOrEqualTo(Tool.get("createdDate"), createdDateLT));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
    @Override
    public Tool findByCreatedDateOrderByCreatedDateAsc(@NonNull Date createdDate) {
        return toolRepository.findByCreatedDateOrderByCreatedDateAsc(createdDate);
    }

    @Override
    public Tool findBySourceContainingIgnoreCase(@NonNull String source) {
        return toolRepository.findBySourceContainingIgnoreCase(source);
    }

}
