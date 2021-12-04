package edu.wm.cs.cs301.janellekrupicka.generation;

import java.util.*;

public class MazeBuilderBoruvka extends MazeBuilder {
	
	private static Hashtable<String, Integer> edgeWeights;
	private static Hashtable<Integer, String> nodeIDsToCheapestEdge;
	
	
	/**
	 * setEdgeWeights works by creating a list of possible weights that
	 * is the range of the number of edges in a maze. Then, it shuffles
	 * that list. Then, there's a nested for loop that iterates through 
	 * all of the cells in the maze. It sets the East and South edges weights
	 * by indexing the shuffled list of possible weights with an counter
	 * and then increments that counter. It uses a dictionary that represents
	 * the wallboard as a string as keys and the edgeweight taken from 
	 * indedexing the shuffled edgeweights as values. It doesn't return anything,
	 * only sets up the dictionary edgeWeights already stored as an instance
	 * variable.
	 * 
	 */
	private void setEdgeWeights() {
		int numberOfEdges = width*(height-1) + height*(width-1);
		// create list of possible weights, range 0 to number of edges in mst for maze size
		List<Integer> possibleWeights = new ArrayList<>();
		for(int weight=0; weight<=numberOfEdges; weight++) { // check loop
			possibleWeights.add(weight);
		}
		
		// randomize weights to assign as values to wallboards by shuffling
		Collections.shuffle(possibleWeights);
		
		// assign unique weight to every wallboard from shuffled list of possible weights
		int possibleWeightsIndex = 0;
		for (int col=1;col<=width;col++) {
			for (int row=1;row<=height;row++) {
				if(col!=width-1) {
					// ensure that the weight for a cell's east wallboard matches the cell to its right's west wallboard
					edgeWeights.put(col+row+"E", possibleWeights.get(possibleWeightsIndex));
					int nextCol = col+1;
					edgeWeights.put(nextCol+row+"W", possibleWeights.get(possibleWeightsIndex));
				}
				// increment possibleWeightsIndex to get a new random weight from possibleWeights list
				possibleWeightsIndex++;
				if(row!=height-1) {
					// ensure that the weight for a cell's south wallboard matches the cell to its right's north wallboard
					edgeWeights.put(col+row+"S", possibleWeights.get(possibleWeightsIndex));
					int nextRow = row+1;
					edgeWeights.put(col+nextRow+"N", possibleWeights.get(possibleWeightsIndex));
				}
			}
		}								
	}
	/**
	 * getEdgeWeights works by converting the Wallboard passes as a parameter
	 * into a String representation. Then, it uses that String representation
	 * as the key for the dictionary edgeWeights, which stores the edgeWeights
	 * as values. It returns the int edgeweight found as the value for the
	 * wallboard string representation key.
	 * 
	 * @return integer of edge weight
	 */
	public int getEdgeWeight(Wallboard w) {
		setEdgeWeights();
		// initialize string to then add direction to string 
		// and use string as key to get valuee from edgeWeights dictionary
		String direction = "N";
		int x = w.getX();
		int y = w.getY();
		
		// keys in edgeWeights dictionary stored as strings with xydirection
		// for example a wallboard(0, 0, north) would be a key of 00N
		if(w.getDirection()==CardinalDirection.North) {
			direction = "N";
		}
		if(w.getDirection()==CardinalDirection.East) {
			direction = "E";
		}
		if(w.getDirection()==CardinalDirection.South) {
			direction = "S";
		}
		if(w.getDirection()==CardinalDirection.West) {
			direction = "W";
		}
		return edgeWeights.get(x+y+direction);
	}
	
	/** 
	 * initialzeNodeIDs returns a 2D int array with the number of cells in the maze
	 * as a range in the array. It uses a for loop to add values to the array.
	 * It is called in the constructor for MazeBuilderBoruvka.
	 * 
	 * @return 2D int array with node IDs
	 */
	private int[][] initializeNodeIDs() {
		
		// to use in generatePathways
		int[][] nodeIDs = new int[width][height];
		
		// get id number in range 1 to area of maze
		int idNumber = 1;
		
		// set id number for nodeIDs list
		for(int col=0;col<width;col++) {
			for(int row=0;row<height;row++) {
				nodeIDs[col][row] = idNumber;
				idNumber++;
			}
		}
		return nodeIDs;
	}
	/**
	 * setUpNodeIDsToCheapestEdge uses a dictionary nodeIDsToCheapestEdge to store
	 * node IDs as keys and string representations of wallboards as values.
	 * It iterates through every cell to find the cheapest edge for that cell, 
	 * and if that cell has been merged it considers the node IDs to determine
	 * where to find the cheapest edge. There are four while loops inside of the 
	 * nested for loops that look at every cardinal direction.
	 * 
	 */
	private void setUpNodeIDsToCheapestEdge(int[][] nodeIDs) {
		
		// initialize integer minimum and consider 0 as null case
		int minimum = 0;
		
		// wallboard stored as string in form 00N
		String minWallboard = "";
	
		// nested for loop iterates through every cell in the maze 
		// by way of nodeIDs 2D int array
		for (int col=0;col<width;col++) {
			for (int row=0;row<height;row++) {
				
				// save current ID to use in nodeIDsToCheapestEdge dictionary
				// find minimum weight wallboard/edge and then set that as value for key currentID
				int currentID = nodeIDs[col][row];
				
				// while loop to move all the way east possible in current component
				// component can be one cell or merged cells
				// evaluates if wallboard weight on east side of component is the current smallest weight found
				while(true) {
					
					// if east wallboard at cell is not border and exists
					if(floorplan.canTearDown(new Wallboard(col, row, CardinalDirection.East))) {
						
						// if the next cell over doesn't have the same node id as current cell
						// aka if the next cell over isn't apart of the same component as current cell
						// then look at east wallboard and determine if its weight makes it the minimum
						if(nodeIDs[col][row]!=nodeIDs[col+1][row]) {
							if(minimum==0 || minimum > getEdgeWeight(new Wallboard(col,row, CardinalDirection.East))) {
								minimum = getEdgeWeight(new Wallboard(col,row, CardinalDirection.East));
								minWallboard = col+row+"E";
							}
							// break: move onto next direction
							break;
						}
						// if cells are part of the same component, move to next cell
						else if(nodeIDs[col][row]==nodeIDs[col+1][row]) {
							 col = col+1;
						}
					}
					// if east wallboard is part of boarder, move onto next direction
					if(floorplan.isPartOfBorder(new Wallboard(col, row, CardinalDirection.East))) {
						break;
					}
					// if no wallboard on east side, move to east cell to evaluate for minimum again
					if(floorplan.hasNoWall(col, row, CardinalDirection.East)) {
						col = col+1;
					}		
				}
				
				// while loop to move all the way west possible in current component
				// evaluates if wallboard weight on west side of component is the current smallest weight found
				while(true) {
					
					// if west wallboard at cell is not border and exists
					if(floorplan.canTearDown(new Wallboard(col, row, CardinalDirection.West))) {
						
						// if the next cell over doesn't have the same node id as current cell
						// aka if the next cell over isn't apart of the same component as current cell
						// then look at west wallboard and determine if its weight makes it the minimum
						if(nodeIDs[col][row]!=nodeIDs[col-1][row]) {
							if(minimum==0 || minimum > getEdgeWeight(new Wallboard(col,row, CardinalDirection.West))) {
								minimum = getEdgeWeight(new Wallboard(col,row, CardinalDirection.West));
								minWallboard = col+row+"W";
							}
							// break: move onto next direction
							break;
						}
						// if cells are part of the same component, move to next cell
						else if(nodeIDs[col][row]==nodeIDs[col-1][row]) {
							 col = col-1;
						}
					}
					// if west wallboard is part of border, break: move onto next direction
					if(floorplan.isPartOfBorder(new Wallboard(col, row, CardinalDirection.West))) {
						break;
					}
					// if no wallboard on west side, move to west cell to evaluate for minimum again
					if(floorplan.hasNoWall(col, row, CardinalDirection.West)) {
						col = col-1;
					}
				}
			
				// while loop to move all the way north possible in current component
				// evaluates if wallboard weight on north side of component is the current smallest weight found
				while(true) {
					
					// if north wallboard at cell is not border and exists
					if(floorplan.canTearDown(new Wallboard(col, row, CardinalDirection.North))) {
						
						// if the next cell over doesn't have the same node id as current cell
						// aka if the next cell over isn't apart of the same component as current cell
						// then look at north wallboard and determine if its weight makes it the minimum
						if(nodeIDs[col][row]!=nodeIDs[col][row-1]) {
							if(minimum==0 || minimum > getEdgeWeight(new Wallboard(col,row, CardinalDirection.North))) {
								minimum = getEdgeWeight(new Wallboard(col,row, CardinalDirection.North));
								minWallboard = col+row+"N";
							}
							// break: move onto next direction
							break;
						}
						// if cells are part of the same component, move to next cell
						else if(nodeIDs[col][row]==nodeIDs[col][row-1]) {
							 row = row-1;
						}
					}
					// if north wallboard is part of border, break: move onto next direction
					if(floorplan.isPartOfBorder(new Wallboard(col, row, CardinalDirection.North))) {
						break;
					}
					// if no wallboard on north side, move to north cell to evaluate for minimum again
					if(floorplan.hasNoWall(col, row, CardinalDirection.North)) {
						row = row-1;
					}
				}
				
				// while loop to move all the way south possible in current component
				// evaluates if wallboard weight on south side of component is the current smallest weight found
				while(true) {
					
					// if south wallboard at cell is not border and exists
					if(floorplan.canTearDown(new Wallboard(col, row, CardinalDirection.South))) {
						
						// if the next cell over doesn't have the same node id as current cell
						// aka if the next cell over isn't apart of the same component as current cell
						// then look at south wallboard and determine if its weight makes it the minimum
						if(nodeIDs[col][row]!=nodeIDs[col][row+1]) {
							if(minimum==0 || minimum > getEdgeWeight(new Wallboard(col,row, CardinalDirection.South))) {
								minimum = getEdgeWeight(new Wallboard(col,row, CardinalDirection.South));
								minWallboard = col+row+"S";
							}
							break;
						}
						// if cells are part of the same component, move to next cell
						else if(nodeIDs[col][row]==nodeIDs[col][row+1]) {
							 row=row+1;
						}
					}
					if(floorplan.isPartOfBorder(new Wallboard(col, row, CardinalDirection.South))) {
						break;
					}
					// if no wallboard on south side, move to south cell to evaluate for minimum again
					if(floorplan.hasNoWall(col, row, CardinalDirection.South)) {
						row = row+1;
					}
				}
				
				// use current ID saved earlier as key and its value as the minWallBoard found through while loops
				// add to dictionary so that can reference what the cheapest edge is for a specific ID
				nodeIDsToCheapestEdge.put(currentID, minWallboard);
			}
		}
	}
	/**
	 * mergeComplete looks through the nodeIDs array and determines if
	 * the nodeIDs are all equal to 0, which would imply that all of 
	 * the nodes are finished merging and have been set to the same smallest
	 * value.
	 * 
	 * @return boolean true if mergeComplete and false if not
	 */
	private boolean mergeComplete(int[][] nodeIDs) {
		// merge is complete when all node IDs are equal
		boolean mergeComplete = false;
		// iterates through all node IDs to check equality
		for (int col=0;col<width;col++) {
			for (int row=0;row<height;row++) {
				if(nodeIDs[col][row]!=0) {
					mergeComplete = false;
					return mergeComplete;
				}
				else {
					mergeComplete = true;
				}
			}
		}
		return mergeComplete;
	}
	/**
	 * generatePathways merges nodes and deletes the cheapest edge for 
	 * every node. It iterates through the nodeIDs and sets the nodeIDs 
	 * in a particular cell to the minimum ID between the cell and the one 
	 * it is merging with. It stores all of the wallboards to delete in a list
	 * and then iterates through that list to delete the wallboards. It continues
	 * to merge nodes and delete wallboards until mergeComplete returns true.
	 * 
	 */
	// MazeBuilder already initializes a floorplan with all walls up
	@Override
	protected void generatePathways() {
		int[][] nodeIDs = initializeNodeIDs();
		setUpNodeIDsToCheapestEdge(nodeIDs);
		// when all of the ids in the id array are the same the merging is complete
		// until then, merge nodes by cheapest edge
		while(!mergeComplete(nodeIDs)) {
			
			// array list to save the edges to be deleted at the end of a cycle
			List<String> edgeToDelete = new ArrayList<String>();
			
			// iterate through all of the cells in the maze
			for(int col=0;col<width;col++) {
				for(int row=0;row<height;row++) {
					
					// get coordinates and direction of cheapest edge for cell and save
					int x = nodeIDsToCheapestEdge.get(nodeIDs[col][row]).charAt(0);
					int y = nodeIDsToCheapestEdge.get(nodeIDs[col][row]).charAt(1);
					char direction = nodeIDsToCheapestEdge.get(nodeIDs[col][row]).charAt(2);
					
					// if cheapest edge isn't already going to be deleted, add it to list to delete
					if(!edgeToDelete.contains(nodeIDsToCheapestEdge.get(nodeIDs[col][row]))) {
						edgeToDelete.add(nodeIDsToCheapestEdge.get(nodeIDs[col][row]));
					}
					
					// merging 
					
					// if cheapest edge for node ID at cell is east
					// merge east
					if(direction=='E') {
						// set node id to be the same for the cells to merge
						// use minimum node id to merge
						
						// use node id for cell east of cheapest edge to compare
						if(nodeIDs[col][row]<nodeIDs[x+1][y]) {
							nodeIDs[x+1][y] = nodeIDs[col][row];
						}
						if(nodeIDs[col][row]>nodeIDs[x+1][y]) {
							nodeIDs[col][row] = nodeIDs[x+1][y];
						}
					}
					
					// if cheapest edge for node ID at cell is west
					// merge west
					if(direction=='W') {
						// set node id to be the same for the cells to merge
						// use minimum node id to merge
						
						// use node id for cell west of cheapest edge to compare
						if(nodeIDs[col][row]<nodeIDs[x-1][y]) {
							nodeIDs[x-1][y] = nodeIDs[col][row];
						}
						if(nodeIDs[col][row]>nodeIDs[x-1][y]) {
							nodeIDs[col][row] = nodeIDs[x-1][y];
						}
					}
					
					// if cheapest edge for node ID at cell is north
					// merge north
					if(direction=='N') {
						// set node id to be the same for the cells to merge
						// use minimum node id to merge
						
						// use node id for cell north of cheapest edge to compare
						if(nodeIDs[col][row]<nodeIDs[x][y-1]) {
							nodeIDs[x][y-1] = nodeIDs[col][row];
						}
						if(nodeIDs[col][row]>nodeIDs[x][y-1]) {
							nodeIDs[col][row] = nodeIDs[x][y-1];
						}
					}
					
					// if cheapest edge for node ID at cell is south
					// merge south
					if(direction=='S') {
						// set node id to be the same for the cells to merge
						// use minimum node id to merge
						
						// use node id for cell south of cheapest edge to compare
						if(nodeIDs[col][row]<nodeIDs[x][y+1]) {
							nodeIDs[x][y+1] = nodeIDs[col][row];
						}
						if(nodeIDs[col][row]>nodeIDs[x][y+1]) {
							nodeIDs[col][row] = nodeIDs[x][y+1];
						}
					}
				}
			}
			// deleting edges saved in list
			// for number of edges in list, iterate through
			for(int edge=0;edge<edgeToDelete.size();edge++) {
				
				// get coordinates and direction from string stored in ArrayList edgeToDelete
				int x = edgeToDelete.get(edge).charAt(0);
				int y = edgeToDelete.get(edge).charAt(1);
				char direction = edgeToDelete.get(edge).charAt(2);
				
				// initialize direction variable
				CardinalDirection cardDirection = CardinalDirection.North;
				
				// set direction variable based on direction in string from edgeToDelte
				if(direction=='N') {
					cardDirection = CardinalDirection.North;
				}
				if(direction=='E') {
					cardDirection = CardinalDirection.East;
				}
				if(direction=='S') {
					cardDirection = CardinalDirection.South;
				}
				if(direction=='W') {
					cardDirection = CardinalDirection.West;
				}
				// delete wallboard at coordinates and direction
				floorplan.deleteWallboard(new Wallboard(x, y, cardDirection));
			}
			
			// reset cheapest edges for node ids with newly merged nodes
			setUpNodeIDsToCheapestEdge(nodeIDs);
		}
				
	}
	
	
}
