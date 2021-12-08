/**
 * 
 */
package edu.wm.cs.cs301.janellekrupicka.gui;

import android.util.Log;

import edu.wm.cs.cs301.janellekrupicka.generation.CardinalDirection;
import edu.wm.cs.cs301.janellekrupicka.generation.Maze;
import edu.wm.cs.cs301.janellekrupicka.gui.Robot.Turn;

/**
 * The Wizard implements the strategy the Robot uses to solve the Maze.
 * 
 * The Wizard's strategy is to find the neighbor closer
 * to the exit at the Robot's current position. 
 * Then, using the Robot, drive to the neighbor
 * closer to the exit until the Robot reaches the exit.
 * The Wizard uses the Robot's sensors to determine where 
 * walls are so that the Robot doesn't decide to drive
 * into wall.
 *
 * 
 * Collaborators:
 * implements RobotDriver interface
 * uses Robot object -- reference to Robot object
 * uses Maze object -- reference to Maze object
 * 
 * @author Janelle Krupicka
 *
 */
public class Wizard implements RobotDriver {
	
	// instance variables: 
	ReliableRobot robot;
	Maze mazeUsed;
	// maze
	// energy level at start
	// energy level at end
	// path length
	
	/**
	 * setRobot sets the Robot object that will be driven
	 */
	
	public void setRobot(Robot r) {
		robot = (ReliableRobot) r;
		
		// set robot instance variable with input

	}
	
	/**
	 * setMaze sets the Maze object that the robot 
	 * will drive in.
	 */
	@Override
	public void setMaze(Maze maze) {
		mazeUsed = maze;
		// set maze instance variable with input

	}
	
	/**
	 * drive2Exit uses drive1Step2Exit until robot 
	 * is not at exit to have the robot drive to the exit.
	 * Throws exception if robot crashes or runs out of power:
	 * if the robot hasStopped. 
	 * If robot finds the exit, @return boolean true.
	 * If robot doesn't find the eit, @return boolean false.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while(!robot.isAtExit()) {
			drive1Step2Exit();
			
			if(robot.hasStopped()) {
				throw new Exception("Robot stopped");
			}
		}
		if(robot.isAtExit()) {
			robot.move(1);
		}
		return true;
//		assert(true, robot.isAtExit());
		// while robot is not at exit:
		//		drive1Step2Exit	
		//		if has stopped throws exception
		// if robot is at exit:
		// 		returns true
		// else returns false
		
	}
	
	private CardinalDirection determineNeighborDirection(int[] currentPosition, int[] neighborPosition) {
		int currentX = currentPosition[0];
		int currentY = currentPosition[1];
		int neighborX = neighborPosition[0];
		int neighborY = neighborPosition[1];
		
		if(currentX - neighborX == 1) return CardinalDirection.West;
		if(currentX - neighborX == -1) return CardinalDirection.East;
		if(currentY - neighborY == 1) return CardinalDirection.North;
		if(currentY - neighborY == -1) return CardinalDirection.South;
		return null;
	}
	/**
	 * drive1Step2Exit drives the robot 1 step closer to exit.
	 * It works by getting the neighbor closer to exit from the
	 * floorplan and then determines which direction that neighbor
	 * is in. If the robot is in the same direction as the neighbor,
	 * it moves forward one. 
	 * If the robot is in a different direction than the neighbor,
	 * it turns so it's facing the neighbor correctly and then moves
	 * forward 1. 
	 * If the robot is at the exit, if the robot is turned toward the
	 * exit (canSeeThroughExitToEternity) then don't move it. If it's
	 * turned away from the exit, move it so it's facing the exit.
	 * Determine if the robot has successfully moved to an adjacent cell
	 * closer to the exit and @return true if true.
	 * If unsuccessful @return false.
	 * If robot hasStopped, throw exception.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		int[] curPosition = robot.getCurrentPosition();
		int[] neighPosition =  mazeUsed.getNeighborCloserToExit(curPosition[0], curPosition[1]);
		CardinalDirection curDir = robot.getCurrentDirection();

		CardinalDirection neighDir = determineNeighborDirection(curPosition, neighPosition);
		if(curDir==neighDir) robot.move(1);
		if(robot.hasStopped()) throw new Exception("Robot has stopped.");
		// if currentDirection == directionToNeighbor: 
		// 		robot move 1
		
		else if(curDir!=neighDir) {
			if((curDir==CardinalDirection.West && neighDir==CardinalDirection.North)
				|| (curDir==CardinalDirection.North && neighDir==CardinalDirection.East)
				|| (curDir==CardinalDirection.East && neighDir==CardinalDirection.South)
				|| (curDir==CardinalDirection.South && neighDir==CardinalDirection.West)) {
				
				robot.rotate(Turn.LEFT);
			}
			if((curDir==CardinalDirection.West && neighDir==CardinalDirection.South)
				|| (curDir==CardinalDirection.South && neighDir==CardinalDirection.East)
				|| (curDir==CardinalDirection.East && neighDir==CardinalDirection.North)
				|| (curDir==CardinalDirection.North && neighDir==CardinalDirection.West)) {
					
				robot.rotate(Turn.RIGHT);
			}
			if((curDir==CardinalDirection.West && neighDir==CardinalDirection.East)
					|| (curDir==CardinalDirection.South && neighDir==CardinalDirection.North)
					|| (curDir==CardinalDirection.East && neighDir==CardinalDirection.West)
					|| (curDir==CardinalDirection.North && neighDir==CardinalDirection.South)) {
						
					robot.rotate(Turn.AROUND);
				}
			if(robot.hasStopped()) throw new Exception("Robot has stopped.");
			
			robot.move(1);
			if(robot.hasStopped()) throw new Exception("Robot has stopped.");	
		}
		if(robot.isAtExit()) {
			Log.v("Wizard","Is at exit correctly");
			if(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
				if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
					robot.rotate(Turn.LEFT);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
				}
				else if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
					
				}
				else if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.BACKWARD)) {
					System.out.println("Can See Exit Correctly");
					robot.rotate(Turn.AROUND);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
				}
			}
			if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
				robot.move(1);
			}
		}
		
		assert isAdjacent(curPosition, robot.getCurrentPosition());
		return isAdjacent(curPosition, robot.getCurrentPosition());
		// else if currentDirection != directionToNeighbor:
		//		if (cur=west and neigh=north) or (cur=north and neigh=east)
		//			or (cur=east and neigh=south) or (cur=south and neigh=west):
		//				turn robot left
		//		if (cur=west and neigh=south) or (cur=south and neigh=east)
		//			or (cur=east and neigh=north) or (cur=north and neigh=west):
		//				turn robot right
		// 		if (cur=north and neigh=south) or (cur=south and neigh=north)
		//			of (cur=east and neigh=west) or (cur=west and neigh=east):
		//				turn robot around
		//		move robot 1
		//		path length + 1
		
		// turn depending on relative direction and then move and 
		// if is at exit: 
		//		if not can see through exit to eternity(currentdirection):
		//			if can see through exit to eternity(left):
		//				turn left
		//			if can see through exit to eternity(right):
		//				turn right
		
		// if assert(is adjacent)
		// if robot.HasStopped:
		//		throw Exception
	}
	
	private boolean isAdjacent(int[] previousPosition, int[] currentPosition) {
		int prevX = previousPosition[0];
		int prevY = previousPosition[1];
		int curX = currentPosition[0];
		int curY = currentPosition[1];
		
		if((Math.abs(curX-prevX)==1) && curY==prevY) return true;
		if((Math.abs(curY-prevY)==1) && curX==prevX) return true;
		else return false;
	}
	/**
	 * getEnergyConsumption uses instance variables energy at start
	 * and energy at end. Energy level at end is the battery level
	 * from the robot. The difference between the initial energy and
	 * end battery is the energy consumption.
	 */
	@Override
	public float getEnergyConsumption() {
		return 3500 - robot.getBatteryLevel();
	}
	
	/**
	 * getPathLength uses the odometer reading from the robot to
	 * determine how far the robot has traveled.
	 */
	@Override
	public int getPathLength() {
		int pathLength = robot.getOdometerReading();
		return pathLength;
	}
	
	///////////////////////////
	// Methods for Testing //
	/////////////////////////
	
	public ReliableRobot getRobot() {
		return robot;
	}

}
