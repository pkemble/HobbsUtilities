package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

//https://aviationweather.gov/dataserver/fields?datatype=taf
//line breaks before TEMPO & FM

public class Taf {
	private String _url = Strings.TafUrl;
	private String _radiusUrl = Strings.TafRadiusUrl;
	private String _station;
	private String _time;
	private String[] fmPeriod = null;
	private String _noData; 
	public String FormattedTaf;
	private String _tafRawTextField = Strings.RawTextField;
	
	public Taf(String station) {
		_noData = "No TAF data for station within 10 miles " + station;
		
		Document doc;
		try {
			_url = _url.replace("<STATION_ID>", station);
			doc = new GetWx().execute(_url).get();
			if(doc != null){
				//TODO cast these better
//				_station = doc.getElementsByTagName("station_id").item(0).getTextContent();
//				_time = doc.getElementsByTagName("issue_time").item(0).getTextContent();
				NodeList nodeList = doc.getElementsByTagName(_tafRawTextField);
				if(nodeList.getLength() == 0){
					//try to get a radius search
					//get a metar first
					Metar metar = new Metar(station);
					if(metar.Latitude != null){
						//url is long then lat
						_radiusUrl = _radiusUrl.replace("<LATITUDE>", metar.Latitude).replace("<LONGITUDE>", metar.Longitude).replace("<STATION>", station);
						Document radiusDoc = new GetWx().execute(_radiusUrl).get();
						if(radiusDoc != null){
							NodeList radiusTafNodes = radiusDoc.getElementsByTagName(_tafRawTextField);
							if(radiusTafNodes.getLength() > 0){
								FormattedTaf = "Closest TAF within 20 miles: \n" + radiusTafNodes.item(0).getTextContent();
							} else {
								FormattedTaf = "No TAF data within 20 miles of " + station;
							}
						} else {
							FormattedTaf = _noData;
						}
					}
				} else {
					FormattedTaf = formatRawText(doc.getElementsByTagName(_tafRawTextField).item(0).getTextContent());
				}
			}
					
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String formatRawText(String rawText) {
		String formattedText = rawText.replace("FM", "\n FM").replace("TEMP", "\n TEMPO");
		return formattedText;
	}
}
