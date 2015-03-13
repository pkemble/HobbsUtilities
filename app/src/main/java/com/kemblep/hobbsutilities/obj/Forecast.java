package com.kemblep.hobbsutilities.obj;

import android.graphics.Bitmap;
import android.util.Log;

import com.kemblep.hobbsutilities.GetMap;
import com.kemblep.hobbsutilities.GetWx;
import com.kemblep.hobbsutilities.Strings;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Forecast {

    private String mForecastUrl = Strings.ForecastUrl;
    private String mMapUrl = Strings.GoogleMapUrl;
	public String Description;
	public String MoreInfoUrl;
	private String mNoData;
	private String mDescriptionTextField = Strings.DescriptionTextField;
	private String mTemperatureTextField = Strings.TemperatureTextfield;
    private String mDataTextField = Strings.DataTextField;
	private String mValues = WxReport.ValueTextField;
    private String mMoreWeatherInformation = Strings.MoreWeatherInformationTextField;
    public Bitmap MapForecastLocation;
    public TimeTempMap FCTimeTempMap = new TimeTempMap();

    public class TimeTempMap {
        public String LayoutKey;
        public String Name;
        public ArrayList<TimePeriod> TimePeriods = new ArrayList<>();
    }

    public class TimePeriod {
        public String PeriodName;
        public Date PeriodStart;
        public Date PeriodEnd;
        public int Temperature;
    }

    private static String TAG = Forecast.class.getName();

    public Forecast(String latitude, String longitude){
		mNoData = "No forecast :(";
		//TODO: serialize all the node names
        //TODO: handle a garbage or empty forecast
		Document doc;
		try {
            MapForecastLocation = new GetMap().execute(mMapUrl.replace("<LATITUDE>", latitude).replace("<LONGITUDE>", longitude)).get();
			mForecastUrl = mForecastUrl.replace("<LATITUDE>", latitude).replace("<LONGITUDE>", longitude);
			doc = new GetWx().execute(mForecastUrl).get();
			if(doc != null){
                Log.d(TAG, "Beginning forecast generation from Document");

                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();

                //get the more information link from the forecast data node
                XPathExpression exprMoreWeatherInformation = xPath.compile(Strings.PathMoreWeatherInformation);
                MoreInfoUrl = (String) exprMoreWeatherInformation.evaluate(doc, XPathConstants.STRING);

                //get the description
                XPathExpression exprDescription = xPath.compile(Strings.PathDescription);
                Description = (String) exprDescription.evaluate(doc, XPathConstants.STRING);

                //get the layout key for the minimums
                XPathExpression exprMinsLayoutKey = xPath.compile(Strings.PathMinsLayoutKey);
                FCTimeTempMap.LayoutKey = (String) exprMinsLayoutKey.evaluate(doc, XPathConstants.STRING);

                XPathExpression exprMinimumTemps = xPath.compile(Strings.PathMinimumTemps);
                NodeList nlMinTemps = (NodeList) exprMinimumTemps.evaluate(doc, XPathConstants.NODESET);

                for (int k = 0; k < nlMinTemps.getLength(); k++){
                    if(nlMinTemps.item(k).getNodeName().equalsIgnoreCase("value")){
                        TimePeriod timePeriod = new TimePeriod();
                        timePeriod.Temperature = Integer.parseInt(nlMinTemps.item(k).getTextContent());
                        FCTimeTempMap.TimePeriods.add(timePeriod);
                    } else if(nlMinTemps.item(k).getNodeName().equalsIgnoreCase("name")) {
                        FCTimeTempMap.Name = nlMinTemps.item(k).getTextContent();
                    }
                }

                XPathExpression exprTimePeriods = xPath.compile(Strings.PathTimePeriods.replace("<LAYOUT-KEY>", FCTimeTempMap.LayoutKey));
                NodeList nlTimePeriods = (NodeList) exprTimePeriods.evaluate(doc, XPathConstants.NODESET);
                int z = 0;
                for (int n = 0; n < nlTimePeriods.getLength(); n++){
                    String pn = getNodeAttributeValue(nlTimePeriods.item(n), "period-name");
                    if(pn != null) {
                        String pt = nlTimePeriods.item(n).getTextContent();
                        FCTimeTempMap.TimePeriods.get(z).PeriodName = pn;
                        z++;
                    }
                }
			}
            Log.d(TAG, "Finished parsing Document for " + this.Description);
		} catch (InterruptedException | ExecutionException | XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
    }

	private String getNodeAttributeValue(Node node, String nodeAttribute) {
			NamedNodeMap map = node.getAttributes();
			if(map == null || map.getNamedItem(nodeAttribute) == null) {
				return null;
			}
			return map.getNamedItem(nodeAttribute).getNodeValue();
	}
}
