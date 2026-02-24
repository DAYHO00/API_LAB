package com.example.api.experiments.cache;

import com.example.api.domain.project.Project;
import com.example.api.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CachedStatsService {

    private final ProjectRepository projectRepository;

    @Cacheable(value = "projectTaskStats", key = "'v1'")
    public String getProjectsTaskCountCached() {
        List<Project> projects = projectRepository.findAllWithTasks();

        int totalTasks = 0;
        for (Project p : projects) {
            totalTasks += p.getTasks().size();
        }

        return "projects=" + projects.size() + ", totalTasks=" + totalTasks;
    }
}