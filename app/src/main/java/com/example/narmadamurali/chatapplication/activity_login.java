package com.example.narmadamurali.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// user:abc@gmail.com pass:123456
//user:xyz@gmail.com pass:123456

public class activity_login extends AppCompatActivity implements View.OnClickListener{
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),camera.class));
        }

        buttonSignIn=(Button)findViewById(R.id.buttonSignIn);

        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);

        textViewSignUp=(TextView)findViewById(R.id.textViewSignUp);

        buttonSignIn.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


    }

    private void userLogin()
    {
        String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter the email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)&& password.length()<6)
        {
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("User redirected...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),camera.class));
                        }
                        else
                        {
                            Toast.makeText(activity_login.this, "please check ur userid and password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    @Override
    public void onClick(View view) {
        if (view==buttonSignIn)
        {
            userLogin();
        }
        if(view==textViewSignUp)
        {
            finish();
            startActivity(new Intent(this,activity_register.class));
        }
    }
}
