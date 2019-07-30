package com.module.android.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList){
        this.context=context;
        this.userList=userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_user,viewGroup,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {

        User user=userList.get(position);
        userViewHolder.textviewemail.setText(user.email);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView textviewemail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewemail=itemView.findViewById(R.id.textViewEmail);
        }
    }
}
