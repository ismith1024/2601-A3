package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnection {

    private String serverMessage;
    private final String TAG ="ClientConnection";

    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private String serverIP;
    private int portNumber;
    private String userID;

    private PrintWriter mOut;
    private BufferedReader mIn;

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    /**
     *  Constructor of the class.
     *  OnMessagedReceived is the interface which listens for the messages received from server
     */
    public ClientConnection(OnMessageReceived listener, String c_serverIP, int c_portNumber, String c_userID) {
        mMessageListener = listener;
        serverIP = c_serverIP.replace("/", "");
        Log.e(TAG, "IP: " + serverIP);
        portNumber = c_portNumber;
        userID = c_userID;
    }

    /**
     * Sends the message entered by client to the server
     * String message is the Message object that has been stringified
     */
    public void sendMessage(String message){

        Log.e(TAG, "Message being sent: " + message);
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }
    }

    //method to stop the connection
    public void stopClient(){
        mRun = false;
    }

    //Connection's blocking loop which will listen as long as mRun is true
    public void run() throws ConnectException {

        mRun = true;

        try {
            InetAddress serverAddr = InetAddress.getByName(serverIP);
            Socket socket = new Socket(serverAddr, portNumber);
            Log.e(TAG, "C: Connecting... IP: " + serverAddr.toString() + "   socket: " + socket.toString());

            try {

                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Identify self to server
                Message id = new Message("admin", userID, true, false, false, "Hello");
                Log.e(TAG, "Sending message: " + id.toJSON());
                sendMessage(id.toJSON());

                while (mRun) {
                    serverMessage = mIn.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        mMessageListener.messageReceived(serverMessage);
                        if(serverMessage.equalsIgnoreCase("Disconnected")){
                            mRun = false;
                        }
                    }
                    serverMessage = null;

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }

        } catch (ConnectException e) {
            e.printStackTrace();
            throw e;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
