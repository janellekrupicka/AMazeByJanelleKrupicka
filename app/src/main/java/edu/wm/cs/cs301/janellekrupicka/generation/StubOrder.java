package edu.wm.cs.cs301.janellekrupicka.generation;

public class StubOrder implements Order {
	
	private int skillLevel;
	private boolean perfect;
	private int seed;
	private Maze maze;
	private Builder builder;
	
	// set methods to change value of instance 
	// variables for testing
	public void setSkillLevel(int inputSkillLevel) {
		skillLevel = inputSkillLevel;
	}
	
	public void setBuilder(Builder inputBuilder) {
		builder = inputBuilder;
	}
	
	public void setPerfect(boolean inputPerfect) {
		perfect = inputPerfect;
	}
	
	public void setSeed(int inputSeed) {
		seed = inputSeed;
	}
	
	// get methods
	public Maze getMaze() {
		return maze;
	}
	
	@Override
	public int getSkillLevel() {
		return skillLevel;
	}
	
	@Override
	public Builder getBuilder() {
		return builder;
	}

	@Override
	public boolean isPerfect() {
		return perfect;
	}
	@Override
	public int getSeed() {
		return seed;
	}

	// set maze configuration
	@Override
	public void deliver(Maze mazeConfig) {
		maze = mazeConfig;
	}

	@Override
	public void updateProgress(int percentage) {
		// not necessary
	}

}
