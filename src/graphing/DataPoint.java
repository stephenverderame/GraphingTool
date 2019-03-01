package graphing;

public class DataPoint {
	final private Object[] variables;
	final private String[] variableNames;
	public DataPoint(Object[] variables, DataType[] variableTypes, String[] variableNames){
		this.variables = variables;
		this.variableNames = variableNames;
	}
	public Object getVariable(String var) {
		for(int i = 0; i < variableNames.length; ++i)
			if(variableNames[i].equals(var)) return variables[i];
		return null;
	}
	public Object getVariable(int index){
		return variables[index];
	}
	
}
