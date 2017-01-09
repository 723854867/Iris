package com.busap.vcs.util;

import java.util.regex.Pattern;
public class TimeCalculate {
	
	int hh;
	int mm;
	int ss;
	public TimeCalculate(String hhmmss){
		hhmmss = hhmmss.trim();
		if(!p.matcher(hhmmss).matches()){
			throw new IllegalArgumentException("incorrect duration : " + hhmmss);
		}
		String[] dd = hhmmss.split(":");
		this.ss = this.ss + Integer.parseInt(dd[2]);
		if(this.ss>60){
			this.ss = this.ss%60;
			this.mm = this.mm + this.ss/60;
		}
		this.mm = this.mm + Integer.parseInt(dd[1]);
		if(this.mm>60){
			this.mm = this.mm%60;
			this.hh = this.hh + this.ss/60;
		}
		this.hh = this.hh + Integer.parseInt(dd[0]);
	}
	public TimeCalculate(int hh, int mm, int ss) {
		super();
		this.hh = hh;
		this.mm = mm;
		this.ss = ss;
	}
	private static final String ps = "^\\d{1,2}:\\d{1,2}:\\d{1,2}$";
	private Pattern p = Pattern.compile(ps);
	/**
	 * @param duration “HH:MM:SS”
	 */
	public void addDuration(String duration){
		duration = duration.trim();
		if(!p.matcher(duration).matches()){
			throw new IllegalArgumentException("incorrect duration : " + duration);
		}
		String[] dd = duration.split(":");
		this.ss = this.ss + Integer.parseInt(dd[2]);
		if(this.ss>=60){
			int temp=this.ss;
			this.ss = temp%60;
			this.mm = this.mm + temp/60;
		}
		this.mm = this.mm + Integer.parseInt(dd[1]);
		if(this.mm>=60){
			int temp=this.mm;
			this.mm = temp%60;
			this.hh = this.hh + temp/60;
		}
		this.hh = this.hh + Integer.parseInt(dd[0]);
	}

	public String toString() {
		return (this.hh > 9 ? this.hh : "0" + this.hh) + ":"
				+ (this.mm > 9 ? this.mm : "0" + this.mm) + ":"
				+ (this.ss > 9 ? this.ss : "0" + this.ss);
	}
	public TimeCalculate() {
		super();
	}
	public int getHh() {
		return hh;
	}
	public void setHh(int hh) {
		this.hh = hh;
	}
	public int getMm() {
		return mm;
	}
	public void setMm(int mm) {
		this.mm = mm;
	}
	public int getSs() {
		return ss;
	}
	public void setSs(int ss) {
		this.ss = ss;
	}
	
	
}