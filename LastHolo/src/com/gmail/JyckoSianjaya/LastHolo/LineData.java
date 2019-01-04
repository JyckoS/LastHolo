package com.gmail.JyckoSianjaya.LastHolo;

public class LineData {
	private String lines = "";
	private Boolean isValid = false;
	private int currentline = 0;
	private String cache = "";
	private String lastcolors = "";
	public LineData(int line, String word, Boolean valid, String cache, String lastcolors) {
		this.cache = cache;
		currentline = line;
		lines = word;
		this.lastcolors = lastcolors;
		isValid = valid;
	}
	public void setCache(String str) {
		cache = str;
	}
	public String getLastColors() {
		return lastcolors;
	}
	public void setLastColors(String color) {
		this.lastcolors = color;
	}
	public String getCache() {
		return cache;
	}
	public String getFinal() {
		int length = cache.length();
		if (length <= 1) { 
			return lines;
		}
		if (cache.substring(length, length).equals(" ")) {
			return lastcolors + cache + lines;
		}
		return lastcolors + cache + " " + lines;
	}
	public void setLine(String str) {
		lines = str;
	}
	public void changeValid(Boolean val) {
		isValid = val;
	}
	public void changeLine(int newline) {
		currentline = newline;
	}
	public String getLine() {
		return lines;
	}
	public boolean isValid() {
		return isValid;
	}
	public int getCurrentLine() {
		return currentline;
	}
	
}
