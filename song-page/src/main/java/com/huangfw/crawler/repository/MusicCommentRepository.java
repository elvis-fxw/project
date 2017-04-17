package com.huangfw.crawler.repository;

import com.huangfw.crawler.model.MusicComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicCommentRepository extends JpaRepository<MusicComment, String> {
    Page<MusicComment> findAllByTitle(String title,Pageable var1);

    Page<MusicComment> findAllByTitleLike(String title,Pageable var1);

    Page<MusicComment> findAllByTitleMatchesRegex(String title,Pageable var1);

    Page<MusicComment> queryAllByTitleRegex(String title,Pageable var1);

    Page<MusicComment> findAllByTitleMatches(String title,Pageable var1);

    Page<MusicComment> findAllByAppreciationGreaterThan(Long value,Pageable var1);
}
