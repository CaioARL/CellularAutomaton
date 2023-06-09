package project.cellular;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.chart.Chart;

public class CellularAutomaton {
	private static final int SUSCEPTIBLE = 0;
	private static final int INFECTED = 1;
	private static final int RECOVERED = 2;

	private int[][] grid;
	private int gridSize;
	private double infectionRate;
	private double recoveryRate;
	private double vaccinationRate;
	private double importRate;

	private Random random = new Random();

	public CellularAutomaton(int gridSize, double infectionRate, double recoveryRate, double vaccinationRate,
			double importRate, int initialInfected) {
		this.gridSize = gridSize;
		this.infectionRate = infectionRate;
		this.recoveryRate = recoveryRate;
		this.vaccinationRate = vaccinationRate;
		this.importRate = importRate;
		grid = new int[gridSize][gridSize];
		initializeGrid(initialInfected);
	}

	private void initializeGrid(int initialInfected) {
		int count = 0;

		while (count < initialInfected) {
			int x = random.nextInt(gridSize);
			int y = random.nextInt(gridSize);

			if (grid[x][y] == SUSCEPTIBLE) {
				grid[x][y] = INFECTED;
				count++;
			}
		}
	}

	public float simulateAndPlot(int numSteps) {
		float tempoInicial = System.nanoTime();
		List<Integer> infectedCounts = new ArrayList<>();
		List<Integer> recoveredCounts = new ArrayList<>();
		List<Integer> susceptibleCounts = new ArrayList<>();

		for (int step = 1; step <= numSteps; step++) {
			int[] counts = countStates();
			int numSusceptible = counts[SUSCEPTIBLE];
			int numInfected = counts[INFECTED];
			int numRecovered = counts[RECOVERED];

			infectedCounts.add(numInfected);
			recoveredCounts.add(numRecovered);
			susceptibleCounts.add(numSusceptible);

			simulate(1);
		}
		Chart chart = new Chart();

		chart.showChart(infectedCounts, recoveredCounts, susceptibleCounts, countStates()[INFECTED],
				countStates()[RECOVERED], countStates()[SUSCEPTIBLE]);

		return (System.nanoTime() - tempoInicial) / 1000000000;
	}

	public void simulate(int numSteps) {
		for (int step = 1; step <= numSteps; step++) {
			int[][] newGrid = new int[gridSize][gridSize];

			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					int currentState = grid[i][j];
					int nextState = currentState;

					if (currentState == SUSCEPTIBLE) {
						nextState = updateSusceptible(i, j);
					} else if (currentState == INFECTED) {
						nextState = updateInfected(i, j);
					} else if (currentState == RECOVERED) {
						nextState = updateRecovered(i, j);
					}

					newGrid[i][j] = nextState;
				}
			}

			grid = newGrid;
		}
	}

	private int updateSusceptible(int x, int y) {
		int numInfectedNeighbors = countInfectedNeighbors(x, y);
		double infectionProbability = (1 - vaccinationRate) * (1 - Math.exp(-infectionRate * numInfectedNeighbors));
		double importProbability = importRate * (1 - vaccinationRate);

		double rand = random.nextDouble();

		if (rand < infectionProbability) {
			return INFECTED;
		} else if (rand < (infectionProbability + importProbability)) {
			return INFECTED;
		} else {
			return SUSCEPTIBLE;
		}
	}

	private int updateInfected(int x, int y) {
		double rand = random.nextDouble();

		if (rand < recoveryRate) {
			return RECOVERED;
		} else {
			return INFECTED;
		}
	}

	private int updateRecovered(int x, int y) {
		int numInfectedNeighbors = countInfectedNeighbors(x, y);
		double infectionProbability = (1 - vaccinationRate) * (1 - Math.exp(-infectionRate * numInfectedNeighbors));

		double rand = random.nextDouble();

		if (rand < infectionProbability) {
			return INFECTED;
		} else {
			return RECOVERED;
		}
	}

	private int countInfectedNeighbors(int x, int y) {
		int count = 0;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < gridSize && j >= 0 && j < gridSize) {
					if (grid[i][j] == INFECTED) {
						count++;
					}
				}
			}
		}

		return count;
	}

	private int[] countStates() {
		int[] counts = new int[3];

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				int state = grid[i][j];
				counts[state]++;
			}
		}

		return counts;
	}

	public void printState() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
