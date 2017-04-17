package com.huangfw.crawler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.huangfw.crawler.model.Song;

public interface SongRepository extends JpaRepository<Song, String> {
    Page<Song> findTop48ByTagLike(String tag, Pageable page);

    /*Page<Song> findTop80(Pageable page);*/
    Page<Song> findTop48ByRecommendValueGreaterThan(double recommendValue,Pageable page);
}
