import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Backend.LinkedList;
import Backend.Word_Object;


class manageFile{
  //this is a class that works with the UI to open a file, save a file, etc.

  //instance variables
  public String[] text;
  private String path;

  //method to convert a file to a string
  private static String fileToString(String path){
    //get path
    Path filePath = Paths.get(path);

    //now convert file to string

    //create a string to hold the file
    String file_contents = "";
    
    try{
      //create a buffer reader
      BufferedReader reader = Files.newBufferedReader(filePath);
      //loop to read the file, throw an exception if it fails
      String line = reader.readLine();
       while(line != null){
        file_contents = file_contents + line;
        //append A /n to the end of the line
        file_contents = file_contents + "\n";
        //read the next line
        line = reader.readLine();
      }
    }catch(IOException e){
      return("Error reading file");
    }
    
    //return the file contents in a string. 
    return file_contents;
    

  }

  public LinkedList string_to_list(String text){
    //this function takes the singular string, and converts it to a linked list of words
    
    //split the string into an array of words
    String[] words = text.split(" ");

    //create a linked list
    LinkedList word_list = new LinkedList();

    //loop through the array of words
    for(int i = 0; i < words.length; i++){
      //create a word object
      Word_Object word = new Word_Object();
      //check if the word starts with a capital
      if(Character.isUpperCase(words[i].charAt(0))){
        //set the start with capital to true
        word.setStart_with_capital(true);
      }
      //check if the word ends with a period
      if(words[i].endsWith(".")){
        //set the end with period to true
        word.setEnd_with_period(true);
        //remove the period from the word
        words[i] = words[i].substring(0, words[i].length() - 1);
      }


      //set the word
      word.setWord(words[i]);
      //add the word to the linked list
      word_list.add(word);
    }

    //return the linked list
    return word_list;
  
  }

  //function to convert the string to a file (need filename??)
  private static void stringToFile(LinkedList words, String path){
    
    Word_Object current_word = words.getHead();

    String text = "";

    while(current_word.hasNext()){
      //get the word
      String word = current_word.getWord();
      //check capitalization
      if(current_word.isStart_with_capital()){
        //split into char array
        char[] char_array = word.toCharArray();
        //get the first char
        char first_char = char_array[0];
        //convert to string
        String first_char_string = Character.toString(first_char);
        //capitalize the first char
        first_char_string = first_char_string.toUpperCase();
        //replace the first char with the capitalized char
        word = word.replaceFirst(Character.toString(first_char), first_char_string);
        
      }
      //add the word to the text
      text = text + word;
      //if it has a period add one at the end of the word
      if(current_word.isEnd_with_period()){
        text = text + ".";
      }

      //add a space
      text = text + " ";
      
      //get the next word
      current_word = current_word.getNext_node();
    }

    //now we have the text, we need to write it to a file

    //get the path
    Path filePath = Paths.get(path);

    //create a buffered writer
    try{
      BufferedWriter writer = Files.newBufferedWriter(filePath);
      //write the text to the file
      writer.write(text);
      //close the writer
      writer.close();
  }catch(IOException e){
    System.out.println("Error writing to file");
  }
  }
  
}
