package com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.humanresourcesdepart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PeopleAdapter extends ArrayAdapter<PeopleDataClass> {
    private Context context;
    private int resource;

    public PeopleAdapter(@NonNull Context context, int resource, @NonNull List<PeopleDataClass> people) {
        super(context, resource, people);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }
        PeopleDataClass person = getItem(position);

        TextView nameTextView = view.findViewById(R.id.NameAndSurname);
        TextView ageTextView = view.findViewById(R.id.PostAndAge);
        TextView emailTextView = view.findViewById(R.id.Email);
        ImageView profileImageView = view.findViewById(R.id.imageViewPeople);

        if (person != null && person.surname != null) {
            nameTextView.setText(person.name + " " + person.surname);
            ageTextView.setText("Возраст - " + person.age + " лет\nДолжность - " + person.position);
            emailTextView.setText(person.email);

            if (person.getImagePath() != null && !person.getImagePath().isEmpty()) {
                // библиотеки для загрузки изображений Picasso
                Picasso.get().load(person.getImagePath()).into(profileImageView);
            } else {
                // Если у человека нет изображения, установливается заглушка
                profileImageView.setImageResource(R.drawable.default_profile_image);
            }
        }

        return view;
    }
}