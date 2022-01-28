package com.example.trainingdiary.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingdiary.MainActivity;
import com.example.trainingdiary.R;
import com.example.trainingdiary.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button register, login;
    private EditText edtTxtEmail, edtTxtPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);

        edtTxtEmail = (EditText) findViewById(R.id.edtTxtEmailLogin);
        edtTxtPassword = (EditText) findViewById(R.id.edtTxtPasswordLogin);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegister:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        String email = edtTxtEmail.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();

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
            edtTxtPassword.setError("Pole hasło nie może być puste!");
            edtTxtPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            edtTxtPassword.setError("Hasło musi zawierać minimum 6 znaków!");
            edtTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Logowanie nie powiodło sie", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}


//TODO przypominanie hasła