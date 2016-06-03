package spelling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word){
		if(!isWord(word)){
		    char[] chars = word.toLowerCase().toCharArray();
		    TrieNode currNode = root;
		    for(int i=0; i< chars.length ; i++ ){
		    	if(i < chars.length - 1){
		    		if(currNode.getChild(chars[i]) != null){
		    			currNode = currNode.getChild(chars[i]);
		    		}else{
		    			currNode = currNode.insert(chars[i]);
		    		}
		    	}else{
		    		if(currNode.getChild(chars[i]) != null){
		    			currNode = currNode.getChild(chars[i]);
		    		}else{
		    			currNode = currNode.insert(chars[i]);
		    		}
		    		currNode.setEndsWord(true);
		    	}
		    }
		    this.size++;
		    return true;
		}else{
			return false;
		}
	}


	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String word) {
	   
	    TrieNode currNode = root;
	    currNode = findWordStem(word, currNode);
	    if(currNode != null && currNode.endsWord()){
	    	return true;
	    }
		return false;
	}


	private TrieNode findWordStem(String word, TrieNode currNode) {
		TrieNode newNode = null;
	    char[] chars = word.toLowerCase().toCharArray();
	    for(int i=0; i< chars.length ; i++ ){
	    	newNode = currNode.getChild(chars[i]);
	    	if(newNode == null){
	    		currNode = newNode;
	    		break;
	    	}else{
	    		currNode = newNode;
	    	}
	    }
		return currNode;
	}

	/** 
	 *  * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param text The text to use at the word stem
     * @param n The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
     * 
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 List<String> predictions = new ArrayList<String>();
    	 if(prefix != null){
    		 TrieNode currNode = root;
    		 TrieNode newNode = null;
    		 currNode = findWordStem(prefix, currNode);
    		 
    		 if(currNode == null){
    			 return Collections.EMPTY_LIST;
    		 }else{
    			 List<TrieNode> queue = new LinkedList<>();
    			 queue.add(currNode);
    			 //Set<Character> characters = currNode.getChild(prefix.charAt(prefix.length()-1)).getValidNextCharacters();
    			 
    			 while(numCompletions > 0 && queue.size() > 0){
    				 newNode = queue.remove(0);
    				 if(newNode.endsWord()){
    					 predictions.add(newNode.getText());
    					 numCompletions --;
    				 }
    				 for(Character character : newNode.getValidNextCharacters()){
    					 queue.add(newNode.getChild(character));
    				 }
    			 }
    		 }
    	 }
         return predictions;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}