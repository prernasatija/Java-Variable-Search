package prerna.satija.code;

//Author: Prerna Satija
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/* Query Server program implements a Compressed Trie
that gives results in sub-linear time */
public class QueryServer {
	
	IndexConstruction indexStructure;
	
	// TrieNode is an internal Node class for the trie data structure
	private class TrieNode {
		int index;
		int start;
		int end;
		
		List<Object> children;
		List<Integer> topResults;

		TrieNode () {
			children = new ArrayList<>();
		}
		
		TrieNode (int id, int start, int end) {
			this.index = id;
			this.start = start;
			this.end = end;
			children = new ArrayList<>();
		}
		
		String getString() {
			return indexStructure.get(index).getName().substring(start, end+1);
		}
		
		public String toString() {
			return this.getString();
		}
		
		// Updates the node. Used when parent and child node can be compressed
		void updateNode(int end, List<Object> children) {
			this.end = end;
			this.children = children;
		}
	}
	
	private TrieNode head;
	
	public QueryServer (IndexConstruction index) {
		indexStructure = index;
		head = new TrieNode();
		addAll();
		compress();
	}
	
	// adds the string with corresponding id to the trie
	@SuppressWarnings("unchecked")
	private void add(String str, int id, int index) {
		
		TrieNode cur = head;
		for(int i = 0; i < str.length(); i++) {
			List<Object> childNodes = cur.children;
			boolean flag = true;
			// iterates through all the children to find a match
			for(Object o : childNodes) {
				if(o instanceof TrieNode) {
					TrieNode n = (TrieNode)o;
					if(n.getString().equals(str.substring(i, i+1))) {
						// match found: cur node updated
						cur = n;
						flag = false;
						break;
					}
				}
			}
			if(flag){
				// new node to be created because match not found.
				TrieNode newNode = new TrieNode(id, index+i, index+i);
				childNodes.add(newNode);
				cur = newNode;
			}
		}
		
		// str is added to trie.
		// Now add its index to cur in an arrayList
		List<Object> childNodes = cur.children;
		ArrayList<Integer> ids = null;
		for(Object obj : childNodes) {
			if(obj instanceof TrieNode) {
				continue;
			}
			else {
				// array already exists.
				ids = (ArrayList<Integer>)obj;
				break;
			}
		}
		if(ids == null) {
			// create new array and add it as cur node's child
			ids = new ArrayList<Integer>();
			cur.children.add(ids);
		}
		ids.add(id);
	}
	
	// Compresses the Trie to form Compressed Trie.
	private void compress() {
		Stack<Object> s = new Stack<>();
		for(Object o : head.children) {
			// add all children of head to stack.
			s.add(o);
		}
		while(!s.isEmpty()) {
			Object cur = s.pop();
			if(cur instanceof TrieNode) {
				TrieNode curNode = (TrieNode)cur;
				List<Object> childNodes = curNode.children;
				while(childNodes.size() == 1) {
					Object obj = childNodes.get(0);
					if(obj instanceof TrieNode) {
						// if the node has only one child and that is a TrieNode,
						// then we can compress
						TrieNode nextNode = (TrieNode)obj;
						curNode.updateNode(nextNode.end, nextNode.children);
						childNodes = curNode.children;
					}
					else {
						// but if it is an arrayList then don't compress
						break;
					}
				}
				
				// Fix: To make it more efficient.
				setTopResults(curNode);
				
				for(Object n : childNodes) {
					// add all children to stack
					s.add(n);
				}
			}
		}
	}
	
	// 
	private void setTopResults(TrieNode node) {
		Set<Integer> unique = extractIds(node);
		List<Pair> pairs = indexStructure.getSortedPairs(new ArrayList<Integer>(unique));
		if(node.topResults == null) {
			node.topResults = new ArrayList<>();
		}
		for(int i = 0; i < 10 && i < pairs.size(); i++) {
			node.topResults.add(pairs.get(i).getId());
		}
	}
	
	// adds all the str and _str to the Trie
	private void addAll() {
		for(int i = 0; i < indexStructure.size(); i++) {
			String str = indexStructure.getString(i);
			add(str, i, 0);
			int mainIndex = 0;
			int index = 0;
			while((index = str.indexOf('_')) >= 0) {
				// for each underscore in str
				str = str.substring(index);
				mainIndex += index;
				add(str, i, mainIndex);
				str = str.substring(1);
				mainIndex++;
			}
		}
	}
	
	// finds str and _str in the list and returns their combined results
	public List<Integer> find(String str) {
		Set<Integer> unique = findAll(str);
		if(str.startsWith("_")) {
			//called when str starts with underscore _
			removeUnwantedIds(unique, str);
		}
		unique.addAll(findAll("_" + str));
		return new ArrayList<Integer>(unique);
	}
	
	// Fix: for underscore Test Case : 2
	// removes "new_dog" for query = "_dog" but keeps "_dog_old"
	private void removeUnwantedIds(Set<Integer> unique, String str) {
		Iterator<Integer> itr = unique.iterator();
		while(itr.hasNext()) {
			Integer id = itr.next();
			String fromTrie = indexStructure.getString(id);
			if(!fromTrie.startsWith(str)) {
				itr.remove();
			}
		}
	}
	
	// tree search
	private Set<Integer> findAll(String str) {
		TrieNode cur = head;
		while(str.length() >0) {
			boolean found = false;
			for(Object obj : cur.children) {
				if(obj instanceof TrieNode) {
					TrieNode n = (TrieNode)obj;
					String current = n.getString();
					String prefix;
					String temp;
					if(current.length() > str.length()) {
						temp = current;
						prefix = str;
						//found = current.startsWith(str);
					}
					else {
						temp = str;
						prefix = current;
						//found = str.startsWith(current);
					}
					if(found = temp.startsWith(prefix)) {
						str = str.substring(prefix.length());
						cur = n;
						break;
					}
				}
			}
			if(!found) {
				return new HashSet<Integer>(); // no match found. Return empty set
			}
		}	
		return new HashSet<>(cur.topResults);
	}
	
	//depth first search
	@SuppressWarnings("unchecked")
	private Set<Integer> extractIds(TrieNode node) {
		HashSet<Integer> uniqueId = new HashSet<>();
		Stack<TrieNode> s = new Stack<>();
		s.add(node);
		while(!s.isEmpty()) {
			TrieNode cur = s.pop();
			uniqueId.add(cur.index);
			for(Object obj : cur.children) {
				if(obj instanceof TrieNode) {
					s.add((TrieNode)obj);
				}
				else {
					List<Integer> list = (ArrayList<Integer>)obj;
					for(int i : list) {
						uniqueId.add(i);
					}
				}
			}
		}
		return uniqueId;
	}
}
