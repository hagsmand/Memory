package ch.zhaw.facerecognition.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationlibary.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ch.zhaw.facerecognition.R;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextInputLayout  inputPassword;
    private TextInputLayout inputEmail;
    private TextView toRegister,rememberMe;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    AnimationDrawable animationDrawable;
    ConstraintLayout relativeLayout;

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        firebaseAuth = FirebaseAuth.getInstance();
       /* if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Register.class));
            finish();
        }*/

        setContentView(com.example.locationlibary.R.layout.activity_login);

        inputEmail = (TextInputLayout) findViewById(com.example.locationlibary.R.id.email);
        inputPassword = (TextInputLayout) findViewById(com.example.locationlibary.R.id.password);
        progressBar = (ProgressBar) findViewById(com.example.locationlibary.R.id.progressBar);
        toRegister = (TextView) findViewById(com.example.locationlibary.R.id.btn_signup);
        btn_login = (Button) findViewById(com.example.locationlibary.R.id.btn_login);
        rememberMe = (TextView)findViewById(com.example.locationlibary.R.id.textRemember);

        //
        // inputPassword.setError("Min 6 ตัว");
        relativeLayout = (ConstraintLayout) findViewById(R.id.homeLayout);
        animationDrawable =(AnimationDrawable)relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        if (firebaseAuth.getCurrentUser() != null) {
            rememberMe.setText("Login as " + firebaseAuth.getCurrentUser().getEmail());
            rememberMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    startActivity(intent);
                }
            });
        }

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getEditText().getText().toString(); // รับค่า email ให้ถูก
                 final String password = inputPassword.getEditText().getText().toString();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Input both email and password!" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {// there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(com.example.locationlibary.R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(com.example.locationlibary.R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this,Home.class);
                            Intent k = new Intent(LoginActivity.this,MapsActivity.class);
                            k.putExtra("email",email.replace(".","?"));
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

    }
}
