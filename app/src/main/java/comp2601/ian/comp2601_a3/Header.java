package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */

public class Header{

	private String tx;	// Identity of sender; e.g., Bob
	private String rx;	// Identity of receiver; e.g., Lou
	private String type;	// Type of message e.g. login, data, ...
	
	public Header(String c_Tx, String c_Rx, String c_Type) {
		tx = c_Tx;
		rx = c_Rx;
		type = c_Type;
	}

	public void setTx(String s){tx = s;}
	public void setRx(String s){rx = s;}
	public void setType(String s){type = s;}

	public String getTx(){return tx;}
	public String getRx(){return rx;}
	public String getType(){return type;}

}

