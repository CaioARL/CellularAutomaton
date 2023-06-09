package project;

import project.cellular.CellularAutomaton;
import project.util.Util;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		CellularAutomaton model = new CellularAutomaton(Util.GRIDSIZE.getValue(), Util.INFECTIONRATE.getPercentage(),
				Util.RECOVERYRATE.getPercentage(), Util.VACCINATIONRATE.getPercentage(),
				Util.IMPORTRATE.getPercentage(), Util.INITIALINFECTED.getValue());

		model.printState();
		float tempoExecucao = model.simulateAndPlot(Util.NUMSTEPS.getValue());
		model.printState();
		System.out.printf("Tempo de execução: %.3fs", tempoExecucao);
	}

}