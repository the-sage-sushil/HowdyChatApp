package com.example.wadagang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wadagang.Model.Chat;
import com.example.wadagang.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Chat> mChat;
    private String imgURL;

    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT =  0;
    public static final int MSG_TYPE_RIGHT = 1;



    public MessageAdapter(Context context, List<Chat> mChat,String imgURL) {
        this.context = context;
        this.mChat = mChat;
        this.imgURL=imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);




        }else
            {

                View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);

                return new MessageAdapter.ViewHolder(view);

            }}



    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);
        holder.Show_message.setText(chat.getMessage());
        if (imgURL.equals("default")){
            holder.Profile_image.setImageResource(R.mipmap.ic_launcher);

        }
        else {
            Glide.with(context).load(imgURL).into(holder.Profile_image);
        }

        if (position == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            }else {
                holder.txt_seen.setText("Delivered");
            }

        }
        else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView Show_message;
        public ImageView Profile_image;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Show_message =itemView.findViewById(R.id.show_message);
            Profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen_status);
        }





    }
    @Override
    public int getItemViewType(int position) {
      fuser= FirebaseAuth.getInstance().getCurrentUser();
      if (mChat.get(position).getSender().equals(fuser.getUid())){
          return MSG_TYPE_RIGHT;

      }
      else {
          return MSG_TYPE_LEFT;
      }
    }


}
