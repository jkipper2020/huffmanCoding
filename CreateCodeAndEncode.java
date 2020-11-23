import java.io.*;
import java.util.*;

/**
 * This file reads in from a .txt file of which it asks the user for the filepath, loads all of the information from it, and uses a PriorityQueue and a Tree with
 * Custom Node's in the Node.java class to then generate and create the Huffman code words.
 * @author Jonathan Kipper
 * @version 6/7/2020
 */
public class CreateCodeAndEncode
{
    private static Map<Character, String> secondMap;

    /**
     * This is the main method that starts the program off to do what it is supposed to do
     * @param args The flags provided from the OS.
     */
    public static void main(String[] args)
    {
        Map<Character, Integer> myMap = new TreeMap<>(); //initializes the map
        secondMap = new TreeMap<>(); //initializes the second map
        List<Character> myList = new ArrayList<>(); //creates the list

        //input validation loop to force the users to input an extension with .txt, also extracts the directoryPath
        //from the file path extension
        String filePath = "";
        String directoryPath = "";
        boolean keepRunning = true;
        Scanner myScanner = new Scanner(System.in);

        while (keepRunning)
        {
            System.out.println("Please provide the filepath to the file and make sure to include the '.txt' extension");
            String z = myScanner.nextLine();

            //uses a substring to check the extension
            if (z.substring((z.length() - 4)).equalsIgnoreCase(".txt"))
            {
                filePath = z;

                for (int i = z.length()-1; i >= 0; i--)
                {
                    if (z.charAt(i) == '\\')
                    {
                        directoryPath = z.substring(0, i);
                        i = -1;
                    }
                }

                //terminates the loop
                keepRunning = false;
            }
            else
            {
                System.out.print("Invalid Input! ");
            }
        }

        try
        {
            //makes the file
            File myFile = new File(filePath);
            FileInputStream x = new FileInputStream(myFile);

            //reads in from the file character by character
            while (x.available() > 0)
            {
                Character c = (char) x.read();
                myList.add(c);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //loads all of the characters into a map with their respective frequency
        for (Character a : myList)
        {
            Integer value = myMap.get(a);

            if (!myMap.containsKey(a))
            {
                myMap.put(a, 1);
            }
            else
            {
                myMap.put(a, (value + 1));
            }
        }

        //prints out the map for debugging purposes
        System.out.println(myMap);

        PriorityQueue<Node> myQueue = new PriorityQueue<>();

        //creates a node for each Character/value in the map and puts it into the priority Queue
        for (Character a : myMap.keySet())
        {
            Node b = new Node(a, myMap.get(a));
            myQueue.add(b);
        }

        //removes and re-adds nodes to form the tree, remaining node is the root of the tree
        while (myQueue.size() > 1)
        {
            //removes first two nodes
            Node firstRemoved = myQueue.remove();
            Node secondRemoved = myQueue.remove();

            //the value for the new node being a sum of the first two removed node's respective frequencies
            int val = firstRemoved.frequency + secondRemoved.frequency;

            //creates the new node, set's its frequency to the value of the first two, then ties its left/right node values
            //to those originally removed nodes
            Node replacementNode = new Node();
            replacementNode.frequency = val;
            replacementNode.left = firstRemoved;
            replacementNode.right = secondRemoved;

            myQueue.add(replacementNode);
        }

        //does the recursion on the priorityQueue
        huffmanStringSplit(myQueue.peek(), "");

        //prints out the map for debugging purposes
        System.out.println(secondMap);

        //creates and writes to the .CODE file and the .HUFF file
        try
        {
            //PrintWriter for easily writing to a file
            String output = directoryPath + "\\FILENAME.code";
            PrintWriter myPrinter = new PrintWriter(output);

            //goes through the second map
            for (Character a : secondMap.keySet())
            {
                int ascii = (int) a;
                myPrinter.println(ascii);
                myPrinter.println(secondMap.get(a));
            }

            //closes printer
            myPrinter.close();

            //reopens the printer to the new file
            output = directoryPath + "\\FILENAME.huff";
            myPrinter = new PrintWriter(output);

            //outputs every value in the original list to the .huff file with the characters converted to huff code
            for (Character a : myList)
            {
                myPrinter.print(secondMap.get(a));
                myPrinter.flush();
            }

            //closes the printer after it's written
            myPrinter.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
     * This method recursively goes down the tree of nodes and assigns a value of 1 if it was on the left or 0 if it was
     * on the right. Then, it stores the result in a map. Starts off with a default empty string as an input, and also
     * the node to split through.
     */
    private static void huffmanStringSplit(Node x, String value)
    {
        if (x == null)
        {
            return;
        }

        if (x.isLeaf()) //if it's a leaf or not
        {
            secondMap.put(x.character, value);
        }
        else
        {
            huffmanStringSplit(x.right, (value + "1"));
            huffmanStringSplit(x.left, (value + "0"));
        }
    }
}