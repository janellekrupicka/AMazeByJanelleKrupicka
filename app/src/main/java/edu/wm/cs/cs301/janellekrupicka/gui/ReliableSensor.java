/**
 * 
 */
package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.CardinalDirection;
import edu.wm.cs.cs301.janellekrupicka.generation.Floorplan;
import edu.wm.cs.cs301.janellekrupicka.generation.Maze;
import edu.wm.cs.cs301.janellekrupicka.gui.Robot.Direction;

/**
 * ReliableSensor measures distances to obstacles in different directions.
 * 
 * A Robot has 4 sensors, one for each direction, that are stationary.
 * ReliableSensor is each of those sensors with a different direction set.
 * Determines how far it is to an obstacle (a wall)
 * in a certain direction by looking at the Floorplan.
 * 
 * Collaborators:
 * Implements DistanceSensor interface.
 * Interacts with the Maze --
 * interacts with Floorplan from Maze
 * to determine distances to obstacles.
 * 
 * @author Janelle Krupicka
 *
 */
public class ReliableSensor implements DistanceSensor {
	/**
	 * Direction of sensor. 
	 * Can be forward, backward, left, or right.
	 */
	private Direction sensorDirection;
	/**
	 * Maze that the sensor uses to sense where
	 * obstacles are.
	 */
	private static final Maze mazeUsed = MazeSingleton.getInstance().getMaze();
	/**
	 * Returns the int distance to a wall in a @param CardinalDirection
	 * from @param currentPosition of robot. 
	 * If any parameter is null or current position is outside maze,
	 * throws IllegalArgumentException.
	 * If subtracting 1 from the power supply will make it zero, 
	 * the robot has stopped (1 is the energy consumption for sensing).
	 * If there is no wall into eternity, return 500 -- goes through
	 * exit.
	 * Throws power failure if runs out of power.
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
		throws Exception {
		// if any parameter is null or current position is outside legal range:
		//		throws IllegalArgumentException
		if(currentPosition==null || currentDirection==null || powersupply==null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		if(!mazeUsed.isValidPosition(currentPosition[0], currentPosition[1])) {
			throw new IllegalArgumentException("Current position outside legal range.");
		}
		// if the robot has enough energy to sense
		if(powersupply[0]-getEnergyConsumptionForSensing() >= 1) {	
			return distanceToObstacleSensing(currentPosition, currentDirection, powersupply);
		}
		else {
			throw new Exception("PowerFailure");
		}
	}
	/**
	 * Helper method for distanceToObstacle that 
	 * initializes distance to count, subtracts the energy
	 * level used for sensing, accounts for distanceToObstacles
	 * of 0.
	 * @param currentPosition
	 * @param currentDirection
	 * @param powersupply
	 * @return
	 */
	private int distanceToObstacleSensing(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply) {
		// make sure that the robot has enough power to sense
		Floorplan floorplan = mazeUsed.getFloorplan();
		// initialize variable for distance counter
		int distance = 0;
		powersupply[0] = powersupply[0]-getEnergyConsumptionForSensing();
		// if there's a wall in that direction, distance is 0 so just return
		if(floorplan.hasWall(currentPosition[0], currentPosition[1], currentDirection)) {
			return distance;
		}
		distance = distanceToObstacleCount(currentPosition, currentDirection, floorplan);
		// increment distance sensor while there is not a wall in the direction
		return distance;
	}
	/**
	 * Helper method for distanceToObstacle called
	 * in distanceToObstacleSensing that counts the distance
	 * to the next wall if the distance is not 0. Uses a
	 * while loop and increments the distance. Returns
	 * the distance coutned.
	 * @param currentPosition
	 * @param currentDirection
	 * @param floorplan
	 * @return
	 */
	private int distanceToObstacleCount(int[] currentPosition, CardinalDirection currentDirection, Floorplan floorplan) {
		int distance = 0;
		int xToChange = currentPosition[0];
		int yToChange = currentPosition[1];
		while(!floorplan.hasWall(xToChange, yToChange, currentDirection)) {
			switch(currentDirection) {
				// changing x and y depending on the direction
				// to move to the next cell to check for a wall again
				// and to increment the distance
			case North:
				yToChange--;
				break;
			case East:
				xToChange++;
				break;
			case South:
				yToChange++;
				break;
			case West:
				xToChange--;
				break;
			} 
			distance++;
			// if x and y are a position no longer in the maze,
			// the sensor  traveled through the exit out of the maze.
			// return max int to track that the robot can see the
			// exit position in that direction
			if(!mazeUsed.isValidPosition(xToChange, yToChange)) {
				distance = 500;
				break;
			}
		}
		return distance;
	}
	/**
	 * Sets the Maze used by the sensor.
	 */
	@Override
	public void setMaze(Maze maze) {
	//	mazeUsed = maze;

	}
	/**
	 * Sets the mounted direction of the sensor.
	 * Can either be forward, backward, left, or right.
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		sensorDirection = mountedDirection;
	}
	
	/**
	 * Returns float which is 1, the energy used in sensing.
	 */	
	@Override
	public float getEnergyConsumptionForSensing() {
		return 1;
	}
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// Not used in ReliableSensor
	}
	
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// Not used in ReliableSensor

	}

	/////////////////////////
	// Methods for Testing //
	/////////////////////////
	
	public Maze getMaze() {
		return mazeUsed;
	}
	
	public Direction getSensorDirection() {
		return sensorDirection;
	}
	@Override
	public boolean getIsOperational() {
		// reliable sensor is always operational
		return true;
	}
	

}
