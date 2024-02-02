package com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.humanresourcesdepartment.R;

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

        if (person != null) {
            nameTextView.setText(person.name + " " + person.surname);
            ageTextView.setText(person.age + " " + position);
            emailTextView.setText(person.email);
        }

        return view;
    }
}