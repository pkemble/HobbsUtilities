package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

public class StationInfo {
	private String mUrl = Strings.StationInfoUrl;
	public String Latitude;
	public String Longitude;
	//response data station site
	public String SiteName;
	public String SiteId;
	private String mNoData;
	private String mSiteNameField = Strings.SiteNameField;;
	private String mLatitudeTextField = Strings.LatitudeTextField;
	private String mLongitudeTextField = Strings.LongitudeTextField;

	public StationInfo(String stationId){
		
		mNoData = "No data for station " + stationId;
		SiteId = stationId.toUpperCase();
		Document doc;
		try {
			mUrl = mUrl.replace("<STATION_ID>", stationId);
			doc = new GetWx().execute(mUrl).get();
			if(doc != null){
				NodeList nodeList = doc.getElementsByTagName(mSiteNameField);
				if(nodeList.getLength() > 0){
					SiteName = doc.getElementsByTagName(mSiteNameField).item(0).getTextContent();
					Latitude = doc.getElementsByTagName(mLatitudeTextField).item(0).getTextContent();
					Longitude = doc.getElementsByTagName(mLongitudeTextField).item(0).getTextContent();
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
