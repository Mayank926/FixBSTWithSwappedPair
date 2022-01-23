package gfg.FixBST;

// { Driver Code Starts
//Initial Template for Java

import java.util.LinkedList;
import java.util.Queue;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Node
{
    int data;
    Node left;
    Node right;
    Node(int data)
    {
        this.data = data;
        left=null;
        right=null;
    }
}
class pair
{
    int first;
    int second;
    pair(int first , int second)
    {
        this.first = first;
        this.second = second;
    }
}
class GfG {

    static Node buildTree(String str)
    {

        if(str.length()==0 || str.charAt(0)=='N'){
            return null;
        }

        String ip[] = str.split(" ");
        // Create the root of the tree
        Node root = new Node(Integer.parseInt(ip[0]));
        // Push the root to the queue

        Queue<Node> queue = new LinkedList<>();

        queue.add(root);
        // Starting from the second element

        int i = 1;
        while(queue.size()>0 && i < ip.length) {

            // Get and remove the front of the queue
            Node currNode = queue.peek();
            queue.remove();

            // Get the current node's value from the string
            String currVal = ip[i];

            // If the left child is not null
            if(!currVal.equals("N")) {

                // Create the left child for the current node
                currNode.left = new Node(Integer.parseInt(currVal));
                // Push it to the queue
                queue.add(currNode.left);
            }

            // For the right child
            i++;
            if(i >= ip.length)
                break;

            currVal = ip[i];

            // If the right child is not null
            if(!currVal.equals("N")) {

                // Create the right child for the current node
                currNode.right = new Node(Integer.parseInt(currVal));

                // Push it to the queue
                queue.add(currNode.right);
            }
            i++;
        }

        return root;
    }
    static void printInorder(Node root)
    {
        if(root == null)
            return;

        printInorder(root.left);
        System.out.print(root.data+" ");

        printInorder(root.right);
    }

    public static boolean isBST(Node n, int lower, int upper)
    {
        if(n==null)
            return true;
        if( n.data <= lower || n.data >= upper )
            return false;
        return (  isBST( n.left, lower, n.data )  &&  isBST( n.right, n.data, upper )  );
    }

    public static boolean compare( Node a, Node b, ArrayList<pair> mismatch )
    {
        if( a==null && b==null ) return true;
        if( a==null || b==null ) return false;

        if( a.data != b.data )
        {
            pair temp = new pair(a.data,b.data);
            mismatch.add(temp);
        }


        return ( compare( a.left, b.left, mismatch ) && compare( a.right, b.right, mismatch ) );
    }

    public static void main (String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int t=Integer.parseInt(br.readLine());

        while(t-- > 0)
        {
            String s = br.readLine();
            Node root = buildTree(s);
            Node duplicate = buildTree(s);

            Solution ob = new Solution();

            Node temp = ob.correctBST(root);
            if(temp != root)
                System.out.println(0);

            // check 1: is tree now a BST
            if(isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE) == false)
            {
                System.out.println(0);
                continue;
            }

            // check 2: comparing with duplicate tree


            ArrayList<pair> mismatch = new ArrayList<pair>();
            // an arraylist to store data of mismatching nodes

            if( compare( root, duplicate, mismatch) == false)
            {
                // false output from this function indicates change in structure of tree
                System.out.println(0);
                continue;
            }

            // finally, analysing the mismatching nodes
            if( mismatch.size() !=2 || mismatch.get(0).first!=mismatch.get(1).second || mismatch.get(0).second!=mismatch.get(1).first )
                System.out.println(0);
            else
                System.out.println(1);



        }
    }
}

// } Driver Code Ends


//User function Template for Java

class Solution
{
    //Function to fix a given BST where two nodes are swapped.
    public Node correctBST(Node root)
    {
        inorder(root);
        System.out.println();
       Triplets tp = new Triplets(null,new Node(Integer.MIN_VALUE),new Node(Integer.MIN_VALUE));
        List<NodeNeighbours> ls = new ArrayList<>(4);
        traverse(root,tp,ls);
        if(tp.middle.data>tp.last.data)
            ls.add(new NodeNeighbours(tp.last,tp.middle.data,Integer.MAX_VALUE));
        ls.stream().forEach(e-> System.out.println("node data "+e.node.data));
        int temp;
        if(ls.size()>1){
            NodeNeighbours first= ls.get(0);
            temp=ls.get(0).node.data;
            List<NodeNeighbours> swapEntryList=ls.parallelStream().filter(e -> e.node.data> first.predecessor && e.node.data< first.successor).collect(Collectors.toList());
            if(swapEntryList.size()==0){
                ls.get(0).node.data=ls.get(1).node.data;
                ls.get(1).node.data=temp;
            }else{
                Node swapNode = swapEntryList.get(0).node;
                ls.get(0).node.data=swapNode.data;
                swapNode.data=temp;
            }
            swapEntryList.stream().forEach(System.out::println);
        }
        inorder(root);
        System.out.println();
        return root;
    }

    private Node getRightMostNode(Node root) {
        if(root==null)
            return root;
        while(root.right!=null)
            root=root.right;
        return root;
    }


    void traverse(Node root,Triplets triplet,List<NodeNeighbours> ls){
        if(root==null)
            return;
        traverse(root.left,triplet,ls);
        triplet.first= triplet.middle;
        triplet.middle=triplet.last;
        triplet.last=root;
        if((triplet.middle.data> triplet.first.data && triplet.middle.data> triplet.last.data) || (triplet.middle.data< triplet.first.data && triplet.middle.data< triplet.last.data)) {
                ls.add(new NodeNeighbours(triplet.middle,triplet.first.data,triplet.last.data));
        }
        traverse(root.right,triplet,ls);
    }

    void inorder(Node root){
        if(root==null)
            return;
        inorder(root.left);
        System.out.print(" "+root.data);
        inorder(root.right);
    }
}

class Triplets{
    Node first;
    Node middle;
    Node last;

    public Triplets(Node first, Node middle, Node last) {
        this.first = first;
        this.middle = middle;
        this.last = last;
    }
}
class NodeNeighbours{
    Node node;
    int predecessor;
    int successor;

    public NodeNeighbours(Node node, int predecessor, int successor) {
        this.node = node;
        this.predecessor = predecessor;
        this.successor = successor;
    }
}
class DiffStatus{
    Node node;
    boolean diffWithParent;
    boolean diffWithRoot;

    public DiffStatus(Node node, boolean diffWithParent, boolean diffWithRoot) {
        this.node = node;
        this.diffWithParent = diffWithParent;
        this.diffWithRoot = diffWithRoot;
    }
}
