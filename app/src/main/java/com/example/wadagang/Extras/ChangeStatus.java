package com.example.wadagang.Extras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wadagang.MainActivity;
import com.example.wadagang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChangeStatus extends AppCompatActivity {

    EditText changestatus;
    Button Savechanges;

    FirebaseUser firebaseuser;
    DatabaseReference myRef;
    private ProgressDialog mprogressdia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);


        changestatus=findViewById(R.id.Change_status_box);
        Savechanges=findViewById(R.id.Savebtn);

        firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        myRef= FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseuser.getUid());

        Savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressdia=new ProgressDialog(ChangeStatus.this);
                mprogressdia.setTitle("Changing Status");
                mprogressdia.setCanceledOnTouchOutside(false);
                mprogressdia.show();

                String status=changestatus.getText().toString();

                myRef.child("Profilestatus").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mprogressdia.dismiss();
                            Intent goback=new Intent(ChangeStatus.this, MainActivity.class);
                            startActivity(goback);
                            finish();

                        }
                        else {
                            Toast.makeText(ChangeStatus.this,"Some ERROR has occured",Toast.LENGTH_SHORT).show();
                        }


            }
        });

}});}}





