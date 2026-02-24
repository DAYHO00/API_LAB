package com.example.api.experiments.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/experiments/cache")
public class CacheController {

    private final CachedStatsService cachedStatsService;

    @GetMapping("/projects-task-count")
    public String projectsTaskCount() {
        return cachedStatsService.getProjectsTaskCountCached();
    }
}