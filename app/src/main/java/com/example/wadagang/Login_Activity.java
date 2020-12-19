package com.example.wadagang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin,passETLogin;
    Button LoginBtn;
     TextView RegisterBtn;

    FirebaseAuth auth;
    FirebaseUser firebaseuser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseuser=FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);



        userETLogin=findViewById(R.id.usernamey);
        passETLogin=findViewById(R.id.password);
        LoginBtn=findViewById(R.id.loginButton);
        RegisterBtn=findViewById(R.id.Register);

        auth=FirebaseAuth.getInstance();
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();


        if(firebaseuser !=null){
            Intent i=new Intent(Login_Activity.this,MainActivity.class);
            startActivity(i);
            finish();
        }




        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text=userETLogin.getText().toString();
                String pass_text=passETLogin.getText().toString();


                if(TextUtils.isEmpty(email_text)||TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Login_Activity.this, "Please fill all the Parameter", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent i=new Intent(Login_Activity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(Login_Activity.this, "LOGIN FAILED!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent R=new Intent(Login_Activity.this,Registeractivity.class);
                R.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(R);
                finish();

            }
        });


    }
}