package com.example.cognitivekidshometraining;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class todo extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    ListView todoListView;

    @SuppressLint("UseSupportActionBar")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        //Toolbar toolbar = findViewById(R.id.toolbar); setSupportActionBar(toolbar); toolbar.setTitle("Daily Agenda"); toolbar.setBackgroundColor(getResources().getColor(R.color.textcolour)); toolbar.setTitleTextColor(Color.WHITE);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("Daily Agenda ");
        toolbar_left_image.setVisibility(View.GONE);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.todo_drawer_layout);
        toggle = new ActionBarDrawerToggle(todo.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);

        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        toggle.syncState();

        // Load saved to-do data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ChildTodo", MODE_PRIVATE);
        String todoData = prefs.getString("todoData", "");

        // Parse and clean items
        String[] items = todoData.split("\n");
        ArrayList<String> todoList = new ArrayList<>();
        for (String item : items) {
            String cleaned = item.replace("- ", "").trim();
            if (!cleaned.isEmpty()) {
                todoList.add(cleaned);
            }
        }

        // Display the to-do items
        todoListView = findViewById(R.id.todo_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoList);
        todoListView.setAdapter(adapter);

        // Click handler to open appropriate links
        todoListView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = todoList.get(position).trim();

            if (clickedItem.startsWith("Activity 1")) {
                openWebLink("https://cognoappproject.github.io/Counting_Game/");
            } else if (clickedItem.startsWith("Activity 2")) {
                openWebLink("https://cognoappproject.github.io/WordBuilder/");
            } else if (clickedItem.startsWith("Activity 3")) {
                openWebLink("https://cognoappproject.github.io/LearnColors/");
            } else if (clickedItem.startsWith("Activity 4")) {
                openWebLink("https://cognoappproject.github.io/ShapeSorter/");
            } else if (clickedItem.startsWith("Activity 5")) {
                openWebLink("https://cognoappproject.github.io/Sequencing-Tasks-Game//");
            } else {
                Toast.makeText(this, "Material/Resources provided from Doctors for: " + clickedItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openWebLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}