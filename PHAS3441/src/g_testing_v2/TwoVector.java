package g_testing_v2;

public class TwoVector {
	// Defining member variables
	private double x;
	private double y;
	
	/** Constructor method to create the TwoVector Object
	 * @param x
	 * @param y
	 */
	public TwoVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/** Subtraction method that subtracts the second TwoVector (B) from the first TwoVector (A)
	 * static method
	 * @param A
	 * @param B
	 * @return A-B
	 */
	public static TwoVector subtract(TwoVector A, TwoVector B) {
		TwoVector substracted = new TwoVector((A.x - B.x) , (A.y - B.y));
		return substracted;
	}
	
	/** Subtraction method that subtracts the second TwoVector (B) from this TwoVector
	 * non-static method
	 * @param B
	 * @return this-B
	 */
	public TwoVector subtract(TwoVector B) {
		return subtract(this, B);
	} 
	
	/** Addition method that adds the 2 TwoVectors together
	 * static method
	 * @param A
	 * @param B
	 * @return A+B
	 */
	public static TwoVector add(TwoVector A, TwoVector B) {
		TwoVector added = new TwoVector((A.x + B.x) , (A.y + B.y));
		return added;
	}
	
	/** Addition method that adds the current TwoVector to the input twoVector together
	 * non-static method
	 * @param B
	 * @return this+B
	 */
	public TwoVector add(TwoVector B) {
		return add(this, B);
	} 
	
	/** multiply method that multiplies the 2 TwoVectors together
	 * static method
	 * @param A
	 * @param B
	 * @return A*B
	 */
	public static TwoVector multiply(TwoVector A, double variable) {
		TwoVector multiplied = new TwoVector ((A.x*variable), (A.y *variable));
		return multiplied;
	}
	
	/** multiply method that multiplies the current vector with another TwoVectors together
	 * non-static method
	 * @param A
	 * @return A*this
	 */
	public TwoVector multiply(double variable) {
		return multiply(this, variable);
	}
	
	/** magnitude method calculates the magnitude of the input TwoVector
	 * static method
	 * @param A
	 * @return magnitude of TwoVector A
	 */
	public static double magnitude(TwoVector A) {
		double magnitude = Math.sqrt(Math.pow(A.x , 2) + Math.pow(A.y , 2));
		return magnitude;
	}
	
	/** magnitude method calculates the magnitude of this TwoVector
	 * non-static method
	 * @param nil
	 * @return magnitude of this TwoVector
	 */
	public double magnitude() {
		return magnitude(this);
	}
	
	/** unitVect method determines the unit vector for the input TwoVector
	 * static method
	 * @param A
	 * @return unit vector of A
	 */
	public static TwoVector unitVect(TwoVector A) {
		TwoVector unit = new TwoVector((A.x / A.magnitude()), (A.y / A.magnitude()));
		return unit;
	}
	
	/** unitVect method determines the unit vector of this TwoVector
	 * non-static method
	 * @return unit vector of this twovector
	 */
	public TwoVector unitVect() {
		return unitVect(this);
	}
	
	/** equalsTo method checks if the 2 input Twovectors are equal
	 * static method
	 * @param A
	 * @param B
	 * @return boolean true or false
	 */
	public static boolean equalsTo(TwoVector A, TwoVector B) {
		if ( (A.x == B.x) && (A.y == B.y)) {
			return true;
		}
		return false;
	}
	
	/** equalsTo method checks if the input Twovector is equal to this twovector
	 * @param B
	 * @return boolean true or false
	 */
	public boolean equalsTo(TwoVector B) {
		return equalsTo(this, B);
	}

	// Determines the scalar product of two vectors
	public static double scalarProduct(TwoVector v1, TwoVector v2) {
		double scalarProd = (v1.x * v2.x) + (v1.y *v2.y);
		return scalarProd;
	}
	
	// Determines the angle between vector v1 and vector v2 in radians
	public static double angle(TwoVector v1, TwoVector v2) throws Exception {
		// Checks if v1 is a zero vector
		if (v1.magnitude()==0) {
			throw new Exception("First vector entered is a zero vector. Cannot find angle.");
		}
		// Checks if v2 is a zero vector
		else if (v2.magnitude()==0) {
			throw new Exception("Second vector entered is a zero vector. Cannot find angle.");
		}
		
		else {
		
			double ang = Math.acos(scalarProduct(v1,v2) / (v1.magnitude()*v2.magnitude()));
			return ang;
		}
	}
	
	
	@Override
	public String toString() {
		return x + "	" + y ;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static double angle(TwoVector displacement) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
