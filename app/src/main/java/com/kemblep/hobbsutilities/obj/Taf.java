package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

//https://aviationweather.gov/dataserver/fields?datatype=taf
//line breaks before TEMPO & FM

public class Taf {
	private String mUrl = Strings.TafUrl;
	private String mRadiusUrl = Strings.TafRadiusUrl;
	private String mStation;
	private String mTime;
	private String[] mFmPeriod = null;
	private String mNoData;
	public String FormattedTaf;
	private String mTafRawTextField = Strings.RawTextField;
	
	public Taf(String station) {
		mNoData = "No TAF data for station within 10 miles " + station;
		
		Document doc;
		try {
			mUrl = mUrl.replace("<STATION_ID>", station);
			doc = new GetWx().execute(mUrl).get();
			if(doc != null){
				//TODO cast these better
//				mStation = doc.getElementsByTagName("station_id").item(0).getTextContent();
//				mTime = doc.getElementsByTagName("issue_time").item(0).getTextContent();
				NodeList nodeList = doc.getElementsByTagName(mTafRawTextField);
				if(nodeList.getLength() == 0){
					//try to get a radius search
					//get a metar first
					Metar metar = new Metar(station);
					if(metar.Latitude != null){
						//url is long then lat
						mRadiusUrl = mRadiusUrl.replace("<LATITUDE>", metar.Latitude).replace("<LONGITUDE>", metar.Longitude).replace("<STATION>", station);
						Document radiusDoc = new GetWx().execute(mRadiusUrl).get();
						if(radiusDoc != null){
							NodeList radiusTafNodes = radiusDoc.getElementsByTagName(mTafRawTextField);
							if(radiusTafNodes.getLength() > 0){
								FormattedTaf = "Closest TAF within 20 miles: \n" + radiusTafNodes.item(0).getTextContent();
							} else {
								FormattedTaf = "No TAF data within 20 miles of " + station;
							}
						} else {
							FormattedTaf = mNoData;
						}
					}
				} else {
					FormattedTaf = formatRawText(doc.getElementsByTagName(mTafRawTextField).item(0).getTextContent());
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
