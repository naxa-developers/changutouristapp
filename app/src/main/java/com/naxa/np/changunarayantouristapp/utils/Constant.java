package com.naxa.np.changunarayantouristapp.utils;


import android.Manifest;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final String KEY_VALUE = "value";
    public static final String KEY_OBJECT = "object";
    public static final String KEY_EXTRA_OBJECT = "extra_object";

    public Constant() {
    }

    public class SharedPrefKey {
        public static final String IS_APP_FIRST_TIME_LAUNCH = "is_app_first_time_launch";
        public static final String IS_MAYOR_MESSAGE_FIRST_TIME = "is_mayor_message_first_time";
        public static final String KEY_LANGUAGE_LIST_DETAILS = "language_list_details";
        public static final String KEY_SELECTED_APP_LANGUAGE = "selected_app_language";
        public static final String IS_PLACES_DATA_ALREADY_EXISTS = "is_places_data_already_exists";
        public static final String IS_USER_ALREADY_LOGGED_IN = "is_user_already_logged_in";
        public static final String KEY_USER_LOGGED_IN_RESPONSE = "user_log_in_response";
        public static final String KEY_MAIN_PLACES_list_DETAILS = "main_places_list_details";
        public static final String KEY_MAYOR_MESSAGE_DETAILS = "mayor_message_details";
        public static final String KEY_TOURIST_INFORMATION_GUIDE_DETAILS = "tourist_information_guide_details";

    }


    public class Network {
        public static final String API_KEY = "!@10293848576qwerprakadhkhdakatyuiozbsdfgprakashkhdkasdfgsdfhgpasdfghjklmnbvcxzkhadkapoiuytrewq!0_p_123456r_@a_@k_@a_@s_@h_aquickbrownfoxjumpoverthelazydog";
        public static final int KEY_MAX_RETRY_COUNT = 5;
    }

    public class PermissionID {
        public static final int RC_STORAGE = 1994;
        public static final int RC_CAMERA= 1995;
        public static final int RC_LOCATION= 1996;
    }

    public class Permission {
        public static final String CAMERA = Manifest.permission.CAMERA;
        public static final String STORAGE_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static final String STORAGE_READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        public static final String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String LOCATION_COURSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    }

    public class MapKey {
        public static final int GEOPOINT_RESULT_CODE = 1994;
        public static final String MAP_BASE_LAYER = "base_layer";
        public static final String MAP_OVERLAY_LAYER = "overlay_layer";
        public static final String MAP_BOARDER_OVERLAY_LAYER_ID = "boarder_overlay_layer";
        public static final int KEY_STREET = 0 ;
        public static final int KEY_SATELLITE = 1;
        public static final int KEY_OPENSTREET = 2;
        public static final int KEY_CHANGUNARAYAN_BOARDER = 3;
        public static final int GPS_REQUEST = 101;
        public static final int KEY_NAGARKOT_BOARDER = 4;
        public static final String KEY_MAIN_PLACE_TYPE = "main_place_type";
    }

    public List<String> getNearByPlacesTypeList (){
        List<String> nearByPlaces = new ArrayList<>();
        nearByPlaces.add("museum");
        nearByPlaces.add("statue");
        nearByPlaces.add("temple");
        nearByPlaces.add("Hindu temple");
        nearByPlaces.add("Viewpoint");
        nearByPlaces.add("Waterfall");
        nearByPlaces.add("View point");
        nearByPlaces.add("Tap");
        nearByPlaces.add("Pond");
        nearByPlaces.add("Dam");
        nearByPlaces.add("Stone tap");
        nearByPlaces.add("Peace garden");
        nearByPlaces.add("Tank");
        nearByPlaces.add("Picnic spot");
        return nearByPlaces;
    }


    public List<String> getDefaultPlacesTypeListToPlotMarker (){
        List<String> nearByPlaces = new ArrayList<>();
        nearByPlaces.add("museum");
        nearByPlaces.add("statue");
        nearByPlaces.add("sattal");
        nearByPlaces.add("temple");
        nearByPlaces.add("view_point");
        nearByPlaces.add("view_tower");
        nearByPlaces.add("waterfall");
        nearByPlaces.add("cave");

        return nearByPlaces;
    }


    public static String getAboutUsDemoContent(){
        String demoContent = "<p><strong>चाङ्गुनारायण पर्यटन एप</strong> चाङ्गुनारायण नगरपालिकाका मेयरको पहलमा सुरु भएको योजना हो र यो प्रविधिको प्रयोगबाट <strong>चाङ्गुनारायण नगरपालिका</strong> भ्रमण गर्न आउने आन्तरिक तथा बाह्य पयर्टकहरुका लागि सहज पथ प्रदर्शन प्रदान गर्न सकिनेछ । साथै, यो एपको मदतबाट चाङ्गुनारायणको गौरवमय इतिहास, सांस्कृतिक विविधता तथा अन्य पुरातात्विक महत्वका कुराहरुको जानकारी पनि लिन सकिनेछ । यि विशेषताहरु समावेश भएको यो एप यस क्षेत्रको पहिलो हुनेछ ।&nbsp;</p>\n" +
                "<p>अहिलको अवस्थामा यो एप नेपाली, अङ्ग्रेजी, र चिनिंया गरी तिन भाषामा उपलब्ध छ । यस एपका प्रयोगकर्ताहरुले चाङ्गुनारायण तथा आसपासको क्षेत्रको अडियो विवरण , प्राकृतिक सौन्दर्यका सुन्दर तस्बिरहरु साथै प्रयोगकर्ताको स्थानअनुसार नजिकका भ्रमणयोग्य ठाउँहरुको बारेमा पनि जानकारी लिन सक्नेछन् ।&nbsp;</p>\n" +
                "<p>चाङ्गुनारायण विश्व सांस्कृतिक सम्पदाको भ्रमण गर्दै गर्दा एपका प्रयोगकर्ताहरुले आफ्नो स्मार्टफोनबाट मुख्य स्थानहरुमा राखिएका <strong>QR Code</strong> स्क्यान गर्न सक्नेछन्, जसबाट आफ्नो नजिकाका प्रख्यात स्थलहरुको जानकारी लिन सकिनेछ । यो एप <a href=\"http://naxa.com.np/home/\">&lsquo;नक्सा&rsquo; </a>नामक नक्सांकनमा आधारीत सेवा प्रदायक सस्ंथाको प्राविधिक सहयोगबाट बनाईएको हो ।</p>\n" +
                "<p><br />यस एपको प्रयोगबाट चाङ्गुनारायण नगरपालिका भ्रमण गर्ने सम्पुर्ण पर्यटकहरुलाई सरल पथ प्रदर्शनमा मदत गर्नेछ र <strong>चाङ्गुनारायण</strong> तथा <strong>नगरकोट</strong> को पर्यटन क्षेत्रको विकास र प्रवर्दन गर्न मदत गर्नेछ ।</p>";

        return demoContent;
    }
    public static String getAboutUsDemoContentEnglish(){
        String aboutUsContent = "<p><strong>Changunarayan Tourist App</strong> is the vision of the Mayor of Changunarayan Municipality to provide accurate firsthand information to both the internal and international tourists visiting Changunarayan Municipality. This app is the first of its kind in the area to include information on historical sites and major tourists attractions.</p>\n" +
                "<p>This app acts as a <strong>virtual tourist guide</strong> to facilitate the tourist coming to <strong>Changunarayan Municipality</strong> to help them easily navigate around places, get firsthand information and understand more details of the most visited tourist destinations. Also, it provides salient history and culture of such locations along with other interesting features. Currently the app is available in three different languages: English, Nepali and Chinese. Users of this app will also be able to listen to audio based description of these sites and view scenic beauty in different seasons, day/night photographs, some popular attractions and sceneries depending on where they are.</p>\n" +
                "<p>The app users can either scan the <strong>QR code</strong> installed at each major location/temple/statue or look for nearby most visited places in the app when users are navigating around the Changunarayan World Heritage Site and Nagarkot area.The app has been developed in technical partnership with a location based service provider company, <a href=\"https://codebeautify.org/%22http://naxa.com.np/home/about-us//%22\">NAXA</a> Private Limited based in kathmandu.This app enables the visitors to explore <strong>Changunarayan</strong> and <strong>Nagarkot </strong>areas and hence, can contribute in the promotion and development of tourism in these areas. </p>";
        return aboutUsContent;
    }


}
