package com.example.tvshows.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/report")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/top10")
    public ResponseEntity<byte[]> top10() {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=top_10_shows_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createTop10RatedShowsReport());
    }

    @GetMapping("/summary")
    public ResponseEntity<byte[]> summary() {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=summary_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createSummaryReport());
    }

    @GetMapping("/best-episode-per-show")
    public ResponseEntity<byte[]> bestEpisodePerShow() {

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=best_episode_per_show_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createBestEpisodePerShowReport());
    }

    @GetMapping("/recommend")
    public ResponseEntity<byte[]> recommend(@RequestParam(required = true) String genre) throws IOException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=recommend_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createRecommendedShowReport(genre));
    }

    @GetMapping("/nextweek")
    public ResponseEntity<byte[]> nextWeek() throws IOException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=nextweek_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createNextWeekReport());
    }

    @GetMapping("/topnetwork")
    public ResponseEntity<byte[]> topNetwork() throws IOException {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=topnetwork_report.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(reportService.createTopNetworkReport());
    }

}