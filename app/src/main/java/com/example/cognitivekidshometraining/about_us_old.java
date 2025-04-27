package com.example.cognitivekidshometraining;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class about_us_old extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    ImageView toolbar_left_image;
    ActionBarDrawerToggle toggle;
    ExpandableListView expandableListView;
    MyExpandableListAdapter expandableListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_old);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = toolbar.findViewById(R.id.toolbar_right_text);
        toolbar_left_image = toolbar.findViewById(R.id.toolbar_left_image);
        toolbar_title.setText("About Us");
        toolbar_left_image.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.drawer_frag, new DrawerFragment()).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_academic_activity);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        expandableListView = findViewById(R.id.expandableListView);

        // Prepare data for the list
        prepareListData();

        // Set the adapter
        expandableListAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);

        // On item click listener to show description
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String selectedItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
            Toast.makeText(about_us_old.this, selectedItem, Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Add main points
        listDataHeader.add("Empowering Growth");
        listDataHeader.add("Access to Specialists");
        listDataHeader.add("Holistic Development");
        listDataHeader.add("Easy Access to Guidance");
        listDataHeader.add("Personalized Support");
        listDataHeader.add("Informed Parenting");
        listDataHeader.add("Early Intervention");
        listDataHeader.add("Supportive Community");

        // Add descriptions for each main point
        List<String> empoweringGrowth = new ArrayList<>();
        empoweringGrowth.add("Support children’s cognitive development with expert-led tools and resources.");

        List<String> accessToSpecialists = new ArrayList<>();
        accessToSpecialists.add("Connect with top pediatric, psychology, and therapy professionals anytime, anywhere.");

        List<String> holisticDevelopment = new ArrayList<>();
        holisticDevelopment.add("Comprehensive resou    rces for speech, occupational therapy, and behavioral growth.");

        List<String> easyAccessToGuidance = new ArrayList<>();
        easyAccessToGuidance.add("Simplifies finding and consulting specialists for each child’s unique needs.");

        List<String> personalizedSupport = new ArrayList<>();
        personalizedSupport.add("Tailored therapies and resources to meet diverse developmental needs.");

        List<String> informedParenting = new ArrayList<>();
        informedParenting.add("Equip parents with expert advice and activities for effective caregiving.");

        List<String> earlyIntervention = new ArrayList<>();
        earlyIntervention.add("Provides tools to address developmental delays early for better outcomes.");

        List<String> supportiveCommunity = new ArrayList<>();
        supportiveCommunity.add("Offers resources and a community for shared experiences and support.");

        // Add children to the main points
        listDataChild.put(listDataHeader.get(0), empoweringGrowth);
        listDataChild.put(listDataHeader.get(1), accessToSpecialists);
        listDataChild.put(listDataHeader.get(2), holisticDevelopment);
        listDataChild.put(listDataHeader.get(3), easyAccessToGuidance);
        listDataChild.put(listDataHeader.get(4), personalizedSupport);
        listDataChild.put(listDataHeader.get(5), informedParenting);
        listDataChild.put(listDataHeader.get(6), earlyIntervention);
        listDataChild.put(listDataHeader.get(7), supportiveCommunity);
    }
}
