package com.kemblep.hobbsutilities;

/**
 * Created by Pete on 3/11/2015.
 */
public class Strings {
//TODO rename this class
    public static String MetarUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=<STATION_ID>&hoursBeforeNow=1";
    public static String TafUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&stationString=<STATION_ID>&hoursBeforeNow=1";
    public static String TafRadiusUrl = "https://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=tafs&requestType=retrieve&format=xml&radialDistance=20;<LONGITUDE>,<LATITUDE>&hoursBeforeNow=1";
    public static String StationInfoUrl = "http://www.aviationweather.gov/adds/dataserver_current/httpparam?dataSource=stations&requestType=retrieve&format=xml&stationString=<STATION_ID>";
    public static String ForecastUrl = "http://forecast.weather.gov/MapClick.php?lat=<LATITUDE>&lon=<LONGITUDE>&site=okx&unit=0&lg=en&FcstType=dwml";
    public static String GoogleMapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=<LATITUDE>,<LONGITUDE>&zoom=12&size=200x200&markers=size:mid%7Ccolor:red%7C<LATITUDE>,<LONGITUDE>";
    //public static String ForecastUrl = "http://graphical.weather.gov/xml/SOAP_server/ndfdXMLclient.php?whichClient=NDFDgen&lat=<LATITUDE>&lon=<LONGITUDE>&product=glance&Unit=e&Submit=Submit";
    public static String RawTextField = "raw_text";
    public static String LatitudeTextField = "latitude";
    public static String LongitudeTextField = "longitude";
    public static String SiteNameField = "site";
    public static String DescriptionTextField = "area-description";
    public static String TemperatureTextfield = "temperature";
    public static String DataTextField = "data";
    public static String MoreWeatherInformationTextField = "moreWeatherInformation";
    public static String PathMoreWeatherInformation = "/dwml/data[1]/moreWeatherInformation/text()";
    public static String PathDescription = "/dwml/data/location/area-description/text()";
    public static String PathMinsLayoutKey = "/dwml/data/parameters/temperature[@type='minimum']/@time-layout";
    public static String PathMinimumTemps = "/dwml/data[1]/parameters/temperature[@type='minimum']/*";
    public static String PathTimePeriods = "/dwml/data[1]/time-layout/layout-key[text()='<LAYOUT-KEY>']/../*";
    public static String PathTimePeriodNames = "/dwml/data[1]/time-layout/start-valid-time[@period-name]";
}
