package com.example.humanresourcesdepart.SecondPages;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.humanresourcesdepartment.R;
import com.google.android.material.navigation.NavigationView;

public class PeoplesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peoples, container, false);

        // Находим NavigationView в макете фрагмента
        NavigationView navigationView = view.findViewById(R.id.nav_view);

        // Устанавливаем слушателя событий для элементов меню
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    case R.id.nav_input:
                        // Здесь добавьте код для обработки выхода (если необходимо)
                        break;
                }

                // Закрываем боковое меню после нажатия
                DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        return view;
    }

    // Метод для замены текущего фрагмента на новый
    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
