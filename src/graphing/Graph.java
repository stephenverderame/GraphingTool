package graphing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public abstract class Graph {
	protected JFreeChart chart;
	public JFreeChart getChart() {
		return chart;
	}
	public abstract void init(String x, String y, String id, String title, DataReader dr);
	public static Graph GraphFactory(String graph) {
		if(graph.equals("Time-Line"))
			return new TimeGraph();
		else if(graph.equals("Line"))
			return new LineGraph();
		else if(graph.equals("Bar"))
			return new BarGraph();
		return new XYGraph();
	}
}
class TimeGraph extends Graph{

	@Override
	public void init(String x, String y, String id, String title, DataReader dr) {
		DataType typex = dr.getVariableDataType(x);
		DataType typey = dr.getVariableDataType(y);
		if(typex == DataType.STRING || typey == DataType.STRING){
			JOptionPane.showMessageDialog(null, "Basic time graph only allows numerical inputs and output", "Graph Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		if(id.equals("NULL")) {
			DataPoint[] pts = dr.getAllPoints();
			TimeSeries series = new TimeSeries(y + " vs " + x);
			for(DataPoint pt : pts){
				Date d = new Date(((Double)pt.getVariable(x)).longValue() * 1000);
				Double yd = (Double)pt.getVariable(y);
				Second second = new Second(d);
				series.add(second, yd);
			}
			dataset.addSeries(series);
		}else{
			String objVarName = id;
			ArrayList<String> list = dr.getAllObjects(objVarName);
			for(String obj : list){
				ArrayList<DataPoint> pts = dr.getAllPointsWith(objVarName, obj);
				TimeSeries series = new TimeSeries(obj);
				for(DataPoint pt : pts){
					Date d = new Date(((Double)pt.getVariable(x)).longValue() * 1000);
					Double yd = (Double)pt.getVariable(y);
					Second second = new Second(d);
					series.add(second, yd);
				}
				dataset.addSeries(series);
			}
		}
		chart = ChartFactory.createTimeSeriesChart(title, x, y, dataset);
		XYPlot plot = chart.getXYPlot();
		plot.setRenderer(new XYLineAndShapeRenderer());
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
	}
	
}
class XYGraph extends Graph{

	@Override
	public void init(String x, String y, String id, String title, DataReader dr) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		DataType typex = dr.getVariableDataType(x);
		DataType typey = dr.getVariableDataType(y);
		if(typex == DataType.STRING || typey == DataType.STRING){
			JOptionPane.showMessageDialog(null, "Basic line graph only allows numerical inputs and output", "Graph Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(id.equals("NULL")){
			DataPoint[] pts = dr.getAllPoints();
			XYSeries series = new XYSeries(y + " vs " + x);
			for(DataPoint pt : pts){
				Double xd = (Double)pt.getVariable(x); //only numbers allowed in this basic graph
				Double yd = (Double)pt.getVariable(y);
				series.add(xd, yd);
				
			}
			dataset.addSeries(series);
		}else{
			String objVarName = id;
			ArrayList<String> objs = dr.getAllObjects(objVarName);
			for(String obj : objs){
				ArrayList<DataPoint> pts = dr.getAllPointsWith(objVarName, obj);
				XYSeries series = new XYSeries(obj);
				for(DataPoint pt : pts){
					Double xd = (Double)pt.getVariable(x); //only numbers allowed in this basic graph
					Double yd = (Double)pt.getVariable(y);
					series.add(xd, yd);
				}
				dataset.addSeries(series);
			}
		}
		chart = ChartFactory.createXYLineChart(title, x, y, dataset);
		XYPlot plot = chart.getXYPlot();
		plot.setRenderer(new XYLineAndShapeRenderer());
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
	}
	
}
class LineGraph extends Graph {

	@Override
	public void init(String x, String y, String id, String title, DataReader dr) {
		if(dr.getVariableType(y) == DataType.STRING){
			JOptionPane.showMessageDialog(null, "Only numeric outputs allowed", "Graph Tool", JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if(id.equals("NULL")) {
			DataPoint[] pts = dr.getAllPoints();
			for(DataPoint pt : pts){
				String series = y + " vs " + x;
				DataType xt = dr.getVariableType(x);
				if(xt == DataType.STRING)
					dataset.addValue((Double)pt.getVariable(y), series, (String)pt.getVariable(x));
				else
					dataset.addValue((Double)pt.getVariable(y), series, (Double)pt.getVariable(x));
			}
		}else {
			ArrayList<String> vars = dr.getAllObjects(id);
			for(String var : vars){
				ArrayList<DataPoint> pts = dr.getAllPointsWith(id, var);
				for(DataPoint pt : pts){
					DataType xt = dr.getVariableType(x);
					if(xt == DataType.STRING)
						dataset.addValue((Double)pt.getVariable(y), var, (String)pt.getVariable(x));
					else
						dataset.addValue((Double)pt.getVariable(y), var, (Double)pt.getVariable(x));
				}
			}
		}
		chart = ChartFactory.createLineChart(title, x, y, dataset);
		
	}
	
}
class BarGraph extends Graph{

	@Override
	public void init(String x, String y, String id, String title, DataReader dr) {
		if(dr.getVariableType(y) == DataType.STRING){
			JOptionPane.showMessageDialog(null, "Only numeric outputs allowed", "Graph Tool", JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if(id.equals("NULL")) {
			DataPoint[] pts = dr.getAllPoints();
			for(DataPoint pt : pts){
				String series = y + " vs " + x;
				DataType xt = dr.getVariableType(x);
				if(xt == DataType.STRING)
					dataset.addValue((Double)pt.getVariable(y), series, (String)pt.getVariable(x));
				else
					dataset.addValue((Double)pt.getVariable(y), series, (Double)pt.getVariable(x));
			}
		}else {
			ArrayList<String> vars = dr.getAllObjects(id);
			for(String var : vars){
				ArrayList<DataPoint> pts = dr.getAllPointsWith(id, var);
				for(DataPoint pt : pts){
					DataType xt = dr.getVariableType(x);
					if(xt == DataType.STRING)
						dataset.addValue((Double)pt.getVariable(y), var, (String)pt.getVariable(x));
					else
						dataset.addValue((Double)pt.getVariable(y), var, (Double)pt.getVariable(x));
				}
			}
		}
		chart = ChartFactory.createBarChart(title, x, y, dataset);
		
		
	}
	
}
