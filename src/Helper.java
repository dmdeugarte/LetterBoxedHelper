import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Helper
{
  public static void main(String[] args)
  {
    String myLetters = "XNRAMIELVGUT";
    String longest = "";
    
    try
    {
      File input = new File("input.txt");
      Scanner reader = new Scanner(input);
      
      File output = new File("output.txt");
      
      if (!output.createNewFile())
      {
        System.out.println("Output file already exists.\nDeleting preexisting output file...");
        output.delete();
        System.out.println("Preexisting output file deleted.");
        output.createNewFile();
      }
      System.out.println("Output file created.");
      
      FileWriter writer = new FileWriter("output.txt");
      
      //-----------------------------------------------------------------------------------------------------------------------
      // Begin reading from input line-by-line for checking
      //-----------------------------------------------------------------------------------------------------------------------
      while (reader.hasNextLine())
      {
        String word = reader.nextLine();
        
        int lastGroup = -1;
        int i = 0;
        int length = word.length();
        boolean valid = true;
        
        while (i < length && valid)
        {
          int newGroup = myLetters.indexOf(word.charAt(i));
          
          if (newGroup == -1) //index not found
          {
            valid = false;
          }
          else
          {
            newGroup = newGroup / 3; // converting index to group value
            
            if (newGroup == lastGroup) // curLetter in same group as lastLetter
            {
              valid = false;
            }
            else
            {
              lastGroup = newGroup;
            }
          }
          
          i++;
        }
        
        if (valid)
        {
          System.out.println(word + " is valid.");
          writer.write(word + "\n");
          
          if (word.length() > longest.length())
            longest = word;
        }
      }
      
      System.out.println(longest + " was the longest word.");
      reader.close();
      writer.close();
    } 
    catch (FileNotFoundException e)
    {
      System.out.println("A file was not found.");
      e.printStackTrace();
    }
    catch (IOException e)
    {
      System.out.println("An IO error occured.");
      e.printStackTrace();
    }
    
    System.out.println("Program halt.");
  }
}