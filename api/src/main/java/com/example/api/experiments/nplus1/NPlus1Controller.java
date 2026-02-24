package com.example.api.experiments.nplus1;

import com.example.api.domain.project.Project;
import com.example.api.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experiments/nplus1")
public class NPlus1Controller {

    private final ProjectRepository projectRepository;

    @GetMapping("/projects-task-count")
    public String projectsTaskCount() {
        List<Project> projects = projectRepository.findAll(); // 1번 쿼리

        int totalTasks = 0;
        for (Project p : projects) {
            // 여기서 p.getTasks() 접근마다 추가 쿼리 발생 가능 → N+1
            totalTasks += p.getTasks().size();
        }

        return "projects=" + projects.size() + ", totalTasks=" + totalTasks;
    }
}