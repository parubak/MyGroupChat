package com.example.mychat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class ChatFragment extends Fragment {


    FirebaseAuth mAuth;
    FirebaseDatabase fDB;
    DatabaseReference dbRef;

    ArrayList<Message> messages;
    ChatAdapter chatAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);


        TextInputEditText text = view.findViewById(R.id.msgInput);
        ImageView sendBtn = view.findViewById(R.id.sendBtn);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();
        dbRef = fDB.getReference().child("chats");
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(view.getContext(), messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(chatAdapter);

        String senderId = mAuth.getUid();


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(v -> {
            String msg = text.getText().toString();
            Message message = new Message(senderId, msg);
            message.setCreatedAt(new Date().getTime());
            text.setText("");

            dbRef.push()
                    .setValue(message)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

        });
        return view;
    }
}