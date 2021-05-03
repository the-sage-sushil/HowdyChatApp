package com.example.wadagang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wadagang.Calling.CallingFirst;
import com.example.wadagang.Calling.CallingSecond;
import com.example.wadagang.Extras.ChangeStatus;
import com.example.wadagang.Fragments.ChatFragment;
import com.example.wadagang.Fragments.ProfileFragment;
import com.example.wadagang.Fragments.UserFragment;
import com.example.wadagang.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    FirebaseUser firebaseuser;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseuser= FirebaseAuth.getInstance().getCurrentUser();

        //OneSignal is for Notification management.

        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference("MyUsers").child(FirebaseAuth.getInstance().getUid()).child("UserKey").setValue(userId);

            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        OneSignal.enableSound(true);
        OneSignal.enableVibrate(true);


        myRef= FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseuser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        TabLayout tablayout=findViewById(R.id.tablayout);
        ViewPager viewpager=findViewById(R.id.view_pager);

        ViewpagerAdapter viewpagerAdapter=new ViewpagerAdapter(getSupportFragmentManager());

        viewpagerAdapter.addFragments(new ChatFragment(),"Chats");
        viewpagerAdapter.addFragments(new UserFragment(),"Users");
        viewpagerAdapter.addFragments(new ProfileFragment(),"Profile");


        viewpager.setAdapter(viewpagerAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.logout:
               OneSignal.setSubscription(false);
               FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseuser.getUid()).child("key").setValue(null);
               FirebaseAuth.getInstance().signOut();

               startActivity(new Intent(MainActivity.this,Login_Activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

               return true;
           case R.id.Profile_Settings:
               Intent j=new Intent(MainActivity.this, ChangeStatus.class);
               startActivity(j);

           case R.id.calling:
               String userid=firebaseuser.getUid().toString();
                Intent i=new Intent(MainActivity.this, CallingSecond.class);
                i.putExtra("Userid",userid);
                startActivity(i);







       }
       return false;
    }

    // class Viewpager ADAPTER

    class ViewpagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewpagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles =new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);

        }


        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragments(Fragment fragment, String title){
           fragments.add(fragment);
           titles.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return  titles.get(position);

        }
    }
    private void CheckStatus(String status){
        myRef=FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseuser.getUid());


        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("status",status);

        myRef.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckStatus("Offline");

    }
}