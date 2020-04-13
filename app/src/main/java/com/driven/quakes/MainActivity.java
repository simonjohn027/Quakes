package com.driven.quakes;

/**
 *
 * Author Simon John Sanga
 * S1803451
 */
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private PositionService position;
    private boolean bound = false;
    public String locationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DataParser().execute();
    }



    private class DataParser extends AsyncTask<String[], Void, ArrayList<Quake>> {
        private Quake seismic = new Quake();

        private ArrayList<Quake> quakeArrayList;
        private ProgressDialog progressDialog;

        private final static String url = "https://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";


        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Earthquakes Data");
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected ArrayList<Quake> doInBackground(String[]... strings) {

            try {
                URL url = new URL(DataParser.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(20000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream stream = connection.getInputStream();


                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlFactoryObject.newPullParser();

                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(stream, null);

                ArrayList<Quake> quakeArrayList = parseXML(xmlPullParser);
                stream.close();

                return quakeArrayList;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Loading Data:", "Failed to load data on first Try");
                return null;
            }
        }

        public ArrayList<Quake> parseXML(XmlPullParser xmlPullParser) {
            int currentEvent;
            String text = null;
            try {
                currentEvent = xmlPullParser.getEventType();
                while (currentEvent != XmlPullParser.END_DOCUMENT) {
                    String elemName = xmlPullParser.getName();
                    if (currentEvent == XmlPullParser.START_TAG) {
                        switch (elemName.toLowerCase()) {
                            case "channel":
                                quakeArrayList = new ArrayList<Quake>();
                                break;
                            case "item":
                                seismic = new Quake();
                                break;
                            case "title":
                                seismic.setName(xmlPullParser.nextText());
                                break;
                            case "description":
                                String description = xmlPullParser.nextText();
                                seismic.setDescription(description);
                                String dth = " Depth: (\\d+) km ;";
                                String lcn = "Location: (.*?);";
                                String mgt = " Magnitude: (.*)";
                                Matcher dpth = Pattern.compile(dth).matcher(description);
                                Matcher ltn = Pattern.compile(lcn).matcher(description);
                                Matcher mgtd = Pattern.compile(mgt).matcher(description);
                                if (dpth.find() && ltn.find() && mgtd.find()) {
                                    String depth = dpth.group(1);
                                    String location = ltn.group(1);
                                    String magnitude = mgtd.group(1);
                                    seismic.setDepth(depth);
                                    seismic.setLocation(location);
                                    seismic.setMagnitude(new Float(magnitude));
                                }

                                break;
                            case "link":
                                seismic.setSource(xmlPullParser.nextText());
                                break;
                            case "pubdate":
                                seismic.setDate(xmlPullParser.nextText());
                                break;
                            case "geo:lat":
                                seismic.setLat(xmlPullParser.nextText());
                                break;
                            case "geo:long":
                                seismic.setLon(xmlPullParser.nextText());
                                break;

                        }

                    } else if (currentEvent == XmlPullParser.END_TAG) {
                        switch (elemName.toLowerCase()) {
                            case "item":
                                quakeArrayList.add(seismic);
                                break;
                            case "channel":
                                Log.e("Parsing", "channel size is " + quakeArrayList.size());
                                break;

                        }
                    }
                    currentEvent = xmlPullParser.next();
                }
            } catch (XmlPullParserException | IOException ae1) {
                Log.e("Parsing", "Parsing error" + ae1.toString());
            }

            return quakeArrayList;
        }

        protected void onPostExecute(ArrayList<Quake> result) {
            ArrayList<Quake> quakes = new ArrayList<Quake>(result);
            if (quakes.size() != 0) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, QuakeListActivity.class);
                intent.putParcelableArrayListExtra("quakesbundle", quakes);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            } else {

                TextView failedInfo = (TextView) findViewById(R.id.info_failed);
                failedInfo.setText("Connecting to Server");
                Toast toast = Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT);
                toast.show();

            }
            progressDialog.dismiss();
        }

    }



}
