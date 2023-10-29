package com.example.tvshows.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/top10")
    public void top10(){
        reportService.createTop10RatedShowsReport("top_10_shows_report");
    }

    @GetMapping("/summary")
    public void summary(){
        reportService.createSummaryReport("summary_report");
    }

    @GetMapping("/best-episode-per-show")
    public void bestEpisodePerShow(){
        reportService.createBestEpisodePerShowReport("best_episode_per_show_report");
    }

    @GetMapping("/recommend")
    public void recommend(@RequestParam(required = true) String genre){
        reportService.createRecommendedShowReport("recommended_show_report", genre);
    }

    @GetMapping("/nextweek")
    public void nextWeek(){
        reportService.createNextWeekReport("nextweek_report");
    }

}