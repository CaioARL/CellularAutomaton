import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;

public class CellularAutomaton {
    private int[][] lattice;
    private int gridSize;
    private double infectionRate;
    private double recoveryRate;
    private double vaccinationRate;
    private double importRate;

    public CellularAutomaton(int gridSize, double infectionRate, double recoveryRate, double vaccinationRate,
            double importRate, int initialInfected) {
        this.gridSize = gridSize;
        this.infectionRate = infectionRate;
        this.recoveryRate = recoveryRate;
        this.vaccinationRate = vaccinationRate;
        this.importRate = importRate;
        lattice = new int[gridSize][gridSize];
        initializeLattice(initialInfected);
    }

    private void initializeLattice(int initialInfected) {
        Random random = new Random();
        int count = 0;

        while (count < initialInfected) {
            int x = random.nextInt(gridSize);
            int y = random.nextInt(gridSize);

            if (lattice[x][y] == 0) {
                lattice[x][y] = 1;
                count++;
            }
        }
    }

    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps; step++) {
            int[][] newLattice = new int[gridSize][gridSize];

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    int currentState = lattice[i][j];
                    int nextState = currentState;

                    if (currentState == 0) { // Susceptible
                        nextState = updateSusceptible(i, j);
                    } else if (currentState == 1) { // Infected
                        nextState = updateInfected(i, j);
                    } else if (currentState == 2) { // Recovered
                        nextState = updateRecovered(i, j);
                    }

                    newLattice[i][j] = nextState;
                }
            }

            lattice = newLattice;
        }
    }

    private int updateSusceptible(int x, int y) {
        int numInfectedNeighbors = countInfectedNeighbors(x, y);
        double infectionProbability = (1 - vaccinationRate) * (1 - Math.exp(-infectionRate * numInfectedNeighbors));
        double importProbability = importRate * (1 - vaccinationRate);

        Random random = new Random();
        double rand = random.nextDouble();

        if (rand < infectionProbability * 0.99) {
            return 1; // Infected
        } else if (rand < (infectionProbability + importProbability) * 0.01) {
            return 1; // Infected (imported case)
        } else {
            return 0; // Susceptible
        }
    }

    private int updateInfected(int x, int y) {
        Random random = new Random();
        double rand = random.nextDouble();

        if (rand < recoveryRate * 0.01) {
            return 2; // Recovered
        } else {
            return 1; // Infected
        }
    }

    private int updateRecovered(int x, int y) {
        int numInfectedNeighbors = countInfectedNeighbors(x, y);
        double infectionProbability = (1 - vaccinationRate) * (1 - Math.exp(-infectionRate * numInfectedNeighbors));

        Random random = new Random();
        double rand = random.nextDouble();

        if (rand < infectionProbability * 0.65) {
            return 1; // Infected (re-infected)
        } else {
            return 2; // Recovered
        }
    }

    private int countInfectedNeighbors(int x, int y) {
        int count = 0;

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < gridSize && j >= 0 && j < gridSize) {
                    if (lattice[i][j] == 1) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public void printState() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(lattice[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void simulateAndPlot(int numSteps, String seriesName) {
        List<Integer> infectedCounts = new ArrayList<>();
        List<Integer> recoveredCounts = new ArrayList<>();
        List<Integer> susceptibleCounts = new ArrayList<>();

        for (int step = 1; step <= numSteps; step++) {
            int numInfected = countInfected();
            int numRecovered = countRecovered();
            int numSusceptible = countSusceptible();

            infectedCounts.add(numInfected);
            recoveredCounts.add(numRecovered);
            susceptibleCounts.add(numSusceptible);

            simulate(1);
        }

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Número de infectados, recuperados e suscetíveis ao longo do tempo")
                .xAxisTitle("Etapa")
                .yAxisTitle("Número de indivíduos")
                .build();

        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setChartFontColor(Color.DARK_GRAY);
        chart.getStyler().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendVisible(true);

        chart.addSeries("Infectados", null, infectedCounts);
        chart.addSeries("Recuperados", null, recoveredCounts);
        chart.addSeries("Suscetíveis", null, susceptibleCounts);

        new SwingWrapper<>(chart).displayChart();
    }

    private int countInfected() {
        int count = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (lattice[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countRecovered() {
        int count = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (lattice[i][j] == 2) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countSusceptible() {
        int count = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (lattice[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
