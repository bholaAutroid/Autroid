package autroid.business.api;
public class ApiURL {

    public static final String CLOUD_BASE_URL = "http://cgdip3kc.cloudimg.io/width/";
    public static final String CLOUD_BASE_URL_CROP = "http://cgdip3kc.cloudimg.io/crop/";
    public static final String INVOICE_PREMIX = "/my/booking/invoice/get?invoice=";

   //private static String URL="http://13.233.36.16:4000"; //Development
    //private String URL="http://13.127.210.146:4000"; //Staging
//    public static String URL = "http://13.234.81.188:4000";//Production
    public static String URL = "http://3.6.93.32:4000";//testing..
 
    public static final String BASE_URL = URL + "/api/";

    public static final String ABOUT_US_URL = "http://nexcloudinfotech.com/careager/public/careager/about-us";
    public static final String PRIVACY_POLICY_URL = "http://nexcloudinfotech.com/careager/public/careager/privacy-policy";
    public static final String TERMS_CONDITION_URL = "http://nexcloudinfotech.com/careager/public/careager/terms-conditions";

    public static final String LOGIN = "v2.0/login";
    public static final String VALIDATE_ACCOUNT = "v2.0/account";
    public static final String SIGNUP = "v2.0/business/signup";
    public static final String REGISTRATION_DATA = "v2.0/registration-data";
    public static final String PHONE_VERIFICATION = "v2.0/phone/verification";
    public static final String RESET_PASSWORD_VERIFY = "v2.0/reset/password/otp/verification";//phone/check

    public static final String RESEND_OTP = "v2.0/resend/otp";
    public static final String ADD_LOCAL_BUSINESS = "v2.0/local-business/add";
    public static final String SEARCH_BUSINESS = "v2.0/business/search";
    public static final String SEARCH_USER = "v2.0/business/user/search";
    public static final String SEARCH_USER_DETAIL = "v2.0/business/user/details";
    public static final String CLAIM_BUSINESS = "v2.0/claim/business";
    public static final String LOGOUT = "v2.0/logout";

    public static final String FORGOT_PASSWORD = "v2.0/forgot/password";
    public static final String RESET_PASSWORD = "v2.0/reset/password";

    public static final String SHOWROOM_CARS = "v2.0/all/cars";
    public static final String SHOWROOM_PRODUCTSS = "v2.0/all/products";
    public static final String SHOWROOM_OFFERSS = "v2.0/all/offers";
    public static final String SHOWROOM_REVIEWSS = "v2.0/all/reviews";

    /*                  CARS              */
    public static final String CAR_ITEMS = "v2.0/vehicle/detail";
    public static final String CAR_MODELS = "v2.0/models/get";
    public static final String CAR_VARIANT = "v2.0/variants/get";
    public static final String ADD_CAR = "v2.0/business/car/add";
    public static final String USER_CAR_ADD = "v2.0/business/user/car/add";
    public static final String ADD_CAR_IMAGES = "v2.0/business/car/add/image";
    public static final String REMOVE_CAR_PIC = "v2.0/business/car/image/delete";
    public static final String GARAGE_CARS_LIST = "v2.0/cars/get";
    public static final String CAR_STOCK = "v2.0/car";
    public static final String CAR_DETAIL = "v2.0/car/get";
    public static final String GARAGE_CAR_DETAIL = "v2.0/editable/car/get";
    public static final String EDIT_STOCK = "v2.0/business/car/edit";
    public static final String UNPUBLISH_CAR = "v2.0/car/unpublish";
    public static final String REMOVE_MEMBER = "v2.0/business/management/remove";
    public static final String USER_CAR_DETAIL = "v2.0/business/user/cars/get";
    public static final String SEARCH_CAR = "v2.0/makers/get";

    /*                  OFFERS              */
    public static final String OFFERS_LIST = "v2.0/business/offer";
    public static final String ADD_OFFER = "v2.0/business/offer/add";
    public static final String EDIT_OFFER = "v2.0/business/offer/edit";
    public static final String EDIT_OFFER_IMAGE = "v2.0/business/offer/image/add";
    public static final String PUBLISH_OFFER = "v2.0/business/offer/publish";

    /*                  PRODUCTS               */
    public static final String PRODUCT_STOCK = "v2.0/business/product";
    public static final String PRODUCT_CATEGORY = "v2.0/business/product/category";
    public static final String PRODUCT_SUBCATEGORY = "v2.0/business/product/subcategories";
    //
    public static final String PRODUCT_BRANDS = "v2.0/brand/get";
    public static final String ADD_PRODUCT = "v2.0/business/product/add";
    public static final String ADD_PRODUCT_IMAGES = "v2.0/business/product/image/add";
    public static final String REMOVE_PRODUCT_PIC = "v2.0/business/product/image/delete";
    public static final String EDIT_PRODUCT = "v2.0/business/product/edit";
    public static final String PUBLISH_PRODUCT = "v2.0/business/product/publish";

    /*                   HOME                    */
    public static final String SHOWROOM_PROFILE = "v2.0/profile/detail";
    public static final String DRAWER_MENU = "v2.0/business/side-menu/get";

    //public static final String ANALYTICS="v2.0/business/performance/analytic";
    public static final String ANALYTICS = "v2.0/business/teams/analytic";
    public static final String ADD_SHOWROOM_REVIEW = "v2.0/review/add";
    public static final String SAVE_BUSINESS = "v2.0/business/bookmark";
    public static final String SAVED_BUSINESSES = "v2.0/business/bookmark/get";
    public static final String NEW_TOKEN = "v2.0/create/token";
    public static final String FCM_TOKEN = "v2.0/fcm/update";
    public static final String REMOVE_SHOWROOM_PIC = "v2.0/business/gallery/delete";

    /*                  SETTINGS                */
    public static final String TIMINGS_ARRAY = "v2.0/timing";
    public static final String UPDATE_PROFILE = "v2.0/business/profile/update";
    public static final String CHANGE_PASSWORD = "v2.0/password/change";
    public static final String ADD_SHOWROOM_IMAGES = "v2.0/business/gallery/update";

    public static final String ADD_TIMINGS = "v2.0/business/timing/update";
    public static final String UPDATE_COVER = "v2.0/avatar/update";
    public static final String UPDATE_SOCIAL_LINKS = "v2.0/social-info/update";
    /*                  UPDATE BUSINESS LOCATION                */
    public static final String UPDATE_LOCATION = "v2.0/business/location/update";


    public static final String VENDOR_SERVICES = "v2.0/business/estimation/get";
    public static final String ADD_SERVICES = "add-services";
    public static final String SERVICES_TYPES = "get-business-type";

    public static final String BOOKING_CATEGORY = "v2.0/business/booking/category/get";
    public static final String BOOKING_SERVICES = "v2.0/business/booking/services/get";
    public static final String ESTIMATE_SERVICES = "v2.0/business/services/rate";

    public static final String VENDER_ENQUIRIES = "vendor-enquiries";
    public static final String BOOKINGS = "v2.0/business/bookings/get";//booking-status
    public static final String STATUS_COUNT = "v2.0/business/bookings/count";//booking-status
    public static final String BOOKINGS_BY_NUMBER = "v2.0/booking/details";
    public static final String BOOKING_STATUS = "v2.0/business/booking/status";
    public static final String MY_BOOKING_STATUS = "v2.0/business/my/booking/status";//get-stock-detail
    public static final String REVIEW_POINTS = "v2.0/review-points";
    public static final String SET_REVIEW_POINTS = "v2.0/business/job/review/add";
    public static final String ADD_BUSINESS_TYPE = "add-business-type";
    public static final String NOTIFICATIONS = "v2.0/notifications/get";

    public static final String NOTIFICATIONS_WEB = "v2.1/business/web/notifications/get";

    public static final String TYPE_SPECIFICATION = "v2.0/business/tyreSize/get";

    public static final String BOOKING_SEARCH = "v2.0/business/booking/search/get";

    public static final String BUSINESS_OVERVIEW = "v2.0/business/analytics";

    /*Lead*/
    public static final String LEADS = "v2.0/business/leads/get";
    public static final String LEAD_STATUS = "v2.0/business/lead/status/get";
    public static final String LEAD_REMARK = "v2.0/business/lead/remark/update";
    public static final String LEAD_CREATE = "v2.0/business/lead/add";
    public static final String LEAD_CREATE_CHAT = "v2.0/business/chat/leads/add";
    public static final String LEAD_DETAILS = "v2.0/business/lead/details/get";
    public static final String LEAD_IMPORTANT = "v2.0/business/lead/important";
    public static final String PURCHASED_PACKAGES = "v2.0/business/packages/get";
    public static final String BOOKINGS_RESCHEDULE = "v2.0/business/booking/reschedule";
    public static final String MY_BOOKINGS_RESCHEDULE = "v2.0/business/my/booking/reschedule";
    public static final String CRE_GET ="v2.0/business/cre/get";


    public static final String CONVERT_LEAD = "v2.0/business/convert/lead";
    public static final String UPDATE_PRIORITY = "v2.0/business/lead/priority/update";
    public static final String ADD_ADVISOR = "v2.0/business/lead/advisor/add";
    public static final String LEAD_BOOKING = "v2.0/business/leads/booking/get";
    public static final String UPDATE_USER = "v2.0/business/lead/edit";
    public static final String ADVISOR_LIST = "v2.0/business/service-advisors/get";
    public static final String CATEGORY_LIST = "v2.0/business/lead/category/get";
    public static final String USER_CARS_LEAD = "v2.0/business/lead/user/cars/get";
    public static final String CAR_DOCUMENTS = "v2.0/business/car/documents/get";

    /*Jobcard*/

    public static final String GET_USER = "v2.0/users/get";
    public static final String ADD_OWNER = "v2.0/business/owner/add";
    public static final String ADD_OWNER_CAR = "v2.0/business/owner/car/add";  // api is used for booking
    public static final String ADD_MANUAL_BOOKING = "v2.0/business/booking/add";

    public static final String LEAD_BOOKING_ADD = "v2.0/business/lead/booking/add";
    public static final String ADD_INSPECTION_IMAGES = "v2.0/business/job/inspection/add";
    public static final String ADD_APPROVAL_IMAGES = "v2.0/business/quality/inspection/add";
    public static final String ADD_ADDITIONAL_IMAGES = "v2.0/business/job/snaps/add";
    public static final String GET_PARTICULARS = "v2.0/business/assets/get";
    public static final String UPDATE_PARTICULARS = "v2.0/business/assets/update";
    public static final String GET_TECHNICIANS = "v2.0/business/technicians/get";
    public static final String GET_DRIVER = "v2.0/user/get";
    public static final String PUT_REQUIREMENTS = "v2.0/business/job/requirements/update";
    public static final String ADD_INSPECTION_RECORDING = "v2.0/business/job/recording/add";
    public static final String GET_COMPANY_NAME = "v2.0/insurance-company/search/get";
    public static final String GET_BOOKING_CAR = "v2.0/business/booking/car/get";
    public static final String POST_EXISTING_BOOKING = "v2.0/business/booking/job/add";
    public static final String GET_BOOKED_SERVICES = "v2.0/business/booked/services/get";
    public static final String JOBS_LIST = "v2.0/business/jobs/get";
    public static final String UPDATE_DELIVERY_DETAILS = "v2.0/business/job/delivery-date/update";
    public static final String ORDERS_LIST = "v2.0/business/orders/get";
    public static final String ORDER_DETAILS = "v2.0/business/order/details/get";
    public static final String ANY_DETAILS = "v2.0/business/search/all";
    public static final String BOOKING_DETAILS = "v2.0/booking/details";
    public static final String ADDRESS_DETAILS = "v2.0/business/job/convenience";
    public static final String ACCIDENT_UPDATE = "v2.0/business/job/insurance/update";
    public static final String ADD_ADDRESS = "v2.0/business/address/add";
    public static final String ADDRESS_UPDATE = "v2.0/business/booking/address/update";
    public static final String GET_PIN_DATA = "v2.0/business/postal/get";
    public static final String GET_BUSINESS_PIN_DATA = "v2.0/business/postals/get";
    public static final String BOOKING_REMARK_EDIT = "v2.0/business/booking/remarks/edit";
    public static final String SURVEYOR_DETAILS = "v2.0/business/job/surveyor/update";
    public static final String INSPECTION_IMAGES = "v2.0/business/job/inspection/get";
    public static final String UPDATE_JOBS_STATUS = "v2.0/business/job/status/update";
    public static final String UPDATE_CUSTOMER_DETAILS = "v2.0/business/customer-info/update";
    public static final String UPDATE_CAR_DETAILS = "v2.0/business/booking/car/update";
    public static final String PAYMENT_MODE = "v2.0/business/payment/mode";
    public static final String PAYMENT_RECEIVED = "v2.0/business/job/payment/receive";
    public static final String JOBCARD_QC = "v2.0/business/job/qc";
    public static final String PUT_JOBCARD_QC = "v2.0/business/job/qc/update";
    public static final String ADD_CLAIM = "v2.0/business/job/claim-info/update";
    public static final String INSURANCE_UPDATE = "v2.0/business/job/insurance-info/update";
    public static final String ASSETS_UPDATE = "v2.0/business/job/assets/update";
    public static final String PAYMENT_LOG = "v2.0/business/job/payment/logs";
    public static final String TECHNICIAN_UPDATE = "v2.0/business/technician/update";
    public static final String ADVISOR_UPDATE = "v2.0/business/advisor/update";


    public static final String ADD_BOOKING_ADDRESS = "v2.0/business/address/add";
    public static final String GET_BOOKING_ADDRESS = "v2.0/user/address/get";
    public static final String BOOKING_TIMESLOT = "v2.0/business/booking/time-slot/get";
    public static final String SCHEDULE_BOOKING = "v2.0/business/booking/info/update";
    public static final String BOOKING_ADDRESS_BY_POSTAL = "v2.0/business/postal/get";
    public static final String BOOKING_CONVENIENCE = "v2.0/business/job/convenience";

    public static final String UPLOAD_RC_IMG = "v2.0/car/rc/add";
    public static final String UPLOAD_INSURANCE_IMG = "v2.0/car/ic/add";
    public static final String UPLOAD_CAR_DOCUMENT = "v2.0/business/car/document/add";
    public static final String DELETE_CAR_DOCUMENT = "v2.0/business/car/document/delete";
    public static final String DELETE_RC_IMG = "v2.0/car/rc/delete";
    public static final String DELETE_INSURANCE_IMG = "v2.0/car/ic/delete";
    public static final String REMOVE_CAR = "v2.0/car/delete";

    public static final String OUTLETS = "v3.4/explore/careager/outlets";
    public static final String HOME_GET_PACKAGES = "v2.0/business/packages/get";
    public static final String BOOKING_SERVICES_GALLERY = "v2.0/business/service/gallery/get";
    public static final String ADD_BOOKING = "v2.0/business/new/booking/add";

    public static final String BOOKING_SERVICES_DESCRIPTION = "v2.0/business/service/description/get";
    public static final String My_BOOKINGS = "v2.0/business/my/bookings/get";//"v3.4/bookings/get";
    public static final String My_BOOKING_TIMESLOT = "v2.0/business/bookings/time-slot";
    public static final String My_BOOKING_CONVENIENCE = "v2.0/business/booking/convenience";
    public static final String BOOKING__SERVICE_DETAILS = "v2.0/booking/services/details";
    public static final String BOOKING_SERVICES_APPROVED = "v2.0/business/my/approved/services/add";
    public static final String BOOKING_PAY_LATER = "v2.0/business/pay/later";
    public static final String BOOKING_DISCOUNT_APPLY = "v2.0/business/booking/coupon/add";


    public static final String COIN_DETAILS = "v2.0/business/wallet/get";
    public static final String MY_REFERRALS = "v2.0/business/referrals/get";
    public static final String ROLES = "v2.0/business/management/roles/get";
    public static final String SET_MEMBER = "v2.0/business/management/add";

    public static final String BOOKING_CHECKSUM = "v2.0/business/sell/car/package/checksum";
    public static final String PAYMENT_RECHECK = "v2.0/business/sell/car/package/transaction";

    public static final String CAR_SOLD = "v2.0/business/car/sold";
    public static final String CAR_SOLD_OTP = "v2.0/business/car/buyer/verification";
    public static final String USED_CAR = "v2.0/business/explore/used/car";
    public static final String CAR_LEAD = "v2.0/car/lead";

    /* LEAD GENERATION*/
    public static final String INSURANCE_DUE = "v2.0/business/insurance/due/get";
    public static final String SERVICE_DUE = "v2.0/business/service-reminder/get";



}
