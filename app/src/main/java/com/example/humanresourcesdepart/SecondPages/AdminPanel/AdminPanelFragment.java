package com.example.humanresourcesdepart.SecondPages.AdminPanel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.humanresourcesdepart.R;

public class AdminPanelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addPeople = view.findViewById(R.id.add_people);
        addPeople.setOnClickListener(view1 -> {
            // Создаем экземпляр AddPeopleFragment
            AddPeopleFragment addPeopleFragment = new AddPeopleFragment();

            // Заменяем текущий фрагмент на AddPeopleFragment
            replaceFragment(addPeopleFragment);
        });
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            // Используем FragmentTransaction для замены фрагмента
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}