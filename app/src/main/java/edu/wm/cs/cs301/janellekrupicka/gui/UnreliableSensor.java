package edu.wm.cs.cs301.janellekrupicka.gui;

/**
 * UnreliableSensor fails and repairs sensors.
 * If a robot has an unreliable sensor, it fails 
 * and repairs using startFailureAndRepairProcess.
 * 
 * Collaborators:
 * Is a subclass of ReliableSensor.
 * Implements Runnable
 * 
 * @author Janelle Krupicka
 *
 */
public class UnreliableSensor extends ReliableSensor implements Runnable {
	/**
	 * True if sensor is currently operational
	 * False if not
	 */
	private boolean isOperational;
	/**
	 * Starts running thread. run()
	 */
	public UnreliableSensor() {
		isOperational = true;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				isOperational = true;
				// wait for 4 seconds
				// should be working for 4 seconds
				Thread.sleep(4000);
				isOperational = false;
				// wait for 2 seconds
				// should be broken for 2 seconds
				Thread.sleep(2000);
				isOperational = true;
				break;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
//	public static void main (String[] args) {
//		UnreliableSensor sensor = new UnreliableSensor();
//		Thread sensorThread = new Thread(sensor); 
//		sensorThread.start();
//	}
	public boolean getIsOperational() {
		return isOperational;
	}
	
	/**
	 * Start the thread that fails and repairs. 
	 * Background thread flips the robot between fail and repair.
	 * @param meanTimeToRepair assumed to be constant, fixed to 2
	 * @param meanTimeBetweenFailures assumed to be constant, fixed to 4.
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		UnreliableSensor sensor = new UnreliableSensor();
		Thread sensorThread = new Thread(sensor);
		sensorThread.start();
	}
	
	/**
	 * Ends the startFailureAndRepairProcess.
	 * Terminates the thread.
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		

	}

}
