package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cognitivekidshometraining.databinding.ActivityAcademicBinding;

public class academic_activity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Academic Activities");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_academic_activity);
        toggle = new ActionBarDrawerToggle(academic_activity.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        LinearLayout numbers = findViewById(R.id.numbers);
        LinearLayout words = findViewById(R.id.words);
        LinearLayout colours = findViewById(R.id.colours);
        LinearLayout shapes = findViewById(R.id.shapes);
        LinearLayout sequence = findViewById(R.id.sequence);

        numbers.setOnClickListener(v -> openWeb("https://cognoappproject.github.io/Counting_Game/"));
        words.setOnClickListener(v -> openWeb("https://cognoappproject.github.io/WordBuilder/"));
        colours.setOnClickListener(v -> openWeb("https://cognoappproject.github.io/LearnColors/"));
        shapes.setOnClickListener(v -> openWeb("https://cognoappproject.github.io/ShapeSorter/"));
        sequence.setOnClickListener(v -> openWeb("https://cognoappproject.github.io/Sequencing-Tasks-Game/"));
    }

    private void openWeb(String url) {
        Intent intent = new Intent(this, webview.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}



// connections
        /*LinearLayout numbers=findViewById(R.id.numbers);
        LinearLayout words=findViewById(R.id.words);
        LinearLayout colours=findViewById(R.id.colours);
        LinearLayout shapes=findViewById(R.id.shapes);
        LinearLayout sequence=findViewById(R.id.sequence);

        numbers.setOnClickListener(v -> {
            String meetUrl = "https://cognoappproject.github.io/Counting_Game/";

            // Create an Intent to open the link in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(meetUrl));

            // Start the activity with the intent
            startActivity(intent);
        });

        words.setOnClickListener(v -> {
            String meetUrl = "https://cognoappproject.github.io/WordBuilder/";

            // Create an Intent to open the link in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(meetUrl));

            // Start the activity with the intent
            startActivity(intent);
        });

        colours.setOnClickListener(v -> {
            String meetUrl = "https://cognoappproject.github.io/LearnColors/";

            // Create an Intent to open the link in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(meetUrl));

            // Start the activity with the intent
            startActivity(intent);
        });

        shapes.setOnClickListener(v -> {
            String meetUrl = "https://cognoappproject.github.io/ShapeSorter/";

            // Create an Intent to open the link in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(meetUrl));

            // Start the activity with the intent
            startActivity(intent);
        });

        sequence.setOnClickListener(v -> {
            String meetUrl = "https://cognoappproject.github.io/Sequencing-Tasks-Game/";

            // Create an Intent to open the link in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(meetUrl));

            // Start the activity with the intent
            startActivity(intent);
        });
        }}
        */