package autroid.business.utils;

/**
 * Created by pranav.mittal on 06/01/17.
 */

public class Constant {

    public static final String KEY_LOCATION ="location" ;
    public static String bookingCategory[]={"diagnosis","insurance","collision","services","washing","package","product","customization"};

    /* KEYS */

    public static String YES="Yes";

    public static String NO="No";

    public static String KEY_AUTOMAKER="automaker";

    public static String keyUrl="url";
    public static String KEY_MAKER_ID="makerid";
    public static String KEY_MAKER_NAME="makername";

    public static String KEY_MODEL_ID="modelid";
    public static String KEY_MODEL_NAME="modelname";

    public static String KEY_VARIANT_ID="variantid";
    public static String KEY_VARIANT_NAME="variantname";

    public static String KEY_CATEGORY_ID="categoryid";
    public static String KEY_CATEGORY_NAME="categoryname";

    public static String KEY_SUB_CATEGORY_ID="subcategoryid";
    public static String KEY_SUB_CATEGORY_NAME="subcategoryname";

    public static String KEY_CAR_ID="car_id";

    public static String KEY_PACKAGE_ID="package_id";
    public static String KEY_PACKAGE_NAME="package_name";

    public static String KEY_VENDOR_ID="vendor_id";
    public static String KEY_MY_BOOKING="my_booking";

    public static String Key_Name="name";




    public static String Key_lat="lat";
    public static String Key_lng="lng";


    public static String KEY_ID="id";
    public static String KEY_ID_TAB="id_tab";
    public static String KEY_NAME_TAB="id_tab";
    public static String KEY_CHARGES="charges";
    public static String KEY_FORGOT="forgot";
    public static String KEY_TYPE="type";
    public static String KEY_IMAGES="images";
    public static String FROM_CAR_SALES="from_car_sales";
    public static String EDIT_CAR="EDITCAR";
    public static String PUBLISH_CAR="PUBLISHCAR";
    public static String KEY_IS_VIEW="isview";


    public static String KEY_CAR_TYPE="car_type";
    public static String[] CAR_TYPES={"GARAGE","BOOKING"};


    public static String KEY_OFFER_ARRAY="offer_array";
    public static String KEY_IS_OFFER_EDITABLE="offer_editable";

    public static String KEY_FCM_TOKEN="fcm_token";
    public static String Key_Mobile="mobile";
    public static String Key_Email="email";
    public static String Key_Source="source";

    public static String Key_Business_Name="business_name";
    public static String Key_Business_address="business_address";
    public static String Key_Business_overview="business_overview";
    public static String Key_Business_cover="business_cover";

    public static String Is_Category="Key_Is_Category";

    public static String KEY_EVENT_ID="event_id";
    public static String LINK="link";


    /* VALUES*/

    public static String VALUE_CAR="car";
    public static String VALUE_PRODUCT="product";
    public static String VALUE_SHOWROOM="showroom";
    public static String VALUE_ASSETS="assets";


    /*Broadcast actions*/

    public static final String BROADCAST_VARIANT = "com.broadcast.variant";
    public static final String BROADCAST_ADD_CAR = "com.broadcast.addcar";
    public static final String BROADCAST_ADD_OFFER = "com.broadcast.addoffer";
    public static final String BROADCAST_ADD_IMAGES = "com.broadcast.images";
    public static final String BROADCAST_REFRESH_TOKEN = "com.broadcast.refreshToken";
    public static final String BROADCAST_UPDATE_COVER = "com.broadcast.updateCover";

    /*Shared Preference*/
    public static final String SP_USERID="userid";
    public static final String SP_TOKEN="token";
    public static final String SP_BUSINESS="business";
    public static final String SP_MANIFEST="manifest";
    public static final String SP_FCM_TOKEN="fcmToken";
    public static final String SP_CAR_ITEMS="caritems";
    public static final String SP_NOTIFICATION_COUNT="notification_count";
    public static final String SP_ROLE="cre";



    /*Events Response Code*/

    public static final int EVENT_UPDATE_PROFILE=1;
    public static final int EVENT_UPDATE_COVER=2;
    public static final int EVENT_UPDATE_TIMING=3;
    public static final int EVENT_UPDATE_GALLERY=4;


    public static final int EVENT_REFRESH_OFFER=5;
    public static final int EVENT_SELECT_CAR=6;
    public static final double EVENT_SELECT_CAR1=6.1;
    public static final int EVENT_POST_CAR=7;
    public static final int EVENT_UPDATE_ADDRESS=8;

    public static final int EVENT_COVER_IMAGE=9;
    public static final int EVENT_SELECT_PRODUCT=10;
    public static final int EVENT_SELECT_PRODUCT1=11;

    public static final int EVENT_CHANGE_ACCOUNT=12;
    public static final int EVENT_NOTIFICATION=13;
    public static final int EVENT_NOTIFICATION_COUNT=14;
    public static final int EVENT_RESCHEDULE_BOOKING=15;


    public static int EVENT_GET_IMAGE=16;
    public static int EVENT_SEND_CAR=17;
    public static int EVENT_UPDATE=18;
    public static int EVENT_COLLISION=19;
    public static int EVENT_APPROVAL=20;
    public static int EVENT_SEND_MANUAL_CAR=21;
    public static int EVENT_POST_MANUAL_CAR=22;
    public static int EVENT_USER_DETAILS_UPDATE=23;
    public static int EVENT_BOOKING_ADDRESS=24;
    public static int EVENT_REFRESH_LEAD=25;
    public static int EVENT_REFRESH_BOOKING=26;
    public static int EVENT_ADD_CAR=27;



    public static int EVENT_LOCATION=28;

    public static int EVENT_SELECT_PACKAGE=29;
    public static int EVENT_BOOKING_CATEGORY_VISIBLE=30;
    public static int EVENT_EDIT_CART=31;
    public static int EVENT_DISMISS_CART_EDIT=32;
    public static int EVENT_DISMISS_CART=33;
    public static int EVENT_EDIT_CAR=34;
    public static int EVENT_RC_UPDATE=35;
    public static int EVENT_IC_UPDATE=36;
    public static int EVENT_MEMBER_ADDED=37;
    public static int EVENT_REFRESH_CAR_DETAIL=38;
    public static int EVENT_IMAGE_SELECTED_CARSALE =39;
    public static int EVENT_IMAGE_CAPTURED_CARSALE =40;
    public static int EVENT_ADD_ADDRESS =41;
    public static int EVENT_SEND_CAR_INSURANCE =42;
    public static int EVENT_SEND_INSURANCE_CAR =43;
    public static int EVENT_SEND_CAR_UPDATE =44;

    public static final String IS_BOOKING ="is_booking" ;
    public static final String IS_BOOKING_DETAILS ="is_booking_details" ;
    public static final String IS_JOBCARD ="is_jobcard";
    public static final String IS_MANUAL ="is_manual";
    public static final String IS_GARAGE_CAR ="is_garage_car";
    public static final String IS_QUALITY_CHECK ="is_quality_check";
    public static final String IS_IMAGE_SELECTED_CAR_SALES ="is_image_selected_car_sales";
    public static final String IS_IMAGE_CAPTURED_CAR_SALES ="is_image_captured_car_sales";
    public static final String IS_ADDITIONAL_PHOTOS ="is_additional_photos";
    public static final String IS_MAIN_SEARCH ="is_mainsearch" ;
    public static final String IS_JOBCARD_DETAILS ="is_jobcard";
    public static final String IS_LEAD ="is_lead";
    public static final String IS_CRE ="is_cre";
    public static final String IS_IMAGE ="is_image";
    public static final String IMAGE_URI ="image_uri";
    public static final String IS_CAR_UPDATE ="is_car_update";



    public static final String BOOKING_ID ="booking_id" ;
    public static final String LEAD_ID ="lead_id" ;
    public static final String USER_ID ="user_id" ;
    public static final String ADDRESS_ID ="address_id" ;
    public static final String DETAILS_TYPE ="details_type" ;
    public static final String INSURANCE_DETAILS ="details_insurance" ;
    public static final String RESPONSE ="response" ;
    public static final String QC_LIST ="qc_list" ;
    public static final String CAR_DETAILS ="car_details" ;
    public static final String ADVISOR_ID ="advisor_id" ;
    public static final String VALUE ="value" ;

    public static final String IC_URL ="ic_url";
    public static final String RC_URL ="rc_url";

    public static final String IMAGE_ARRAY ="image_array";

    public static final String ADDRESS ="address";
    public static final String LANDMARK ="landmark";
    public static final String ZIP ="zip";
    public static final String TOWN ="town";
    public static final String STATE ="state";
    public static final String LATITUDE ="latitude";
    public static final String LONGITUDE ="longitude";


    /*UTILITY*/

    public static final String FUEL ="fuel" ;
    public static final String ODOMETER ="odometer" ;

    /*LEADS*/

    public static final String OPEN ="Open";
    public static final String ASSIGNED ="Assigned";
    public static final String FOLLOW_UP ="Follow-Up";
    public static final String LOST="Lost";
    public static final String PSF="PSF";
    public static final String ALL="All";
    public static final String DISSATISFIED="Closed With Dissatisfied";


    public static final String USER_NAME ="UserName";

    public static final String SELLING_PRICE ="selling_price";
    public static final String REFURBISHMENT_PRICE ="refurbishment_price";
    public static final String PURCHASE_PRICE ="purchase_price";


    /*Status to be sent*/

    public static final String START_WORK ="StartWork" ;
    public static final String CLOSE_WORK ="CloseWork";
    public static final String COMPLETE_WORK ="CompleteWork";
    public static final String REWORK ="Rework";
    public static final String APPROVAL ="Approval";
    public static final String APPROVED ="Approved";
    public static final String CONFIRMED ="Confirmed";
    public static final String CLOSED ="Closed";
    public static final String PENDING ="Pending";
    public static final String MISSED ="Missed";
    public static final String ESTIMATE_REQUESTED ="EstimateRequested";

    /*Stages to be sent*/

    public static final String NEW_JOB ="NewJob";
    public static final String ESTIMATION ="Estimation";
    public static final String IN_PROCESS ="In-Process";
    public static final String QUALITY_CHECK ="QC";
    public static final String STORE_APPROVAL ="StoreApproval";
    public static final String GM_APPROVAL ="GMApproval";
    public static final String READY ="Ready";
    public static final String CANCELLED ="Cancelled";


    /*Sub Status to be received*/

    public static final String JOB_OPEN ="JobOpen";
    public static final String JOB_INITIATED ="JobInitiated";
    public static final String JOB_INSPECTION ="Inspection";
    public static final String JOB_ASSETS ="Assets";


    public static final String PRIMARY_STATUS ="primaryStatus" ;
    public static final String STATUS ="status";
    public static final String DATE ="date";
    public static final String TIME ="time";
    public static final String NAME ="name";
    public static final String CONTACT ="contact";
    public static final String EMAIL ="email";
    public static final String BUNDLE_DATA ="bundleData";

    public static String STOCK_TYPE_GARAGE="stock_garage";

    public static String OPERATIONS="Operations";
    public static String CRM="CRM";

    public static String STOCK_TYPE_OLD_CARS="stock_old_car";
    public static String USER_DATA="user_data";


}
