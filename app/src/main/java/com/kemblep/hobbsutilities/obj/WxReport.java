package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.Util;

public class WxReport {
	public String FullReport;
	public Metar Metar;
	public Taf Taf;
	public String FetchedTime;

	public WxReport(String station){
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
	
	public static String MetarUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=<STATION_ID>&hoursBeforeNow=1";
	public static String TafUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&stationString=<STATION_ID>&hoursBeforeNow=1";
	public static String TafRadiusUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&radialDistance=20;<LONGITUDE>,<LATITUDE>&hoursBeforeNow=1";
	public static String StationInfoUrl = "http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=stations&requestType=retrieve&format=xml&stationString=<STATION_ID>";
	public static String ForecastUrl = "http://forecast.weather.gov/MapClick.php?lat=<LATITUDE>&lon=<LONGITUDE>&site=okx&unit=0&lg=en&FcstType=dwml";
	//public static String ForecastUrl = "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php?whichClient=NDFDgen&lat=<LATITUDE>&lon=<LONGITUDE>&product=glance&Unit=e&Submit=Submit";
	public static String RawTextField = "raw_text";
	public static String LatitudeTextField = "latitude";
	public static String LongitudeTextField = "longitude";
	public static String SiteNameField = "site";
	public static String DescriptionTextField = "area-description";
	public static String TemperatureTextfield = "temperature";
	public static String ValueTextField = "value";
}
