package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

public class Metar {
	private String mUrl = Strings.MetarUrl;
	public String FormattedMetar;
	public String Latitude;
	public String Longitude;
	private String mNoData;
	private String mMetarRawTextField = Strings.RawTextField;
	private String mLatitudeTextField = Strings.LatitudeTextField;
	private String mLongitudeTextField = Strings.LongitudeTextField;
	
	public Metar(String station){
		
		mNoData = "No METAR data for station " + station;
		
		Document doc;
		try {
			mUrl = mUrl.replace("<STATION_ID>", station);
			doc = new GetWx().execute(mUrl).get();
			if(doc != null){
				NodeList nodeList = doc.getElementsByTagName(mMetarRawTextField);
				if(nodeList.getLength() == 0){
					FormattedMetar = mNoData;
				} else {
					Latitude = doc.getElementsByTagName(mLatitudeTextField).item(0).getTextContent();
					Longitude = doc.getElementsByTagName(mLongitudeTextField).item(0).getTextContent();
					FormattedMetar = nodeList.item(0).getTextContent(); 
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
