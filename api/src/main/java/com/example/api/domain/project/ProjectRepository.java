package com.example.api.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // ✅ 핵심: tasks까지 같이 가져오기 (N+1 제거)
    @Query("select distinct p from Project p left join fetch p.tasks")
    List<Project> findAllWithTasks();
}