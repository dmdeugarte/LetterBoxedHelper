import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

public class Helper
{
  public static void main(String[] args)
  {
    String myLetters = "LACUDINSRTPW";
    
    doFirstPass(myLetters, true, true, true);
    bruteForceSolution();
  }
  
  /* Creates an output log, and reports as directed.
  *  reportNum determines if the number of valid words will be reported to the console
  *  reportLongest determines if the first, longest, valid word will be reported to the console
  *  reportMostLetters determines if the first word that hits the most different letters will be reported to the console 
  */
  private static void doFirstPass(String myLetters, boolean reportNum, boolean reportLongest, boolean reportMostLetters) 
  {
    String longest = "";
    String mostUsedLetters = "";
    int mostUsedLettersBitCount = 0;
    
    try
    {
      File input = new File("input.txt");
      Scanner reader = new Scanner(input);
      
      File output = new File("validWords.txt");
      
      if (!output.createNewFile())
      {
        //System.out.println("Output file already exists.\nDeleting preexisting output file...");
        output.delete();
        //System.out.println("Preexisting output file deleted.");
        output.createNewFile();
      }
      //System.out.println("Output file created.");
      
      FileWriter writer = new FileWriter("validWords.txt");
      
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
        
        // check letter by letter
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
            
            lastGroup = newGroup; // If valid = false, saving this here won't matter. Less if-else logic
          }
          
          i++;
        }
        
        // letters fully passed through, now add the new word to the reports
        if (valid)
        {
          //System.out.println(word + " is valid.");
          totalCount++;
          writer.write(word + "," + mask + "\n");
          
          if (word.length() > longest.length())
            longest = word;
          
          if (countBits(mask) > mostUsedLettersBitCount)
          {
            mostUsedLetters = word;
            mostUsedLettersBitCount = countBits(mask);
          }
        }
      }
      
      // report checks
      if (reportNum)
      {
        System.out.println(totalCount + " total valid words.");
      }
      if (reportLongest)
      {
        System.out.println(longest + " was the longest word.");
      }
      if (reportMostLetters)
      {
        System.out.println(mostUsedLetters + " hit the most letters, with " + mostUsedLettersBitCount + " letters used.");
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
    
    System.out.println("First pass halt.");
  }
  
  // using output.txt, finds the smallest length solutions
  // requires optimization
  // solutions put into arraylist: if word2;word3 is in solutions. 
  // move tempMem into incompleteSolutions. 
  // consolidate words and masks into one custom object type. 
  public static void bruteForceSolution()
  {
    try
    {
      File input = new File("validWords.txt");
      Scanner reader = new Scanner(input);
      
      File solutions = new File("solutions.txt");
      
      if (!solutions.createNewFile())
      {
        solutions.delete();
        solutions.createNewFile();
      }
      
      FileWriter solutionWriter = new FileWriter("solutions.txt");
      
      ArrayList<String> words = new ArrayList<String>();
      ArrayList<Integer> masks = new ArrayList<Integer>();
      
      while (reader.hasNextLine())
      {
        String line = reader.nextLine(); // of pattern "word,mask"
        int commaPosition = line.indexOf(',');
        
        String word = line.substring(0, commaPosition);
        int mask = Integer.parseInt(line.substring(commaPosition+1));
        
        words.add(word);
        masks.add(mask);
      }
      
      ArrayList<String> tempWords = new ArrayList<String>();
      ArrayList<Integer> tempMasks = new ArrayList<Integer>();
      
      for (int i = 0; i < words.size(); i++)
      {
        for (int j = 0; j < words.size(); j++)
        {
          String word1 = words.get(i);
          String word2 = words.get(j);
          
          // last letter matches first letter
          if (word1.charAt(word1.length()-1) == word2.charAt(0) && !word1.equals(word2))
          {
            String wordOut = word1 + ";" + word2;
            int mask1 = masks.get(i);
            int mask2 = masks.get(j);
            
            int mask = mask1 | mask2;
            
            if (countBits(mask) == 12)
            {
              solutionWriter.write(wordOut + "\n");
            }
            else
            {
              tempWords.add(wordOut);
              tempMasks.add(mask);
            }
          }
        }
      }
      
      /*for (int i = 0; i < tempWords.size(); i++)
      {
        for (int j = 0; j < words.size(); j++)
        {
          String word1 = tempWords.get(i);
          String word2 = words.get(j);
          
          // last letter matches first letter
          if (word1.charAt(word1.length()-1) == word2.charAt(0) && !word1.equals(word2))
          {
            String wordOut = word1 + ";" + word2;
            int mask1 = tempMasks.get(i);
            int mask2 = masks.get(j);
            
            int mask = mask1 | mask2;
            
            if (countBits(mask) == 12)
            {
              solutionWriter.write(wordOut + "\n");
            }
          }
        }
      }*/
      
      reader.close();
      solutionWriter.close();
      
      System.out.println("BruteForce Solutions found.");
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
  }
  
  // The naive flipped bit counter
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