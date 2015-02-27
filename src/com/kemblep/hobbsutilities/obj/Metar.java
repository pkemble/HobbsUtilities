package com.kemblep.hobbsutilities.obj;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.kemblep.hobbsutilities.GetWx;

import android.content.Context;
import android.content.res.Resources;

public class Metar {
	private String _url = WxReport.MetarUrl;
	public String FormattedMetar;
	public String Latitude;
	public String Longitude;
	private String _noData;
	private String _metarRawTextField = WxReport.RawTextField;
	private String _latitudeTextField = WxReport.LatitudeTextField;
	private String _longitudeTextField = WxReport.LongitudeTextField;
	
	public Metar(String station){
		
		_noData = "No METAR data for station " + station;
		
		Document doc;
		try {
			_url = _url.replace("<STATION_ID>", station);
			doc = new GetWx().execute(_url).get();
			if(doc != null){
				NodeList nodeList = doc.getElementsByTagName(_metarRawTextField);
				if(nodeList.getLength() == 0){
					FormattedMetar = _noData;
				} else {
					Latitude = doc.getElementsByTagName(_latitudeTextField).item(0).getTextContent();
					Longitude = doc.getElementsByTagName(_longitudeTextField).item(0).getTextContent();
					FormattedMetar = nodeList.item(0).getTextContent(); 
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
