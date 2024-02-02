package com.example.humanresourcesdepart.SecondPages;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.humanresourcesdepartment.R;
import com.google.android.material.navigation.NavigationView;

public class MainSecondPage extends AppCompatActivity {
    private DrawerLayout drawerLayout; // Добавьте поле для DrawerLayout
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_second_page_activity);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            // Обрабатываем нажатие на элемент меню
            switch (item.getItemId()) {
                case R.id.nav_author:
                    replaceFragment(new AuthorFragment());
                    break;
                case R.id.nav_prog:
                    replaceFragment(new ProgramFragment());
                    break;
                case R.id.nav_git:
                    replaceFragment(new GitFragment());
                    break;
                case R.id.nav_home:
                    replaceFragment(new PeoplesFragment());
                    break;
                case R.id.nav_instruction:
                    replaceFragment(new InstructionFragment());
                    break;
                case R.id.nav_input:
                    finish();
                    break;
            }

            // Закрываем боковое меню после нажатия
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
        replaceFragment(new PeoplesFragment());
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


}

