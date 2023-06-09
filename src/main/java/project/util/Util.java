package project.util;

public enum Util {

    GRIDSIZE(25),
    NUMSTEPS(40),
    INITIALINFECTED(5),
    
    INFECTIONRATE(0.99),
    RECOVERYRATE(0.01),
    VACCINATIONRATE(0.0),
    
    IMPORTRATE(0.05);
    

    private double percentage;
    private int value;
    
	private Util(int value) {
		this.value = value;
	}

	private Util(double percentage) {
		this.percentage = percentage;
	}

	public double getPercentage() {
		return percentage;
	}

	public int getValue() {
		return value;
	} 
    
}
