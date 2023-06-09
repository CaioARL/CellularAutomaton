package project.chart;

import java.awt.Color;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;

public class Chart {

	public void showChart(List<Integer> infectedCounts, List<Integer> recoveredCounts,
			List<Integer> susceptibleCounts) {

		CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
				.title("Número de infectados, recuperados e suscetíveis ao longo do tempo").xAxisTitle("Etapa")
				.yAxisTitle("Número de indivíduos").build();

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

}
