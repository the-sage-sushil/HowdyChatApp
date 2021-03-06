package com.example.wadagang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.IMediaControllerCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wadagang.Extras.Displayshow;
import com.example.wadagang.MessageActivity;
import com.example.wadagang.Model.Users;
import com.example.wadagang.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> mUsers;
    private boolean isChat;


    public UserAdapter(Context context, List<Users> mUsers,boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

      return new UserAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Users users=mUsers.get(position);
        holder.username.setText(users.getUsername());
        holder.Profilestatus.setText(users.getProfilestatus());


        if (users.getImageURL().equals("default"))
        {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        else{
         Glide.with(context).load(users.getImageURL()).into(holder.imageView);
        }

        // Status Check
        if (isChat){
            if(users.getStatus().equals("online")){
                holder.imageViewON.setVisibility(View.VISIBLE);
                holder.imageViewOFF.setVisibility(View.GONE);
            }else{
                holder.imageViewON.setVisibility(View.GONE);
                holder.imageViewOFF.setVisibility(View.VISIBLE);
            }

        }
        else {
            holder.imageViewON.setVisibility(View.GONE);
            holder.imageViewOFF.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, MessageActivity.class);
                i.putExtra("userid",users.getId());
                context.startActivity(i);




            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView imageView;
        public ImageView imageViewON;
        public ImageView imageViewOFF;
        public TextView Profilestatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username =itemView.findViewById(R.id.username30);
            imageView=itemView.findViewById(R.id.image30);
            imageViewON=itemView.findViewById(R.id.statusimageON);
            imageViewOFF=itemView.findViewById(R.id.statusimageOFF);
            Profilestatus=itemView.findViewById(R.id.chatORstatus);


        }





    }
}
