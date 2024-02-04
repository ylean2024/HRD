package com.example.humanresourcesdepart.SecondPages.AdminPanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.humanresourcesdepart.R;
import com.example.humanresourcesdepart.SecondPages.PeopleHelpClasses.PeopleDataClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class AddPeopleFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private StorageReference storageReference;
    private DatabaseReference peopleDatabaseReference;

    private EditText nameEditText, surnameEditText, emailEditText, ageEditText, positionEditText;
    private Button addImageButton, saveDataButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_people, container, false);

        // Инициализация Firebase Storage и Realtime Database
        storageReference = FirebaseStorage.getInstance().getReference("people_images");
        peopleDatabaseReference = FirebaseDatabase.getInstance().getReference("user_data");

        // Инициализация всех EditText
        nameEditText = view.findViewById(R.id.name_add);
        surnameEditText = view.findViewById(R.id.surname_add);
        emailEditText = view.findViewById(R.id.email_add);
        ageEditText = view.findViewById(R.id.age_add);
        positionEditText = view.findViewById(R.id.post_add);

        // Инициализация кнопок
        addImageButton = view.findViewById(R.id.add_image);
        addImageButton.setOnClickListener(v -> openFileChooser());

        saveDataButton = view.findViewById(R.id.save_data_button);
        saveDataButton.setOnClickListener(v -> saveDataToFirebase());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void openFileChooser() {
        // Реализация выбора изображения
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

    private void saveDataToFirebase() {
        // Проверка на заполненность обязательных полей
        if (nameEditText.getText().toString().isEmpty()
                || surnameEditText.getText().toString().isEmpty()
                || emailEditText.getText().toString().isEmpty()
                || ageEditText.getText().toString().isEmpty()
                || positionEditText.getText().toString().isEmpty()
                || imageUri == null) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageEditText.getText().toString());

        // Создание уникального имени файла для изображения
        String fileName = FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + System.currentTimeMillis() + ".jpg";

        // Получение ссылки на место в Storage, где будет хранится файл
        StorageReference fileReference = storageReference.child(fileName);
        // Загрузка изображения в Storage
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Получение ссылки на загруженное изображение
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Создание объекта DiskDataClass с введенными данными и ссылкой на изображение
                        PeopleDataClass peopleData = new PeopleDataClass(
                                nameEditText.getText().toString(),
                                surnameEditText.getText().toString(),
                                age,
                                emailEditText.getText().toString(),
                                positionEditText.getText().toString(),
                                uri.toString()
                        );

                        // Сохранение данныых в Realtime Database
                        String peopleId = peopleDatabaseReference.push().getKey();
                        peopleDatabaseReference.child(Objects.requireNonNull(peopleId)).setValue(peopleData)
                                .addOnSuccessListener(aVoid -> {
                                    // Успешно сохранено
                                    Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                                    // Очистка поля ввода
                                    clearInputFields();
                                })
                                .addOnFailureListener(e -> {
                                    // Ошибка при сохранении
                                    Toast.makeText(requireContext(), "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    // Ошибка при загрузке изображения
                    Toast.makeText(requireContext(), "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearInputFields() {
        nameEditText.setText("");
        surnameEditText.setText("");
        emailEditText.setText("");
        ageEditText.setText("");
        positionEditText.setText("");
        imageUri = null;
    }
}