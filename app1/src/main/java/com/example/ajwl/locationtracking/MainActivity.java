package com.example.ajwl.locationtracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button register;
    private ProgressBar progressBar1;
    private EditText email, pass;
    private TextView login, rememeberMe;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar2);
        register = (Button) findViewById(R.id.registBtt);
        email = (EditText) findViewById(R.id.userEmail);
        pass = (EditText) findViewById(R.id.userPass);
        login = (TextView) findViewById(R.id.textLogin);
        rememeberMe = (TextView) findViewById(R.id.textRemember);

        if (firebaseAuth.getCurrentUser() != null) {
            rememeberMe.setText("Login as " + firebaseAuth.getCurrentUser().getEmail());
            rememeberMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                    startActivity(intent);
                }
            });
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUse = email.getText().toString();
                String password = pass.getText().toString();
                if (TextUtils.isEmpty(emailUse)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar1.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(emailUse, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        progressBar1.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference idRef, name, latitudeRef, longtitudeRef;
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            MapsActivity mapsActivity = new MapsActivity();
                            idRef = ref.child("ID_all_user");
                            name = idRef.child(emailUse.replace(".", "?"));

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
