/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptgenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author ssoldner21
 */
public class Word {
    public static int counter = 0;
    int idNum;
    Word right;
    Word down;
    String word = "";
    boolean beginsSentence;
    boolean endsSentence;
    int numOccurrences = 0;
    
    Word(String word)
    {
        this.word = word;
        numOccurrences = 1;
        idNum = counter;
        counter++;
    }
    
    public static Word readIn(Word listStart) throws FileNotFoundException
    {
        Scanner in = new Scanner(System.in);
        File inFile = new File (in.next());
        Scanner freader = new Scanner(inFile);
        StringTokenizer sentences = new StringTokenizer("");
        StringTokenizer words = new StringTokenizer("");
        String delims = ".!?";
        String sentence = "";
        String word = "";
        String previous = "";
        String line = null;
        String paragraph = null;
        
        while (freader.hasNextLine())
        {
            
            paragraph = freader.nextLine();
            sentences = new StringTokenizer(paragraph, delims, true);
            while (sentences.hasMoreTokens())
            {
                sentence = sentences.nextToken();
                words = new StringTokenizer(sentence);
                while (words.hasMoreTokens())
                {
                    word = words.nextToken();
                    listStart = Word.addWord(listStart, word, previous);
                    previous = word;
                }
            }
        }
        freader.close();
        
        return listStart;
    }
    
    public static void printList(Word listStart)
    {
        Word c = listStart;
        Word d = c;
        
        while (c != null)
        {
            System.out.println(c.word + " " + c.numOccurrences + " begins: " + c.beginsSentence + " ends: " + c.endsSentence);
            d = c;
            while (d.down != null)
            {
                System.out.println("--" + d.down.word + " " + d.down.numOccurrences + " ends: " + d.down.endsSentence);
                d = d.down;
            }
            c = c.right;
        }
    }
    
    public static Word addWord(Word listStart, String nextWord, String previous)
    {
        Word c = listStart;
        Word d = null;
        //.right list
        if (c == null) //first word
        {
            listStart = new Word(nextWord);
            listStart.beginsSentence = true;
            return listStart;
        }
        else //next word
        {
            if (count(listStart, nextWord) > 0) //if in the list, get to it and increment
            {
                while (c.right != null)
                {
                    if (c.word.equals(nextWord))
                    {
                        c.numOccurrences++;
                    }
                    c = c.right;
                }
            }
            else //if not in the list, get to the end and add word
            {
                while (c.right != null)
                {
                    c = c.right;
                }
                c.right = new Word(nextWord);
                if (previous.equals(".")       
                    || previous.equals("?") 
                    || previous.equals("!") )
                {
                    c.right.beginsSentence = true;
                }
            }
            c = listStart; //reset
            
            //.down list
            while (c != null) //go through again, looking for previous word to add following word to down list
            {
                d = c;
                if (c.word.equals(".")       
                    || c.word.equals("?") 
                    || c.word.equals("!") )
                {
                    c.endsSentence = true;
                }
                else
                {
                    if (d.word.equals(previous))
                    {
                        if (d.down == null)//no Words in .down list 
                        {
                            d.down = new Word(nextWord);
                            if (d.down.word.equals(".")       
                                || d.down.word.equals("?") 
                                || d.down.word.equals("!") )
                            {
                                d.endsSentence = true;
                            }
                        }
                        else
                        {//if there are words in the .down list
                            if (countDown(c, nextWord) > 0) //find it and increment
                            {
                                while(d.down != null)
                                {
                                    if (d.down.word.equals(nextWord))
                                    {
                                        d.down.numOccurrences++;
                                    }
                                    d = d.down;
                                }
                            }
                            else //get to end and add new
                            {
                                while(d.down != null)
                                {
                                    d=d.down;
                                }
                                d.down = new Word(nextWord);
                                if (d.down.word.equals(".")       
                                    || d.down.word.equals("?") 
                                    || d.down.word.equals("!") )
                                {
                                    d.endsSentence = true;
                                }
                            }
                        }
                    }
                }
                c = c.right;
            }
        }
        return listStart;
    }
    
    //total beginning words
    public static int totalBegin(Word listStart)
    {
        int count = 0;
        Word c = listStart;
        
        while(c.right != null)
        {
            if (c.beginsSentence == true)
            {
                count+=c.numOccurrences;
            }
            c = c.right;
        }
        return count;
    }
    
    //total middle words
    public static int totalMid(Word listStart)
    {
        int count = 0;
        Word c = listStart;
        
        while(c.right != null)
        {
            if (c.beginsSentence == false && c.endsSentence == false)
            {
                count+=c.numOccurrences;
            }
            c = c.right;
        }
        return count;
    }
    
    public static int totalDown(Word d)
    {
        int count = 0;
        while (d != null)
        {
            count+= d.numOccurrences;
            d = d.down;
        }
        
        return count;
    }
    
    public static int countDown(Word listDown, String searchWord)
    {
        int count = 0;
        Word d = listDown;
        
        while (d != null)
        {
            if (d.word.equals(searchWord))
            {
                count++;
            }
            d = d.down;
        }
        
        return count;
    }
    
    //count right
    public static int count(Word listStart, String searchWord)
    {
        int count = 0;
        Word c = listStart;
        
        while (c != null)
        {
            if (c.word.equals(searchWord))
            {
                count++;
            }
            c = c.right;
        }
        
        return count;
    }
}