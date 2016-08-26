package ca.aeso.ltlf.server.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Utility class for generating objects used in charting
 * 
 * @author mbodor
 */
public class GraphUtil {

	protected static Log logger = LogFactory.getLog(GraphUtil.class);
	private static GraphUtil _instance = null;
	private static HashMap colorMap = new HashMap();
	
	public static GraphUtil getInstance() {
		if (_instance==null) {
			_instance = new GraphUtil();
			colorMap.put("Original", Color.BLUE);
			colorMap.put("Corrected", Color.BLACK);
			colorMap.put("Comparison", Color.RED);
			colorMap.put("Fix 1", Color.GREEN);
			colorMap.put("Fix 2", Color.MAGENTA);
			colorMap.put("Fix 3", Color.ORANGE);
			colorMap.put("Fix 4", Color.CYAN);
		}
		return _instance;
	}
	
	/**
	 * Generate a chart for the MP Analysis editor
	 * @param dataset
	 * @param width
	 * @param height
	 * @return
	 */
	public static String generateTimeChart(TimeSeriesCollection dataset, Double maxValue, int width, int height) {
		logger.debug("GraphUtil.generateTimeChart() starting");

		final JFreeChart chart = createTimeChart(dataset, maxValue);
		String chartName = "";
		
        try {
    		ChartRenderingInfo info = new ChartRenderingInfo(null);
    		info.setEntityCollection(null);
            chartName = ServletUtilities.saveChartAsPNG(chart, width, height, info, null);
    		logger.debug("GraphUtil.generateTimeChart() PNG chart saved, chartname=" + chartName);
        } catch(Exception e) {
            // handle exception
    		logger.error("GraphUtil.generateTimeChart() exception saving chart: " + e.getMessage());
    		e.printStackTrace();
        }
		
		logger.debug("GraphUtil.generateTimeChart() done.");
		
		return chartName;
	}

    /**
     * Create a time chart, using the supplied dataset
     * @param dataset
     * @param maxValue
     * @return
     */
    private static JFreeChart createTimeChart(final TimeSeriesCollection dataset, Double maxValue) {

		logger.debug("GraphUtil.createTimeChart() starting");

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(null, null, "MW", dataset, true, false, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

        int numSeries = dataset.getSeriesCount();
		logger.debug("GraphUtil.createTimeChart() num series = " + numSeries);
        
		int seriesNum = 0;
		
        for (Iterator iterator = dataset.getSeries().iterator(); iterator.hasNext();) {
			TimeSeries series = (TimeSeries) iterator.next();
			Comparable name = series.getKey();
			logger.debug("GraphUtil.createTimeChart() processing series #" + seriesNum + ", key = " + name.toString());

			Object colorObj = colorMap.get(name);

	        if (colorObj!=null) {
	            renderer.setSeriesPaint(seriesNum, (Color)colorObj);
	        }

	        if (name.equals("Original"))
	        	renderer.setSeriesStroke(seriesNum, new BasicStroke(2));
	        else
	        	renderer.setSeriesStroke(seriesNum, new BasicStroke(1));
	        	
			seriesNum++;
        }			
        
        plot.setRenderer(renderer);
        
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        final TickUnits units = new TickUnits();
        
        units.add(new DateTickUnit(DateTickUnit.DAY, 1, DateTickUnit.HOUR, 1, new SimpleDateFormat("d-MMM-yy")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 2, DateTickUnit.HOUR, 1, new SimpleDateFormat("d-MMM-yy")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 7, DateTickUnit.DAY, 1, new SimpleDateFormat("d-MMM-yy")));
        units.add(new DateTickUnit(DateTickUnit.DAY, 15,  DateTickUnit.DAY, 1, new SimpleDateFormat("d-MMM-yy")));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 1, new SimpleDateFormat("MMM-yy")));
        
        axis.setStandardTickUnits(units);

        ValueAxis rangeAxis = plot.getRangeAxis(); 
        rangeAxis.setLowerBound(0);
        rangeAxis.setUpperBound(maxValue.doubleValue());
        
		logger.debug("GraphUtil.createTimeChart() done");
        
        return chart;
    }    

    /**
     * Generate a chart for the Load Shape Editor 
     * @param dataset
     * @param width
     * @param height
     * @param lowerBound
     * @param upperBound
     * @return
     */
	public static String generateShapeChart(XYSeriesCollection dataset, int width, int height, double lowerBound, double upperBound) {
		logger.debug("GraphUtil.generateShapeChart() starting");

		final JFreeChart chart = createShapeChart(dataset, lowerBound, upperBound);
		String chartName = "";
		
        try {
    		ChartRenderingInfo info = new ChartRenderingInfo(null);
    		info.setEntityCollection(null);
            chartName = ServletUtilities.saveChartAsPNG(chart, width, height, info, null);
    		logger.debug("GraphUtil.generateShapeChart() PNG chart saved, chartname=" + chartName);
        } catch(Exception e) {
            // handle exception
    		logger.error("GraphUtil.generateShapeChart() exception saving chart: " + e.getMessage());
    		e.printStackTrace();
        }
		
		logger.debug("GraphUtil.generateShapeChart() done.");
		
		return chartName;
	}

    /**
     * Create a shape chart, using the supplied dataset, with the supplied lower and upper Y-axis bounds
     * @param dataset
     * @return
     */
    private static JFreeChart createShapeChart(final XYSeriesCollection dataset, double lowerBound, double upperBound) {

		logger.debug("GraphUtil.createShapeChart() starting");
		 
		final JFreeChart chart = ChartFactory.createXYLineChart(null, null, "Unitized value", dataset, PlotOrientation.VERTICAL, true, false, false);

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);

		int seriesNum = 0;
		
        for (Iterator iterator = dataset.getSeries().iterator(); iterator.hasNext();) {
			XYSeries series = (XYSeries) iterator.next();
			Comparable name = series.getKey();
			logger.debug("GraphUtil.createShapeChart() processing series #" + seriesNum + ", key=" + name.toString() + ", size=" + series.getItemCount());
			
			Object colorObj = colorMap.get(name);

	        if (colorObj!=null) {
	            renderer.setSeriesPaint(seriesNum, (Color)colorObj);
	        }

	        if (name.equals("Original"))
	        	renderer.setSeriesStroke(seriesNum, new BasicStroke(2));
	        else
	        	renderer.setSeriesStroke(seriesNum, new BasicStroke(1));
	        	
			seriesNum++;
        }			
        
        plot.setRenderer(renderer);
        
        // set X axis ticks invisible
        ValueAxis domainAxis = plot.getDomainAxis(); 
        domainAxis.setTickLabelsVisible(false);
        
        // set Y axis boundaries
        ValueAxis rangeAxis = plot.getRangeAxis(); 
        rangeAxis.setLowerBound(lowerBound);
        rangeAxis.setUpperBound(upperBound);
        
		logger.debug("GraphUtil.createShapeChart() done");
        return chart;
    }    
}
