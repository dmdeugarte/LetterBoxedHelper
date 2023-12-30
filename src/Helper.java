import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;

public class Helper
{
  public static void main(String[] args)
  {
    String myLetters = "BTLJWIEHYOCV";
    String longest = "";
    String mostUsedLetters = "";
    int mostUsedLettersBitCount = 0;
    
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
      
      int totalCount = 0;
      
      //-----------------------------------------------------------------------------------------------------------------------
      // Begin reading from input line-by-line for checking
      //-----------------------------------------------------------------------------------------------------------------------
      while (reader.hasNextLine())
      {
        String word = reader.nextLine();
        
        int lastGroup = -1;
        int i = 0;
        int length = word.length();
        int mask = 0;
        boolean valid = true;
        
        while (i < length && valid)
        {
          int index = myLetters.indexOf(word.charAt(i));
          
          if (index == -1) //index not found
          {
            valid = false;
          }
          else
          {
            int num = (int)Math.pow(2, index); //Getting the bit length item
            mask |= num;
            
            int newGroup = index / 3; // converting index to group value
            
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
          totalCount++;
          writer.write(word + "\n");
          
          if (word.length() > longest.length())
            longest = word;
          
          if (countBits(mask) > mostUsedLettersBitCount)
          {
            mostUsedLetters = word;
            mostUsedLettersBitCount = countBits(mask);
          }
        }
      }
      
      System.out.println(longest + " was the longest word.");
      System.out.println(mostUsedLetters + " hit the most letters, with " + mostUsedLettersBitCount + " letters used.");
      System.out.println(totalCount + " total valid words.");
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
  
  private static int countBits(int n) 
  {
    int count = 0;
    while (n != 0) 
    {
      count += n % 2;
      n /= 2;
    }
    return count;
  }
}
