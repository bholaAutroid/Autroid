package autroid.business.api;


import autroid.business.model.bean.BusinessPlanResponse;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.AddOwnerRequest;
import autroid.business.model.request.AddressRequest;
import autroid.business.model.request.BookingAddressRequest;
import autroid.business.model.request.BookingRescheduleRequest;
import autroid.business.model.request.BookingUpdateRequest;
import autroid.business.model.request.CarLeadRequest;
import autroid.business.model.request.CarSoldRequest;
import autroid.business.model.request.CreateLeadRequest;
import autroid.business.model.request.ForgotPasswordRequest;
import autroid.business.model.request.GetTimeSlotRequest;
import autroid.business.model.request.InsuranceAccidentUpdateRequest;
import autroid.business.model.request.InsuranceUpdateRequest;
import autroid.business.model.request.JobCardBookingRequest;
import autroid.business.model.request.JobCardPaymentRequest;
import autroid.business.model.request.LeadAdvisorRequest;
import autroid.business.model.request.LeadBookingRequest;
import autroid.business.model.request.LeadPriorityRequest;
import autroid.business.model.request.LeadUpdateRequest;
import autroid.business.model.request.LeadUserRequest;
import autroid.business.model.request.MemberRequest;
import autroid.business.model.request.PaymentDetailRequest;
import autroid.business.model.request.PointsRequest;
import autroid.business.model.request.RequirementRequest;
import autroid.business.model.request.ResetPasswordRequest;
import autroid.business.model.request.ReviewRequest;
import autroid.business.model.request.ServiceApproveRequest;
import autroid.business.model.request.ServiceRequest;
import autroid.business.model.request.SurveyorRequest;
import autroid.business.model.request.UpdateAssetsRequest;
import autroid.business.model.request.UpdateCarDetailsRequest;
import autroid.business.model.request.UpdateCustomerDetailsRequest;
import autroid.business.model.request.UpdateDeliveryDetailsRequest;
import autroid.business.model.request.UpdateMemberRequest;
import autroid.business.model.request.UpdateRequest;
import autroid.business.model.request.UpdateStatusRequest;
import autroid.business.model.request.UploadMultipleImagesRequest;
import autroid.business.model.response.*;
import autroid.business.model.bean.SocialiteBE;
import autroid.business.model.request.AddReviewRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.request.FcmTokenRequest;
import autroid.business.model.request.PhoneVerificationRequest;
import autroid.business.model.request.SaveBusinessRequest;
import autroid.business.model.request.AddBusinessRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.AddOfferRequest;
import autroid.business.model.request.AddProductRequest;
import autroid.business.model.request.AddServiceRequest;
import autroid.business.model.request.AddTimingsRequest;
import autroid.business.model.request.ChangePasswordRequest;
import autroid.business.model.request.ClaimBusinessRequest;
import autroid.business.model.request.LoginRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.request.RefreshTokenRequest;
import autroid.business.model.request.UpdateBusinessLocationRequest;
import autroid.business.model.request.UpdateProfileRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Careager on 17-05-2018.
 * <p>
 * All the Service call are registered in this class along with the URL, input data fields and
 * response type are define while declaring it as retrofit requests.
 */

public interface ApiRequest {

    /* POST LOGIN */
    @POST(ApiURL.LOGIN)
    ApiCallback.MyCall<LoginResponse> postLogin(@Body LoginRequest loginPostRequest);

    /* POST LOGIN */
    @POST(ApiURL.VALIDATE_ACCOUNT)
    ApiCallback.MyCall<LoginResponse> validateAccount(@Body LoginRequest loginPostRequest);

    /* POST REGISTER */
    @POST(ApiURL.SIGNUP)
    ApiCallback.MyCall<LoginResponse> registeration(@Body AddBusinessRequest addBusinessRequest);

    /* FORGOT PASSWORD */
    @POST(ApiURL.FORGOT_PASSWORD)
    ApiCallback.MyCall<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    /* RESET PASSWORD */
    @POST(ApiURL.RESET_PASSWORD)
    ApiCallback.MyCall<LoginResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    /* logout */
    @POST(ApiURL.LOGOUT)
    ApiCallback.MyCall<BaseResponse> logout();

    /* POST LOGIN */
    @GET(ApiURL.CAR_ITEMS)
    ApiCallback.MyCall<CarItemsResponse> getCarItems();

    /* GET ROLES */
    @GET(ApiURL.ROLES)
    ApiCallback.MyCall<RolesResponse> getRoles();

    /* SET MEMBER */
    @POST(ApiURL.SET_MEMBER)
    ApiCallback.MyCall<BaseResponse> setMember(@Body MemberRequest memberRequest);

    /* SEARCH BUSINESS*/
    @GET(ApiURL.SEARCH_BUSINESS)
    ApiCallback.MyCall<SearchBusinessResponse> searchBussiness(@Query("query") String id);

    /* SEARCH BUSINESS*/
    @GET(ApiURL.SEARCH_USER)
    ApiCallback.MyCall<UserSearchResponse> searchUser(@Query("query") String id);

    /* SEARCH BUSINESS*/
    @GET(ApiURL.SEARCH_BUSINESS)
    ApiCallback.MyCall<UserSearchResponse> searchBusiness(@Query("query") String id);

    /* SEARCH DETAIL BUSINESS*/
    @GET(ApiURL.SEARCH_USER_DETAIL)
    ApiCallback.MyCall<SearchDetailResponse> searchDetail(@Query("query") String id);

    /* Get Makers List */
    @GET(ApiURL.CAR_MODELS)
    ApiCallback.MyCall<CarModelsResponse> getMakersList(@Query("id") String id);

    /* Get Variant List */
    @GET(ApiURL.CAR_VARIANT)
    ApiCallback.MyCall<CarVariantResponse> getVariantList(@Query("id") String id);

    /* Get Car Stock */
    @GET(ApiURL.CAR_STOCK)
    ApiCallback.MyCall<CarListResponse> getCarStock(@Query("page") int page);

    /* Get Car Stock */
    @GET(ApiURL.GARAGE_CARS_LIST)
    ApiCallback.MyCall<CarListResponse> getGarageCarsList(@Query("page") int page);

    /* Get Car Details */
    @GET(ApiURL.CAR_DETAIL)
    ApiCallback.MyCall<CarStockResponse> getCarDetails(@Query("id") String id);

    /* Get Car Details */
    @GET(ApiURL.GARAGE_CAR_DETAIL)
    ApiCallback.MyCall<GarageCarResponse> getGarageCarDetails(@Query("by") String by, @Query("id") String carId);

    /* Get Product Stock */
    @GET(ApiURL.PRODUCT_STOCK)
    ApiCallback.MyCall<ProductStockResponse> getProductStock(@Query("page") int page);

    /* OFFERS LIST*/
    @GET(ApiURL.OFFERS_LIST)
    ApiCallback.MyCall<OffersResponse> getOffers(@Query("page") int page);

    /* ADD OFFER*/
    @Multipart
    @POST(ApiURL.ADD_OFFER)
    ApiCallback.MyCall<AddOfferResponse> postOffer(@Part MultipartBody.Part imageFile, @Part("description") RequestBody offer, @Part("offer") RequestBody description, @Part("valid_till") RequestBody valid_till);

    /* EDIT OFFER*/
    @PUT(ApiURL.EDIT_OFFER)
    ApiCallback.MyCall<AddOfferResponse> editOffer(@Body AddOfferRequest addOfferRequest);

    /* EDIT OFFER Image*/
    @Multipart
    @POST(ApiURL.EDIT_OFFER_IMAGE)
    ApiCallback.MyCall<AddOfferResponse> editOfferImage(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    /* Publish offer */
    @PUT(ApiURL.PUBLISH_OFFER)
    ApiCallback.MyCall<PublishResponse> putPublishOfferRequest(@Body PublishUnpublishRequest publishUnpublishRequest);

    /* PRODUCT CATEGORY*/
    @GET(ApiURL.PRODUCT_CATEGORY)
    ApiCallback.MyCall<ProductCategoryResponse> getProductCategory();

    /* PRODUCT SUB CATEGORY*/
    @GET(ApiURL.PRODUCT_SUBCATEGORY)
    ApiCallback.MyCall<ProductCategoryResponse> getProductSubCategory(@Query("id") String id);


    /* ADD Product*/
    @POST(ApiURL.ADD_PRODUCT)
    ApiCallback.MyCall<AddProductResponse> postProduct(@Body AddProductRequest addProductRequest);

    /*  Product Brands*/
    @GET(ApiURL.PRODUCT_BRANDS)
    ApiCallback.MyCall<ProductBrandResponse> getBrands(@Query("type") String type);

    /*  Product Brands*/
    @GET(ApiURL.TYPE_SPECIFICATION)
    ApiCallback.MyCall<ProductBrandResponse> getTyreSpecification(@Query("query") String query);

    /* ADD CAR*/
    @POST(ApiURL.ADD_CAR)
        ApiCallback.MyCall<AddCarResponse> postCar(@Body AddCarRequest addCarRequest);

    /* ADD CAR*/
    @POST(ApiURL.USER_CAR_ADD)
    ApiCallback.MyCall<AddCarResponse> postUserCar(@Body AddCarRequest addCarRequest);

    /* ADD CAR images*/
    @Multipart
    @POST(ApiURL.ADD_CAR_IMAGES)
    ApiCallback.MyCall<AddImageResponse> postImageCar(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    /* ADD Product images*/
    @Multipart
    @POST(ApiURL.ADD_PRODUCT_IMAGES)
    ApiCallback.MyCall<AddImageResponse> postImageProduct(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    /* ADD ShowroomRealm images*/
    @Multipart
    @POST(ApiURL.ADD_SHOWROOM_IMAGES)
    ApiCallback.MyCall<AddImageResponse> postImageShowroom(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    /* SHOWROOM PROFILE*/
    @GET(ApiURL.SHOWROOM_PROFILE)
    ApiCallback.MyCall<BusinessProfileResponse> getShowroomProfile(@Query("id") String id);

    /* SHOWROOM/USER PROFILE*/
    @GET(ApiURL.SHOWROOM_PROFILE)
    ApiCallback.MyCall<UserProfileResponse> getProfile(@Query("id") String id);

    /* SHOWROOM PROFILE*/
    @GET(ApiURL.DRAWER_MENU)
    ApiCallback.MyCall<BusinessPlanResponse> getNavigation();

    /* SHOWROOM CARS*/
    @GET(ApiURL.SHOWROOM_CARS)
    ApiCallback.MyCall<ShowroomCarsResponse> getShowroomCars(@Query("id") String id, @Query("page") int page);

    /* SHOWROOM PRODUCTS*/
    @GET(ApiURL.SHOWROOM_PRODUCTSS)
    ApiCallback.MyCall<ShowroomProductResponse> getShowroomProducts(@Query("id") String id, @Query("page") int page);

    /* SHOWROOM OFFERS*/
    @GET(ApiURL.SHOWROOM_OFFERSS)
    ApiCallback.MyCall<ShowroomOfferResponse> getShowroomOffers(@Query("id") String id, @Query("page") int page);

    /* SHOWROOM REVIEWS*/
    @GET(ApiURL.SHOWROOM_REVIEWSS)
    ApiCallback.MyCall<ShowroomReviewResponse> getShowroomReviews(@Query("id") String id, @Query("page") int page);

    /* ANALYTICS*/
    @GET(ApiURL.ANALYTICS)
    ApiCallback.MyCall<AnalyticsResponse> getAnalyticsData(@Query("department") String department,@Query("type") String type, @Query("query") String query);

    /* UnPublish CAR */
    @HTTP(method = "DELETE", path = ApiURL.REMOVE_MEMBER, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeMember(@Query("user") String userId);

    /* UnPublish CAR */
    @HTTP(method = "DELETE", path = ApiURL.UNPUBLISH_CAR, hasBody = true)
    ApiCallback.MyCall<PublishResponse> putUnPublishCarRequest(@Body PublishUnpublishRequest publishUnpublishRequest);

    /* Publish Product */
    @PUT(ApiURL.PUBLISH_PRODUCT)
    ApiCallback.MyCall<PublishResponse> putPublishProductRequest(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Update business location*/
    @PUT(ApiURL.UPDATE_LOCATION)
    ApiCallback.MyCall<BaseResponse> updateLocation(@Body UpdateBusinessLocationRequest updateBusinessLocationRequest);

    /*Remove Car images*/
    @HTTP(method = "DELETE", path = ApiURL.REMOVE_CAR_PIC, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeCarPic(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Remove product images*/
    @HTTP(method = "DELETE", path = ApiURL.REMOVE_PRODUCT_PIC, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeProductPic(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Remove showroom images*/
    @HTTP(method = "DELETE", path = ApiURL.REMOVE_SHOWROOM_PIC, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeShowroomPic(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Edit Car stock*/
    @PUT(ApiURL.EDIT_STOCK)
    ApiCallback.MyCall<AddCarResponse> editCarStock(@Body AddCarRequest addCarRequest);

    /*Edit Product stock*/
    @PUT(ApiURL.EDIT_PRODUCT)
    ApiCallback.MyCall<AddProductResponse> editProductStock(@Body AddProductRequest addProductRequest);


    /* GET VENDOR SERVICES */
    @POST(ApiURL.BOOKING_SERVICES)
    ApiCallback.MyCall<VendorServicesResponse> getVendorServices(@Body ServiceRequest serviceRequest);

    /* ADD SERVICES*/
    @POST(ApiURL.ADD_SERVICES)
    ApiCallback.MyCall<BaseResponse> addService(@Body AddServiceRequest addServiceRequest);

    /* GET  SERVICES TYPES*/
    @GET(ApiURL.SERVICES_TYPES)
    ApiCallback.MyCall<ServiceTypeResponse> getServicesTypes();

    /* GET  TIMINGS ARRAY*/
    @GET(ApiURL.TIMINGS_ARRAY)
    ApiCallback.MyCall<TimingArrayResponse> getTimingArray();

    /* ADD TIMINGS*/
    @PUT(ApiURL.ADD_TIMINGS)
    ApiCallback.MyCall<BaseResponse> addTiming(@Body AddTimingsRequest addTimingsRequest);

    /* Change password*/
    @PUT(ApiURL.CHANGE_PASSWORD)
    ApiCallback.MyCall<BaseResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    /* Update Profile */
    @PUT(ApiURL.UPDATE_PROFILE)
    ApiCallback.MyCall<BaseResponse> updateProfile(@Body UpdateProfileRequest updateProfileRequest);

    /* Update Profile */
    @POST(ApiURL.ADD_BUSINESS_TYPE)
    ApiCallback.MyCall<BaseResponse> addServiceType(@Body PublishUnpublishRequest publishUnpublishRequest);

    /* Update Profile */
    @POST(ApiURL.NEW_TOKEN)
    ApiCallback.MyCall<LoginResponse> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);


    /* GET  REGISTRATION DATA */
    @GET(ApiURL.REGISTRATION_DATA)
    ApiCallback.MyCall<RegistrationDataResponse> getRegistrationData();

    /* Add Local business */
    @Multipart
    @POST(ApiURL.ADD_LOCAL_BUSINESS)
    ApiCallback.MyCall<BaseResponse> addLocalBusiness(@Part MultipartBody.Part imageFile, @Part("name") RequestBody name, @Part("email") RequestBody email, @Part("contact_no") RequestBody contact_no, @Part("category") RequestBody category, @Part("company") RequestBody company, @Part("location") RequestBody location, @Part("latitude") Double latitude, @Part("longitude") Double longitude, @Part("country") RequestBody country, @Part("username") RequestBody username);

    /* CLAIM business */
    @POST(ApiURL.CLAIM_BUSINESS)
    ApiCallback.MyCall<BaseResponse> claimBusiness(@Body ClaimBusinessRequest claimBusinessRequest);

    /* GET  REGISTRATION DATA */
    @GET(ApiURL.VENDER_ENQUIRIES)
    ApiCallback.MyCall<RequestLeadResponse> getLeads();

    /* GET Booking Category */
    @GET(ApiURL.BOOKING_CATEGORY)
    ApiCallback.MyCall<BookingCategoryResponse> getBookingCategory(@Query("car") String car);

    /* GET DIAGNOSIS SERVICES */
    @POST(ApiURL.BOOKING_SERVICES)
    ApiCallback.MyCall<BookingServicesResponse> getDiagnosis(@Body ServiceRequest serviceRequest);

    /* GET DIAGNOSIS SERVICES */
    @POST(ApiURL.ESTIMATE_SERVICES)
    ApiCallback.MyCall<BookingServicesResponse> getEstimate(@Query("car") String car,@Query("type") String type);


    @POST(ApiURL.ESTIMATE_SERVICES)
    ApiCallback.MyCall<VendorServicesResponse> getEstimateNested(@Query("car") String car,@Query("type") String type);


    /* BOOKINGS*/
    @GET(ApiURL.BOOKINGS)
    ApiCallback.MyCall<BookingsResponse> getBookings(@Query("status") String status, @Query("page") int page,@Query("sortBy") String soryBy);

    /* BUSINESS OVERVIEW*/
    @GET(ApiURL.BUSINESS_OVERVIEW)
    ApiCallback.MyCall<BusinessOverviewResponse> getOverview(@Query("type") String type,@Query("query") String query);

    /* BOOKINGS*/
    @GET(ApiURL.STATUS_COUNT)
    ApiCallback.MyCall<StatusCountResponse> getStatusCount();

    /* BOOKINGS*/
    @GET(ApiURL.BOOKINGS_BY_NUMBER)
    ApiCallback.MyCall<BookingsResponse> getBookingsByNumber(@Query("booking") String status);

    /* BOOKINGS STATUS*/
    @PUT(ApiURL.BOOKING_STATUS)
    ApiCallback.MyCall<BaseResponse> updateStatus(@Body BookingStatusRequest bookingStatusRequest);

    /* BOOKINGS STATUS*/
    @PUT(ApiURL.MY_BOOKING_STATUS)
    ApiCallback.MyCall<BaseResponse> updateStatusMyBooking(@Body BookingStatusRequest bookingStatusRequest);

    /* REVIEW POINTS*/
    @GET(ApiURL.REVIEW_POINTS)
    ApiCallback.MyCall<ReviewResponse> getReviewQuestions(@Query("booking") String bookingId);

    /* SET REVIEW POINTS*/
    @POST(ApiURL.SET_REVIEW_POINTS)
    ApiCallback.MyCall<BaseResponse> setReviewQuestions(@Body ReviewRequest reviewRequest);

    /* UPDATE SOCIAL LINKS */
    @PUT(ApiURL.UPDATE_SOCIAL_LINKS)
    ApiCallback.MyCall<BaseResponse> updateSocialLinks(@Body SocialiteBE socialiteBE);

    /* Update Cover Photo */

    @PUT(ApiURL.UPDATE_COVER)
    ApiCallback.MyCall<UploadCoverResponse> updateCover(@Body UploadMultipleImagesRequest uploadMultipleImagesRequest);


    /* FCM TOKEN */
    @PUT(ApiURL.FCM_TOKEN)
    ApiCallback.MyCall<BaseResponse> refreshFcmToken(@Body FcmTokenRequest fcmTokenRequest);

    /* verify mobile for reset password */
    @POST(ApiURL.RESET_PASSWORD_VERIFY)
    ApiCallback.MyCall<BaseResponse> resetPasswordVerify(@Body PhoneVerificationRequest phoneVerificationRequest);

    /* PHONE NUMBER VERIFICATION*/
    @POST(ApiURL.PHONE_VERIFICATION)
    ApiCallback.MyCall<LoginResponse> verifyNumber(@Body PhoneVerificationRequest phoneVerificationRequest);

    /* RESEND OTP*/
    @POST(ApiURL.RESEND_OTP)
    ApiCallback.MyCall<BaseResponse> resendOtp(@Body PhoneVerificationRequest phoneVerificationRequest);

    /* ADD SHOWROOM REVIEW */
    @POST(ApiURL.ADD_SHOWROOM_REVIEW)
    ApiCallback.MyCall<BaseResponse> addReview(@Body AddReviewRequest addReviewRequest);

    /* SAVE BUSINESS*/
    @POST(ApiURL.SAVE_BUSINESS)
    ApiCallback.MyCall<SaveBusinessResponse> saveBusiness(@Body SaveBusinessRequest saveBusinessRequest);

    /* SAVED BUSINESSESS*/
    @GET(ApiURL.SAVED_BUSINESSES)
    ApiCallback.MyCall<SavedBusinessResponse> getSaveBusiness();

    /* NOTIFICATIONS LIST */
    @GET(ApiURL.NOTIFICATIONS)
    ApiCallback.MyCall<NotificationsListResponse> getNotifications(@Query("page") int page);

    /* NOTIFICATIONS LIST */
    @GET(ApiURL.LEADS)
    ApiCallback.MyCall<LeadsResponse> getAllLeads(@Query("by") String filter,@Query("page") int page, @Query("status") String status,@Query("priority") int priority,@Query("date") String date);

    /* NOTIFICATIONS LIST */
    @GET(ApiURL.LEAD_STATUS)
    ApiCallback.MyCall<LeadStatusResponse> getAllLeadStatus(@Query("stage") String stage);

    /* NOTIFICATIONS LIST */
    @PUT(ApiURL.LEAD_IMPORTANT)
    ApiCallback.MyCall<LeadImportantResponse> setImportant(@Body LeadUpdateRequest leadUpdateRequest);

    /* LEAD DETAILS */
    @GET(ApiURL.LEAD_DETAILS)
    ApiCallback.MyCall<LeadDetailsResponse> getLeadDetail(@Query("lead") String id);

    /*UPDATE PRIORITY*/
    @PUT(ApiURL.UPDATE_PRIORITY)
    ApiCallback.MyCall<BaseResponse> updatePriority(@Body LeadPriorityRequest leadConvertRequest);

    /*ADD ADVISOR*/
    @POST(ApiURL.ADD_ADVISOR)
    ApiCallback.MyCall<BaseResponse> addAdvisor(@Body LeadAdvisorRequest leadAdvisorRequest);

    /* NOTIFICATIONS LIST */
    @PUT(ApiURL.LEAD_REMARK)
    ApiCallback.MyCall<CreateLeadResponse> addRemark(@Body LeadUpdateRequest leadUpdateRequest);

    /* NOTIFICATIONS LIST */
    @POST(ApiURL.LEAD_CREATE)
    ApiCallback.MyCall<CreateLeadResponse> createLead(@Body CreateLeadRequest createLeadRequest);

    /*ASSIGNED LEADS*/
    @GET(ApiURL.LEAD_BOOKING)
    ApiCallback.MyCall<BookingsResponse> getLeadBookings(@Query("page") int page);

    /* NOTIFICATIONS LIST */
    @GET(ApiURL.LEAD_CREATE_CHAT)
    ApiCallback.MyCall<BaseResponse> createLeadChat(@Query("id") String id, @Query("type") String source);

    /* REFER EARN */
    @GET(ApiURL.PURCHASED_PACKAGES)
    ApiCallback.MyCall<PurchasedPackagesResponse> purchasedPackages();

    /* GET SLOTS */
    @GET(ApiURL.BOOKING_TIMESLOT)
    ApiCallback.MyCall<BookingSlotResponse> getSlots(@Query("booking") String id,@Query("date") String date);

    /* GET SLOTS */
    @GET(ApiURL.BOOKING_CONVENIENCE)
    ApiCallback.MyCall<BookingConvenienceResponse> getConvenience(@Query("booking") String id,@Query("business") String business);

    /* BOOKINGS*/
    @PUT(ApiURL.BOOKINGS_RESCHEDULE)
    ApiCallback.MyCall<BookingRescheduleResponse> bookingReschedule(@Body BookingRescheduleRequest bookingRescheduleRequest);

    /* BOOKINGS*/
    @PUT(ApiURL.MY_BOOKINGS_RESCHEDULE)
    ApiCallback.MyCall<BookingRescheduleResponse> myBookingReschedule(@Body BookingRescheduleRequest bookingRescheduleRequest);

    /* SEARCH CAR */
    @GET(ApiURL.SEARCH_CAR)
    ApiCallback.MyCall<SearchCarResponse> searchCar(@Query("query") String query);

    /* VERIFY USER */
    @GET(ApiURL.GET_USER)
    ApiCallback.MyCall<GetUserResponse> verifyUser(@Query("query") String query, @Query("by") String by);

    /*ADD OWNER*/
    @POST(ApiURL.ADD_OWNER)
    ApiCallback.MyCall<AddOwnerResponse> addOwner(@Body AddOwnerRequest addOwnerRequest);

    /*GET USER CAR DETAILS*/
    @GET(ApiURL.USER_CAR_DETAIL)
    ApiCallback.MyCall<GetUserCarResponse> getUserCar(@Query("user") String userId);

    /*ADD CAR/BOOKING*/
    @POST(ApiURL.ADD_OWNER_CAR)
    ApiCallback.MyCall<GetUserBookingResponse> addBooking(@Body JobCardBookingRequest jobCardBookingRequest);

    /* ADD BOOKINGS */
    @PUT(ApiURL.SCHEDULE_BOOKING)
    ApiCallback.MyCall<AddBookingResponse> scheduleBooking(@Body AddBookingRequest addBookingRequest);


    /*ADD MANUAL BOOKING*/
    @POST(ApiURL.ADD_MANUAL_BOOKING)
    ApiCallback.MyCall<BookingsResponse> addManualBooking(@Body JobCardBookingRequest jobCardBookingRequest);

    /*LEAD BOOKING ADD*/
    @POST(ApiURL.LEAD_BOOKING_ADD)
    ApiCallback.MyCall<BookingsResponse> addLeadBooking(@Body LeadBookingRequest leadBookingRequest);

    /*ADD INSPECTION IMAGES*/
    @Multipart
    @PUT(ApiURL.ADD_INSPECTION_IMAGES)
    ApiCallback.MyCall<InspectionImageResponse> addInspectionImages(@Part("index") RequestBody index, @Part("booking") RequestBody bookingId,@Part MultipartBody.Part imageFile);

    /*ADD APPROVAL IMAGES*/
    @Multipart
    @PUT(ApiURL.ADD_APPROVAL_IMAGES)
    ApiCallback.MyCall<InspectionImageResponse> addApprovalImages(@Part("index") RequestBody index, @Part("booking") RequestBody bookingId,@Part MultipartBody.Part imageFile);

    /*ADD ADDITIONAL IMAGES*/
    @Multipart
    @PUT(ApiURL.ADD_ADDITIONAL_IMAGES)
    ApiCallback.MyCall<InspectionImageResponse> addAdditionalImages(@Part("index") RequestBody index, @Part("booking") RequestBody bookingId,@Part MultipartBody.Part imageFile);

    /*CAR PARTICULARS*/
    @GET(ApiURL.GET_PARTICULARS)
    ApiCallback.MyCall<GetParticularsResponse> getCarParticulars(@Query("booking") String bookingId);

    /*UPDATE PARTICULARS*/
    @PUT(ApiURL.UPDATE_PARTICULARS)
    ApiCallback.MyCall<GetParticularsResponse> updateParticulars(@Body UpdateRequest updateRequest);

    /*GET TECHNICIANS*/
    @GET(ApiURL.GET_TECHNICIANS)
    ApiCallback.MyCall<GetTechniciansResponse> getTechnicians();

    /*GET DRIVER*/
    @GET(ApiURL.GET_DRIVER)
    ApiCallback.MyCall<GetDriverResponse> getDriver(@Query("query") String query, @Query("by") String by, @Query("type") String type);

    /*SEND REQUIREMENTS*/
    @PUT(ApiURL.PUT_REQUIREMENTS)
    ApiCallback.MyCall<BaseResponse> putRequirements(@Body RequirementRequest requirementRequest);

    /*ADD CLAIM*/
    @PUT(ApiURL.ADD_CLAIM)
    ApiCallback.MyCall<BaseResponse> addClaim(@Body RequirementRequest requirementRequest);

    /*GET COMPANY NAME*/
    @GET(ApiURL.GET_COMPANY_NAME)
    ApiCallback.MyCall<GetCompanyResponse> getCompanyName();

    /*GET BOOKING CAR*/
    @GET(ApiURL.GET_BOOKING_CAR)
    ApiCallback.MyCall<GetUserCarResponse> getBookingCar(@Query("booking") String bookingId);

    /*ESXISTING BOOKING*/
    @POST(ApiURL.POST_EXISTING_BOOKING)
    ApiCallback.MyCall<GetUserBookingResponse> getExistingBookingResponse(@Body JobCardBookingRequest jobCardBookingRequest);

    /*GET USER SERVICES*/
    @GET(ApiURL.GET_BOOKED_SERVICES)
    ApiCallback.MyCall<GetBookedServicesResponse> getBookedServices(@Query("booking") String bookingId);

    /*GET JOBS*/
    @GET(ApiURL.JOBS_LIST)
    ApiCallback.MyCall<JobsResponse> jobsList(@Query("status") String query, @Query("page") int page);

    /*UPDATE DELIVERY DETAILS*/
    @PUT(ApiURL.UPDATE_DELIVERY_DETAILS)
    ApiCallback.MyCall<BaseResponse> updateDeliveryDetails(@Body UpdateDeliveryDetailsRequest updateDeliveryDetailsRequest);

    /*GET ORDERS RESPONSE*/
    @GET(ApiURL.ORDERS_LIST)
    ApiCallback.MyCall<OrdersResponse> getOrdersResponse(@Query("page") int page);

    /*GET ORDERS DETAILS*/
    @GET(ApiURL.ORDER_DETAILS)
    ApiCallback.MyCall<OrderDetailsResponse> getOrdersDetailsResponse(@Query("order") String orderId);

    /*GET BOOKING DETAILS*/
    @GET(ApiURL.BOOKING_DETAILS)
    ApiCallback.MyCall<BookingDetailsResponse> getBookingDetailsResponse(@Query("booking") String bookingId, @Query("by") String id);

    /*GET ANY DATA*/
    @GET(ApiURL.ANY_DETAILS)
    ApiCallback.MyCall<MultipleDataResponse> getSearchData(@Query("query") String detail,@Query("type") String type);

    /*GET ADDRESS DETAILS*/
    @GET(ApiURL.ADDRESS_DETAILS)
    ApiCallback.MyCall<BookingAddressResponse> getBookingAddress(@Query("booking") String bookingId);

    /*UPDATE ACCIDENT DETAILS*/
    @PUT(ApiURL.ACCIDENT_UPDATE)
    ApiCallback.MyCall<BaseResponse>updateAccidentDetails(@Body InsuranceAccidentUpdateRequest request);

    /* GET BOOKING ADDRESS */
    @GET(ApiURL.GET_BOOKING_ADDRESS)
    ApiCallback.MyCall<ScheduleBookingAddressResponse> getCustomerAddress();

    /* ADD BOOKING ADDRESS */
    @POST(ApiURL.ADD_BOOKING_ADDRESS)
    ApiCallback.MyCall<AddBookingAddressResponse> addBookingAddress(@Body BookingAddressRequest bookingAddressRequest);

    /* GET BOOKING ADDRESS BY POSTAL */
    @GET(ApiURL.BOOKING_ADDRESS_BY_POSTAL)
    ApiCallback.MyCall<AddBookingAddressResponse> getBookingAddressCity(@Query("zip") String zip);

    /*POST ADDRESS DATA*/
    @POST(ApiURL.ADD_ADDRESS)
    ApiCallback.MyCall<AddressResponse> addAddress(@Body AddressRequest addressRequest);

    /*UPDATE ADDRESS*/
    @PUT(ApiURL.ADDRESS_UPDATE)
    ApiCallback.MyCall<BaseResponse> addSelectedAddress(@Body UpdateMemberRequest request);

    /*GET PIN DATA*/
    @GET(ApiURL.GET_PIN_DATA)
    ApiCallback.MyCall<PinResponse> getPinData(@Query("zip") String zip);

    /*GET BUSINESS PIN DATA*/
    @GET(ApiURL.GET_BUSINESS_PIN_DATA)
    ApiCallback.MyCall<BusinessPinResponse> getBusinessPinData(@Query("zip") String zip);

    /*UPDATE USER DETAILS*/
    @PUT(ApiURL.UPDATE_USER)
    ApiCallback.MyCall<EditLeadResponse> updateUser(@Body LeadUserRequest leadUserRequest);

    /*GET PIN DATA*/
    @PUT(ApiURL.BOOKING_REMARK_EDIT)
    ApiCallback.MyCall<BaseResponse> addBookingRemark(@Body BookingUpdateRequest bookingUpdateRequest);

    /*SET SURVEYOR DETAILS*/
    @PUT(ApiURL.SURVEYOR_DETAILS)
    ApiCallback.MyCall<BaseResponse> setSurveyorDetails(@Body SurveyorRequest surveyorRequest);

    /*GET INSPECTION DETAILS*/
    @GET(ApiURL.INSPECTION_IMAGES)
    ApiCallback.MyCall<CarInspectionResponse> getInspectionImages(@Query("booking") String bookingId);

    /*UPDATE JOBS STATUS*/
    @PUT(ApiURL.UPDATE_JOBS_STATUS)
    ApiCallback.MyCall<BaseResponse> updateJobsStatus(@Body UpdateStatusRequest updateStatusRequest);

    /*UPDATE CUSTOMER INFO*/
    @PUT(ApiURL.UPDATE_CUSTOMER_DETAILS)
    ApiCallback.MyCall<BaseResponse> updateCustomerDetails(@Body UpdateCustomerDetailsRequest updateCustomerDetailsRequest);

    /*UPDATE CAR INFO*/
    @PUT(ApiURL.UPDATE_CAR_DETAILS)
    ApiCallback.MyCall<BaseResponse> updateCarDetails(@Body UpdateCarDetailsRequest updateCarDetailsRequest);

    /*PAYMENT MODE*/
    @GET(ApiURL.PAYMENT_MODE)
    ApiCallback.MyCall<PaymentModeResponse> getPayment();

    /*PAYMENT RECEIVED*/
    @POST(ApiURL.PAYMENT_RECEIVED)
    ApiCallback.MyCall<BaseResponse> receivePayment(@Body JobCardPaymentRequest jobCardPaymentRequest);

    /*JOBCARD QC*/
    @GET(ApiURL.JOBCARD_QC)
    ApiCallback.MyCall<JobsQCResponse> getQCquestions(@Query("booking") String bookingId);

    /*PUT JOBCARD QC*/
    @PUT(ApiURL.PUT_JOBCARD_QC)
    ApiCallback.MyCall<BaseResponse> putQCquestions(@Body PointsRequest pointsRequest);

    /*PUT INSURANCE DATA*/
    @PUT(ApiURL.INSURANCE_UPDATE)
    ApiCallback.MyCall<BaseResponse> updateInsuranceData(@Body InsuranceUpdateRequest insuranceUpdateRequest);

    /*PUT INSURANCE DATA*/
    @PUT(ApiURL.TECHNICIAN_UPDATE)
    ApiCallback.MyCall<BaseResponse> updateTechnician(@Body UpdateMemberRequest updateMemberRequest);

    /*PUT INSURANCE DATA*/
    @PUT(ApiURL.ADVISOR_UPDATE)
    ApiCallback.MyCall<BaseResponse> updateAdvisor(@Body UpdateMemberRequest updateMemberRequest);

    /*PUT ASSETS DATA*/
    @PUT(ApiURL.ASSETS_UPDATE)
    ApiCallback.MyCall<BaseResponse> updateAssets(@Body UpdateAssetsRequest assetsRequest);

    @GET(ApiURL.PAYMENT_LOG)
    ApiCallback.MyCall<PaymentLogResponse> getPaymentLog(@Query("booking") String bookingId);

    /* ADVISORS LIST */
    @GET(ApiURL.ADVISOR_LIST)
    ApiCallback.MyCall<AdvisorResponse> getAdvisorsList();

    /* CATEGORY LIST */
    @GET(ApiURL.CATEGORY_LIST)
    ApiCallback.MyCall<CategoryResponse> getLeadCategory();

    /* USER CARS BY LEAD */
    @GET(ApiURL.USER_CARS_LEAD)
    ApiCallback.MyCall<LeadCarsResponse> getCarsLead(@Query("lead") String leadId);

    @Multipart
    @PUT(ApiURL.UPLOAD_RC_IMG)
    ApiCallback.MyCall<AddImageResponse> postRCImage(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    @Multipart
    @PUT(ApiURL.UPLOAD_INSURANCE_IMG)
    ApiCallback.MyCall<AddImageResponse> postInsuranceImage(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id);

    @Multipart
    @POST(ApiURL.UPLOAD_CAR_DOCUMENT)
    ApiCallback.MyCall<DocumentResponse> postCarDocument(@Part MultipartBody.Part imageFile, @Part("id") RequestBody id, @Part("caption") RequestBody caption);

    /* CAR DOCUMENTS */
    @GET(ApiURL.CAR_DOCUMENTS)
    ApiCallback.MyCall<CarDocumentResponse> getCarDocuments(@Query("car") String carId);

    /*REMOVE DOCUMENT*/
    @HTTP(method = "DELETE", path =ApiURL.DELETE_CAR_DOCUMENT, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeDocument(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*REMOVE IC IMAGES*/
    @HTTP(method = "DELETE", path =ApiURL.DELETE_RC_IMG, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeRcPic(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Remove INSURANCE IMAGES*/
    @HTTP(method = "DELETE", path =ApiURL.DELETE_INSURANCE_IMG, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeInsurancePic(@Body PublishUnpublishRequest publishUnpublishRequest);

    /*Remove Car*/
    @HTTP(method = "DELETE", path =ApiURL.REMOVE_CAR, hasBody = true)
    ApiCallback.MyCall<BaseResponse> removeCar(@Body AddCarRequest addCarRequest);

    /* OutLets */
    @GET(ApiURL.OUTLETS)
    ApiCallback.MyCall<ShowroomProfileResponse> getOutlets(@Query("page") int page);

    /* REQUEST CLAIM */
    @POST(ApiURL.HOME_GET_PACKAGES)
    ApiCallback.MyCall<BookedPackageResponse> getPackages(@Body AddBookingRequest addBookingRequest);

    @GET(ApiURL.BOOKING_SERVICES_GALLERY)
    ApiCallback.MyCall<ServiceGalleryResponse> getVendorServicesGallery(@Query("id") String id, @Query("type") String type);

    /* VENDOR SERVICES DESCRIPTION */
    @GET(ApiURL.BOOKING_SERVICES_DESCRIPTION)
    ApiCallback.MyCall<BaseResponse> getVendorServicesDetails(@Query("id") String id,@Query("type") String type);

    /* ADD BOOKINGS */
    @POST(ApiURL.ADD_BOOKING)
    ApiCallback.MyCall<AddBookingResponse> addCarBooking(@Body AddBookingRequest addBookingRequest);

    /* BOOKINGS */
    @GET(ApiURL.My_BOOKINGS)
    ApiCallback.MyCall<BookingsResponse> getMyBookings(@Query("page") int page);

    /* GET SLOTS */
    @POST(ApiURL.My_BOOKING_TIMESLOT)
    ApiCallback.MyCall<BookingSlotResponse> getMySlots(@Body GetTimeSlotRequest getTimeSlotRequest);

    /* GET SLOTS */
    @GET(ApiURL.My_BOOKING_CONVENIENCE)
    ApiCallback.MyCall<BookingConvenienceResponse> getBookingConvenience(@Query("business") String business);

    /* GET BOOKING DETAILS */
    @GET(ApiURL.BOOKING__SERVICE_DETAILS)
    ApiCallback.MyCall<BookingEstimateResponse> getBookingDetail(@Query("booking") String bookingId, @Query("by") String by);

    /* APPROVE SERVICE */
    @POST(ApiURL.BOOKING_SERVICES_APPROVED)
    ApiCallback.MyCall<BaseResponse> approveServices(@Body ServiceApproveRequest serviceApproveRequest);

    /* PAYMENT SUCCESS */
    @POST(ApiURL.BOOKING_PAY_LATER)
    ApiCallback.MyCall<AddBookingResponse> bookingPayLater(@Body PaymentDetailRequest paymentDetailRequest);

    /* GET DISCOUNT DETAILS */
    @POST(ApiURL.BOOKING_DISCOUNT_APPLY)
    ApiCallback.MyCall<PaymentDetailResponse> getDiscountDetail(@Body PaymentDetailRequest paymentDetailRequest);


    /* GET COINS DATA*/
    @GET(ApiURL.COIN_DETAILS)
    ApiCallback.MyCall<CarEagerCoinsResponse> getCoinsData();

    /*GET MY REFERRALS*/
    @GET(ApiURL.MY_REFERRALS)
    ApiCallback.MyCall<UserSearchResponse> getReferrals();

    /* GET  CHECKSUM */
    @GET(ApiURL.BOOKING_CHECKSUM)
    ApiCallback.MyCall<ChecksumResponse> getCheckSum(@Query("sell") String id);

    /* PAYMENT RECHECK */
    @GET(ApiURL.PAYMENT_RECHECK)
    ApiCallback.MyCall<PaymentRecheckResponse> paymentRecheck(@Query("id") String id);

    /* Buyer Details */
    @POST(ApiURL.CAR_SOLD)
    ApiCallback.MyCall<BaseResponse> markCarSold(@Body CarSoldRequest carSoldRequest);

    /* Buyer OTP verification */
    @POST(ApiURL.CAR_SOLD_OTP)
    ApiCallback.MyCall<CarSoldResponse> markCarSoldOTP(@Body CarSoldRequest carSoldRequest);

    @POST(ApiURL.CAR_LEAD)
    ApiCallback.MyCall<CarLeadResponse> carLead(@Body CarLeadRequest carLeadRequest);

    /* USER CAR LIST*/
    @GET(ApiURL.USED_CAR)
    ApiCallback.MyCall<OldCarResponse> getUsedCar(@Query("fuel") String fuel, @Query("transmission") String gear, @Query("body") String body, @Query("color") String color, @Query("min") String min, @Query("max") String max, @Query("model") String model, @Query("brand") String brand, @Query("range") String range, @Query("latitude") Double latitude, @Query("longitude") Double longitude, @Query("postedBy") String postedBy, @Query("page") int page);

    /* INSURANCE DUE */
    @GET(ApiURL.INSURANCE_DUE)
    ApiCallback.MyCall<InsuranceDueResponse> getInsuranceDue(@Query("page") int page);

    /* SERVICE DUE */
    @GET(ApiURL.SERVICE_DUE)
    ApiCallback.MyCall<ServiceDueResponse> getService(@Query("page") int page);
}
