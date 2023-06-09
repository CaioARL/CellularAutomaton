package project;

import project.cellular.CellularAutomaton;
import project.util.Util;

public class Main {

	public static void main(String[] args) {

		CellularAutomaton model = new CellularAutomaton(Util.GRIDSIZE.getValue(), Util.INFECTIONRATE.getPercentage(),
				Util.RECOVERYRATE.getPercentage(), Util.VACCINATIONRATE.getPercentage(),
				Util.IMPORTRATE.getPercentage(), Util.INITIALINFECTED.getValue());

		model.printState();
		model.simulateAndPlot(Util.NUMSTEPS.getValue());
		model.printState();
	}

}