package com.example.humanresourcesdepart.SecondPages;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses.PeopleAdapter;
import com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses.PeopleDataClass;
import com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses.PeopleDatabaseHelper;
import com.example.humanresourcesdepartment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PeoplesFragment extends Fragment {

    private List<PeopleDataClass> peopleList;
    private List<PeopleDataClass> originalPeopleList;

    private PeopleDatabaseHelper dbHelper;

    private PeopleAdapter adapter;
    private ListView peopleListView;
    private boolean up = false;
    private boolean down = false;

    private ImageView upArrow;
    private ImageView downArrow;

    private SearchView search;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference peopleRef = database.getReference("user_data");

    private SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_peoples, container, false);

        dbHelper = new PeopleDatabaseHelper(requireContext());

        peopleList = dbHelper.getAllPeople();
        originalPeopleList = new ArrayList<>(peopleList);

        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        peopleList = dbHelper.getAllPeople();
        originalPeopleList = new ArrayList<>(peopleList); // Копирование оригинального списка
        getPeople();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upArrow = (ImageView) view.findViewById(R.id.upForAge);
        downArrow = (ImageView) view.findViewById(R.id.downForAge);
        CardView age = (CardView) view.findViewById(R.id.memory);
        search = (SearchView) view.findViewById(R.id.search);
        peopleListView = (ListView) view.findViewById(R.id.peopleList);

        age.setOnClickListener(view1 -> filterByAge());

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Вызов метода фильтрации по имени диска с использованием введенного текста
                filterByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Вызов метода фильтрации по имени диска при изменении текста ввода
                filterByName(newText);
                return true;
            }
        });
    }

    public void getPeople() {
        peopleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PeopleDataClass> people = new ArrayList<>();

                for (DataSnapshot peopleSnapshot : dataSnapshot.getChildren()) {
                    PeopleDataClass peopleDataClass = peopleSnapshot.getValue(PeopleDataClass.class);
                    if (peopleDataClass != null) {
                        people.add(peopleDataClass);
                    }
                }

                dbHelper.loadPeopleFromFirebase(people);
                addPeople();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addPeople() {
        PeopleDataClass person1 = new PeopleDataClass("Михаил", "Иванов", 30, "Разработчик", "misha@mail.ru");
        PeopleDataClass person2 = new PeopleDataClass("Сергей", "Михайлов", 25, "Дизайнер", "sergey@mail.ru");

        if (dbHelper.isEmailUnique(person1.email)) {
            peopleRef.child(person1.email.replace(".", "_")).setValue(person1);
        }

        if (dbHelper.isEmailUnique(person2.email)) {
            peopleRef.child(person2.email.replace(".", "_")).setValue(person2);
        }

        peopleList.clear();
        peopleList.addAll(dbHelper.getAllPeople());

        adapter = new PeopleAdapter(requireContext(), R.layout.list_item_people, peopleList);
        peopleListView.setAdapter(adapter);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    protected void filterByAge() {
        Comparator<PeopleDataClass> memoryComparator = new Comparator<PeopleDataClass>() {
            @Override
            public int compare(PeopleDataClass disk1, PeopleDataClass disk2) {
                int memory1 = disk1.getAge();
                int memory2 = disk2.getAge();
                return Integer.compare(memory1, memory2);
            }
        };

        if (down) {
            Collections.sort(peopleList, memoryComparator);
            up = true;
            down = false;
            upArrow.setVisibility(View.VISIBLE);
            downArrow.setVisibility(View.INVISIBLE);
        } else {
            Collections.sort(peopleList, Collections.reverseOrder(memoryComparator));
            up = false;
            down = true;
            upArrow.setVisibility(View.INVISIBLE);
            downArrow.setVisibility(View.VISIBLE);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    protected void filterByName(String query) {
        List<PeopleDataClass> filteredList = new ArrayList<>();

        for (PeopleDataClass disk : originalPeopleList) {
            // Приведение имени диска и введенного запрос к нижнему регистру и проверка, содержится ли запрос в имени диска
            if (disk.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(disk);
            }
        }

        // Обновление списка дисков с отфильтрованным списком
        peopleList.clear();
        peopleList.addAll(filteredList);

        // Обновление адаптера для отображения изменений
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}

