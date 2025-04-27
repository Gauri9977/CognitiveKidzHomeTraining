package com.example.cognitivekidshometraining;

import java.util.ArrayList;
import java.util.List;

public class ReportStaticDatabase {

    public static List<ReportWeeklyReport> getReports() {
        List<ReportWeeklyReport> reports = new ArrayList<>();

        // Week 1
        reports.add(new ReportWeeklyReport(
                "01 Apr 2025",
                "John Doe",
                "9876543210",
                8,
                "ADHD",
                "Dr. Smith",
                5,
                2,
                8,
                1,
                "Cognitive response test - Passed\nMemory test - Moderate",
                "Shows improvement in focus and response time.",
                50,
                55,
                "Continue current therapy plan. Add memory games next week."
        ));

        // Week 2
        reports.add(new ReportWeeklyReport(
                "08 Apr 2025",
                "John Doe",
                "9876543210",
                8,
                "ADHD",
                "Dr. Smith",
                6,
                1,
                9,
                0,
                "Cognitive test - Improved\nMemory test - Better retention",
                "Increased attention span during tasks.",
                75,
                75,
                "Excellent progress. Encourage social interaction activities."
        ));

        // Week 3
        reports.add(new ReportWeeklyReport(
                "15 Apr 2025",
                "Joeee Doe",
                "9876543210",
                8,
                "ADHD",
                "Dr. Smith",
                6,
                0,
                9,
                1,
                "Cognitive test - Consistent\nBehavioral observation - Needs improvement",
                "Patient showed more focus but still struggles with impulse control.",
                90,
                85,
                "Add behavioral therapy sessions."
        ));

        // Week 4
        reports.add(new ReportWeeklyReport(
                "27 Apr 2025",
                "Joeee Doe",
                "9876543210",
                8,
                "ADHD",
                "Dr. Smith",
                6,
                0,
                9,
                1,
                "Cognitive test - Consistent\nBehavioral observation - Needs improvement",
                "Patient showed more focus but still struggles with impulse control.",
                90,
                85,
                "Add behavioral therapy sessions."
        ));


        return reports;
    }
}
