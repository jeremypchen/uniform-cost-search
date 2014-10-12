package uniformCostSearch;

import java.io.*;
import java.util.*;

public class UniformCostSearch {
	public int A_Row, A_Col, B_Row, B_Col, size;
	public Node start;

	public UniformCostSearch() throws IOException{
		List<Node> finalList = new ArrayList<Node>();
		boolean success = false;

		readInputFile();
		Node goal = search();
		if(goal!=null){
			success = true;
			finalList.add(goal);
			Node parent = goal.Parent;
			while (parent != null){
				finalList.add(parent);
				parent = parent.Parent;
			}
		}
		outputFile(finalList, success);
	}

	public void readInputFile() throws IOException{
		String newLine;
		String inputPath = "src/input.txt";
		FileReader fr = new FileReader(inputPath);
		BufferedReader br = new BufferedReader(fr);

		newLine = br.readLine();
		StringTokenizer st = new StringTokenizer(newLine);
		size = Integer.parseInt(st.nextToken());

		newLine = br.readLine();
		st = new StringTokenizer(newLine);
		A_Row = Integer.parseInt(st.nextToken());
		A_Col = Integer.parseInt(st.nextToken());

		newLine = br.readLine();
		st = new StringTokenizer(newLine);
		B_Row = Integer.parseInt(st.nextToken());
		B_Col = Integer.parseInt(st.nextToken());
	}

	public void outputFile(List<Node> finalList, boolean success){
		PrintWriter writer;
		Node nodeToPrint;
		// print finalQueue
		try {
			writer = new PrintWriter("src/output.txt");
			if(success){
				System.out.println(finalList.get(0).totalCost);
//				writer.println(finalList.get(0).totalCost);
				while (!finalList.isEmpty()){
					nodeToPrint = (Node) finalList.remove(finalList.size()-1);
					System.out.println(nodeToPrint.getCoordinates());
//					writer.println(nodeToPrint.getCoordinates()); // + " " + nodeToPrint.totalCost);
				}
			} else {
				System.out.println("-1");
//				writer.println("-1");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// If A moves up, B moves right 	[cost: 3]
	// If A moves left, B moves up		[cost: 4]
	// If A moves right, B moves down	[cost: 5]
	// If A moves down, B moves left	[cost: 6]

	public void generateChildren(Node current){		
		int currentRow = current.row;
		int currentColumn = current.column;

		// A move up, B move right [if A not at top and B not at right-most], cost: 3
		if (currentRow!=1 && B_Col!=size){
			current.addChild(new Node(currentRow-1, currentColumn, 3, current, B_Row, B_Col+1));
		}
		// A move left, B move up [if A not at left-most and B not at top], cost: 4 
		if (currentColumn!=1 && B_Row!=1){
			current.addChild(new Node(currentRow, currentColumn-1, 4, current, B_Row-1, B_Col));
		}
		// A move right, B move down [if A not at right-most and B not at bottom], cost: 5
		if (currentColumn!=size && B_Row!=size){
			current.addChild(new Node(currentRow, currentColumn+1, 5, current, B_Row+1, B_Col));
		}
		// A move down, B move left [if A is not at bottom and B not at left-most], cost: 6
		if (currentRow!=size && B_Col!=1){
			current.addChild(new Node(currentRow+1, currentColumn, 6, current, B_Row, B_Col-1));
		}
	}

	public boolean isNotRepeat(Node n, List<Node> checked){
		for (Node checkedNode : checked){
			if (n.row == checkedNode.row && n.column == checkedNode.column)
				return false;
		}
		return true;
	}

	public Node search(){
		Comparator<Node> comparator = new CostComparator();
		PriorityQueue<Node> searchQueue = new PriorityQueue<Node>(1, comparator);
		List<Node> checked = new ArrayList<Node>();
		Node next;

		searchQueue.add(new Node(A_Row, A_Col, B_Row, B_Col)); 				// Initialize Queue with root node (built from start state)
		while (!searchQueue.isEmpty()){ 									// Repeat until (Queue empty)
			next = searchQueue.poll(); 										// Remove first node from front of queue
			B_Row = next.b_row;												// Get Agent B coordinates based on current node
			B_Col = next.b_col;
			if (next.row == B_Row && next.column == B_Col) 					// If Agent A is at same place as Agent B
				return next;												// Return this node - success.

			generateChildren(next);											// Generate children nodes
			for (Node n : next.Children){
				if (isNotRepeat(n, checked))								// Reject children that have already been considered to avoid loops
					searchQueue.add(n);										// Add these children nodes to queue
			}
			checked.add(next);
		} 
		return null;														// Node not found, return nothing
	}

	public static void main(String arg[]) throws IOException{
		new UniformCostSearch();
	}
}
