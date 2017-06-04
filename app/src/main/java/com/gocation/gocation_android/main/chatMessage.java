package com.gocation.gocation_android.main;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.gocation.gocation_android.R;
import java.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Billy on 29/05/17.
 */

public class chatMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);


        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new chatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });

    }

        private String messageText;
        private String messageUser;
        private long messageTime;

        public chatMessage(String messageText, String messageUser) {
            this.messageText = messageText;
            this.messageUser = messageUser;

            // Initialize to current time
            messageTime = new Date().getTime();
        }

        public chatMessage(){

        }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public String getMessageUser() {
            return messageUser;
        }

        public void setMessageUser(String messageUser) {
            this.messageUser = messageUser;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }
    }



