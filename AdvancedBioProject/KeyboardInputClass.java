//Class:			KeyboardInputClass
//Description:	Provides one or more methods for entering information from the
//					keyboard for console based programs
import java.io.*;
class KeyboardInputClass 
{
	//Method:		getKeyboardInput
	//Description:	Permits keyboard input for strings
	//Parameters:	prompt - descriptive text telling the user what to enter
	//Returns:     The entered text (i.e., the user's response). Note that even
	//					though this is a string, it can be converted to an integer,
	//					float, etc. if necessary in the client program.
	//Throws			Exception (but doesn't do anything with it!)
	//Calls:       Nothing
	public String getKeyboardInput(String prompt) 
	{
		String inputString="";
		System.out.println(prompt);
		try {
			InputStreamReader reader=new InputStreamReader(System.in);
			BufferedReader buffer=new BufferedReader(reader);
			inputString=buffer.readLine();
		}
		catch (Exception e) {}
		return inputString;
	}
	//***********************************************************************
}
//**************************************************************************
//**************************************************************************

//Here is how to use it... (remove the comments!)
//KeyboardInputClass keyboardInput = new KeyboardInputClass();
//String userInput="";
//userInput=keyboardInput.getKeyboardInput("Specify the string to be processed");

