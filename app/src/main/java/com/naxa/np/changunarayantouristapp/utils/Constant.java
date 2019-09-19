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
        nearByPlaces.add("Museum");
        nearByPlaces.add("Staue");
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
        nearByPlaces.add("Museum");
        nearByPlaces.add("Staue");
        nearByPlaces.add("Statue");
        nearByPlaces.add("Hindu temple");
        nearByPlaces.add("Viewpoint");
        nearByPlaces.add("View point");
        nearByPlaces.add("Cave");

        return nearByPlaces;
    }

    public static String getAboutUsDemoContent(){
        String demoContent = "<h1>संक्षिप्त परिचय</h1>\n" +
                "<div>\n" +
                "<p>ऐतिहासिक एवं पुरातात्विक महत्वको दृष्टीकोणले परिचित भक्तपुर जिल्ला भित्रका ४ वटा नगरपालिका मध्ये चाँगुनारायण नगरपालिका नेपाल सरकारले मिति २०७३ ११ २७ गतेको स्थानिय तह घोषणाबाट साविक चाँगुनारायण नगरपालिका र नगरकोट नगरपालिकालाई समेटेर गठन भएको हो । मिति २०७१ ८ १६ को मन्त्रीपरिषदको निर्णय अनुसार साविक चाँगुनारायण, झौखेल, छालिड, दुवाकोट गा.वि.स समेटेर चाँगुनारायण नगरपालिका तथा साविक नगरकोट बागेश्वरी, सुडाल र ताथली गा.वि.स समेटेर महामन्जुश्री नगरकोट नगरपालिका घोषणा भएको थियो । पछि महामन्जुश्री नगरकोट नगरपालिका मिलाएर नाम संशोधन भै नगरकोट नगरपालिका कायम भयो । चाँगुनारायण नगरपालिका र नगरकोट नगरपालिका मिलाएर चाँगुनारायण नगरपालिका भए पछि ९ वटा वडा कायम भई नगरपालिकाको केन्द्र खरिपाटी, भक्तपुरमा रहेको छ ।</p>\n" +
                "<p><strong>क्षेत्रफल : </strong>६२.९८ बर्ग कि.मि</p>\n" +
                "<p><strong>जनसंख्या : </strong>५५,४३० (२०११)</p>\n" +
                "<section>\n" +
                "<div>\n" +
                "<h2>संपर्क</h2>\n" +
                "<div>\n" +
                "<p>चाँगुनारायण नगरपालिका, खरिपाटी,भक्तपुर</p>\n" +
                "<p>०१-६६१४८०९ (प्रशासन शाखा)</p>\n" +
                "<p>१६६००११४६६६ (नगर स्तरीय आपतकालीन कार्य संचालन केन्द्र)</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</section>\n" +
                "</div>";

        return demoContent;
    }

    public static String getAboutUsDemoContentEnglish(){
        String aboutUsContent = "<p style=\"text-align: justify;\"><strong>Changunarayan app</strong> is a vision of the Mayor of Changunarayan Municipality to provide accurate first hand information to any tourists coming to visit changunarayan municipality in different forms.</p>\n" +
                "<p style=\"text-align: justify;\">This app acts as a <strong>virtual tourist guide</strong> to facilitate the tourist coming to <strong><em>Changunarayan </em></strong><strong><em>Municipality</em> </strong>to help them easily navigate around places, get firsthand information andunderstand more details of the most visited tourist destinations. Also, it provides salient history, culture of such locations along with other interesting features. Currently the app is available in three different langugages : <em>English</em> , <em>Nepali</em> and <em>Chinese</em>. Users would also be able to listen to audio based description of these sites and also view multiple seasons , day/night photographs some popular locations and scenaries depending on where they are.</p>\n" +
                "<p style=\"text-align: justify;\"><br />The app user can either scan the <strong>QR code</strong> pasted at each major locations/temples/statues or look for nearby most visit places when he/she is navigating around changunarayan world heritage sites and Nagarkot area.The app has been developed in technical partnership with a location based service provider company <em><a href=\"http://naxa.com.np/home/about-us/\">NAXA</a></em> based in kathmandu.This app can facilitate the visitors coming to visit <strong>Changunarayan</strong> and <strong>Nagarkot </strong>areas and also become a boon for the promotion and development of tourism in the area.</p>";
        return aboutUsContent;
    }


}
