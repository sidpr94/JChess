package chess;

public enum Alliance {
	WHITE ("W"),
	BLACK ("B"),
	EMPTY ("");
	
	String colorString;
	
	private Alliance(String colorString) {
		this.colorString = colorString;
	}
	
	public String getColorString() {
		return colorString;
	}
}
