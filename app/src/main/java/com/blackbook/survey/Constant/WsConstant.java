/*
 *
 * @author c61
 * All Web service Constants
 *
 */

package com.blackbook.survey.Constant;

public class WsConstant
{
    public static final String WS_ROOT = "http://s618129727.onlinehome.us/blackbooksurvey/api/";

    public static final String WS_All_DATA = WS_ROOT + "Webservice.php?Service=getPredefinedData";

    public static final String WS_USERBY_FBID = WS_ROOT + "Webservice.php?Service=CheckExistingUser";

    public static final String WS_SAVE_PROFILE = WS_ROOT + "Webservice.php?Service=setProfile";

    public static final String WS_SURVEY_STATISTICS_FOR_USER = WS_ROOT + "Webservice.php?Service=setSurveyStatisticsForUser";

    public static final String WS_SURVEY_STATISTICS = WS_ROOT + "Webservice.php?Service=setSurveyStatistics";

    /**********
     * WS Parameters Request Types
     ************/

    public static final String Req_alldata = "Request_alldata";

    public static final String Req_Userby_Fbid = "Request_userbyfbid";

    public static final String Req_Userby_Gid = "Request_userbygid";

    public static final String Req_Userby_Tid = "Request_userbytid";

    public static final String Req_Save_Profile = "Request_saveprofile";

    public static final String Req_SURVEYSTATISTICSFORUSER = "Request_setSurveyStatisticsForUser";

    public static final String Req_SURVEYSTATISTICS = "Request_setSurveyStatistics";

    /********** WS Parameters ************/


}
