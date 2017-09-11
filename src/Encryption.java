import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Encryption 
{
	public static void main (String [] args) throws IOException
	{
		//setup
		final char[] charArray = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
				   ,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
				   ,'0','1','2','3','4','5','6','7','8','9','.',',','-','!','?',' ','\n'};
		HashMap<Character,Integer> charCodes = new HashMap<Character,Integer>();
		for (int i = 0; i < charArray.length; i++)
		{
			charCodes.put(charArray[i],i+2);
		}
		//end of setup
		//initialize variables
		int p = 0;
		int q = 0;
		int k = 0;
		int n = 0;
		String unencrypted = "";
		String encrypted = "";
		String decrypted = "";
		//end of initialize variables
		
		String choice = JOptionPane.showInputDialog(null,"encrypt or decrypt");
		if (choice.equals("encrypt"))
		{
			String input = JOptionPane.showInputDialog(null,"give k value");
			k = Integer.parseInt(input);
			input = JOptionPane.showInputDialog(null,"give n value");
			n = Integer.parseInt(input);
			input = JOptionPane.showInputDialog(null,"give file to encrypt");
			unencrypted = getFromFile(input);
			for (int i = 0; i < unencrypted.length(); i++)
			{
				int code = charCodes.get(unencrypted.charAt(i));
				encrypted += modCalc(code,k,n);
				if (i != unencrypted.length()-1)
				{
					encrypted += ",";
				}
			}
			System.out.println(encrypted);
			input = JOptionPane.showInputDialog(null,"give output filename");
			writeToFile(input, encrypted);
		}
		else
		{
			String input = JOptionPane.showInputDialog(null,"give k value");
			k = Integer.parseInt(input);
			input = JOptionPane.showInputDialog(null,"give p value");
			p = Integer.parseInt(input);
			input = JOptionPane.showInputDialog(null,"give q value");
			q = Integer.parseInt(input);
			input = JOptionPane.showInputDialog(null,"give encrypted file");
			encrypted = getFromFile(input);
			n = p * q;
			int phiN = (p-1)*(q-1);
			String[] splitArray = encrypted.split(",");
			int power = reverseEuclidian(phiN, k);
			for (int i = 0; i < splitArray.length; i++)
			{
				int code = modCalc(Integer.parseInt(splitArray[i]),power,n);
				decrypted += charArray[code-2];
			}
			System.out.println(decrypted);
			input = JOptionPane.showInputDialog(null,"give output filename");
			writeToFile(input, decrypted);
		}
		
		
	}
	public static void writeToFile(String filename, String output) throws IOException
	{
		File file = new File("/Users/MichaelOtt/Desktop/Programming/RSA Encryption/bin/RSA Files/"  + filename + ".txt");
		if (!file.exists())
		{
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		bw.write(output);
		bw.close();	
	}
	public static String getFromFile(String filename)
	{
		String fullString = "";
		try
		{
			
			BufferedReader in = new BufferedReader(new FileReader("/Users/MichaelOtt/Desktop/Programming/RSA Encryption/bin/RSA Files/" + filename + ".txt"));
			String line = "";
			line = in.readLine();
			while (line != null && line != "")
			{
				fullString += line;
				line = in.readLine();
				if (line != null && line != "")fullString += '\n';
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			filename = JOptionPane.showInputDialog(null,"that file doesn't exist");
		}
		return fullString;
	}
	public static int modCalc(int num, int power, int mod)
	{
		int currentSquare = num % mod;
		int product = 1;
		while (power > 0) 
		{
		     if (power%2==1) 
		     {
		         product *= currentSquare;
		         product = product % mod;
		     }
		     power /= 2;
		     currentSquare = (currentSquare * currentSquare) % mod;
		}
		return product;
	}
	public static int reverseEuclidian(int phiN, int power)
	{
		int leftNum = phiN;
		int rightNum = power;
		int remainder = 0;
		ArrayList<Integer> rightSide = new ArrayList<Integer>();
		ArrayList<Integer> leftSide = new ArrayList<Integer>();
		leftSide.add(0);
		leftSide.add(1);
		while (rightNum != 0)
		{
			rightSide.add(leftNum/rightNum);
			remainder = leftNum % rightNum;
			leftNum = rightNum;
			rightNum = remainder;
		}
		int size = rightSide.size();
		for (int i = 1; i < size; i++)
		{
			leftSide.add(leftSide.get(i)*rightSide.get(size-1-i)+leftSide.get(i-1));
		}
		int last = leftSide.get(leftSide.size()-1);
		int scndLast = leftSide.get(leftSide.size()-2);
		int answer = 0;
		if (last*power > scndLast*phiN)
		{
			answer = last;
		}
		else
		{
			answer = phiN-last;
		}
		return answer;
	}
}
