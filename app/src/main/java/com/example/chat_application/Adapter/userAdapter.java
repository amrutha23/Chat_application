package com.example.chat_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_application.Message;
import com.example.chat_application.R;
import com.example.chat_application.model.Chat;
import com.example.chat_application.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder>{
    Context mcontext;
    List<User> musers;
    boolean ischat;
    String the_last_message;

    public userAdapter(Context mcontext, List<User> muser, boolean ischat) {
        this.mcontext = mcontext;
        this.musers = musers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new userAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = musers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }
        else{
            Glide.with(mcontext).load(user.getImageURL()).into(holder.profile_image);
        }
        if(ischat){
            last_message(user.getId(),holder.last_message);
        }
        else{
            holder.last_message.setVisibility(View.GONE);
        }
        if(ischat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, Message.class);
                intent.putExtra("userid",user.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    private void the_last_message(String id, TextView last_message) {
    }


    @Override
    public int getItemCount() {
        return musers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_message;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_off= itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);
            last_message = itemView.findViewById(R.id.last_msg);

        }
    }

    private void last_message(final String user_id, final TextView last_message){
        the_last_message="default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(user_id)) || (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(user_id))){
                        the_last_message = chat.getMessage();
                    }
                }
                switch(the_last_message){
                    case "default": last_message.setText("No message");
                                    break;
                    default: last_message.setText(the_last_message);
                             break;
                }
                the_last_message="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
