public class Main {

    public static void main(String[] args) {
        int gridSize = 25;
        double infectionRate = 0.5;
        double recoveryRate = 0.3;
        double vaccinationRate = 0.0;
        double importRate = 0.05;
        int initialInfected = 5;
        int numSteps = 100;

        CellularAutomaton model = new CellularAutomaton(gridSize, infectionRate, recoveryRate, vaccinationRate, importRate, initialInfected);
        model.printState();

        model.simulateAndPlot(numSteps, "Execução 1");
        model.printState();
    }

}