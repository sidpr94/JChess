package chess;

public enum Color {
	WHITE ("W"),
	BLACK ("B"),
	EMPTY ("");
	
	String colorString;
	
	private Color(String colorString) {
		this.colorString = colorString;
	}
	
	public String getColorString() {
		return colorString;
	}
}
