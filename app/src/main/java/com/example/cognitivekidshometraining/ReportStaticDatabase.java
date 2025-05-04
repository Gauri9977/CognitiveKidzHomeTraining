package com.example.cognitivekidshometraining;

import java.util.ArrayList;
import java.util.List;

public class ReportStaticDatabase {

    public static List<ReportWeeklyReport> getReports() {
        List<ReportWeeklyReport> reports = new ArrayList<>();

        // Week 1
        /*reports.add(new ReportWeeklyReport(
                "04 Mar 2025",// Report Date (You can update this dynamically later)
                "Mr. Vihan Jagtap",
                "Dr.Nitin Chavan"  // Client Name
                "+91-8623812780",  // Contact Number
                3, // Age in years (03 Years 01 Months 13 Days)
                "Global Developmental Delay along with Mild IDD", // Condition
                "Dr. Shreeganesh Patil", // Reference doctor
                2, // Cognition score row (2 years)
                1, // Cognition score row (1 month)
                15, // Cognition score row (15 days)
                23, // DST Test Score
                "Attention and Concentration: Not good; lacks environmental functioning.\n" +
                        "Comprehension: Low; unable to follow simple instructions.", // Clinical Observation
                50, // Placeholder Score 1 (replace as needed)
                55, // Placeholder Score 2 (replace as needed)
                "Recommendations:\n- Occupational Therapy\n- Speech Therapy\n- Parental Counselling\n\n" +
                        "Reassessment after 01 Year 011 Months 17 Days.", // Recommendations

                // Extra Fields from App Screenshot (new points below 👇)
                "Date of Birth: 04/03/2021\nReferral for: IQ/DQ\nReg. No.: 019/24", // Registration Info
                "Informant: Mother, Father, Both Parents, Other Relationships", // Informant
                "Medium: Marathi/English\nCurrent School Name: Nil\nSTD: Nil\nLanguage of Administration: Marathi, Hindi, English, Kannada", // Language & School Info
                "Test Administered: Development Screening Test (DST) and VSMS", // Test Administered
                "Mother's conception age was 27 y.; consanguinity is absent. She had regular antenatal check-ups and a full-term C-section at the hospital. Birth weight was normal (3.0 kg). Immediate birth cry was present, breastfeeding was normal, and developmental milestones were age-appropriate. All vaccinations were given on time, and no complications were reported. Developmental information was provided by the parent." // Brief History
        ));*/


        reports.add(new ReportWeeklyReport(
                "27 Apr 2025",
                "Vihan Jagtap",
                "+91-8623812780",
                4,
                "Autism",
                "Dr. Nitin Chavan",
                1,
                2,
                4,
                2,
                "Cognitive response test - Passed\nMemory test - Moderate",
                "Shows improvement in focus and response time.",
                50,
                55,
                "Continue current therapy plan. Add memory games next week."
        ));

        // Week 2
        reports.add(new ReportWeeklyReport(
                "20 Apr 2025",
                "Vihan Jagtap",
                "+91-8623812780",
                4,
                "Autism",
                "Dr. Nitin Chavan",
                2,
                1,
                5,
                1,
                "Cognitive test - Improved\nMemory test - Better retention",
                "Increased attention span during tasks.",
                75,
                75,
                "Excellent progress. Encourage social interaction activities."
        ));

        // Week 3
        /*reports.add(new ReportWeeklyReport(
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
        ));*/

        // Week 4
        /*reports.add(new ReportWeeklyReport(
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
        ));*/


        return reports;
    }
}
