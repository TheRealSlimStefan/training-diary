package com.example.trainingdiary.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainingdiary.R;
import com.example.trainingdiary.objectsClasses.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtTxtName, edtTxtSurname, edtTxtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", edtTxtName.getText().toString());
        outState.putString("surname", edtTxtSurname.getText().toString());
        outState.putString("email", edtTxtEmail.getText().toString());
        outState.putString("password", edtPassword.getText().toString());
        outState.putString("cpassword", edtConfirmPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        edtTxtName.setText(savedInstanceState.getString("name"));
        edtTxtSurname.setText(savedInstanceState.getString("surname"));
        edtTxtEmail.setText(savedInstanceState.getString("email"));
        edtPassword.setText(savedInstanceState.getString("password"));
        edtConfirmPassword.setText(savedInstanceState.getString("cpassword"));
    }

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
            edtTxtName.setError("Pole imie nie mo??e by?? puste!");
            edtTxtName.requestFocus();
            return;
        }

        if(surname.isEmpty()){
            edtTxtSurname.setError("Pole nazwisko nie mo??e by?? puste!");
            edtTxtSurname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edtTxtEmail.setError("Pole e-mail nie mo??e by?? puste!");
            edtTxtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtTxtEmail.setError("Podany e-mail jest nieprawid??owy!");
            edtTxtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edtPassword.setError("Pole has??o nie mo??e by?? puste!");
            edtPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            edtPassword.setError("Has??o musi zawiera?? minimum 6 znak??w!");
            edtPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            edtConfirmPassword.setError("Pole potwierd?? has??o nie mo??e by?? puste!");
            edtConfirmPassword.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            edtConfirmPassword.setError("Podane has??a si?? r????ni??!");
            edtConfirmPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),name, surname, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "Dodano u??ytkownika", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        RegistrationActivity.this.finish();
                                    }else{
                                        Toast.makeText(RegistrationActivity.this, "U??ytkownik nie zosta?? dodany", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Rejestracja si?? nie powiod??a", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}