package com.huangfw.crawler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huangfw.crawler.model.Song;

public interface SongRepository extends JpaRepository<Song, String> {
    
}
