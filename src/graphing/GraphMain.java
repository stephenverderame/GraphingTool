package graphing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

//A new comment! (Git tutorial)

public class GraphMain extends JFrame implements ActionListener {
	
	private JButton importBtn, graphBtn;
	private JTextField titleEntry;
	private JComboBox<String> xSel, ySel, idSel, graphSel;
	private DataReader dReader;
	private JFreeChart chart;
	private ChartPanel panel;
	public GraphMain(){
		super("Table Grapher");
		this.setVisible(true);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		c.gridx = 0;
		c.gridy = 0;
		importBtn = new JButton("Import Data");
		importBtn.addActionListener(this);
		this.add(importBtn, c);
		String[] graphs = {"XY", "Time-Line", "Line", "Bar"};
		graphSel = new JComboBox<String>(graphs);
		c.gridx = 2;
		this.add(graphSel, c);
		String[] comboInit = {"No Imported Data"};
		c.weighty = 0;
		
		c.gridx = 0;
		c.gridy = 1;
		this.add(new JLabel("X:"), c);
		c.gridy = 2;
		xSel = new JComboBox<String>(comboInit);
		xSel.addActionListener(this);
		this.add(xSel, c);
		c.gridx = 1;
		c.gridy = 1;
		this.add(new JLabel("Y:"), c);
		c.gridy = 2;
		ySel = new JComboBox<String>(comboInit);
		ySel.addActionListener(this);
		this.add(ySel, c);
		c.gridx = 2;
		c.gridy = 1;
		this.add(new JLabel("Obj:"), c);
		c.gridy = 2;
		idSel = new JComboBox<String>(comboInit);
		idSel.addActionListener(this);
		this.add(idSel, c);
		c.weighty = 0.1;
		titleEntry = new JTextField("Graph Title");
		c.gridx = 0;
		c.gridy = 3;
		this.add(titleEntry, c);
		c.weighty = 0.5;
		graphBtn = new JButton("Graph It!");
		graphBtn.addActionListener(this);
		c.gridx = 1;
		c.gridy = 4;
		this.add(graphBtn, c);

		
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				GraphMain main = new GraphMain();
			}
			
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == importBtn) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Accepted Files (.csv)", "csv");
			fc.setFileFilter(filter);
			int ret = fc.showOpenDialog(this);
			if(ret == JFileChooser.APPROVE_OPTION){
				System.out.println("Chosen!");
				dReader = DataReader.ReaderFactory(fc.getSelectedFile());
				initUI();
			}
		}else if(e.getSource() == graphBtn) {
			Graph graph = Graph.GraphFactory((String)graphSel.getSelectedItem());
			graph.init((String)xSel.getSelectedItem(), (String)ySel.getSelectedItem(), (String)idSel.getSelectedItem(), titleEntry.getText(), dReader);
			chart = graph.getChart();
/*			XYPlot plot = chart.getXYPlot();
			plot.setRenderer(new XYLineAndShapeRenderer());
			plot.setBackgroundPaint(Color.white);
			plot.setRangeGridlinesVisible(true);
			plot.setRangeGridlinePaint(Color.BLACK);
			plot.setDomainGridlinesVisible(true);
			plot.setDomainGridlinePaint(Color.BLACK);*/
			
			if(panel != null) this.remove(panel);
			panel = new ChartPanel(chart);
			panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			panel.setBackground(Color.white);
			panel.setMouseZoomable(true, false);
			panel.setDomainZoomable(true);
			panel.setRangeZoomable(true);
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 0.5;
			c.weighty = 0.5;
			c.gridx = 0;
			c.gridwidth = 3;
			c.gridy = 5;
			c.gridheight = 2;
			this.add(panel, c);
			revalidate();
			
		}
		
	}
	private void initUI() {
		xSel.removeAllItems();
		ySel.removeAllItems();
		idSel.removeAllItems();
		String[] vars = dReader.getVariables();
		for(int i = 0; i < vars.length; ++i){
			xSel.addItem(vars[i]);
			ySel.addItem(vars[i]);
			idSel.addItem(vars[i]);
		}
		idSel.addItem("NULL");
		
	}

}
