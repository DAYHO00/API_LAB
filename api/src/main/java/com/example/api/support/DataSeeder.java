package com.example.api.support;

import com.example.api.domain.project.Project;
import com.example.api.domain.project.ProjectRepository;
import com.example.api.domain.task.Task;
import com.example.api.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
      
        if (projectRepository.count() > 0) {
            System.out.println("[SEED] skip (already seeded)");
            return;
        }

        int projectCount = 100;
        int tasksPerProject = 50;

        System.out.println("[SEED] start: projectCount=" + projectCount + ", tasksPerProject=" + tasksPerProject);

        for (int p = 1; p <= projectCount; p++) {
            Project project = projectRepository.save(new Project("Project " + p));
            for (int t = 1; t <= tasksPerProject; t++) {
                taskRepository.save(new Task("Task " + t, project));
            }
        }

        System.out.println("[SEED] done");
    }
}