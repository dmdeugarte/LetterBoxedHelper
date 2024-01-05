import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

/*
 * A program to clean the input file for Helper.java. 
 * Double or triple letters are not to be desired. 
 * Words containing double or triple letters will be deleted, then placed back into input.txt, to be used by Helper.java
 * 
 * Meant to be run separately. 
 */
public class CleanInput
{
  public static void main(String[] args)
  {
    try
    {
      File input = new File("Original_Input.txt");
      Scanner reader = new Scanner(input);
      
      File output = new File("input.txt");
      
      if (!output.createNewFile())
      {
        System.out.println("input.txt already exists.\nDeleting preexisting file...");
        output.delete();
        System.out.println("Preexisting file deleted.");
        output.createNewFile();
      }
      System.out.println("New input.txt created.");
      
      FileWriter writer = new FileWriter("input.txt");
      
      while (reader.hasNextLine())
      {
        String word = reader.nextLine();
        char pastLetter = ' ';
        int index = 0;
        boolean valid = true;
        
        while (index < word.length() && valid)
        {
          char letter = word.charAt(index);
          
          if (letter == pastLetter)
          {
            valid = false;
          }
          
          pastLetter = letter;
          index++;
        }
        
        if (valid)
        {
          writer.write(word + "\n");
        }
      }
      
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
