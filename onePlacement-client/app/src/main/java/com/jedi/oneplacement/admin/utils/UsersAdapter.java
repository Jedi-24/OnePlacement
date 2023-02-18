package com.jedi.oneplacement.admin.utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.admin.fragments.UserListFragment;
import com.jedi.oneplacement.payloads.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private static final String TAG = "UsersAdapter";
    protected ArrayList<User> usersList = new ArrayList<>(); // <|--|>

    private static UserListFragment userListFragment;
    public UsersAdapter(UserListFragment userListFragment){
        this.userListFragment = userListFragment;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rv_layout, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.bindView(usersList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    // Inner VH Class:
    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        MaterialCardView userCard;
        CircleImageView img;
        TextView username, regNo, branch;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            init();
        }
        
        public void bindView(User user){
            username.setText(user.getName());
            regNo.setText(user.getRegNo());
               branch.setText(user.getBranch());
            // todo: fetch photo of the users:
//            img.setBitmap()

            // onClickListener on User's Card:
            userCard.setOnClickListener(v -> {
                Log.d(TAG, "bindView: clickedddd");

                // data sharing BW fragments in the same fragment manager using Fragment-Result API --> not working.
                // | other methods include interface in activity/shared View Model/Target Fragment (deprecated).
                String toJson = new Gson().toJson(user);
                userListFragment.loadUserFragment(toJson);
            });
        }

        private void init(){
            userCard = itemView.findViewById(R.id.user);
            img = itemView.findViewById(R.id.userprofile_img);
            username = itemView.findViewById(R.id.user_name);
            regNo = itemView.findViewById(R.id.user_reg_no);
            branch = itemView.findViewById(R.id.user_branch);
        }
    }
}
