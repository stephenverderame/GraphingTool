package graphing;

public enum DataType {
	FLOAT{
		public String toString(){
			return "FLOAT";
		}
	},
	INT{
		public String toString(){
			return "INTEGER";
		}
	},
	STRING{
		public String toString(){
			return "STRING";
		}
	};
}
