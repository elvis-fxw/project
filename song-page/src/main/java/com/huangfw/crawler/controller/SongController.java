package com.huangfw.crawler.controller;

import com.huangfw.crawler.model.Message;
import com.huangfw.crawler.model.MusicComment;
import com.huangfw.crawler.model.Song;
import com.huangfw.crawler.repository.GtpRepository;
import com.huangfw.crawler.repository.MusicCommentRepository;
import com.huangfw.crawler.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huangfw.crawler.utils.Constants.TAG_MAP;


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
                        @PageableDefault(size = 30, sort = "commentCount", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("songs", songRepository.findAll(pageable));
        return "songs";
    }

    @GetMapping({"/comments", ""})
    public String comment(Model model,
                          @PageableDefault(size = 30, sort = "appreciation", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("comments", musicCommentRepository.findAll(pageable));
        model.addAttribute("message",new Message());
        return "comments";
    }

    @GetMapping({"/gtp", ""})
    public String gtp(Model model,
                          @PageableDefault(size = 52) Pageable pageable) {
        model.addAttribute("gtps", gtpRepository.findAll(pageable));
        return "gtp";
    }

    @RequestMapping(value="/search.do",method= RequestMethod.POST)
    public String search(@ModelAttribute(value="message") Message message,Model model,
                         @PageableDefault(size = 30, sort = "appreciation", direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("comments",musicCommentRepository.findAllByTitleLike("%"+message.getInfo()+"%",pageable));
        //model.addAttribute("comments",musicCommentRepository.findAllByTitleMatches(message.getInfo(),pageable));
        return "comments";
    }

    @GetMapping({"/recommend", ""})
    public String recommend(Model model,
                      @PageableDefault(size = 40, sort = "recommendValue", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("songs", songRepository.findTop48ByRecommendValueGreaterThan(0,pageable));
        model.addAttribute("message",new Message());
        model.addAttribute("tagMap",TAG_MAP);
        return "recommend";
    }

    @RequestMapping(value="/recommend.do",method= RequestMethod.POST)
    public String recommendDo(@ModelAttribute(value="message") Message message,Model model,
                         @PageableDefault(size = 30, sort = "recommendValue", direction = Sort.Direction.DESC) Pageable pageable){
        System.out.println(message.getInfo());
        model.addAttribute("songs",songRepository.findTop48ByTagLike("%"+message.getInfo()+"%",pageable));
        model.addAttribute("tagMap",TAG_MAP);
        return "recommend";
    }
}
