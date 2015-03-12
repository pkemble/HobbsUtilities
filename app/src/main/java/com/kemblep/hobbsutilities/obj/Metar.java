package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

public class Metar {
	private String _url = Strings.MetarUrl;
	public String FormattedMetar;
	public String Latitude;
	public String Longitude;
	private String _noData;
	private String _metarRawTextField = Strings.RawTextField;
	private String _latitudeTextField = Strings.LatitudeTextField;
	private String _longitudeTextField = Strings.LongitudeTextField;
	
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
