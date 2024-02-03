package com.example.humanresourcesdepart.SecondPages;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.humanresourcesdepart.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

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

    FirebaseStorage storage = FirebaseStorage.getInstance();
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

        search.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_URI);
        age.setOnClickListener(view1 -> filterByAge());

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Вызов метода фильтрации по имени человека с использованием введенного текста
                filterByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Вызов метода фильтрации по имени человека при изменении текста ввода
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
        PeopleDataClass person1 = new PeopleDataClass("Михаил", "Иванов", 30, "Разработчик", "misha@mail.ru", "https://firebasestorage.googleapis.com/v0/b/humanresourcesdepart-40127.appspot.com/o/1614344197_75-p-chelovek-na-svetlom-fone-83.jpg?alt=media&token=b07d12aa-d259-4cb3-bdf4-0e78c7c6679b");
        PeopleDataClass person2 = new PeopleDataClass("Сергей", "Михайлов", 25, "Дизайнер", "sergey@mail.ru", "https://firebasestorage.googleapis.com/v0/b/humanresourcesdepart-40127.appspot.com/o/secondPeople.jpg?alt=media&token=cb0496ca-2718-488e-9806-8d9bd2040285");

        if (dbHelper.isEmailUnique(person1.email)) {
            peopleRef.child(person1.email.replace(".", "_")).setValue(person1);
        }

        if (dbHelper.isEmailUnique(person2.email)) {
            peopleRef.child(person2.email.replace(".", "_")).setValue(person2);
        }

        peopleList.clear();
        peopleList.addAll(dbHelper.getAllPeople());
        dbHelper.deletePeopleWithEmailNull();
        adapter = new PeopleAdapter(requireContext(), R.layout.list_item_people, peopleList);
        peopleListView.setAdapter(adapter);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    protected void filterByAge() {
        Comparator<PeopleDataClass> ageComparator = new Comparator<PeopleDataClass>() {
            @Override
            public int compare(PeopleDataClass people1, PeopleDataClass people2) {
                int age1 = people1.getAge();
                int age2 = people2.getAge();
                return Integer.compare(age1, age2);
            }
        };

        if (down) {
            Collections.sort(peopleList, ageComparator);
            up = true;
            down = false;
            upArrow.setVisibility(View.VISIBLE);
            downArrow.setVisibility(View.INVISIBLE);
        } else {
            Collections.sort(peopleList, Collections.reverseOrder(ageComparator));
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

        for (PeopleDataClass people : originalPeopleList) {
            // Приведение имени диска и введенного запрос к нижнему регистру и проверка, содержится ли запрос в имени диска
            if (people.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(people);
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

