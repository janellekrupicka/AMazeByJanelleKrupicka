package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.CardinalDirection;
import edu.wm.cs.cs301.janellekrupicka.gui.Robot.Direction;

/**
 * UnreliableRobot has four sensors,
 * some or all of its sensors are unreliable.
 * UnreliableRobot runs the UnreliableSensor's threads,
 * to make them fail and then be operational again.
 * 
 * Collaborators:
 * Subclass of ReliableRobot
 * Collaborates with UnreliableSensor
 * and ReliableSensor
 * to make sensors.
 * 
 * @author janellekrupicka
 *
 */
public class UnreliableRobot extends ReliableRobot {
	/**
	 * UnreliableRobot's left DistanceSensor
	 */
	private DistanceSensor sensorLeft;
	/**
	 * UnreliableRobot's right DistanceSensor
	 */
	private DistanceSensor sensorRight;
	/**
	 * UnreliableRobot's forward DistanceSensor
	 */
	private DistanceSensor sensorForward;
	/**
	 * UnreliableRobot's backward DistanceSensor
	 */
	private DistanceSensor sensorBackward;
	// not sure if these are needed
	/**
	 * Thread for left DistanceSensor
	 */
	private Thread leftThread;
	/**
	 * Thread for right DistanceSensor
	 */
	private Thread rightThread;
	/**
	 * Thread for forward DistanceSensor
	 */
	private Thread forwardThread;
	/**
	 * Thread for backward DistanceSensor
	 */
	private Thread backwardThread;
	// will be set to 4
	/**
	 * Amount of time where sensor is operational
	 */
	private int meanTimeBetweenFailures;
	// will be set to 2
	/**
	 * Amount of time where sensor is not operational
	 */
	private int meanTimeToRepair;
	/**
	 * Sets the distance sensor for the given direction based
	 * on sensor input. UnreliableRobot takes either
	 * Reliable or Unreliable sensors so it overrides
	 * the method in ReliableRobot.
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		switch(mountedDirection) {
		// add distance sensor in given direction by assigning it to instance variable
			case LEFT:
				// cast to type of sensor and then assign
				if(sensor instanceof UnreliableSensor) {
					sensorLeft = (UnreliableSensor) sensorLeft;
				}
				if(sensor instanceof ReliableSensor) {
					sensorLeft = (ReliableSensor) sensorLeft;
				}
				sensorLeft = sensor;
				break;
			case RIGHT:
				// cast to type of sensor and then assign
				if(sensor instanceof UnreliableSensor) {
					sensorRight = (UnreliableSensor) sensorRight;
				}
				if(sensor instanceof ReliableSensor) {
					sensorRight = (ReliableSensor) sensorRight;
				}
				sensorRight = sensor;
				break;
			case FORWARD:
				// cast to type of sensor and then assign
				if(sensor instanceof UnreliableSensor) {
					sensorForward = (UnreliableSensor) sensorForward;
				}
				if(sensor instanceof ReliableSensor) {
					sensorForward = (ReliableSensor) sensorForward;
				}
				sensorForward = sensor;
				break;
			case BACKWARD:
				// cast to type of sensor and then assign
				if(sensor instanceof UnreliableSensor) {
					sensorBackward = (UnreliableSensor) sensorBackward;
				}
				if(sensor instanceof ReliableSensor) {
					sensorBackward = (ReliableSensor) sensorBackward;
				}
				break;
		}
	}
	public DistanceSensor getDistanceSensor(Direction direction) {
		switch(direction) {
		case FORWARD: 
			return sensorForward;
		case BACKWARD:
			return sensorBackward;
		case LEFT:
			return sensorLeft;
		case RIGHT:
			return sensorRight;
		}
		return null;
	}
	/**
	 * Calculates the distance to obstacle (a wall)
	 * in a given direction with that sensor's direction.
	 * Overrides ReliableRobot method because it
	 * uses the sensors saved in UnreliableRobot.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws 
		UnsupportedOperationException, Exception {
		// UnreliableSensor is not always operational
		// check that there is a sensor in the given direction
		// and check if that sensor is operational
		boolean sensorExistsOperational = determineSensorIsOperational(direction);
		// if there isn't a sensor, throw exception
		if (!sensorExistsOperational) throw new UnsupportedOperationException();
		switch(direction) {
			case LEFT:
				return distanceToObstacleLeft();
			case RIGHT:
				return distanceToObstacleRight();
			case FORWARD:
				// forward sensor look in current direction
				return sensorForward.distanceToObstacle(getCurrentPosition(), robotController.getCurrentDirection(), batteryLevel);
			case BACKWARD:
				return distanceToObstacleBackward();
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle
	 * Determines if sensor in given direction is null
	 * or is not currently operational (is failed)
	 * @param direction
	 * @return
	 */
	private boolean determineSensorIsOperational(Direction direction) {
		boolean sensorExistsOperational = true;
		switch(direction) {
			case LEFT:
				if(sensorLeft==null || !sensorLeft.getIsOperational()) {
					sensorExistsOperational = false;
				}
				break;
			case RIGHT:
				if(sensorRight==null || !sensorRight.getIsOperational()) {
					sensorExistsOperational = false;
				}
				break;
			case FORWARD:
				if(sensorForward==null || !sensorForward.getIsOperational()) {
					sensorExistsOperational = false;
				}
				break;
			case BACKWARD:
				if(sensorBackward==null || !sensorBackward.getIsOperational()) {
					sensorExistsOperational = false;
				}
				break;
		}
		return sensorExistsOperational;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines what cardinal direction to check distance with
	 * with given sensor based on current direction of robot and 
	 * placement of sensor (left in this case).
	 * @return
	 * @throws Exception
	 */
	private int distanceToObstacleLeft() throws Exception {
		switch(robotController.getCurrentDirection()) {
		case North:
			// if left sensor and north current direction, look east
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		case East:
			// if left sensor and east current direction, look south
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		case South:
			// if left sensor and south current direction, look west
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case West:
			return sensorLeft.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines what cardinal direction to check distance with
	 * with given sensor based on current direction of robot and 
	 * placement of sensor (right in this case).
	 * @return
	 * @throws Exception
	 */
	private int distanceToObstacleRight() throws Exception {
		switch(robotController.getCurrentDirection()) {
		case North:
			// if right sensor and north current direction, look west
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case East:
			// if right sensor and north current direction, look north
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		case South:
			// if right sensor and north current direction, look east
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		case West:
			// if right sensor and north current direction, look south
			return sensorRight.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		}
		return 0;
	}
	/**
	 * Helper method for distanceToObstacle.
	 * Determines what cardinal direction to check distance with
	 * with given sensor based on current direction of robot and 
	 * placement of sensor (backward in this case).
	 * @return
	 * @throws Exception
	 */
	private int distanceToObstacleBackward() throws Exception {
		switch(robotController.getCurrentDirection()) {
		case North:
			// if backward sensor and north current direction, look south
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.South, batteryLevel);
		case East:
			// if backward sensor and east current direction, look west
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.West, batteryLevel);
		case South:
			// if backward sensor and south current direction, look north
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.North, batteryLevel);
		case West:
			// if backward sensor and west current direction, look east
			return sensorBackward.distanceToObstacle(getCurrentPosition(), CardinalDirection.East, batteryLevel);
		}
		return 0;
	}
	/**
	 * Start thread for sensor in given direction.
	 * The thread should flip the sensor between
	 * operational and non-opertational.
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
			switch (direction) {
				case LEFT:
					// start left thread -- failure and repair process for left sensor
					// as long as left sensor is an UnreliableSensor
					if (sensorLeft instanceof UnreliableSensor) {
						leftThread = new Thread((UnreliableSensor) sensorLeft);
						leftThread.start();
					}
					else throw new UnsupportedOperationException();
					break;
				case RIGHT:
					// start right thread -- failure and repair process for left sensor
					// as long as left sensor is an UnreliableSensor
					if (sensorRight instanceof UnreliableSensor) {
						rightThread = new Thread((UnreliableSensor) sensorRight);
						rightThread.start();
					}
					else throw new UnsupportedOperationException();
					break;
				case FORWARD:
					// start forwrad thread -- failure and repair process for left sensor
					// as long as forward sensor is an UnreliableSensor
					if (sensorForward instanceof UnreliableSensor) {
						forwardThread = new Thread((UnreliableSensor) sensorForward);
						forwardThread.start();
					}
					else throw new UnsupportedOperationException();
					break;
				case BACKWARD:
					// start backward thread -- failure and repair process for left sensor
					// as long as backward sensor is an UnreliableSensor
					if (sensorBackward instanceof UnreliableSensor) {
						backwardThread = new Thread((UnreliableSensor) sensorBackward);
						backwardThread.start();
					}
					else throw new UnsupportedOperationException();
					break;
			}

	}
	/**
	 * Stops the thread activity for a sensor in a
	 * given @param direction by interrupting the thread.
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		switch (direction) {
			case LEFT:
				leftThread.interrupt();
				break;
			case RIGHT:
				rightThread.interrupt();
				break;
			case FORWARD:
				forwardThread.interrupt();
				break;
			case BACKWARD:
				backwardThread.interrupt();
				break;
		}
	}
}		
