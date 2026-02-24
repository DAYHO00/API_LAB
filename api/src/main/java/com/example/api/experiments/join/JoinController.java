package com.example.api.experiments.join;

import com.example.api.domain.project.Project;
import com.example.api.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experiments/join")
public class JoinController {

    private final ProjectRepository projectRepository;

    @GetMapping("/projects-task-count")
    public String projectsTaskCount() {
        List<Project> projects = projectRepository.findAllWithTasks(); // ✅ fetch join

        int totalTasks = 0;
        for (Project p : projects) {
            totalTasks += p.getTasks().size(); // ✅ 여기서 추가 쿼리 안 나와야 함
        }

        return "projects=" + projects.size() + ", totalTasks=" + totalTasks;
    }
}