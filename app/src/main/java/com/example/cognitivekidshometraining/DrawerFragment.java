package com.example.cognitivekidshometraining;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DrawerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content,container,false);

        LinearLayout profile=view.findViewById(R.id.profile_drawer_layout);
        LinearLayout settings=view.findViewById(R.id.settings_drawer_layout);
        LinearLayout account_privacy=view.findViewById(R.id.account_privacy_layout);
        LinearLayout terms=view.findViewById(R.id.terms_drawer_layout);
        LinearLayout feedback=view.findViewById(R.id.feedback_drawer_layout);
        LinearLayout about_us=view.findViewById(R.id.aboutus_drawer_layout);
        LinearLayout signout=view.findViewById(R.id.signout_layout_drawer);


        //profile
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), profile.class);
            startActivity(intent);
        });

        //settings
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        //account_privacy
        account_privacy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        });

        //terms
        terms.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TermsCondition.class);
            startActivity(intent);
        });

        //feedback
        feedback.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), feedback.class);
            startActivity(intent);
        });

        //about_us
        about_us.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(intent);
        });

        // signout
        signout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Sign Out Alert")
                    .setMessage("Are you sure you want to sign out from the application?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Positive Button: Handle Sign-Out
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), login.class);
                        startActivity(intent);
                        getActivity().finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // Negative Button: Just dismiss the dialog
                        dialog.dismiss();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        // about_us
        //about_us.setOnClickListener(v ->{
            //Intent intent = new Intent(getActivity(), about_us_old.class);
            //startActivity(intent);
        //});

        return view;
    }
}