package mods.battleclasses.core;

public interface ICooldownHolder {
	
	/*
	 * Each class implementing this interface, should have a field like this to store the last time (/date) when it was set to cooldown
	 */
	//private float setTime
	
	public static final float COOLDOWN_INITIALIZER = 3600;
	
	/**
	 * Initializes the cooldownHolder by setting the setTime field to currentTime.
	 */
	public void initCooldownHolder();
	
	/**
	 * Returns the duration of cooldown time in seconds
	 * @return - cooldown time in seconds
	 */
	public float getCooldownDuration();
	
	/**
	 * Sets the cooldownSetTime to the current Time, if there is no cooldown going on at the moment
	 */
	public void setToCooldown();
	
	
	/**
	 * Sets the cooldownSetTime to the current Time, regardless if there is a cooldown going on at the moment
	 */
	public void setToCooldownForced();
	
	/**
	 * Returns the ammount of time remaining of the cooldown
	 * @return - remaining cooldown in seconds
	 */
	public float getCooldownRemaining();
	
	/**
	 * Returns the boolean value of the remaining cooldown is greater smaller than zero or not
	 * @return - is implementer on cooldown
	 */
	public boolean isOnCooldown();
	
	/**
	 * Returns the key (int) to get this cooldown holder from the main cooldown map
	 * @return - main cooldown map key
	 */
	public int getCooldownHashCode();
	
	
	// Methods for TotalCooldownSync
	/**
	 * Used for TotalCooldownSync
	 * @return the last time (/date) when it was set to cooldown 
	 */
	public float getSetTime();
	
	/**
	 * Sets the setTime of the cooldown. Used for TotalCooldownSync.
	 */
	public void setSetTime(float t);
	
	
}