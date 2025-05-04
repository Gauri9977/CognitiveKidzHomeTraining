package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cognitivekidshometraining.Model.Child;
import com.example.cognitivekidshometraining.adapter.ChildrenAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class dr_child_list extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;

    private RecyclerView recyclerView;
    private List<Child> childrenList = new ArrayList<>();
    private ChildrenAdapter adapter;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_child_list);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("\uD83D\uDC64 Client Demographics");
        toolbar_left_image.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag1, new DrawerFragment1()).commit();
        DrawerLayout drawer = findViewById(R.id.dr_child_list);
        toggle = new ActionBarDrawerToggle(dr_child_list.this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(v ->{});
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();


        recyclerView = findViewById(R.id.childrenRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChildrenAdapter(this, childrenList, child -> {
            // Handle View button click
            Toast.makeText(this, "Viewing " + child.name, Toast.LENGTH_SHORT).show();
            // You can also navigate to a detailed activity
        });

        recyclerView.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        loadChildrenData();
    }

    private void loadChildrenData() {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String uid = userSnapshot.getKey();
                    String name = userSnapshot.child("ChildData/child_name").getValue(String.class);
                    String disorder = userSnapshot.child("ChildData/disability_type").getValue(String.class);

                    if (name != null) {
                        childrenList.add(new Child(uid, name, disorder != null ? disorder : "Not specified"));
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}