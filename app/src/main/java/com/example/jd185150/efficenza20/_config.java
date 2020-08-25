/* naimportovano do GitHubu 8/25/2020 */

package com.example.jd185150.efficenza20;

public class _config {

    private static String URL_hosting = "http://efficenza.cz/";
    private final String URL_NEXT_MATCH = URL_hosting + "gui/mobile/returnsezonu.php?sezona=";
    private final String URL_PREHLED_ZAPASU = URL_hosting + "gui/mobile/returnsezonu.php?sezona=";
    private final String URL_Auth = URL_hosting + "gui/mobile/auth.php";
    private final String URL_forum = URL_hosting + "gui/mobile/forum.php";
    private final String URL_forumInsertmsg = URL_hosting + "gui/mobile/forum_insertme.php";
    private final String URL_budunebudu = URL_hosting + "gui/mobile/budunebudu.php";
    private final String URL_isnewmsg = URL_hosting + "gui/mobile/isnewmessage.php";
    private final String URL_fetchsezona = URL_hosting + "gui/mobile/fetch_sezonalist.php";
    private static final String URL_getSestavu = URL_hosting + "gui/mobile/getsestavu.php";
    private static final String URL_zapaspreview = URL_hosting + "gui/mobile/zapaspreview.php";
    private static final String URL_gethriste = URL_hosting + "gui/mobile/get_hriste.php";
    private static final String URL_vzajemnyzapas = URL_hosting + "gui/mobile/vzajemny_zapas.php";

    private static final String apiHosting = "https://api.psmf.hrajfrisbee.cz/api/v1/";
    private final String URL_Lookupteam = apiHosting + "teams-by-name?name=efficenza%20ac&token&year=";
    private final String URL_Teamdetails = apiHosting + "team-details?token=&year=";
    private static final String apiHosting_teamsbyname = apiHosting + "teams-by-name?name=efficenza%20&token&year=";
    private static final String apiHosting_teamdetails = apiHosting  + "team-details?token=&year=";
    private static final String apiHosting_standings = apiHosting  + "table?token=&year=";

    public static String getURL_hosting() {
        return URL_hosting;
    }

    public String getURL_NEXT_MATCH() {
        return URL_NEXT_MATCH;
    }

    public String getURL_PREHLED_ZAPASU() {
        return URL_PREHLED_ZAPASU;
    }

    public String getURL_Auth() {
        return URL_Auth;
    }

    public String getURL_Lookupteam() {
        return URL_Lookupteam;
    }

    public String getURL_Teamdetails() {
        return URL_Teamdetails;
    }

    public String getURL_forum() {
        return URL_forum;
    }

    public String getURL_forumInsertmsg() {
        return URL_forumInsertmsg;
    }

    public String getURL_budunebudu() {
        return URL_budunebudu;
    }

    public String getURL_isnewmsg() {
        return URL_isnewmsg;
    }

    public String getURL_fetchsezona() {
        return URL_fetchsezona;
    }

    public String getApiHosting_teamsbyname() {
        return apiHosting_teamsbyname;
    }

    public static String getURL_getSestavu() {
        return URL_getSestavu;
    }

    public String getApiHosting_teamdetails() {
        return apiHosting_teamdetails;
    }

    public static String getURL_zapaspreview() {
        return URL_zapaspreview;
    }

    public static String getApiHosting() {
        return apiHosting;
    }

    public static String getURL_gethriste() {
        return URL_gethriste;
    }

    public static String getURL_vzajemnyzapas() {
        return URL_vzajemnyzapas;
    }

    public static String getApiHosting_standings() {
        return apiHosting_standings;
    }
}
