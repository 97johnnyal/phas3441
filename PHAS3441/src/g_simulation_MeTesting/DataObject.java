package g_simulation_MeTesting;

public class DataObject {
	//variables to be carried , steady state velocity, @time reached
	TwoVector steadyVel;
	double time;
	boolean steadyStateReached = false;
	
	public DataObject(TwoVector steadyVel, double time) {
		this.steadyVel = steadyVel;
		this.time = time;
		}
	
}
