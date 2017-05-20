package comp2601.ian.comp2601_a3;

/******
 * COMP2601 A3
 * Submitted by Ian Smith #100910972
 * 2016-03-16
 */

//Represents the payload of the Message class
//For A3, this consists of a String
public class Body {

	private String data;
	public Body(String c_data){
        data = c_data;
    }

    public String getData(){return data;}
    public void setData(String s){data = s;}
}

