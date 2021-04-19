package vn.com.gojobs;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.com.gojobs.Model.Directions;

public class DirectionJSONParse {

    private String mRequest;

    public DirectionJSONParse(String mRequest) {
        this.mRequest = mRequest;
    }

    public ArrayList<LatLng> testDirection(){
        ArrayList<LatLng> res = new ArrayList<>();

        try {
            URL url;
            url = new URL(mRequest);

            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");

            Directions results = new Gson().fromJson(reader, Directions.class);
            Directions.Route[] routes = results.getRoutes();
            Directions.Leg[] legs = routes[0].getLegs();
            Directions.Leg.Step[] steps = legs[0].getSteps();

            for (Directions.Leg.Step step : steps){
                LatLng latLngStart = new LatLng(step.getStart_location().getLat(),step.getStart_location().getLng());
                LatLng latLngEnd = new LatLng(step.getEnd_location().getLat(),step.getEnd_location().getLng());

                res.add(latLngStart);
                res.add(latLngEnd);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

//    public List<List<HashMap<String,String>>> parse(JSONObject jObject){
//
//        List<List<HashMap<String, String>>> routes = new ArrayList<>() ;
//        JSONArray jRoutes = null;
//        JSONArray jLegs = null;
//        JSONArray jSteps = null;
//
//        try {
//
//            jRoutes = jObject.getJSONArray("routes");
//
//            /** Traversing all routes */
//            for(int i=0;i<jRoutes.length();i++){
//                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
//                List path = new ArrayList<HashMap<String, String>>();
//
//                /** Traversing all legs */
//                for(int j=0;j<jLegs.length();j++){
//                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
//
//                    /** Traversing all steps */
//                    for(int k=0;k<jSteps.length();k++){
//                        String polyline = "";
//                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
//                        List<LatLng> list = decodePoly(polyline);
//
//                        /** Traversing all points */
//                        for(int l=0;l<list.size();l++){
//                            HashMap<String, String> hm = new HashMap<String, String>();
//                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
//                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
//                            path.add(hm);
//                        }
//                    }
//                    routes.add(path);
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//        }
//        return routes;
//    }
//
//    private List<LatLng> decodePoly(String encoded) {
//
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b &amp; 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result &amp; 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b &amp; 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result &amp; 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//        return poly;
//    }
}
