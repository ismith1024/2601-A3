package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */

/**
 * Message class:
 *
 * Represents the messages sent between the clients or client-server controls
 * Data: represents chat between two clients
 * Login: used by a client to log into the server
 * Logout: used by a client to request the connection to close
 * Update: used by the server to update the clients' user lists
 *
 * Also includes methods to stringify to and parse from JSON
 */

public class Message {

    //message types
    public static final String DATA = "data"; //represents a message to be read by a human user
    public static final String LOGIN = "login"; //represents a message from client to server identifying a new user
    public static final String LOGOUT = "logout"; //represents a message from client to server requesting disconnection
    public static final String UPDATE = "update"; //represents a message from server to all clients updating the connections list
    public static final String EVERYONE = "everyone";
    public static final String JOIN_NOTIFICATION = " has joined.";
    public static final String LEAVE_NOTIFICATION = " has left.";

    private Header header;
    private Body body;

    public static final String[] JSON_PIECES = {"{'message': { 'tx': '", "', 'rx': '", "', 'type' : '", "', 'body' : '", "'}}"};

    //don't intend to call
    private Message() {
    }

    /*Message constructor expected for the assignment 3 implementation
     * To: receiver
     * From: sender
     * Login: is this a login message
     * Logout: is this a logout message
     * Disconnect: is this a disconnect message
     * (Note: if these three are false, the message will default to data)
     * Body: message text
     */
    public Message(String to, String from, Boolean login, Boolean logout, Boolean disconnect, String bod){

        String type;
        if(login){
            type = Message.LOGIN;
        } else if(logout){
            type = Message.LOGOUT;
        } else if(disconnect){
            type = Message.UPDATE;
        } else type = Message.DATA;

        header = new Header(from, to, type);
        body = new Body(bod);

    }

    public String toJSON(){
        //later on we will parse the string using tab characters, so escape all tabs with spaces
        return (Message.JSON_PIECES[0] + header.getTx()
                + Message.JSON_PIECES[1] + header.getRx()
                + Message.JSON_PIECES[2] + header.getType()
                + Message.JSON_PIECES[3] + getBody()
                + Message.JSON_PIECES[4]).replace('\t', ' ');
    }

    public static Message parseFromJSON(String s){
        String replacedJSON = s.replace(Message.JSON_PIECES[0], "").replace(Message.JSON_PIECES[1], "\t").replace(Message.JSON_PIECES[2], "\t").replace(Message.JSON_PIECES[3], "\t").replace(Message.JSON_PIECES[4], "");
        String[] pieces = replacedJSON.split("\t",5);

        if(pieces.length == 4){
            return new Message(pieces[1], pieces[0], pieces[2].equals(Message.LOGIN), pieces[2].equals(Message.LOGOUT), pieces[2].equals(Message.UPDATE), pieces[3]);
        } else
            return null;
    }

    public String getTx(){return header.getTx();}
    public String getRx(){return header.getRx();}
    public String getType(){return header.getType();}
    public String getBody(){return "" + body.getData();}
    public void setBody(String s){body.setData(s);}

    public String toString(){
        return this.toJSON();
    }
}
