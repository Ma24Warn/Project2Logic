import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * This program takes in a Propositional Logic statement written in a file, converts it into
 * conjunctive normal form, and outputs the results to another file.
 * 
 * @author Matthew Warner
 * @version Spring 2023
 * 
 */

public class CNFConverter {

    public static void main(String args[]) throws IOException {
        
        //input test cases
        File test1 = new File("TestCases/testcase1.txt");
        File test2 = new File("TestCases/testcase2.txt");
        File test3 = new File("TestCases/testcase3.txt");
        File test4 = new File("TestCases/testcase4.txt");
        File test5 = new File("TestCases/testcase5.txt");

        //output text files
        File output1 = new File("TestCases/testcase1output.txt");
        File output2 = new File("TestCases/testcase2output.txt");
        File output3 = new File("TestCases/testcase3output.txt");
        File output4 = new File("TestCases/testcase4output.txt");
        File output5 = new File("TestCases/testcase5output.txt");

        //read in from file
        Scanner scan = new Scanner(test5);
        String plSentence = scan.nextLine();
        scan.close();

        //send Propositional Logic sentence to CNF converter method
        cnfConvert(plSentence, output5);

    }


    //This method is what converts the propositional logic into conjunctive normal form and writes
    //it to an output file
    public static void cnfConvert(String s, File f) throws IOException {
        int index = 0; //frequently used as an index for iterating over the PL sentence
        StringBuilder PLString = new StringBuilder(s); //I chose to use StringBuilder to convert the sentence
        StringBuilder substr; //a StringBuilder object which is used a few times to store a substring (I probably could have just used a String for this but the end result )

        //create FileWriter object
        FileWriter writer = new FileWriter(f);

        //System.out.println("ORIGINAL " + PLString.toString());

        //STEP ONE
        //Remove > (implies) and = (biconditional)
        int openParen = 0, closedParen = 0, end = 0, begin = 0; //used to store indexes of parens and the beginning and end of where the substring should go)
        String p1 = "", p2 = ""; //holds the string values of both sides of the biconditional

        //biconditional
        while (index < PLString.length()-1) {

            //if a biconditional is seen
            if (PLString.charAt(index) == '=') {
                substr = new StringBuilder();

                //if next symbol is a letter
                if (Character.isLetter(PLString.charAt(index+1))) {
                    p1 = String.valueOf(PLString.charAt(index+1));
                    end = index+2; //get the index of where the end will be
                }

                //else if next symbol is an open paren, get a substring of everything within it
                else if (PLString.charAt(index+1) == '(') {
                    closedParen = forwardParen(PLString.toString(), index+1);
                    end = closedParen+1; //get the index of where the end will be

                    //get substring
                    p1 = PLString.substring(index+1, closedParen+1);
                }


                //if prev symbol is a letter
                if (Character.isLetter(PLString.charAt(index-1))) {
                    p2 = String.valueOf(PLString.charAt(index-1));
                    begin = index-1; //get the index of where the beginning will be
                }

                //else if prev symbol is a closed paren, get a substring of everything within it
                else if (PLString.charAt(index-1) == ')') {
                    openParen = backwardParen(PLString.toString(), index-1);
                    begin = openParen; //get the index of where the beginning will be

                    //get substring
                    p2 = PLString.substring(openParen, index);

                }

                //form the substring based on the biconditional rule
                substr.append("(" + p2 + ">" + p1 + ")&(" + p1 + ">" + p2 + ")");

                //replace that substring into PLString at its respective spot
                PLString.replace(begin, end, substr.toString());


            }

            index++;

        }


        //System.out.println("STEP1 P1 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused


        //implies
        while (index < PLString.length()-1) {
            
            //if implications is seen
            if (PLString.charAt(index) == '>') {

                //just insert a | in place of the >
                PLString.replace(index, index+1, "|");


                //if the previous thing is a single letter, put a ~ before it
                if (Character.isLetter(PLString.charAt(index-1))) {
                    PLString.insert(index-2, "~");
                }

                //if the previous thing is a closed paren, call the reverseParen method and insert a ~ before the
                //open paren
                else if (PLString.charAt(index-1) == ')') {
                    int pIndex = backwardParen(PLString.toString(), index-1);
                    PLString.insert(pIndex, "~");
                }


            }
            
            index++;  

        }

        //System.out.println("STEP1 P2 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused



        //STEP TWO
        //move NOTs inwards.
        int nIndex = 0;
        Stack<Character> stack; //stack to help keep track of matching amount of parens

        //DeMorgan's Law
        while (index < PLString.length()-1) {
            
            //if a not is seen
            if (PLString.charAt(index) == '~') {

                //if the not is followed by an open parentheses
                if (PLString.charAt(index+1) == '(') {
                    nIndex = index+1;
                    stack = new Stack<Character>();

                    //insert a negative before the first element that was distributed to
                    PLString.insert(index+2, "~");


                    //until we have reached the closing paren of the first open paren we see.
                    //This is very similar to my forwardParen and backwardParen, except I am actually
                    //modifying the PLString here
                    while (true) {

                        //if you see an open paren, push it to the stack
                        if (PLString.charAt(nIndex) == '(') {
                            stack.push('(');
                        }

                        //else if you see a closed paren, pop from the stack
                        else if (PLString.charAt(nIndex) == ')') {
                            stack.pop();
                        }

                        //else if you see a symbol and the stack size is 1, insert a ~ after it
                        //my reasoning for the stack size needing to be 1 is that if it is 1, then we are not
                        //inside another open paren, which shouldn't get a ~. It would mean we are in the outer-
                        //most part of the original set of parentheses, if that makes sense.
                        else if (PLString.charAt(nIndex) == '&' && stack.size() == 1) {
                            PLString.replace(nIndex, nIndex+1, "|~");

                            nIndex++;
                        }

                        //same as description above, just needed two so I could easily flip the symbol around
                        else if (PLString.charAt(nIndex) == '|' && stack.size() == 1) {
                            PLString.replace(nIndex, nIndex+1, "&~");
                            nIndex++;
                        }

                        //if the stack is empty, exit the loop
                        if (stack.isEmpty()) {
                            break;
                        }
                        
                        nIndex++;
                    }


                    //delete original ~ before the first open paren
                    PLString.delete(index, index+1);

                }

            }

            index++;  
        }

        //System.out.println("STEP2 P1 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused

        //Remove Double Negatives
        while (index < PLString.length()-1) {
            
            //if a NOT is seen
            if (PLString.charAt(index) == '~') {

                //if it is followed by another NOT
                if (PLString.charAt(index+1) == '~') {

                    //remove the double negative
                    PLString.delete(index, index+2);

                }
            }
            
            index++;  

        }

        //System.out.println("STEP2 P2 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused



        //STEP THREE
        //apply distributive and associative properties
        char outer, inner = ' '; //holds char values of the outer and inner symbols 
        String prev = "", next1 = "", next2 = "", sub; //stores parts of substring to be added and original substring
        int ind1 = 0, ind2 = 0, ind3 = 0; //used to hold the indexes after calling the forwardParen/backwardParen methods
        int st = 0, en = 0; //used to get the start and end values for updating PLString with insert()
        boolean more = true, cnfForm = false; //used to determine if changes should be made and if a substring is in its final form
        
        //below regular expression ensures a substring is in the form "((parentheses with anything in it or letter with or without ~)|(same as the other side of the |))"
        String regex = "\\((([~]*[a-zA-Z])|(\\(.*\\)))\\|(([~]*[a-zA-Z])|(\\(.*\\)))\\)";


        //need to make sure new possible distributions are accounted for. Once nothing gets modified in the
        //inner while loop, then there must be no new possible distributions, so we can exit this while loop
        while (more == true) {
            more = false; //if nothing is changed, this will stay false and break out of this outermost while loop

            //Distribute
            while (index < PLString.length()-1) {
                
                //if a | or a & is seen
                if ((PLString.charAt(index) == '|' || PLString.charAt(index) == '&')) {
                    
                    //if it is followed by an open paren
                    if (PLString.charAt(index+1) == '(') {
                        more = true; //something is going to be changed, so we have to check for new possible distributions
                        outer = PLString.charAt(index); //save what the char seen was (| or &)
                        substr = new StringBuilder();

                        //get the string of what we want to distribute over
                        ind3 = forwardParen(PLString.toString(), index+1);
                        sub = PLString.substring(index+1, ind3+1);                        

                        //if this string does not match the regular expression
                        if (!Pattern.matches(regex, sub)) {
                            cnfForm = false; //this means we can modify the PLString
                        }

                        //else it must then be in the correct form, so we can skip this distribution
                        else {
                            cnfForm = true; //do not modify PLString
                            more = false; //no changes were actually made, so we set this back to false
                        }

                        //if the string did not match the regular expression
                        if (cnfForm == false) {

                            //if the first element in the parens is a letter, save it as a string
                            if (Character.isLetter(PLString.charAt(index+2))) {
                                ind1 = index+2; //save the index of that letter
                                next1 = String.valueOf(PLString.charAt(index+2));
                                inner = PLString.charAt(index+3); //get the inner symbol
                            }

                            //else if the first element in the parens is a ~ letter, save it as a string
                            if (PLString.charAt(index+2) == '~' && Character.isLetter(PLString.charAt(index+3))) {
                                ind1 = index+3; //save the index of that letter
                                next1 = "~" + String.valueOf(PLString.charAt(index+3));
                                inner = PLString.charAt(index+4); //get the inner symbol
                            }

                            //else if the first element in the parens is another open paren, get a substring of what it contains
                            else if (PLString.charAt(index+2) == '(') {
                                ind1 = forwardParen(PLString.toString(), index+2); //save the index of the closing paren
                                next1 = PLString.substring(index+2, ind1+1);
                                inner = PLString.charAt(ind1+1); //get the inner symbol
                            }

                            ind2 = ind1;

                            //if outermost symbol is the same as the inner symbol, do not distribute
                            if (outer == inner) {
                                more = false; //no changes should be made. this will soon be checked before PLString is actually modified
                            }


                            //if the second element in the parens is a letter, save it as a string
                            if (Character.isLetter(PLString.charAt(ind2+2))) {
                                next2 = String.valueOf(PLString.charAt(ind2+2));
                                en = ind2+4; //save the end index
                            }

                            //else if the second element in the parens is a letter with a ~, save it as a string
                            else if (PLString.charAt(ind2+2) == '~' && Character.isLetter(PLString.charAt(ind2+3))) {
                                next2 = "~" + String.valueOf(PLString.charAt(ind2+3));
                                en = ind2+5; //save the end index
                            }

                            //else if the second element in the parens starts with an open paren, get a substring of what it contains
                            else if (PLString.charAt(ind2+2) == '(') {
                                ind1 = forwardParen(PLString.toString(), ind2+2);
                                next2 = PLString.substring(ind2+2, ind1+1);
                                en = ind1+2; //save the end index
                            }


                            //if the previous element is a letter, save it as a string
                            if (Character.isLetter(PLString.charAt(index-1))) {
                                prev = String.valueOf(PLString.charAt(index-1));
                                st = index-1; //save the start index
                            }

                            //else if the previous element is a letter with a ~, save it as a string
                            else if (PLString.charAt(index-2) == '~' && Character.isLetter(PLString.charAt(index-1))) {
                                prev = "~" + String.valueOf(PLString.charAt(index-1));
                                st = index-2; //save the start index
                            }

                            //else if the previous element is a close paren, get a substring of what it contains
                            else if (PLString.charAt(index-1) == ')') {
                                ind1 = backwardParen(PLString.toString(), index-1);
                                prev = PLString.substring(ind1, index);
                                st = ind1; //save the start index
                            }


                            //concatenate all gathered strings into a substring and update PLString with it
                            substr.append("(" + prev + outer + next1 + ")" + inner + "(" + prev + outer + next2 + ")");


                            if (more == false) {
                                //this is for if the outer and inner symbols are not opposites, then do not
                                //modifying the sentence but continue to go down it
                            }
                            else {
                                //if the symbols did match, replace the substring into PLString
                                PLString.replace(st, en, substr.toString());
                            }

                        }

                    }

                    //else if we have to reverse distribute (ex. ((B&C)|D) the D would get distributed)
                    else if ((Character.isLetter(PLString.charAt(index+1)) || PLString.charAt(index+1) == '~') && PLString.charAt(index-1) == ')') {

                        more = true; //something is going to be changed, so we have to check for new possible distributions
                        outer = PLString.charAt(index); //save what the char seen was (| or &)
                        substr = new StringBuilder();

                        //get the string of what we want to distribute over
                        ind3 = backwardParen(PLString.toString(), index-1);
                        sub = PLString.substring(ind3, index);

                        //if this string does not match the regular expression
                        if (!Pattern.matches(regex, sub)) {
                            cnfForm = false; //this means we can modify the PLString                         
                        }

                        //else it must then be in the correct form, so we can skip this distribution
                        else {
                            cnfForm = true;
                            more = false;
                        }

                        //if the string did not match the regular expression
                        if (cnfForm == false) {

                            //if the first element in the parens is a letter with a not, save it as a string
                            if (PLString.charAt(index-3) == '~' && Character.isLetter(PLString.charAt(index-2))) {
                                ind1 = index-3; //set ind1 equal to the ~
                                next1 = "~" + String.valueOf(PLString.charAt(index-2));
                                inner = PLString.charAt(index-4); //save the inner symbol
                            }

                            //if the first element in the parens is a letter, save it as a string (this has to go after the ~letter check)
                            else if (Character.isLetter(PLString.charAt(index-2))) {
                                ind1 = index-2; //set ind1 equal to the letter
                                next1 = String.valueOf(PLString.charAt(index-2));
                                inner = PLString.charAt(index-3); //save the inner symbol
                            }

                            //else if the first element in the parens is another closed paren, get a substring of what it contains
                            else if (PLString.charAt(index-2) == ')') {
                                ind1 = backwardParen(PLString.toString(), index-2); //set ind1 equal to the proper open paren
                                next1 = PLString.substring(ind1, index-1);
                                inner = PLString.charAt(ind1-1); //save the inner symbol
                            }

                            ind2 = ind1;

                            //if outermost symbol is the same as the inner symbol, do not distribute
                            if (outer == inner) {
                                more = false; //no changes should be made. this will soon be checked before PLString is actually modified
                            }


                            //else if the second element in the parens is a letter with a ~, save it as a string
                            if (PLString.charAt(ind2-3) == '~' && Character.isLetter(PLString.charAt(ind2-2))) {
                                next2 = "~" + String.valueOf(PLString.charAt(ind2-2));
                                st = ind2-4; //save the start index
                            }

                            //if the second element in the parens is a letter, save it as a string (this also has to go second now)
                            else if (Character.isLetter(PLString.charAt(ind2-2))) {
                                next2 = String.valueOf(PLString.charAt(ind2-2));
                                st = ind2-3; //save the start index
                            }

                            //else if the second element in the parens starts with a closed paren, get a substring of what it contains
                            else if (PLString.charAt(ind2-2) == ')') {
                                ind1 = backwardParen(PLString.toString(), ind2-2);
                                next2 = PLString.substring(ind1, ind2-1);
                                st = ind1-1; //save the start index
                            }



                            //if the right side element is a letter, save it as a string
                            if (Character.isLetter(PLString.charAt(index+1))) {
                                prev = String.valueOf(PLString.charAt(index+1));
                                en = index+2; //save the end index
                            }

                            //else if the right side element is a letter with a ~, save it as a string
                            else if (PLString.charAt(index+1) == '~' && Character.isLetter(PLString.charAt(index+2))) {
                                prev = "~" + String.valueOf(PLString.charAt(index+2));
                                en = index+3; //save the end index
                            }



                            //concatenate all gathered strings into a substring and update PLString with it
                            substr.append("(" + prev + outer + next1 + ")" + inner + "(" + prev + outer + next2 + ")");


                            if (more == false) {
                                //this is for if the outer and inner symbols are not opposites, then do not
                                //modifying the sentence but continue to go down it
                            }
                            else {
                                //if the symbols did match, replace the substring into PLString
                                PLString.replace(st, en, substr.toString());
                                
                            }

                        }

                    }

                }
                
                index++;  
                
            }

            index = 0;

        }

        //System.out.println("STEP3    " + PLString.toString());
        
        index = 0; //reset index back to 0 to be reused


        //Sort and print out clauses
        char curChar; //holds the value of the current character we are looking at

        //go through final string and delete all parens and replace all | with ,
        for (int z = 0; z < PLString.toString().length(); z++) {
            curChar = PLString.charAt(z);

            //get rid of parentheses
            if (curChar == '(' || curChar == ')') {
                PLString.delete(z, z+1);
            }
        }

        //String array to get each clause, all of which are separated by the & symbol
        String[] clauses = PLString.toString().split("&");

        //go through each of those clauses
        for (String str : clauses) {

            //create String ArrayLists to hold the regular letters and letters with a ~
            ArrayList<String> pos = new ArrayList<String>();
            ArrayList<String> neg = new ArrayList<String>();

            //go through the values of each clause
            for (int x = 0; x < str.length(); x++) {

                //if the character has a ~
                if (str.charAt(x) == '~') {
                    neg.add("~" + str.charAt(x+1)); //add it to the negative arraylist
                }

                //else if the first character is letter (prevents out of bounds for the next else if)
                else if (x == 0 && Character.isLetter(str.charAt(x))) {
                    pos.add(Character.toString(str.charAt(x))); //add it to the positive arraylist
                }

                //else if the character is a letter which is not preceded by a NOT
                else if (x != 0 && Character.isLetter(str.charAt(x)) && str.charAt(x-1) != '~') {
                    pos.add(Character.toString(str.charAt(x))); //add it to the positive arraylist
                }

            }

            //sort the ArrayLists in lexicographical order
            Collections.sort(pos);
            Collections.sort(neg);

            //concatenate the ArrayLists, with the NOT letters coming last
            pos.addAll(neg);
      
            //write to output file (replace the square brackets with nothing and remove white space)
            writer.write(pos.toString().replace("[", "").replace("]", "").replace(" ", ""));
            writer.write("\n");
        }

        writer.close();
    }




    //This method takes in a string and an integer (the index is the string to start at)
    //It will return the index of the closing parentheses that matches that open parentheses
    //at the starting index
    public static int forwardParen(String s, int i) {
        int pIndex = 0;
        Stack<Character> stk = new Stack<Character>(); //stack to keep track of parentheses pairs


        //go through string starting at the sent index,
        for (int x = i; x < s.length(); x++) {

            //if another open paren is seen, push to the stack. (this also adds the initial open paren)
            if (s.charAt(x) == '(') {
                stk.push('(');
            }

            //if a closing paren is seen, pop from the stack
            else if (s.charAt(x) == ')') {
                stk.pop();
            }

            //if the stack is empty, we have found the end parentheses index
            if (stk.isEmpty()) {
                pIndex = x;
                break;
            }

        }

        return pIndex;
    }



    //This method takes in a string and an integer (the index is the string to start at)
    //It will return the index of the open parentheses that matches that closing parentheses
    //at the starting index
    public static int backwardParen(String s, int i) {
        int pIndex = 0;
        Stack<Character> stk = new Stack<Character>(); //stack to keep track of parentheses pairs



        //go through string backwards starting at the sent index, 
        for (int x = i; x >= 0; x--) {

            //if another closing paren is seen, push to the stack. (this also adds the initial closing paren)
            if (s.charAt(x) == ')') {
                stk.push(')');
            }

            //if an open paren is seen, pop from the stack
            else if (s.charAt(x) == '(') {
                stk.pop();
            }

            //if the stack is empty, we have found the final parentheses index
            if (stk.isEmpty()) {
                pIndex = x;
                break;
            }

        }

        return pIndex;
    }

}