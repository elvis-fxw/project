package com.huangfw.crawler.controller;

import com.huangfw.crawler.repository.GtpRepository;
import com.huangfw.crawler.repository.MusicCommentRepository;
import com.huangfw.crawler.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SongController {
    @Autowired
    SongRepository songRepository;
    @Autowired
    MusicCommentRepository musicCommentRepository;
    @Autowired
    GtpRepository gtpRepository;

    @GetMapping({"/songs", ""})
    public String songs(Model model,
                        @PageableDefault(size = 100, sort = "commentCount", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("songs", songRepository.findAll(pageable));
        return "songs";
    }

    @GetMapping({"/comments", ""})
    public String comment(Model model,
                          @PageableDefault(size = 100, sort = "appreciation", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("comments", musicCommentRepository.findAll(pageable));
        return "comments";
    }

    @GetMapping({"/gtp", ""})
    public String gtp(Model model,
                          @PageableDefault(size = 100) Pageable pageable) {
        model.addAttribute("gtps", gtpRepository.findAll(pageable));
        return "gtp";
    }
}
