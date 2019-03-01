package graphing;

import java.io.File;
import java.util.ArrayList;

public abstract class DataReader {
	public static DataReader ReaderFactory(File f){
		String s = f.getPath();
		if(s.toLowerCase().contains(".csv"))
			return new CSVReader(f);
		else
			return null;
	}
	public abstract DataType getVariableDataType(String var);
	public abstract String[] getVariables();
	public abstract DataPoint[] getAllPoints();
	public abstract ArrayList<DataPoint> getAllPointsWith(String objVarName, String objVar);
	public abstract DataType getVariableType(String varName);
	public abstract ArrayList<String> getAllObjects(String objVarName);
}
