/**
 * This is the Node.java class, which is the Node that is used in a binary tree for the priorityqueue. This node contains
 * two pieces of data, a Character and that character's frequency. It also has pointer nodes to the left and right child
 * nodes.
 * @author Jonathan Kipper
 * @version 6/9/2020
 */
public class Node implements Comparable<Node>
{
    public Character character; //the data stored at this node
    public Integer frequency; //the data stored at this node
    public Node left; //reference to the left subtree
    public Node right; //reference to the right subtree

    /**
     * Creates a leaf node with null parameters
     */
    public Node()
    {
        character = null;
        frequency = null;
        left = null;
        right = null;
    }

    /**
     * This constructor takes in a Character and an Integer and sets the node equal to them, throws exception if they
     * are invalid data. creates a leaf node with that data.
     * @param data a character
     * @param frequency an Integer
     */
    public Node(Character data, Integer frequency)
    {
        //checks for null errors
        if (data == null)
        {
            throw new NullPointerException("The Character passed to the node is null!");
        }

        if (frequency == null)
        {
            throw new NullPointerException("The Integer passed to the node is null!");
        }

        this.character = data;
        this.frequency = frequency;
        left = null;
        right = null;
    }

    /**
     * This method checks to see if the Node is a leaf or not by determining if the child nodes are null or not
     * @return true if it's a leaf, false if it's not
     */
    public boolean isLeaf()
    {
        return left == null && right == null;
    }

    @Override
    public String toString()
    {
        return "Node{" + "character=" + character + ", frequency=" + frequency + '}';
    }

    @Override
    public int compareTo(Node o)
    {
        return this.frequency.compareTo(o.frequency);
    }
}