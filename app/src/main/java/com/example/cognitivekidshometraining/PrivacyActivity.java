package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        TextView privacyText = findViewById(R.id.privacyText);

        //String content = "Privacy Policy & Terms of Use\n\n" +
                //"We respect your privacy. All your data is stored securely and used only for enhancing your experience.\n\n" +
                //"1. We never share your data with third parties.\n" +
                //"2. You have full control over your data.\n" +
                //"3. You can contact us anytime to get your data removed.\n\n" +
                //"Use of this app means you agree to these terms.";

        //privacyText.setText(content);
    }
}
