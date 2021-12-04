/**
 * 
 */
package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.Maze;
import edu.wm.cs.cs301.janellekrupicka.gui.Robot.Direction;
import edu.wm.cs.cs301.janellekrupicka.gui.Robot.Turn;

/**
 * WallFollower solves the maze by drive the robot to 
 * follow the left wall until it gets to the exit.
 * 
 * Collaborators: 
 * Implements RobotDriver.
 * 
 * @author Janelle Krupicka
 *
 */
public class WallFollower implements RobotDriver {
	/**
	 * Robot the wallfollower uses.
	 */
	UnreliableRobot robot;
	/**
	 * Maze the wallfollower traverses.
	 */
	Maze mazeUsed;
	
	/**
	 * Wallfollower gets set with UnreliableRobot
	 */
	@Override
	public void setRobot(Robot r) {
		robot = (UnreliableRobot) r;

	}
	/**
	 * Sets maze for wallfollower to traverse.
	 */
	@Override
	public void setMaze(Maze maze) {
		mazeUsed = maze;

	}
	/**
	 * Drive the robot to the exit by following the 
	 * left wall, unless the robot can see the exit
	 * in which case the robot just runs to the exit.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// drive1Step2Exit
		// until can see exit
		while(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
			drive1Step2Exit();
		}
//		move until at exit
		if(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
			while(!robot.isAtExit()) {
				robot.move(1);
			}
			if(robot.isAtExit()) {
				robot.move(1);
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Implements the strategy for determining
	 * where the left wall is and how to follow it
	 * to move with wallfollower. 
	 * Uses sensors to check left and forward distances
	 * to obstacle. If those sensors are unreliable and 
	 * not operational, two strategies are used to remedy
	 * that to continue the wallfollower.
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		int distance = 0;
		// first check if left sensor is operational
		// if not operational perform strategy 1
		while(true) {
			if(!robot.getDistanceSensor(Direction.LEFT).getIsOperational()) {
				distance = strategy1SensorNotOperational(Robot.Direction.LEFT);
			}
			// if left sensor is operational save distance
			else {
				distance = robot.distanceToObstacle(Robot.Direction.LEFT);
			}
			// if there is no left wall:
			if(distance > 0) {
				//	turn left
				//	move 1
				robot.rotate(Robot.Turn.LEFT);
				robot.move(1);
				break;
			}
			// check if front sensor is operational
			if(!robot.getDistanceSensor(Direction.FORWARD).getIsOperational()) {
				distance = strategy1SensorNotOperational(Robot.Direction.FORWARD);
			}
			// if left sensor is operational save distance
			if (robot.getDistanceSensor(Direction.LEFT).getIsOperational()){
				distance = robot.distanceToObstacle(Robot.Direction.LEFT);
			}
			// else if there is no front wall:
			if(distance > 0) {
				//	move 1
				robot.move(1);
				break;
			}
			// else (wall at front and left):
			else {
				//	turn right
				robot.rotate(Robot.Turn.RIGHT);
			}
		}
		// if at exit:
		if(robot.isAtExit()) {
			//	 	if can't see to exit:
			//		turn
			if(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
				if(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
					robot.rotate(Turn.LEFT);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
				}
				else if(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)) {
					robot.rotate(Turn.RIGHT);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
					
				}
				else if(!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.BACKWARD)) {
					robot.rotate(Turn.AROUND);
					if(robot.hasStopped()) throw new Exception("Robot has stopped.");
				}
			}
		}
		return true;
	}
	
	/**
	 * If the current sensor isn't operational
	 * (can be left or forward with wallfollower)
	 * then rotate the robot so that an operational
	 * sensor is facing the direction the wallfollower
	 * was initially trying to sense. Get the distance,
	 * then rotate the robot back.
	 * @return the integer distance to the obstacle in the direction
	 * @param direction
	 * @throws Exception 
	 * @throws UnsupportedOperationException 
	 */
	private int strategy1SensorNotOperational(Direction direction) throws UnsupportedOperationException, Exception {
		int distance = 0;
		// save distance int
		// if left sensor not operational:
		if(Robot.Direction.LEFT==direction) {
		// 	if forward sensor is operational:
			if(robot.getDistanceSensor(Direction.FORWARD).getIsOperational()) {
				//	turn robot left
				robot.rotate(Robot.Turn.LEFT);
				//	get distance to obstacle backward
				distance = robot.distanceToObstacle(Robot.Direction.FORWARD);
				//	turn robot right	
			}
		//	else if backward sensor is operational:
			else if(robot.getDistanceSensor(Direction.BACKWARD).getIsOperational()) {
				// 	turn robot right
				robot.rotate(Robot.Turn.RIGHT);
				//	get distance to obstacle 
				distance = robot.distanceToObstacle(Robot.Direction.BACKWARD);
				//  turn robot left	
				robot.rotate(Robot.Turn.LEFT);
			}
		//	else if right sensor is operational:
			else if(robot.getDistanceSensor(Direction.RIGHT).getIsOperational()) {
				// turn robot around
				robot.rotate(Robot.Turn.AROUND);
				// get distance to obstacle
				distance = robot.distanceToObstacle(Robot.Direction.RIGHT);
				// turn robot around 
				robot.rotate(Robot.Turn.AROUND);
			}
		// 	else (none of the sensors are operational):
			else {
				distance = strategy2SensorNotOperational(Robot.Direction.LEFT);
			}
			return distance;
		}
		if(Robot.Direction.FORWARD==direction) {
			if(robot.getDistanceSensor(Direction.LEFT).getIsOperational()) {
				//	turn robot right
				robot.rotate(Robot.Turn.RIGHT);
				//	get distance to obstacle left
				distance = robot.distanceToObstacle(Robot.Direction.LEFT);
				//	turn robot left
				robot.rotate(Robot.Turn.LEFT);
			}
			else if(robot.getDistanceSensor(Direction.RIGHT).getIsOperational()) {
				//	turn robot left
				robot.rotate(Robot.Turn.LEFT);
				//	get distance to obstacle backward
				distance = robot.distanceToObstacle(Robot.Direction.RIGHT);
				//	turn robot right	
				robot.rotate(Robot.Turn.RIGHT);
			}
			//	else if backward sensor is operational:
			else if(robot.getDistanceSensor(Direction.BACKWARD).getIsOperational()) {
				// 	turn robot right
				robot.rotate(Robot.Turn.AROUND);
				//	get distance to obstacle 
				distance = robot.distanceToObstacle(Robot.Direction.BACKWARD);
				//  turn robot left	
				robot.rotate(Robot.Turn.AROUND);
			}
			else {
				distance = strategy2SensorNotOperational(Robot.Direction.LEFT);
			}
			return distance;
		}
		else {
			throw new UnsupportedOperationException();
		}
	}
	/**
	 * If strategy 1 to rotate the robot to get an operational
	 * sensor doesn't work, strategy2 waits for the left or
	 * forward sensor (depending on input direction) to become
	 * operational.
	 * @param direction
	 * @return 
	 * @throws UnsupportedOperationException
	 * @throws Exception
	 */
	private int strategy2SensorNotOperational(Direction direction) throws UnsupportedOperationException, Exception {
		// while sensor left is not operational
		if(direction==Robot.Direction.LEFT) {
			while(!robot.getDistanceSensor(Direction.LEFT).getIsOperational()) {
				//	wait
				try {
					// this will generate an exception
					// current thread is not owner
					// not sure how to fix
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return robot.distanceToObstacle(Robot.Direction.LEFT);
		}
		if(direction==Robot.Direction.FORWARD) {
			while(!robot.getDistanceSensor(Direction.FORWARD).getIsOperational()) {
				//	wait
				try {
					// this will generate an exception
					// current thread is not owner
					// not sure how to fix
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return robot.distanceToObstacle(Robot.Direction.FORWARD);
		}
		return 0;
	}
	@Override
	public float getEnergyConsumption() {
		// 3500 - robot battery level
		return 3500 - robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// path length is robot's odometer reading
		return robot.getOdometerReading();
	}
	
	// for testing
	
	public Robot getRobot() {
		return robot;
	}

}
