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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registeractivity extends AppCompatActivity {

    EditText userET,passET, EmailET;
    Button registerBtn;
    TextView goback;

    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        userET=findViewById(R.id.Username);
        passET=findViewById(R.id.Password);
        EmailET=findViewById(R.id.Email);
        registerBtn=findViewById(R.id.buttonRegister);
        goback=findViewById(R.id.go_back);

        auth=FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text=userET.getText().toString();
                String pass_text=passET.getText().toString();
                String email_text=EmailET.getText().toString();

                if(TextUtils.isEmpty(username_text)||TextUtils.isEmpty(email_text)||TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Registeractivity.this, "ALL FIELDS ARE NEssasery", Toast.LENGTH_SHORT).show();
                }else {
                    RegisterNow(username_text,email_text,pass_text);
                }
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Registeractivity.this,Login_Activity.class);
                startActivity(i);
                finish();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });



    }



    private void RegisterNow(final String username,String email,String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser firebaseuser =auth.getCurrentUser();
                    String userid=firebaseuser.getUid();

                    myRef= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);


                    HashMap<String ,String >hashMap=new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");
                    hashMap.put("status","offline");
                    hashMap.put("Profilestatus","I'm Doing great baby!,How you Doin'");




                    myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent i=new Intent(Registeractivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(Registeractivity.this,"INVALID EMAIL OR PASSWORD",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });

    }
}