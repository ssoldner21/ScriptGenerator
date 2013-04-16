/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptgenerator;
import java.io.*;
import java.util.*;

/**
 *
 * @author ssoldner21
 */
public class ScriptGenerator {

    /**
     * @param args the command line arguments
     */
    public static Random generator = new Random();
       
    public static Word beginSentence(Word listStart)
    {
        int random = 0;
        Word start = null;
        Word c = listStart;
        
        while (start == null)
        {
            while (start == null && c.right !=null)
            {
                if (c.beginsSentence == true)
                {
                    random = generator.nextInt(Word.totalBegin(listStart));
                    if (random < c.numOccurrences || c.right == null)
                    {
                        start = new Word(c.word);
                        return start;
                    }
                }
                c = c.right;
            }
            c = listStart;
        }
        
        return start;
    }
    
    public static Word midSentence(Word listStart, String previous)
    {
        int random = 0;
        Word mid = null;
        Word c = listStart;
        Word d = null;
        
        while (mid == null)
        {
            while (c != null && mid == null)
            {
                if (c.word.equals(previous))
                {
                    d = c.down;
                    if (d!= null && (d.word.equals(".") || d.word.equals("!") || d.word.equals("?")))
                    {
                        mid = new Word(d.word);
                        mid.endsSentence = true;
                        return mid;
                    }
                    else
                    {
                        while (d != null && mid == null)
                        {
                            random = generator.nextInt(Word.totalDown(c.down));
                            if (random <= d.numOccurrences)
                            {
                                mid = new Word(d.word);
                                return mid;
                            }
                            if (d.down == null)
                            {
                                mid = new Word(d.word);
                                return mid;
                            }
                            else
                            {
                                d = d.down;
                            }
                        }
                    }
                }
                c = c.right;
            }
            c = listStart;
        }
        return mid;
    }
    
    public static Word outputSentence(Word listStart)
    {
        Word output = null;
        Word m = null;
        Word current = null;
        String previous = "";
        
        output = beginSentence(listStart);
        previous = output.word;
        current = output;
        
        m = midSentence(listStart, previous);
        previous = m.word;
        
        while (m.endsSentence != true)
        {
            while (current.right != null)
            {
                current = current.right;
            }
            current.right = m;
            
            m = midSentence(listStart, previous);
            
            previous = m.word;
        }
        
        if (m.endsSentence)
        {
            while (current.right != null)
            {
                current = current.right;
            }
            current.right = m;
        }
        
        
        return output;
    }
    
    public static void printOutput(Word output)
    {
        Word c = output;
        while (c != null)
        {
            if (c.right != null && (c.right.word.equals(".") || c.right.word.equals("!") || c.right.word.equals("?") ))
            {
                System.out.print(c.word + c.right.word + " ");
                c = c.right; //skip last one because already printed
            }
            else
            {
                if (c.right != null && (c.right.word.equals(".") || c.right.word.equals("!") || c.right.word.equals("?")))
                {}
                else
                {
                    System.out.print(c.word + " ");
                }
            }
            c = c.right;
        }
        System.out.println();
            
    }
    
    public static void results(Word listStart)
    {
        for(int i = 0; i < 10; i++) //gets and prints 10 unique lines
        {
            Word output = outputSentence(listStart);
            printOutput(output);
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        Word listStart = null;
        System.out.println("U:\\Java\\SMS_ScriptGenerator\\Test2.txt");
        System.out.println("Enter a file to use as input: ");
        listStart = Word.readIn(listStart);
        System.out.println("Full list:");
        Word.printList(listStart);
        System.out.println();
        results(listStart); 
        //Occassionally infinite loops mid sentence. Haven't tracked why yet
        //Probably has something to do with probability of picking each middle word
    }
}