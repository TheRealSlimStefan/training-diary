package com.example.trainingdiary.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.trainingdiary.MainActivity;
import com.example.trainingdiary.R;
import com.example.trainingdiary.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtTxtName, edtTxtSurname, edtTxtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(this);

        edtTxtName = (EditText) findViewById(R.id.edtTxtName);
        edtTxtSurname = (EditText)  findViewById(R.id.edtTxtSurname);
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtEmail);
        edtPassword = (EditText) findViewById(R.id.edtTxtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtTxtConfirmPassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
//                startActivity(new Intent(this, MainActivity.class));
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String name = edtTxtName.getText().toString().trim();
        String surname = edtTxtSurname.getText().toString().trim();
        String email = edtTxtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if(name.isEmpty()){
            edtTxtName.setError("Pole imie nie może być puste!");
            edtTxtName.requestFocus();
            return;
        }

        if(surname.isEmpty()){
            edtTxtSurname.setError("Pole nazwisko nie może być puste!");
            edtTxtSurname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edtTxtEmail.setError("Pole e-mail nie może być puste!");
            edtTxtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtTxtEmail.setError("Podany e-mail jest nieprawidłowy!");
            edtTxtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edtPassword.setError("Pole hasło nie może być puste!");
            edtPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            edtPassword.setError("Hasło musi zawierać minimum 6 znaków!");
            edtPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            edtConfirmPassword.setError("Pole potwierdź hasło nie może być puste!");
            edtConfirmPassword.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            edtConfirmPassword.setError("Podane hasła się różnią!");
            edtConfirmPassword.requestFocus();
            return;
        }

        //TODO: ProgressBar dodać tutaj i w layout

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, surname, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "Dodano użytkownika", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        }else{
                                            Toast.makeText(RegistrationActivity.this, "Użytkownik nie został dodany", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Rejestracja się nie powiodła", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}