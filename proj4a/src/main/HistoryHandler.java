package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import java.util.List;
import browser.Plotter;
import org.knowm.xchart.XYChart;
import java.util.ArrayList;


public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap map;

    public HistoryHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        ArrayList<TimeSeries> lst = new ArrayList<>();
        for (String word : words) {
            lst.add(map.weightHistory(word, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lst);

        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
