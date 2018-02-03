package g_testing;

public class SimPoints extends TwoVector{
	//intiate value holder for PDF
	double pdf;
	
	public SimPoints(double x, double y,double pdf) {
		super(x, y);
		this.pdf = pdf;
	}
	
}
