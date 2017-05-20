package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.ConnectException;

/* LoginActivity class
 * Allows the user to configure the server connection and login
 * by selecting a user name.
 * Launches the ChatActivity when this is done.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText ip1EditText;
    private EditText ip2EditText;
    private EditText ip3EditText;
    private EditText ip4EditText;
    private EditText portEditText;
    private EditText userIDEditText;
    private Button loginButton;


    //connection is shared with the chatactivity class
    public static ClientConnection mClientConnection;

    private int ip1;
    private int ip2;
    private int ip3;
    private int ip4;
    private String ip;
    private int port;
    private static String userID;

    private final String TAG ="LoginActivityConnection";

    public static String getID(){return userID;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ip1EditText = (EditText) findViewById(R.id.IPeditText1);
        ip2EditText = (EditText) findViewById(R.id.IPeditText2);
        ip3EditText = (EditText) findViewById(R.id.IPeditText3);
        ip4EditText = (EditText) findViewById(R.id.IPeditText4);
        portEditText = (EditText) findViewById(R.id.portEditText);
        userIDEditText = (EditText) findViewById(R.id.userIDeditText);
        loginButton = (Button) findViewById(R.id.signInButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ip1 = Integer.parseInt(ip1EditText.getText().toString());
                    ip2 = Integer.parseInt(ip2EditText.getText().toString());
                    ip3 = Integer.parseInt(ip3EditText.getText().toString());
                    ip4 = Integer.parseInt(ip4EditText.getText().toString());
                    port = Integer.parseInt(portEditText.getText().toString());
                } catch(NumberFormatException e){
                    //do nothing, just fail
                }

                ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
                userID = userIDEditText.getText().toString();

                // connect to the server
                new ConnectionTask().execute("");

                Message m = new Message("Admin", userID, true, false, false, "log me in");
                Intent chatIntent = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(chatIntent);

            }
        });

    }

    //ConnectionTask class which performs the asynchronous receive message task
    private class ConnectionTask extends AsyncTask<String, String, ClientConnection> {

        @Override
        protected ClientConnection doInBackground(String... message) {

            mClientConnection = new ClientConnection(new ClientConnection.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                }
            }, ip, port, userID);

            try {
                mClientConnection.run();
            } catch (ConnectException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        //Receives the messages - stringifies, then sends to the reactToMessage method
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Message msg = Message.parseFromJSON(values[0]);
            Log.i(TAG, "Message : " + msg.getBody());
            ChatActivity.getInstance().reactToMessage(msg);
        }

    }
}
