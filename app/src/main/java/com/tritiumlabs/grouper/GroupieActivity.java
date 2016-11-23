package com.tritiumlabs.grouper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import fragments.DatePickerFragment;
import fragments.groupiefragments.CreateGroupieFragment;
import fragments.groupiefragments.DeleteGroupieFragment;
import fragments.groupiefragments.EditGroupieFragment;

/**
 * Created by Arthur on 11/21/2016.
 *
 * This activity should be used when the user wants to create, edit, or delete a groupie
 * this activity will be called with "startActivityForResult()"
 * this activity will be composed of 3 fragments create, edit and delete
 *
 * -AB
 */

public class GroupieActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupie_activity);

        String action = getIntent().getStringExtra("action");

        if (action.equals("create")) {
            createGroupieFragment();
        } else if (action.equals("delete")) {
            deleteGroupieFragment();
        } else if (action.equals("edit")) {
            editGroupieFragment();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        CreateGroupieFragment createGroupie = (CreateGroupieFragment) getSupportFragmentManager().findFragmentByTag("CreateGroupieFragment");
        EditGroupieFragment editGroupie = (EditGroupieFragment) getSupportFragmentManager().findFragmentByTag("EditGroupieFragment");
        DeleteGroupieFragment deleteGroupie = (DeleteGroupieFragment) getSupportFragmentManager().findFragmentByTag("DeleteGroupieFragment");

        if (createGroupie != null && createGroupie.isVisible()) {
            finish();
        } else if (editGroupie != null && editGroupie.isVisible()) {
            finish();
        } else if (deleteGroupie != null && deleteGroupie.isVisible()) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void createGroupieFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        CreateGroupieFragment createGroupieFragment = new CreateGroupieFragment();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,  R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.fragContainerGroupie, createGroupieFragment, "CreateGroupieFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void editGroupieFragment() {
    }

    private void deleteGroupieFragment() {
    }
}

