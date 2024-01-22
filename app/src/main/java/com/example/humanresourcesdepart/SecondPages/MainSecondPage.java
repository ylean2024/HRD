package com.example.humanresourcesdepart.SecondPages;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.humanresourcesdepartment.R;

public class MainSecondPage extends AppCompatActivity {
    private NavController navController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_second_page_activity);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_new_activity);

        navController = navHostFragment.getNavController();
    }


}

