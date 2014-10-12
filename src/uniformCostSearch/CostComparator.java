package uniformCostSearch;

import java.util.Comparator;

public class CostComparator implements Comparator<Node> {
	public int compare(Node x, Node y) {
		if(x.totalCost < y.totalCost)
			return -1;
		if(x.totalCost > y.totalCost)
			return 1;
		return 0;
	}
}
