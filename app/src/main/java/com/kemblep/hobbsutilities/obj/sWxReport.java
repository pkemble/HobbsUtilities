package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.Util;

public class sWxReport {
	public String FullReport;
	public Metar Metar;
	public Taf Taf;
	public String FetchedTime;

	public sWxReport(String station){
		Metar = new Metar(station);
		Taf = new Taf(station);
		FullReport = _fullReport();
		FetchedTime = _fetchedTime();
	}
	
	private String _fullReport() {
		// TODO Auto-generated method stub
		if(Metar != null && Taf != null) {
			return Metar.FormattedMetar + "\n\n" + Taf.FormattedTaf;
		}
		return "";
	}
	
	private String _fetchedTime() {
		return Util.getZuluTime();		
	}

    public static String ValueTextField = "value";
}
