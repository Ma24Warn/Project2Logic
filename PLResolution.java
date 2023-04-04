import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PLResolution {
    


    public static void main(String args[]) throws FileNotFoundException {
        ArrayList<ArrayList<String>> clauses = new ArrayList<ArrayList<String>>();
        ArrayList<String> innerList;
        String line; 
        StringBuilder tempString;
        char[] tempArray;

        //input test cases
        File test1 = new File("TestCases/restest1.txt");
        File test2 = new File("TestCases/restest2.txt");
        File test3 = new File("TestCases/restest3.txt");


        //output text files
        File output1 = new File("TestCases/restest1output.txt");
        File output2 = new File("TestCases/restest2output.txt");
        File output3 = new File("TestCases/restest3output.txt");


        //read in from file
        
        Scanner scan = new Scanner(test1);

        //get each line of the file and add its individual characters to their respective String ArrayList and
        //add all of those to another ArrayList (This is the best way I could think of that would make editing
        //the clauses easier since Strings are immutable)
        while (scan.hasNextLine()) {
            innerList = new ArrayList<String>();

            line = scan.nextLine();
            tempArray = line.toCharArray();

            for (int x = 0; x < tempArray.length; x++) {

                //ignore commas
                if (tempArray[x] == ',') {

                }
                
                //if its a not, get both the not and next letter
                else if (tempArray[x] == '~') {
                    tempString = new StringBuilder();
                    tempString.append(tempArray[x]);
                    tempString.append(tempArray[x+1]);
                    innerList.add(tempString.toString());
                }

                //its a normal letter and is the first letter (prevents out of bounds for next else if)
                else if (x == 0 && Character.isLetter(tempArray[x])) {
                    innerList.add(String.valueOf(tempArray[x]));
                }

                else if (Character.isLetter(tempArray[x]) && tempArray[x-1] != '~') {
                    innerList.add(String.valueOf(tempArray[x]));
                }
                


                System.out.println(innerList.toString());
            }

            clauses.add(innerList);

        }
        scan.close();


        //FOR TESTING (DELETE WHEN DONE!!!!!!!!!!)
        System.out.println(clauses.toString());

        
        PLRes(clauses);

    }


    public static void PLRes (ArrayList<ArrayList<String>> c) {
        ArrayList<String> cur1, cur2;
        boolean more;
        String curElem;


        //so i think i have to find VAR and ~VAR and remove them and 
        //combine their neighbors together with an OR
        //example from the slides says (A|P),(B|~P) turns into (A|B)

        //so maybe if you find two that match, remove the VAR and ~VAR from their respective strings
        //then combine the remaining strings into the first spot or something????????????????????

        //SHOULD I USE STRINGBUILDERS AGAIN? ARRAYLIST OF STRINGBUILDERS???
        //OR MAYBE AND ARRAYLIST OF CHAR ARRAYLISTS????????????????????????

        
        while (true) {

            more = false;

            


            //for each inner arraylist
            for (int x = 0; x < c.size(); x++) {
                cur1 = c.get(x);

                //for each element in that arraylist
                for (int y = 0; y < cur1.size(); y++) {

                    //use my getOpposite method to get the opposite string we are looking for

                    
                    curElem = getOpposite(cur1.get(y)); //get the string we are looking for (opposite of current string)



                    

                    //check every other array to see if it contains the value we are looking for
                    //z=x+1 we only need to check the next ArrayList and beyond, doing all arraylists for each
                    //character would just review duplicates. RIGHT???????????????????????????????????????????
                    for (int z = x+1; z < c.size(); z++) {
                        cur2 = c.get(z);

                        //skip checking within the same arraylist SHOULD I DO THIS??????????????????????????????????
                        if (z == x) {
                            break;
                        }

                        if (cur2.contains(curElem)) {
                            //use Contains() method to see if an arraylist contains what were looking for
                            //then delete the current string we are searching with
                            //then delete the opposite one we have found
                            //JOIN THESE TWO TOGETHER?????????????????????????????????????????????????????????????
                            
                            //remove the element and its opposite from their respective arraylists
                            cur1.remove(y);
                            cur2.remove(cur2.indexOf(curElem));
                            
                            //append the second arraylist to the original
                            cur1.addAll(cur2);

                            //remove the second arraylist
                            c.remove(cur2);

                            //if something is modified, we have to re look at possible resolutions again
                            more = true;

                        }

                    }







                }


            }





            //if there are no more clauses to resolve
            if (more == false) {

                break;
            }

            //produces an empty clause
            //if () {
                //make file ONLY CONTAIN "Contradiction"
            //    break;
            //} 



        }

        

        //WHAT ABOUT TWO ~A and one A. Do THEY ALL CANCEL OUT????????????????? or just one per?????



        System.out.println(c.toString());


    }


    public static String getOpposite(String s) {
        char c = s.charAt(0);

        //if the sent string is a letter with a NOT
        if (c == '~' && s.length() == 2) {
            return String.valueOf(s.charAt(1)); //return the letter without the NOT
        }

        //if the sent string is a single letter
        else if (Character.isLetter(c) && s.length() == 1) {
            return "~"+s.charAt(0);
        }

        else {
            return "INVALID";
        }



    }







    
}
