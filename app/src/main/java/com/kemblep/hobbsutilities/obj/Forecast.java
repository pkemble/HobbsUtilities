package com.kemblep.hobbsutilities.obj;

import com.kemblep.hobbsutilities.GetWx;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Forecast {
	private String _url = WxReport.ForecastUrl;
	public String Description;
	public String moreInfoUrl;
	private String _noData;
	private String _descriptionTextField = WxReport.DescriptionTextField;
	private String _temperatureTextField = WxReport.TemperatureTextfield;
	private String _values = WxReport.ValueTextField;
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
        public Float Temperature;
    }

    public Forecast(String latitude, String longitude){
		_noData = "No forecast :(";
		//TODO serialize all the node names
		Document doc;
		try {
			_url = _url.replace("<LATITUDE>", latitude).replace("<LONGITUDE>", longitude);
			doc = new GetWx().execute(_url).get();
			if(doc != null){
				//String layoutKey = new String();
				//ArrayList<String> minimums = new ArrayList<String>();
				//ArrayList<String> timePeriods = new ArrayList<String>();
				NodeList data = doc.getElementsByTagName("data");
				for (int i = 0; i < data.getLength(); i++){
					if(getNodeAttributeValue(data.item(i), "type").equalsIgnoreCase("forecast")){
						//get a list of periods
						NodeList timeLayouts = data.item(i).getChildNodes();
						for (int j = 0; j < timeLayouts.getLength(); j++){
							//populate more info
							if(timeLayouts.item(j).getNodeName().equalsIgnoreCase("moreWeatherInformation")){
								moreInfoUrl = timeLayouts.item(j).getTextContent();
							}
							
//							<parameters applicable-location="point1">
//								<temperature type="minimum" units="Fahrenheit" time-layout="k-p24h-n7-1">
//									<name>Daily Minimum Temperature</name>
//									<value>10</value>
//									<value>9</value>...
							
							if(timeLayouts.item(j).getNodeName().equalsIgnoreCase("parameters")){
								NodeList tempTypeNodes = timeLayouts.item(j).getChildNodes();
								for(int q=0; q < tempTypeNodes.getLength(); q++){
									String tt = getNodeAttributeValue(tempTypeNodes.item(q), "type");
									if(tt != null && tt.equalsIgnoreCase("minimum")){		
										//grab the text from the layout-key node
										FCTimeTempMap.LayoutKey = getNodeAttributeValue(tempTypeNodes.item(q), "time-layout");
										//get the minimums
										NodeList valueMins = tempTypeNodes.item(q).getChildNodes();
										for (int k = 0; k < valueMins.getLength(); k++){
											if(valueMins.item(k).getNodeName().equalsIgnoreCase("value")){
                                                TimePeriod timePeriod = new TimePeriod();
											    timePeriod.Temperature = Float.parseFloat(valueMins.item(k).getTextContent());
                                                FCTimeTempMap.TimePeriods.add(timePeriod);
											} else if(valueMins.item(k).getNodeName().equalsIgnoreCase("name")) {
                                                FCTimeTempMap.Name = valueMins.item(k).getTextContent();
                                            }
										}
									}
									
								}
								
							}
							//look through each parameters node and find the daily minimums one
						}
						//match the time-layout node with the layout key
						for (int l = 0; l < timeLayouts.getLength(); l++){
							//get the time period for the minimums
							NodeList periodList = timeLayouts.item(l).getChildNodes();
                            int z = 0;
							for (int m = 0; m < periodList.getLength(); m++){
								if(periodList.item(m).getNodeName().equalsIgnoreCase("layout-key") && periodList.item(m).getTextContent().equalsIgnoreCase(FCTimeTempMap.LayoutKey)){
									for (int n = 0; n < periodList.getLength(); n++){
                                        String pn = getNodeAttributeValue(periodList.item(n), "period-name");
                                        if(pn != null) {
                                            String pt = periodList.item(n).getTextContent();
                                            FCTimeTempMap.TimePeriods.get(z).PeriodName = pn;
                                            //FCTimeTempMap.TimePeriods.get(z).PeriodStart = extractStartDate(pt);
                                            //FCTimeTempMap.TimePeriods.get(z).PeriodEnd = extractEndDate(pt);
                                            z++;
                                        }
									}
								}
							}
						}
						
						//create a hash map of time periods and temps
//						if(minimums.size() == timePeriods.size()){
//							TimeTempMap = new HashMap<String, String>();
//							for(int p = 0; p < minimums.size(); p++){
//								TimeTempMap.put(timePeriods.get(p), minimums.get(p));
//							}
//						}
					}
				}
				
				if(data.getLength() > 0){
					Node descNode = doc.getElementsByTagName(_descriptionTextField).item(0);
					if(descNode != null) Description = descNode.getTextContent();
				}
//				NodeList values = doc.getElementsByTagName(_values);
//				if(values.getLength() > 0){
//					NextLow = values.item(0).getTextContent();
//				}
//				if(FCTimeTempMap != null && !FCTimeTempMap.TimePeriods.isEmpty()){
//                    for (int i = 0; i < FCTimeTempMap.TimePeriods.size(); i++){
//                        if(FCTimeTempMap.TimePeriods.get(i).PeriodName.equalsIgnoreCase("Tonight")){
//                            NextLow = FCTimeTempMap.TimePeriods.get(i).Temperature;
//                        }
//                    }
//
//				}
			}
		} 
		catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

    private Date extractStartDate(String periodText) {
        //2015-03-10T06:00:00-05:00
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSSS....");
        try {
            return sdf.parse(periodText);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Node getNodeByAttributeValue(NodeList nodeList, String nodeAttribute, String nodeAttributeValue){
		for (int i = 0; i < nodeList.getLength(); i++){
			if(nodeList.item(i).hasAttributes() && getNodeAttributeValue(nodeList.item(i), nodeAttribute).equalsIgnoreCase(nodeAttributeValue)){
				nodeList.item(i).setNodeValue("frist");
				return nodeList.item(i);
			}
		}
		return null;
		
	}

	private String getNodeAttributeValue(Node node, String nodeAttribute) {
			NamedNodeMap map = node.getAttributes();
			if(map == null || map.getNamedItem(nodeAttribute) == null) {
				return null;
			}
			return map.getNamedItem(nodeAttribute).getNodeValue();
	}
}
