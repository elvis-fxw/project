package com.huangfw.crawler.repository;

import com.huangfw.crawler.model.MusicComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicCommentRepository extends JpaRepository<MusicComment, String> {
    List<MusicComment> findByTitle(String title);
}
