package com.example.wadagang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.wadagang.Adapter.MessageAdapter;
import com.example.wadagang.Model.Chat;
import com.example.wadagang.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private RequestQueue mRequestQue;

    private String checker="",myUrl="";
    private Uri fileUri;
    private StorageTask uploadTask;



    TextView username;
    ImageView imageView;
    ImageButton attachbttn;

    RecyclerView recyclerViewy;
    EditText msg_editText;
    ImageView sendBtn;



    FirebaseUser fuser;
    DatabaseReference reference,UserRefrence;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;
    String userid,UserKey,SenderName;

    ValueEventListener seenListener;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        imageView=findViewById(R.id.imageview_profile);
        username=findViewById(R.id.usernamey);
        sendBtn=findViewById(R.id.btn_send);
        msg_editText=findViewById(R.id.text_send);

        attachbttn=findViewById(R.id.Attachbttn);






        recyclerView=findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        intent=getIntent();
        userid=intent.getStringExtra("userid");


        fuser= FirebaseAuth.getInstance().getCurrentUser();

        UserRefrence= FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());
        UserRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
                SenderName=user.getUsername();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());
                UserKey=user.getUserKey();





                if (user.getImageURL().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);

                }else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(imageView);

                }
                readMessages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String key=FirebaseDatabase.getInstance().getReference("MyUsers").child(userid).child("key").getKey();
                String msg=msg_editText.getText().toString();
                String heading=fuser.getDisplayName().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                    SendNotification(msg,SenderName,UserKey);
                }
                else {
                    Toast.makeText(MessageActivity.this, "Please type to send!", Toast.LENGTH_SHORT).show();
                }
                msg_editText.setText("");


                }



        });

        SeenMessage(userid);

    }







    private void SeenMessage(final String userid){

        reference=FirebaseDatabase.getInstance().getReference("Chats");

        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();


        HashMap<String ,Object>hashMap =new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid()).child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        





    }



    private void readMessages(final String myid, final String userid, final String imageurl){
        mchat=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    if (myid.equals(chat.getReceiver()) && chat.getSender().equals(userid) || userid.equals(chat.getReceiver()) && chat.getSender().equals(myid)){

                        mchat.add(chat);

                    }
                    messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void CheckStatus(String status){
        reference=FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.getUid());


        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        CheckStatus("Offline");

    }
     public void SendNotification(String message, String heading, String notificationKey){

        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" + (message) + "'},"+
                            "'include_player_ids':['" + notificationKey + "']," +
                            "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification(notificationContent, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}