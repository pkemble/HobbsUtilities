package com.kemblep.hobbsutilities;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

public class GetWx extends AsyncTask<String, Void, Document> {

	private final String TAG = GetWx.class.getName();
	
	//TODO add station / type / etc / params
	@Override
	protected Document doInBackground(String... urlStrings) {
		/*
		 * https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&stationString=KBED&hoursBeforeNow=1
		 * 
		 * metar:
		 * https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=KBED&hoursBeforeNow=1
		 */

		Document doc = null;
		//TODO station is a string list. Add in multiple station support
		URL url;
		
		HttpURLConnection huc;
		try {
			url = new URL(urlStrings[0]);
			//Log.d(TAG, "Opening connection to " + url);
			huc = (HttpURLConnection) url.openConnection();
			Log.d(TAG, "Getting input stream from " + url);
			InputStream is = huc.getInputStream();
			Log.d(TAG, "Input stream received from " + url);
			doc = parseXML(is);
			//Log.d(TAG, "Done parsing stream from" + url);
			huc.disconnect();
			//Log.d(TAG, "Disconnected from " + url);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	private Document parseXML(InputStream inputStream) throws Exception
	    {
	        DocumentBuilderFactory objDocumentBuilderFactory = null;
	        DocumentBuilder objDocumentBuilder = null;
	        Document doc = null;
	        try
	        {
	            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
	            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

	            doc = objDocumentBuilder.parse(inputStream);
	        }
	        catch(Exception ex)
	        {
	            throw ex;
	        }       

	        return doc;
	}
}

