package com.kemblep.hobbsutilities.obj;

import java.util.concurrent.ExecutionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.kemblep.hobbsutilities.GetWx;

public class StationInfo {
	private String _url = WxReport.StationInfoUrl;
	public String Latitude;
	public String Longitude;
	//response data station site
	public String SiteName;
	public String SiteId;
	private String _noData;
	private String _siteNameField = WxReport.SiteNameField;;
	private String _latitudeTextField = WxReport.LatitudeTextField;
	private String _longitudeTextField = WxReport.LongitudeTextField;
	
	public StationInfo(String stationId){
		
		_noData = "No data for station " + stationId;
		SiteId = stationId.toUpperCase();
		Document doc;
		try {
			_url = _url.replace("<STATION_ID>", stationId);
			doc = new GetWx().execute(_url).get();
			if(doc != null){
				NodeList nodeList = doc.getElementsByTagName(_siteNameField);
				if(nodeList.getLength() > 0){
					SiteName = doc.getElementsByTagName(_siteNameField).item(0).getTextContent();
					Latitude = doc.getElementsByTagName(_latitudeTextField).item(0).getTextContent();
					Longitude = doc.getElementsByTagName(_longitudeTextField).item(0).getTextContent();
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
