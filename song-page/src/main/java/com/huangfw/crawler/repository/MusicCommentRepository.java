package com.huangfw.crawler.repository;

import com.huangfw.crawler.model.MusicComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicCommentRepository extends JpaRepository<MusicComment, String> {
    Page<MusicComment> findAllByTitle(String title,Pageable var1);
}
