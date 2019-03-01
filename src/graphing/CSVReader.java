package graphing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class CSVReader extends DataReader {
	private File file;
	private ArrayList<String> variables;
	private DataType[] variableTypes;
	private ArrayList<String[]> points;
	CSVReader(File f) {
		this.file = f;
		variables = new ArrayList<String>();
		points = new ArrayList<String[]>();
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			String vars = reader.readLine();
			String v = vars.replace("\"", "");
			System.out.println(vars);
			while(v.indexOf(',') != -1){
				variables.add(v.substring(0, v.indexOf(',')));
				v = v.substring(v.indexOf(',') + 1);
			}
			variables.add(v);
			variableTypes = new DataType[variables.size()];
			String line = reader.readLine();
			boolean first = true;
			while(line != null){
				String[] data = new String[variables.size()];
				line = line.replace("\"", "");
				int i = 0;
				while(line.indexOf(',') != -1){
					data[i++] = line.substring(0, line.indexOf(','));
					if(first)
						variableTypes[i - 1] = getVarType(data[i - 1]);
					line = line.substring(line.indexOf(',') + 1);
				}
				data[i] = line;
				if(first) variableTypes[i] = getVarType(line);
				points.add(data);
				first = false;
				line = reader.readLine();
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private DataType getVarType(String element){
		if(element.matches("(^-)*[\\d]+(\\.)[\\d]+") && !element.matches("[a-zA-Z]"))
			return DataType.FLOAT;
		else if(element.matches("(^-)*[\\d]+") && !element.matches("[a-zA-Z]"))
			return DataType.INT;
		return DataType.STRING;
	}
	@Override
	public DataType getVariableDataType(String var) {
		for(int i = 0; i < variables.size(); ++i){
			if(variables.get(i).equals(var)) return variableTypes[i];
		}
		return null;
	}
	@Override
	public String[] getVariables() {
		String[] ret = new String[variables.size()];
		for(int i = 0; i < ret.length; ++i)
			ret[i] = variables.get(i);
		return ret;
	}
	@Override
	public DataPoint[] getAllPoints() {
		DataPoint[] dp = new DataPoint[points.size()];
		for(int i = 0; i < dp.length; ++i){
			dp[i] = toDataPoint(points.get(i));
		}
		return dp;
	}
	@Override
	public ArrayList<DataPoint> getAllPointsWith(String objVarName, String objVar) {
		ArrayList<DataPoint> dp = new ArrayList<DataPoint>();
		int objId = -1;
		for(int i = 0; i < variables.size(); ++i){
			if(variables.get(i).equals(objVarName)){
				objId = i;
				break;
			}
		}
		for(int i = 0; i < points.size(); ++i){
			if(points.get(i)[objId].equals(objVar))
				dp.add(toDataPoint(points.get(i)));
		}
		return dp;
	}
	private DataPoint toDataPoint(String[] ptData) {
		Object[] data = new Object[ptData.length];
		for(int j = 0; j < ptData.length; ++j) {
			switch(variableTypes[j]){
			case STRING:
				data[j] = ptData[j];
				break;
			default:
				data[j] = Double.parseDouble(ptData[j]);
				break;
			
			}
		}
		String[] varNames = new String[variables.size()];
		for(int j = 0; j < variables.size(); ++j)
			varNames[j] = variables.get(j);
		return new DataPoint(data, variableTypes, varNames);
	}
	@Override
	public DataType getVariableType(String varName) {
		for(int i = 0; i < variables.size(); ++i)
			if(variables.get(i).equals(varName)) return variableTypes[i];
		return null;
	}
	@Override
	public ArrayList<String> getAllObjects(String objVarName) {
		ArrayList<String> objects = new ArrayList<String>();
		int objIndex = -1;
		for(int i = 0; i < variables.size(); ++i){
			if(variables.get(i).equals(objVarName)){
				objIndex = i;
				break;
			}
		}
		for(String[] pt : points){
			boolean add = true;
			for(String already : objects){
				if(already.equals(pt[objIndex])){
					add = false;
					break;
				}
			}
			if(add) objects.add(pt[objIndex]);
		}
		return objects;
	}
}
