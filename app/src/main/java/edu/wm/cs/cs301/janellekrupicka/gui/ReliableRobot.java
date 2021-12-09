/**
 * 
 */
package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.CardinalDirection;
import edu.wm.cs.cs301.janellekrupicka.generation.Maze;
import edu.wm.cs.cs301.janellekrupicka.gui.Constants.UserInput;

/**
 * ReliableRobot moves the Robot and keeps track of Robot energy level.
 * 
 * Performs actions like turn and move by using the Controller.
 * Has four sensors (one for each direction) that it uses
 * to determine the distance to an obstacle.
 * Subtracts from Robot energy level based on movements
 * the Robot makes.
 * 
 * Collaborators:
 * Implements Robot interface.
 * Interacts with Controller to move Robot.
 * Use ReliableSensor -- 
 * has instances of ReliableSensor for each direction.
 * 
 * @author Janelle Krupicka
 *
 */
public class ReliableRobot implements Robot {
	// instance variables:
	// one sensor for each direction
	/**
	 * ReliableSensor with direction set to forward.
	 */
	protected ReliableSensor sensorForward;
	/**
	 * ReliableSensor with direction set to backward.
	 */
	protected ReliableSensor sensorBackward;
	/**
	 * ReliableSensor with direction set to left.
	 */
	protected ReliableSensor sensorLeft; 
	/**
	 * ReliableSensor with direction set to right.
	 */
	protected ReliableSensor sensorRight;
	/**
	 * Controller the robot collaborates with.
	 */
//	protected Controller robotController;
	protected StatePlaying statePlaying;
	// battery level
	/**
	 * Float array for batteryLevel.
	 */
	protected float[] batteryLevel;
	/**
	 * Odometer tracks how far the robot has traveled.
	 */
	protected int odometer;
	/**
	 * Stopped is true when robot runs into a wall or runs out of
	 * battery.
	 */
	private boolean stopped;
	private static final float ENERGY_FOR_FULL_ROTATION = 12;
	private static final float ENERGY_FOR_STEP_FORWARD = 6;
	private static final float ENERGY_FOR_JUMP = 40;
	private static final int MAX_INT = 500;
	/**
	 * Constructor
	 * Initialize stopped as false. Initialize odometer to 0.
	 * addDistanceSensor for each sensor in each direction. 
	 */
	public ReliableRobot() {
		// set stopped to false
		stopped = false;
		// set odometer to 0
		resetOdometer();
		// set initial batteryLevel to 3500
		batteryLevel = new float[1];
		batteryLevel[0] = 3500;
		// set sensor for each direction
		sensorForward = null;
		sensorBackward = null;
		sensorLeft = null;
		sensorRight = null;
	}
	/**
	 * setController using @param Controller object.
	 * Throw IllegalArgumentException if controller or
	 * maze are null.
	 */
	@Override
	public void setController(Controller controller) {
		// throws IllegalArgument exception if controller is null
		// or if controller is not in playing state
		// or if controller does not have a maze
		if(controller==null
			|| controller.getMazeConfiguration()==null) {
			throw new IllegalArgumentException();
		}
		// set controller instance variable with input
	//	robotController = controller;
	}
	public void setStatePlaying(StatePlaying state) {
		statePlaying = state;
	}
	/**
	 * addDistanceSensor at left, right, forward, or backward
	 * given input direciton. Sets instance variable for sensor
	 * in that direction.
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		switch(mountedDirection) {
		// case (left):
		// 		left sensor instance variable equals input sensor with left direction
			case LEFT:
				sensorLeft = (ReliableSensor) sensor;
				sensorLeft.setSensorDirection(mountedDirection);
				break;
		// case(right):
		//		right sensor instance variable equals input sensor with right direction	
			case RIGHT:
				sensorRight = (ReliableSensor) sensor;
				sensorRight.setSensorDirection(mountedDirection);
				break;
		//	case(forward):
		//		forward sensor instance variable equals input sensor with forward direction
			case FORWARD:
				sensorForward = (ReliableSensor) sensor;
				sensorForward.setSensorDirection(mountedDirection);
				break;
		//  case(backward):
		// 		backward sensor instance variable equals input sensor with backward direction
			case BACKWARD:
				sensorBackward = (ReliableSensor) sensor;
				sensorBackward.setSensorDirection(mountedDirection);
				break;
		}
	}
	/**
	 * getCurrentPosition() gets the current position from the controller.
	 * Throws exception if position is not valid position (is outside the maze).
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int[] currentPosition = statePlaying.getCurrentPosition();
		if (statePlaying.getMazeConfiguration().isValidPosition(currentPosition[0], currentPosition[1])) {
			return currentPosition;
		}
		else throw new Exception("Position is outside of maze.");
	}
	/**
	 * getCurrentDirection returns CardinalDirection the robot is
	 * facing from controller.
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		return statePlaying.getCurrentDirection();
	}
	/**
	 * getBatteryLevel returns power supply saved in instance variable
	 * as float.
	 */
	@Override
	public float getBatteryLevel() {
		return batteryLevel[0];
	}
	/**
	 * setBatteryLevel with @param float level. 
	 * If the input is less than 0, IllegalArgumentException.
	 * If the input it 0, the robot has stopped.
	 */
	@Override
	public void setBatteryLevel(float level) throws IllegalArgumentException{
		// battery level cannot be less than 0
		if(level<0) throw new IllegalArgumentException();
		// set batter level to input level
		batteryLevel[0] = level;
		// if battery level is 0, robot has run out of energy
		// robot has stopped
		if(batteryLevel[0]==0) stopped = true;
	}
	/**
	 * Returns the energy for full rotation of robot: 12.
	 */
	@Override
	public float getEnergyForFullRotation() {
		return ENERGY_FOR_FULL_ROTATION;
	}
	/**
	 * Returns the energy for one step forward by robot: 6.
	 */
	@Override
	public float getEnergyForStepForward() {
		return ENERGY_FOR_STEP_FORWARD;
	}
	/**
	 * Returns int odometer reading, current number of steps
	 * taken.
	 */
	@Override
	public int getOdometerReading() {
		return odometer;
	}
	/**
	 * Resets odometer to 0.
	 */
	@Override
	public void resetOdometer() {
		odometer = 0;
	}
	/**
	 * Uses private method rotate90Degrees to turn robot
	 * depending on input turn. 
	 * Turn could be LEFT, RIGHT, or AROUND.
	 * AROUND makes two right turns.
	 * Determines if the robot has enough energy to turn.
	 * If it doesn't, it stops.
	 */
	@Override
	public void rotate(Turn turn) {
		switch(turn) {
			case LEFT:
				// turn robot 90 degrees left if enough energy
				if(batteryLevel[0]-getEnergyForFullRotation()/4>0) {
					rotate90Degrees(Robot.Turn.LEFT);
				}
				// if not enough energy, robot has stopped
				else stopped = true;
				break;
			case RIGHT:
				// turn robot 90 degrees right if enough energy
				if(batteryLevel[0]-getEnergyForFullRotation()/4>0) {
					rotate90Degrees(Robot.Turn.RIGHT);
				}
				// if not enough energy, robot has stopped
				else stopped = true;
				break;
			case AROUND:
				// turn robot 90 degrees right twice (180 degrees)
				// if enough energy
				if(batteryLevel[0]-getEnergyForFullRotation()/2>0) {
					rotate90Degrees(Robot.Turn.RIGHT);
					rotate90Degrees(Robot.Turn.RIGHT);
				}
				// if not enough energy, robot has stopped
				else stopped = true;
				break;	
		}
	}
	/**
	 * Private method used in rotate().
	 * Uses keyDown to turn according to @param turn.
	 * Updates power supply based on energy used to turn.
	 */
	private void rotate90Degrees(Turn turn) {
		if(!stopped) {
			// if robot has not stopped, turn
			switch(turn) {
				case LEFT:
					statePlaying.keyDown(UserInput.LEFT, 0);
					break;
				case RIGHT:
					statePlaying.keyDown(UserInput.RIGHT, 0);
					break;
				case AROUND:
					break;
			}
			// subtract energy taken to rotate from current energy level
			batteryLevel[0] = batteryLevel[0] - getEnergyForFullRotation()/4;
		}
	}
	/**
	 * Moves robot one cell in the forward direction using keyDown.
	 * If movement will take too much energy, stops robot.
	 * Adjusts power supply for movement -- subtracts getEnergyForStepForward.
	 * If @param distance is not positive, throws IllegalArgumentException.
	 */
	@Override
	public void move(int distance) throws 
		IllegalArgumentException, UnsupportedOperationException, Exception {
		if(distance<0) throw new IllegalArgumentException();
		// if distance < 0 throws IllegalArgumentException
		for(int step=0; step<distance; step++)
		// for each block in distance:
			if(batteryLevel[0] - getEnergyForStepForward() < 1) {
				stopped = true;
			}
			// sensing using distanceToObstacle takes 1 energy
			// account for this by adding 1 to the battery level 
			// so that move only takes 6 energy and not 7
			else if(distanceToObstacle(Robot.Direction.FORWARD) < 1) {
				stopped = true;
			}
			else {
				// if robot has enough energy and isn't running into a wall,
				// move forward
				odometer++;
				statePlaying.keyDown(UserInput.UP, 0);
				// update battery level
				batteryLevel[0] = batteryLevel[0] - getEnergyForStepForward() + 1;
				// robot travels one block, so odometer increases by 1
			}
	}
	/**
	 * Jumps robot over wall in forward direction.
	 * Determines if the position to jump to is a valid position, 
	 * if the position is in the maze. If it isn't, stop the robot.
	 * If the position is valid, use keyDown to make the robot jump.
	 * If the current power supply is less than 41, robot stops.
	 * Subtract 40 from power supply for the jump.
	 * Increment odometer.
	 * 
	 */
	@Override
	public void jump() {
		int[] positionToJumpTo = {0,0};
		// int position to jump to
		int[] currentPosition = statePlaying.getCurrentPosition();
		// switch to find position that robot will jump to in
		// forward direction
		switch(statePlaying.getCurrentDirection()) {
			case North:
				positionToJumpTo[0] = currentPosition[0];
				positionToJumpTo[1] = currentPosition[1]-1;
			case East:
				positionToJumpTo[0] = currentPosition[0]+1;
				positionToJumpTo[1] = currentPosition[1];
			case South:
				positionToJumpTo[0] = currentPosition[0];
				positionToJumpTo[1] = currentPosition[1]+1;
			case West:
				positionToJumpTo[0] = currentPosition[0]-1;
				positionToJumpTo[1] = currentPosition[1];
		}
		// determine if the found position is a valid position (is in the maze)
		// if it isn't a valid position, the robot is stopped
		if(!statePlaying.getMazeConfiguration().isValidPosition(positionToJumpTo[0], positionToJumpTo[1])) {
			stopped = true;
		}
		// if the robot doesn't have enough energy to jump, the robot stops
		if(batteryLevel[0]-ENERGY_FOR_JUMP<1) stopped = true;
		if(!stopped) {
			statePlaying.keyDown(UserInput.JUMP, 0);
			// update energy spent by robot
			batteryLevel[0] = batteryLevel[0] - ENERGY_FOR_JUMP;
			// update blocks traveled by robot
			odometer++;
		}
	}
	/**
	 * Returns a boolean, true if robot isAtExit, false otherwise.
	 * Distance to exit from exit is 1.
	 */
	@Override
	public boolean isAtExit() {
		int[] curPosition = statePlaying.getCurrentPosition();
		// gets maze from controller to get the exit position from floor plan
		Maze maze = statePlaying.getMazeConfiguration();
		// determines if current position is exit position
		return maze.getFloorplan().isExitPosition(curPosition[0], curPosition[1]);
	}
	/**
	 * Returns boolean true if robot is inside room.
	 * Uses controller to determine if robot is in room.
	 */
	@Override
	public boolean isInsideRoom() {
		Maze maze = statePlaying.getMazeConfiguration();
		int[] currentPosition = statePlaying.getCurrentPosition();
		return maze.isInRoom(currentPosition[0], currentPosition[1]);
	}
	/**
	 * Returns true if robot has stopped.
	 */
	@Override
	public boolean hasStopped() {
		return stopped;
	}
	/**
	 * Returns int that is the distance to obstacle in the
	 * distance to obstacle.
	 * @throws Exception 
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws 
		UnsupportedOperationException, Exception {
		// ReliableSensor is always operational
		// check that there is a sensor in the given direction
		boolean sensorExistsOperational = checkIfSensorIsOperational(direction);
		// if there isn't a sensor, throw exception
		if (!sensorExistsOperational) throw new UnsupportedOperationException();
		// use sensor in given direction to find distance to obstacle
		switch(direction) {
			case LEFT:
				return findLeftSensorDistance();
			case RIGHT:
				return findRightSensorDistance();
			case FORWARD:
				// if direction forward, same cardinal direction as current direction
				return sensorForward.distanceToObstacle(getCurrentPosition(), statePlaying.getCurrentDirection(), batteryLevel);
			case BACKWARD:
				return findBackwardSensorDistance();
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines if the sensor in the given @param direction
	 * is not null. 
	 * @return true if sensor is not null, false if it is
	 */
	private boolean checkIfSensorIsOperational(Direction direction) {
		boolean sensorExistsOperational = true;
		switch(direction) {
			case LEFT:
				if(sensorLeft==null) sensorExistsOperational = false;
				break;
			case RIGHT:
				if(sensorRight==null) sensorExistsOperational = false;
				break;
			case FORWARD:
				if(sensorForward==null) sensorExistsOperational = false;
				break;
			case BACKWARD:
				if(sensorBackward==null) sensorExistsOperational = false;
				break;
		}
		return sensorExistsOperational;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines the distanceToObstacle with the left sensor,
	 * depending on the robot's current cardinal direction
	 * determines which cardinal direction the left sensor is in.
	 * Takes into account north and south being flipped in gui.
	 */
	private int findLeftSensorDistance() throws Exception {
		switch(statePlaying.getCurrentDirection()) {
		case North:
			// if current direction is north, left is east
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		case East:
			// if current direction is east, left is south
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		case South:
			// if current direction is south, left is west
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case West:
			// if current direction is west, left is north
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines the distanceToObstacle with the right sensor,
	 * depending on the robot's current cardinal direction
	 * determines which cardinal direction the right sensor is in.
	 * Takes into account north and south being flipped in gui.
	 */
	private int findRightSensorDistance() throws Exception {
		switch(statePlaying.getCurrentDirection()) {
		case North:
			// if current direction is north, right is west
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case East:
			// if current direction is east, right is north
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		case South:
			// if current direction is south, right is east
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		case West:
			// if current direction is west, right is south
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines the distanceToObstacle with the backward sensor,
	 * depending on the robot's current cardinal direction
	 * determines which cardinal direction the backward sensor is in.
	 * Takes into account north and south being flipped in gui.
	 */
	private int findBackwardSensorDistance() throws Exception {
		switch(statePlaying.getCurrentDirection()) {
		case North:
			// if current direction is north, backward is south
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		case East:
			// if current direction is east, backward is west
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case South:
			// if current direction is south, backward is north
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		case West:
			// if current direction is west, backward is east
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		}
		return 0;
	}
	/**
	 * Returns true if the robot can drive through the exit in a straight line
	 * in the @param direction. Uses the robot's sensors to determine
	 * if there is a boundary wall as an obstacle, the sensors return 500
	 * if there is no wall in the way.  
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		int distance;
		try {
			// sensor will return MAX_INT if can see to exit
			distance = distanceToObstacle(direction);
		} catch(Exception e){
			throw new UnsupportedOperationException();
		}
		if(distance==MAX_INT) {
			return true;
		}
		else return false;
	}
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// not used in ReliableRobot

	}
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// not used in ReliableRobot

	}
	
	///////////////////////////
	// Methods for Testing //
	/////////////////////////
	
//	public Controller getController() {
//		return robotController;
//	}

}
