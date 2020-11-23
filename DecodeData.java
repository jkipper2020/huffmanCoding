import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This is the DecodeData.java file that is responsible for opening in a .code and .huff file and printing out the contents.
 * This basically takes in a Huffman language encoded text file and puts it into plain legible English, decoding the file.
 * @author Jonathan Kipper
 * @version 6/7/2020
 */
public class DecodeData
{
    private static Map<String, Character> myMap;

    /**
     * This is the main method that starts the program off to do what it is supposed to do
     * @param args The flags provided from the OS.
     */
    public static void main(String[] args)
    {
        /*
        What this part of the program does is that it asks the user for the file path directory to their file folder, NOT
        to the actual file itself. Uses a while loop and checks to make sure the file path at least exists before moving on.
         */
        Scanner myScanner = new Scanner(System.in);
        String directoryPath = ""; //the path to the folder
        boolean runDirectoryLoop = true;

        while (runDirectoryLoop)
        {
            //gets the file directory path from the user and then formats it
            System.out.println("Please enter the directory Path to the FOLDER in which the files are located (not the specific file!):");
            directoryPath = myScanner.nextLine();

            if (directoryPath.charAt(directoryPath.length() - 1) != '\\')
            {
                directoryPath += "\\";
            }

            try
            {
                if (fileExists(directoryPath))
                {
                    runDirectoryLoop = false;
                }
                else
                {
                    System.out.println("Specified directory does not exist!");
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        //gets the file selection input
        boolean keepRunning = true;
        String file = ""; //the name of the file with the extension
        String fileWithoutExtension = "";

        //input validation loop for the file name. ALSO, this loop oes the checking to see if both files exist or not!
        while (keepRunning)
        {
            System.out.println("Please enter the name of the file you want to open:");
            String input = myScanner.nextLine();
            String tmp = directoryPath + input;

            try
            {
                if (fileExists(tmp)) //if the file has an extension and exists
                {
                    //checks for the correct extensions
                    if (input.contains("."))
                    {
                        //trims just to the extension to make sure extension matches either .huff or .code, if supplied with one
                        String x = input.substring(input.indexOf("."), input.length()); //last part of the input (.extension)
                        String y = input.substring(0, input.indexOf(".")); //first part of the input

                        if (x.contains("huff") || x.contains("code"))
                        {
                            File tmpFile = new File(directoryPath);

                            boolean huffExists = false;
                            boolean codeExists = false;

                            for (File a : Objects.requireNonNull(tmpFile.listFiles()))
                            {
                                if (a.getName().equalsIgnoreCase(y + ".code"))
                                {
                                    codeExists = true;
                                }

                                if (a.getName().equalsIgnoreCase(y + ".huff"))
                                {
                                    huffExists = true;
                                }
                            }

                            if (huffExists && codeExists)
                            {
                                fileWithoutExtension = y;
                                file = input;
                                keepRunning = false;
                            }
                            else
                            {
                                if (input.contains(".huff"))
                                {
                                    System.out.println("There is no .code file!");
                                }
                                else if (input.contains(".code"))
                                {
                                    System.out.println("There is no .huff file!");
                                }
                                else
                                {
                                    System.out.println("File doesn't exist!");
                                }
                            }
                        }
                        else //only runs if it was given an extension AND that extension is NOT .huff or .code
                        {
                            System.out.print("Invalid file extension!: ");
                        }
                    }
                }
                else //IF the file was not given an extension, check through the directory, make sure both exist and get the right file
                {
                    boolean huffExists = false;
                    boolean codeExists = false;
                    String correctName = "";
                    String checkIfSameName = "";

                    File tmpFile = new File(directoryPath);
                    for (File a : Objects.requireNonNull(tmpFile.listFiles()))
                    {
                        //if the file exists which contains the inputed name and has a .code extension, .codeExists
                        if (a.getName().contains(".code") && a.getName().contains(input))
                        {
                            correctName = a.getName(); //ensures that the file extension gets moved correctly
                            codeExists = true;
                        }

                        //makes sure that if you typed in, say "sho" instead of "short", that since it's using contains,
                        //it uses a substring to get rid of the extension and checks for a .huff file that has the exact
                        //same name as the .code file
                        if (!correctName.isEmpty())
                        {
                            checkIfSameName = correctName.substring(0, correctName.indexOf("."));
                        }

                        //if the file exists which contains the inputed name and has a .huff extension, .huffExists
                        if (a.getName().equalsIgnoreCase(checkIfSameName + ".huff"))
                        {
                            huffExists = true;
                        }
                    }

                    //if both the .huff and .code files exist (when the given file had no extension), add the .code extension, end loop
                    if (codeExists && huffExists)
                    {
                        fileWithoutExtension = checkIfSameName;
                        keepRunning = false;
                        file = correctName; //only happens IF there is a .code AND .huff file with the same name
                    }
                    else //if the file doesn't exist
                    {
                        System.out.println("Either the .huff, the .code, or both of the files with the inputted name do not exist.");
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        //sets up the locationPath for the .code file type
        String pathLocation = directoryPath + fileWithoutExtension + ".code";
        File myFile = new File(pathLocation);

        myMap = new TreeMap<>();

        String huffFileValue = ""; //stores the .huff file value so it can get decoded

        try
        {
            Scanner read = new Scanner(myFile);

            //This part reads in from the .code file, and places all of the contents of it in a map after converting the integer values into chars.
            while (read.hasNext())
            {
                int myVal = read.nextInt();
                char val = Arrays.toString(Character.toChars(myVal)).charAt(1); //converts the integer values to a char
                String second = read.next();
                myMap.put(second, val); //puts it into the map
            }

            //closes the scanner so it's reusable
            read.close();

            //resets the path location and file to open up the .huff file next. since it's just one line of text, no need for a loop
            pathLocation = directoryPath + fileWithoutExtension + ".huff";
            myFile = new File(pathLocation);
            read = new Scanner(myFile);

            //stores the value of the file as a string
            huffFileValue = read.nextLine();
            System.out.println("HuffFile: " + huffFileValue);

            read.close(); //closes the scanner
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //decrypts the code and prints it out
        String data = huffFileValue;
        String mini = "";

        for (int i = 0; i < data.length(); i++)
        {
            mini = mini + data.charAt(i);
            if (myMap.containsKey(mini))
            {
                System.out.print(myMap.get(mini));
                mini = "";
            }
        }
    }

    //This method takes in a string which is a file path and checks to see if it exists or not and returns a boolean
    //value where true means the file does exist and false means it does not exist.
    private static boolean fileExists(String path) throws IOException
    {
        File f = new File(path);
        return f.exists();
    }
}