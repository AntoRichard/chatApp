package com.example.narmadamurali.chatapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Message> adapter;
    LinearLayout activity_main;
    private FirebaseAuth firebaseAuth;

    private Button buttonSend;

    EditText editTextMessage;

    FirebaseDatabase firebaseDatabase;

    private  long messageTime = new Date().getTime();



    ListView listOfMessage;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, activity_login.class));
            Toast.makeText(this, "you have signed out", Toast.LENGTH_SHORT).show();
            finish();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==SIGN_IN_REQUEST_CODE && requestCode==RESULT_OK)
        {
            Toast.makeText(this, "Successfully signed in.welcome", Toast.LENGTH_LONG).show();
            displayChatMessage();
        }
        else
        {
            Toast.makeText(this, "failed to log in. try again later", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          Log.d("hi","hi");

        firebaseAuth = FirebaseAuth.getInstance();

        activity_main = (LinearLayout) findViewById(R.id.activity_main);

        buttonSend=(Button)findViewById(R.id.buttonSend);

        listOfMessage=(ListView)findViewById(R.id.listOfMessage);

        final EditText editTextMessage =(EditText)findViewById(R.id.editTextMessage);

        firebaseDatabase=FirebaseDatabase.getInstance();

        //check sign in

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, activity_login.class));
        } else {
            Toast.makeText(this, "welcome", Toast.LENGTH_SHORT).show();
            displayChatMessage();
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=editTextMessage.getText().toString();
                firebaseDatabase.getReference().push().setValue(new Message(msg,firebaseAuth.getCurrentUser().getEmail()));
                editTextMessage.setText("");

            }
        });
    }


        private void displayChatMessage() {
          /*  ListView listOfMessage=(ListView)findViewById(R.id.listOfMessage);
        Query query = firebaseDatabase.getReference().child("chats");

            FirebaseListOptions<Message> options =
                    new FirebaseListOptions.Builder<Message>()
                            .setQuery(query,Message.class)
                            .setLayout(android.R.layout.simple_list_item_1)
                            .build();
            adapter = new FirebaseListAdapter<Message>(options)


            //FirebaseListAdapter(Query mRef, Class<T> mModelClass, int mLayout, Activity activity)
          // FirebaseListAdapter <Message>adapter=new FirebaseListAdapter<Message>(this,Message.class,android.R.layout.simple_list_item_1,firebaseDatabase.getReference())
            {

                @Override
                protected void populateView(View v, Message model, int position) {
                    TextView messageText,messageUser,messageTime;

                    messageUser=(TextView)v.findViewById(R.id.messageUser);

                    messageText=(TextView)v.findViewById(R.id.messageText);

                    messageTime=(TextView)v.findViewById(R.id.messageTime);

                   // messageTime.setText("hi");
                   // messageText.setText("hi");


                    messageUser.setText(model.getMessageUser());
                    messageText.setText(model.getMessageText());
                    //messageTime.setText( DateFormat.format("dd-MM-yyy(HH:mm:ss)",model.getMessageTime()));

                }
            };
            listOfMessage.setAdapter(adapter);
            */

            final List<Message>messages=new LinkedList<>();
            final ArrayAdapter<Message>adapter=new ArrayAdapter<Message>( this,R.layout.list_item,messages){
                public View getView(int position, View view, ViewGroup parent)
                {
                    if(view==null)
                    {
                        view=getLayoutInflater().inflate(R.layout.list_item,parent,false);
                    }
                    Message message=messages.get(position);
                    ((TextView)view.findViewById(R.id.messageUser)).setText(message.getMessageUser());
                    ((TextView)view.findViewById(R.id.messageText)).setText(message.getMessageText());
                   // ((TextView)view.findViewById(R.id.messageTime)).setText((int) message.getMessageTime());




                    return view;


                }

            };
            listOfMessage.setAdapter(adapter);

            firebaseDatabase.getReference().addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message msg=dataSnapshot.getValue(Message.class);
                    messages.add(msg);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });







        }
    }

