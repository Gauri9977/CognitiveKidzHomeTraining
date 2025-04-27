package com.example.cognitivekidshometraining;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TermsCondition extends AppCompatActivity {

    TextView termsText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        termsText = findViewById(R.id.terms_text);

        String termsContent =
                "\nLast Updated: April 2025\n\n" +
                        "Welcome to Cognitive Kidz Home Training. By accessing or using our application, you agree to comply with and be bound by the following Terms and Conditions. Please review them carefully. If you do not agree with these Terms, you should not use our app.\n\n" +

                        "1. Acceptance of Terms\n" +
                        "• Agreement: By downloading, installing, or using the Cognitive Kidz Home Training app (\"App\"), you acknowledge that you have read, understood, and agree to be bound by these Terms and our Privacy Policy.\n" +
                        "• Modifications: We reserve the right to update or modify these Terms at any time without prior notice. Your continued use of the App after any changes indicates your acceptance of the new Terms.\n\n" +

                        "2. Services Overview\n" +
                        "• The App provides interactive educational activities aimed at enhancing children's cognitive skills through home-based training.\n" +
                        "• We strive to ensure the App is available at all times but do not guarantee uninterrupted access.\n\n" +

                        "3. Eligibility\n" +
                        "• The App is intended for use by children under the supervision of a parent or legal guardian. Users must be at least 18 years old to create an account.\n" +
                        "• Parents or guardians are responsible for monitoring their child’s use of the App.\n\n" +

                        "4. User Accounts\n" +
                        "• Registration: You agree to provide accurate and complete information and keep it updated.\n" +
                        "• Security: You are responsible for maintaining the confidentiality of your credentials. Notify us of unauthorized use immediately.\n\n" +

                        "5. User Conduct\n" +
                        "Prohibited Activities include:\n" +
                        "• Using the App for illegal activities.\n" +
                        "• Uploading harmful or malicious code.\n" +
                        "• Disrupting the App’s integrity or performance.\n" +
                        "• Attempting unauthorized access.\n\n" +

                        "6. Intellectual Property\n" +
                        "• All content belongs to Cognitive Kidz Home Training.\n" +
                        "• License granted is personal and non-commercial. You may not copy or modify content.\n\n" +

                        "7. Privacy\n" +
                        "• Use of the App indicates acceptance of our Privacy Policy. We collect and use data as outlined therein.\n\n" +

                        "8. Disclaimers and Limitations\n" +
                        "• The App is provided \"as-is\" and \"as-available.\" We make no warranties.\n" +
                        "• We are not liable for indirect, incidental, or special damages.\n\n" +

                        "9. Indemnification\n" +
                        "• You agree to indemnify and hold Cognitive Kidz and its team harmless against any claims resulting from your use of the App.\n\n" +

                        "10. Termination\n" +
                        "• We may suspend or terminate your access if these Terms are violated. Your right to use the App ceases immediately.\n\n" +

                        "11. Governing Law\n" +
                        "• These Terms are governed by Indian law. Disputes are subject to Indian courts.\n\n" +

                        "12. Contact Us\n" +
                        "📧 Email: support@cognitivekidz.com\n\n" +

                        "Cognitive Kidz Home Training reserves all rights not expressly granted herein.";

        termsText.setText(termsContent);
    }
}
