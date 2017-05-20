package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.DialogInterface;
import java.util.ArrayList;

/**
 * ChatActivity Class
 *
 * This Activity provides the UI for chat use
 * Contains:
 *   - a TextView to display chat from other users and messages from admin
 *   - a spinner to select other logged in users for chat
 *   - four buttons to chat, clear the chat view, make announcement to everyone, or logout
 *
 *  Message text for chat or announcement is entered by means of a dialog with EditText
 *
 */
public class ChatActivity extends Activity {

    private static final String TAG = "ChatActivity";

    private static ChatActivity instance;

    private TextView activeChatTextView;
    private Spinner userSelector;
    private ImageButton sendMessageButton;
    private ImageButton clearChatButton;
    private ImageButton messageEveryoneButton;
    private ImageButton signoutButton;
    private String result;
    private String me;

    private ArrayList<String>activeUsers;
    private ArrayAdapter<String> spinAdapter;

    public static ChatActivity getInstance(){return instance;}

    private void refreshSpinner(String names){
        //parse out names from String -- they will be tab delimited
        String[] pieces = names.split("\\|");
        activeUsers = new ArrayList<String>();
        activeUsers.add(Message.EVERYONE);

        for(String s: pieces){
            if(!s.equals("") && !s.equals(me)) activeUsers.add(s);
        }

        spinAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textviews, activeUsers);
        userSelector.setAdapter(spinAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        instance = this;

        activeChatTextView = (TextView) findViewById(R.id.activeChatTextView);
        userSelector = (Spinner) findViewById(R.id.userSelector);
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageImageButton);
        clearChatButton = (ImageButton) findViewById(R.id.refreshImageButton);
        messageEveryoneButton = (ImageButton) findViewById(R.id.messageAllImageButton);
        signoutButton = (ImageButton) findViewById(R.id.exitImageButton);
        me = LoginActivity.getID();

         activeChatTextView.setMovementMethod(new ScrollingMovementMethod());

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(userSelector.getSelectedItem().toString());
            }
        });

        messageEveryoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(Message.EVERYONE);
            }
        });

        clearChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChatTextView.setText("");
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message signoutMessage = new Message("admin", me, false, true, false, "");
                LoginActivity.mClientConnection.sendMessage(signoutMessage.toJSON());
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshSpinner("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void reactToMessage(Message msg){

        if(msg.getType().equals(Message.UPDATE)){
            //message from the server to refresh the users list
            refreshSpinner(msg.getBody());

        } else {
            String mAllText = activeChatTextView.getText().toString();
            activeChatTextView.setText("");
            if (!mAllText.isEmpty())
                activeChatTextView.setText(mAllText + "\n" + msg.getTx() + ": " + msg.getBody());
            else
                activeChatTextView.setText(msg.getTx() + ": " + msg.getBody());
        }
    }

    //See references in readme file
    public void createDialog(String a_sendto) {

        final String sendto = a_sendto;

        LayoutInflater li = LayoutInflater.from(ChatActivity.this);
        View promptsView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sendmessage),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //'|' is escaped as it is reserved for use as a data delimiter
                                result = userInput.getText().toString().replace("|", "/");
                                Message myMessage = new Message(sendto, me, false, false, false, result);
                                LoginActivity.mClientConnection.sendMessage(myMessage.toJSON());

                                String mAllText = activeChatTextView.getText().toString();
                                activeChatTextView.setText("");
                                if (!mAllText.isEmpty())
                                    activeChatTextView.setText(mAllText + "\n" + me + ": " + result);
                                else
                                    activeChatTextView.setText(me + ": " + result);
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

}
