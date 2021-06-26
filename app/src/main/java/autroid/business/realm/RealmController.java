package autroid.business.realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;

import autroid.business.model.realm.CarStockRealm;
import autroid.business.model.realm.EstimateTaxCalculationRealm;
import autroid.business.model.realm.InsuranceDueRealm;
import autroid.business.model.realm.LeadBookingRealm;
import autroid.business.model.realm.BookingCategoryRealm;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.realm.JobsRealm;
import autroid.business.model.realm.LeadsAssignedRealm;
import autroid.business.model.realm.LeadsRealm;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.realm.OffersRealm;
import autroid.business.model.realm.OrdersRealm;
import autroid.business.model.realm.ProductRealm;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.realm.ServiceDueRealm;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.model.realm.UserRealm;
import autroid.business.utils.Constant;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from ShowroomRealm.class
    public void clearShowroom() {

        realm.beginTransaction();
        realm.delete(ShowroomRealm.class);

        realm.commitTransaction();
    }

    public void addDataToShowroom(ShowroomRealm showroomRealm) {

        realm.beginTransaction();
        realm.copyToRealm(showroomRealm);
        realm.commitTransaction();
    }

    public void updateShowroomLocation(Double latitude, Double longitude, String location) {

        ShowroomRealm toEdited = realm.where(ShowroomRealm.class).findFirst();
        realm.beginTransaction();
        toEdited.setLatitude(latitude);
        toEdited.setLongitude(longitude);
        toEdited.setLocation(location);
        realm.commitTransaction();
    }

    public void addShowroomImage(String id, String imageId, String imagePath) {

        ShowroomRealm toEdited = realm.where(ShowroomRealm.class).findFirst();
        RealmResults<MediaRealm> media = toEdited.getMedia().where().findAll();
        RealmList<MediaRealm> mediaRealms = toEdited.getMedia();
        realm.beginTransaction();
        MediaRealm mediaRealm = new MediaRealm();
        mediaRealm.setPath(imagePath);
        mediaRealm.setId(imageId);
        mediaRealms.add(mediaRealm);
        realm.commitTransaction();

    }

    public void updateShowroomCover(String cover) {

        ShowroomRealm toEdited = realm.where(ShowroomRealm.class).findFirst();
        realm.beginTransaction();
        toEdited.setCover(cover);
        realm.commitTransaction();
    }

    public ShowroomRealm getShowroomData() {

        return realm.where(ShowroomRealm.class).findFirst();
    }

    public void deleteShowRoomImage(String id, String imageId) {

        RealmResults<ShowroomRealm> toEdited = realm.where(ShowroomRealm.class).equalTo("businessId", id).findAll();

        MediaRealm mediaRealm = toEdited.get(0).getMedia().where().equalTo("id", imageId).findFirst();

        if (mediaRealm != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            mediaRealm.deleteFromRealm();

            realm.commitTransaction();
        }
    }

    public UserRealm checkUser(String id) {

        return realm.where(UserRealm.class).equalTo("id", id).findFirst();
    }

    public UserRealm getUserId(String contactno) {

        return realm.where(UserRealm.class).equalTo("contactNo", contactno).findFirst();
    }

    public void updateRecordUser(String id, UserRealm userRealm) {

        UserRealm toEdited = realm.where(UserRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setCover(userRealm.getCover());
        realm.commitTransaction();

    }

    public void updateRecordUserLoggidIn(String id) {

        UserRealm toEdit = realm.where(UserRealm.class)
                .equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdit.setLoggedIn(Boolean.TRUE);
        realm.commitTransaction();

    }

    public void updateRecordUserNotLoggidIn(String id) {

        RealmResults<UserRealm> toEdit = realm.where(UserRealm.class)
                .notEqualTo("id", id).findAll();
        realm.beginTransaction();

        for (UserRealm order : toEdit) {
            order.setLoggedIn(Boolean.FALSE);
        }


        realm.commitTransaction();

    }


    //find all objects in the QuestionRealm.class
    public RealmResults<UserRealm> getAllUsers() {

        return realm.where(UserRealm.class).findAll();
    }


    //find all objects in the QuestionRealm.class
    public RealmResults<UserRealm> getAllNotLoggedInUsers() {

        return realm.where(UserRealm.class).equalTo("isLoggedIn", Boolean.FALSE).findAll();
    }

    public UserRealm getAllLoggedInUsers() {

        return realm.where(UserRealm.class).equalTo("isLoggedIn", Boolean.TRUE).findFirst();
    }

    public void deleteLoggedInUsers() {

        UserRealm userRealm = realm.where(UserRealm.class).equalTo("isLoggedIn", Boolean.TRUE).findFirst();
        try {
            if (null != userRealm) {
                realm.beginTransaction();
                userRealm.deleteFromRealm();
                realm.commitTransaction();
            }
        } catch (Exception e) {

        }
    }

    public void deleteUsersById(String id) {

        UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        userRealm.deleteFromRealm();
        realm.commitTransaction();
    }


    //find all objects in the BookingRealm.class
    public RealmResults<BookingRealm> getBookings(String stage) {
        return realm.where(BookingRealm.class).equalTo("primaryStatus", stage).findAll().sort("updatedAt", Sort.DESCENDING);
    }

    public RealmResults<BookingRealm> getAllBookings() {
        return realm.where(BookingRealm.class).findAll().sort("updatedAt", Sort.DESCENDING);
    }

    //find all objects in the BookingRealm.class
    public RealmResults<BookingRealm> getBookings(String status, String id) {

        return realm.where(BookingRealm.class).equalTo("status", status).findAll();
    }

    public RealmList<SelectedBookingDataRealm> getBookingsById(String id) {

        BookingRealm bookingRealm = realm.where(BookingRealm.class).equalTo("bookingId", id).findFirst();
        RealmList<SelectedBookingDataRealm> selectedBookingDataRealms = bookingRealm.getSelectedBookingDataRealms();
        return selectedBookingDataRealms;
    }


    public void updateBookingStatus(String id, String status) {
        BookingRealm toEdited = realm.where(BookingRealm.class).equalTo("bookingId", id).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setPrimaryStatus(status);
        toEdited.setStatus(status);
        realm.commitTransaction();
    }

    public void updateLeadBookingStatus(String id, String status) {
        LeadBookingRealm toEdited = realm.where(LeadBookingRealm.class).equalTo("id", id).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setStatus(status);
        realm.commitTransaction();
    }

    public void rejectBooking(String bookingId) {
        LeadBookingRealm toEdited = realm.where(LeadBookingRealm.class).equalTo("id", bookingId).findFirst();
        if (!realm.isInTransaction())
            realm.beginTransaction();

        toEdited.deleteFromRealm();
        realm.commitTransaction();
    }

    //clear all objects from BookingRealm.class
    public void clearBookings() {
        realm.beginTransaction();
        realm.delete(BookingRealm.class);
        realm.commitTransaction();
    }


    public void clearBookingsByStage(String stage) {
        if (!realm.isInTransaction()) realm.beginTransaction();
        RealmResults<BookingRealm> toEdited = realm.where(BookingRealm.class).equalTo("primaryStatus", stage).findAll();
        toEdited.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /*                  OFFERS                  */

    public RealmResults<OffersRealm> getOffers() {
        return realm.where(OffersRealm.class).findAll();
    }

    public void clearOffers() {
        realm.beginTransaction();
        realm.delete(OffersRealm.class);
        realm.commitTransaction();

    }

    public void updateOfferPublishStatus(String id, Boolean isPublish) {
        OffersRealm toEdited = realm.where(OffersRealm.class).equalTo("offerId", id).findFirst();
        realm.beginTransaction();
        toEdited.setPublish(isPublish);
        realm.commitTransaction();

    }

    public void updateOffer(String id, OffersRealm offersRealm) {
        OffersRealm toEdited = realm.where(OffersRealm.class).equalTo("offerId", id).findFirst();
        realm.beginTransaction();
        toEdited.setTitle(offersRealm.getTitle());
        toEdited.setDescription(offersRealm.getDescription());
        toEdited.setValidity(offersRealm.getValidity());
        realm.commitTransaction();

    }

    public void updateOfferImage(String id, OffersRealm offersRealm) {
        OffersRealm toEdited = realm.where(OffersRealm.class).equalTo("offerId", id).findFirst();
        realm.beginTransaction();
        toEdited.setOfferImg(offersRealm.getOfferImg());
        realm.commitTransaction();

    }

    public OffersRealm getOffer(String id) {
        return realm.where(OffersRealm.class).equalTo("offerId", id).findFirst();
    }



    /*          PRODUCTS            */

    public RealmResults<ProductRealm> getProducts() {
        return realm.where(ProductRealm.class).findAll();
    }

    public void clearProducts() {
        realm.beginTransaction();
        realm.delete(ProductRealm.class);
        realm.commitTransaction();

    }

    public ProductRealm getProduct(String id) {
        return realm.where(ProductRealm.class).equalTo("id", id).findFirst();
    }

    public void updateProductPublishStatus(String id, Boolean isPublish) {
        ProductRealm toEdited = realm.where(ProductRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setPublish(isPublish);
        realm.commitTransaction();

    }

    public void updateProduct(String id, ProductRealm productRealm) {
        ProductRealm toEdited = realm.where(ProductRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setTitle(productRealm.getTitle());
        toEdited.setDescription(productRealm.getDescription());
        toEdited.setPrice(productRealm.getPrice());
        toEdited.setSpecification(productRealm.getSpecification());
        toEdited.setModel_no(productRealm.getModel_no());
        toEdited.setBrand(productRealm.getBrand());
        realm.commitTransaction();
    }

    public void deleteProductImage(String id, String imageId) {

        RealmResults<ProductRealm> toEdited = realm.where(ProductRealm.class).equalTo("id", id).findAll();

        MediaRealm mediaRealm = toEdited.get(0).getMedia().where().equalTo("id", imageId).findFirst();

        if (mediaRealm != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            mediaRealm.deleteFromRealm();

            realm.commitTransaction();
        }
    }

    public void addProductImage(String id, String imageId, String imagePath) {

        ProductRealm toEdited = realm.where(ProductRealm.class).equalTo("id", id).findFirst();
        RealmResults<MediaRealm> media = toEdited.getMedia().where().findAll();

        RealmList<MediaRealm> mediaRealms = toEdited.getMedia();
        realm.beginTransaction();
        MediaRealm mediaRealm = new MediaRealm();
        mediaRealm.setPath(imagePath);
        mediaRealm.setId(imageId);
        mediaRealms.add(mediaRealm);
        realm.commitTransaction();

    }


    /*              CARS                */

    public RealmResults<CarRealm> getCars() {
        return realm.where(CarRealm.class).findAll();
    }

    public void clearCars() {
        realm.beginTransaction();
        realm.delete(CarRealm.class);
        realm.commitTransaction();

    }

    public void updateCarsStatus(String id, boolean publish, boolean approved) {
        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).findFirst();
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            toEdited.setPublish(publish);
            toEdited.setAdminApproved(approved);
            realm.commitTransaction();
        }
    }

    public void removeCar(String id) {

        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).findFirst();

        if (toEdited != null && !realm.isInTransaction()) {
            realm.beginTransaction();
            toEdited.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public CarRealm getCar(String id) {
        return realm.where(CarRealm.class).equalTo("id", id).findFirst();
    }

    public void deleteCarImage(String id, String imageId) {

        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).findFirst();

        MediaRealm mediaRealm = toEdited.getMedia().where().equalTo("id", imageId).findFirst();

        if (mediaRealm != null && !realm.isInTransaction()) {
            realm.beginTransaction();
            mediaRealm.deleteFromRealm();
            realm.commitTransaction();
        }
    }

//    public void updateCar(String id, AddCarResponse addCarResponse, String type){
//        CarStockRealm carRealm = realm.where(CarStockRealm.class).equalTo("id", id).equalTo("internalType",type).findFirst();
//        realm.beginTransaction();
//
//        carRealm.setCreatedDate(addCarResponse.getGetCarResponse().getItem().getCreated_at());
//        carRealm.setFuelType(addCarResponse.getGetCarResponse().getItem().getFuel_type());
//        carRealm.setId(addCarResponse.getGetCarResponse().getItem().getId());
//        carRealm.setKm(addCarResponse.getGetCarResponse().getItem().getDriven());
//        carRealm.setPrice(addCarResponse.getGetCarResponse().getItem().getPrice());
//        carRealm.setNumericPrice(addCarResponse.getGetCarResponse().getItem().getNumericPrice());
//        carRealm.setAccidential(addCarResponse.getGetCarResponse().getItem().getAccidental());
//        carRealm.setColor(addCarResponse.getGetCarResponse().getItem().getVehicle_color());
//        carRealm.setDescription(addCarResponse.getGetCarResponse().getItem().getDescription());
//        carRealm.setInsurance(addCarResponse.getGetCarResponse().getItem().getInsurance());
//        carRealm.setMileage(addCarResponse.getGetCarResponse().getItem().getMileage());
//        carRealm.setOwnership(addCarResponse.getGetCarResponse().getItem().getOwner());
//        carRealm.setRegistrationNo(addCarResponse.getGetCarResponse().getItem().getRegistration_no());
//        carRealm.setServiceRecord(addCarResponse.getGetCarResponse().getItem().getService_history());
//        carRealm.setTransmission(addCarResponse.getGetCarResponse().getItem().getTransmission());
//        carRealm.setMaker(addCarResponse.getGetCarResponse().getItem().getMaker());
//        carRealm.setModel(addCarResponse.getGetCarResponse().getItem().getModel());
//        carRealm.setVariant(addCarResponse.getGetCarResponse().getItem().getVariant());
//        carRealm.setStatus(addCarResponse.getGetCarResponse().getItem().getVehicle_status());
//        carRealm.setLatitude(addCarResponse.getGetCarResponse().getItem().getGeometry().get(0));
//        carRealm.setLongitude(addCarResponse.getGetCarResponse().getItem().getGeometry().get(1));
//        carRealm.setBodyStyle(addCarResponse.getGetCarResponse().getItem().getBody_style());
//        carRealm.setCarId(addCarResponse.getGetCarResponse().getItem().getCarId());
//
//        carRealm.setPublish(addCarResponse.getGetCarResponse().getItem().getPublish());
//
//        carRealm.setPublisherAddress(addCarResponse.getGetCarResponse().getItem().getLocation());
//        carRealm.setPublisherName(addCarResponse.getGetCarResponse().getItem().getUser().getName());
//        carRealm.setTitle(addCarResponse.getGetCarResponse().getItem().getTitle());
//        carRealm.setYear(addCarResponse.getGetCarResponse().getItem().getManufacture_year());
//        realm.commitTransaction();
//    }


    public void addCarImage(String id, String imageId, String imagePath) {

        CarRealm toEdited = realm.where(CarRealm.class).equalTo("id", id).findFirst();
        RealmResults<MediaRealm> media = toEdited.getMedia().where().findAll();

        RealmList<MediaRealm> mediaRealms = toEdited.getMedia();
        realm.beginTransaction();
        MediaRealm mediaRealm = new MediaRealm();
        mediaRealm.setPath(imagePath);
        mediaRealm.setId(imageId);
        mediaRealms.add(mediaRealm);
        realm.commitTransaction();

    }

    public void addGarageCarImage(String id, String imageId, String imagePath) {

        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).findFirst();

        RealmList<MediaRealm> mediaRealms = toEdited.getMedia();

        if (mediaRealms != null && !realm.isInTransaction()) {
            realm.beginTransaction();
            MediaRealm mediaRealm = new MediaRealm();
            mediaRealm.setId(imageId);
            mediaRealm.setPath(imagePath);
            mediaRealms.add(mediaRealm);
            realm.commitTransaction();
        }
    }

    public void updateBookingSchedule(String id, String status, String date, String timeslot) {
        BookingRealm toEdited = realm.where(BookingRealm.class).equalTo("bookingId", id).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setPrimaryStatus(status);
        toEdited.setStatus(status);
        toEdited.setDated(date);
        toEdited.setTimeSlot(timeslot);
        realm.commitTransaction();
    }


    public void updateLeadBookingSchedule(String id, String date, String timeslot) {
        LeadBookingRealm toEdited = realm.where(LeadBookingRealm.class).equalTo("id", id).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setBookingDate(date);
        toEdited.setBookingTime(timeslot);
        realm.commitTransaction();
    }

    public RealmResults<BookingCategoryRealm> getBookingCategory() {

        return realm.where(BookingCategoryRealm.class).findAll();
    }

    public void clearBookingCategory() {

        realm.beginTransaction();
        realm.delete(BookingCategoryRealm.class);
        realm.commitTransaction();

    }

    public RealmResults<SelectedBookingDataRealm> getSelectedBookingData(String tag) {
        return realm.where(SelectedBookingDataRealm.class).equalTo("tag", tag).findAll();
    }


    public RealmResults<LeadsRealm> getLeads(String statusValue) {
        return realm.where(LeadsRealm.class).equalTo("primaryStatus", statusValue).findAll();//.sort("updatedAt", Sort.DESCENDING);
    }

    public RealmResults<OrdersRealm> getOrders() {
        return realm.where(OrdersRealm.class).findAll();//.sort("updatedAt", Sort.DESCENDING);
    }

    public RealmResults<LeadsAssignedRealm> getAssignedLeads() {
        return realm.where(LeadsAssignedRealm.class).equalTo("status", Constant.OPEN).or().equalTo("status", Constant.FOLLOW_UP).findAll().sort("updatedAt", Sort.DESCENDING);
    }

    public void clearSpecificLeads(String statusValue) {
        if (!realm.isInTransaction()) realm.beginTransaction();
        RealmResults<LeadsRealm> results = realm.where(LeadsRealm.class).equalTo("primaryStatus", statusValue).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void clearAssignedLeads(String statusValue) {
        if (!realm.isInTransaction()) realm.beginTransaction();
        RealmResults<LeadsAssignedRealm> results = realm.where(LeadsAssignedRealm.class).equalTo("primaryStatus", statusValue).findAll();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void clearAllLeads() {
        realm.beginTransaction();
        realm.delete(LeadsRealm.class);
        realm.commitTransaction();
    }

    public void clearAssignedLeads() {
        realm.beginTransaction();
        realm.delete(LeadBookingRealm.class);
        realm.commitTransaction();
    }

    public void clearInsuranceDue() {
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            realm.delete(InsuranceDueRealm.class);
            realm.commitTransaction();
        }
    }

    public void clearServiceDue() {
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            realm.delete(ServiceDueRealm.class);
            realm.commitTransaction();
        }
    }

    public LeadsRealm getLeadById(String tag) {
        return realm.where(LeadsRealm.class).equalTo("id", tag).findFirst();
    }

    public LeadsAssignedRealm getAssignedLeadById(String tag) {
        return realm.where(LeadsAssignedRealm.class).equalTo("id", tag).findFirst();
    }

    public InsuranceDueRealm getInsuranceDueById(String id) {
        return realm.where(InsuranceDueRealm.class).equalTo("id", id).findFirst();
    }

    public ServiceDueRealm getServiceDueById(String id) {
        return realm.where(ServiceDueRealm.class).equalTo("id", id).findFirst();
    }

    public void updateAssignedLead(String id, String stage, String status, String remark, String updatedAt, String followUpDate) {
        LeadsAssignedRealm toEdited = realm.where(LeadsAssignedRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setPrimaryStatus(stage);
        toEdited.setStatus(status);
        toEdited.setAssignee_remark(remark);
        toEdited.setUpdatedAt(updatedAt);
        toEdited.setFollow_up_date(followUpDate);
        realm.commitTransaction();
    }

    public void updateLead(String id, String stage, String status, String remark, String updatedAt, String followUpDate) {
        LeadsRealm toEdited = realm.where(LeadsRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setPrimaryStatus(stage);
        toEdited.setStatus(status);
        toEdited.setAssignee_remark(remark);
        toEdited.setUpdatedAt(updatedAt);
        toEdited.setFollow_up_date(followUpDate);
        realm.commitTransaction();
    }

    public void updatePrimaryData(String id, String contact, String email) {
        LeadsRealm toEdited = realm.where(LeadsRealm.class).equalTo("id", id).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setContactNo(contact);
        toEdited.setEmail(email);
        realm.commitTransaction();
    }

    public void updateLeadImp(String id, Boolean isImp) {
        LeadsRealm toEdited = realm.where(LeadsRealm.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        toEdited.setImportant(isImp);
        realm.commitTransaction();

    }

    public RealmResults<LeadBookingRealm> getLeadBookings() {
        return realm.where(LeadBookingRealm.class).notEqualTo("status", "Cancelled").findAll();//.sort("updatedAt", Sort.DESCENDING);
    }

//    public void sort(){
//        realm.where(LeadsRealm.class).findAllSorted()
//    }

    /*JOBS METHODS*/

    public RealmResults<JobsRealm> getJobs(String status) {
        return realm.where(JobsRealm.class).equalTo("primaryStatus", status).findAll();//.sort("updatedAt", Sort.DESCENDING);
    }

    public void deleteJobs(String status) {
        RealmResults<JobsRealm> toEdited = realm.where(JobsRealm.class).equalTo("primaryStatus", status).findAll();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void clearAllJobs() {
        realm.beginTransaction();
        realm.delete(JobsRealm.class);
        realm.commitTransaction();
    }

    public void clearAllOrders() {
        realm.beginTransaction();
        realm.delete(OrdersRealm.class);
        realm.commitTransaction();
    }

    public void updateJobsStatus(String bookingId, String statusForRealm) {
        JobsRealm toEdited = realm.where(JobsRealm.class).equalTo("id", bookingId).findFirst();
        if (!realm.isInTransaction()) realm.beginTransaction();
        toEdited.setPrimaryStatus(statusForRealm);
        realm.commitTransaction();
    }


    public void clearCarStock(final String type) {


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<CarStockRealm> result = realm.where(CarStockRealm.class).equalTo("internalType", type).findAll();
                result.deleteAllFromRealm();
            }
        });
    }

    /*-------------------------------CAR STOCK------------------------------*/

    public RealmResults<CarStockRealm> getCarStock(String type) {
        return realm.where(CarStockRealm.class).equalTo("internalType", type).findAll();//.findAllSorted("createdDate", Sort.DESCENDING);
    }

    public CarStockRealm getCarStockById(String type, String id) {

        return realm.where(CarStockRealm.class).equalTo("internalType", type).equalTo("id", id).findFirst();
    }

//    public void updateCarImages(String carId,RealmList<MediaRealm> carImagesList) {
//
//        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", carId).findFirst();
//
//        if(!realm.isInTransaction()){
//            realm.beginTransaction();
//            toEdited.setMedia(carImagesList);
//            realm.commitTransaction();
//        }
//    }


    public void updateRcAddress(String id, String address, String type) {
        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).equalTo("internalType", type).findFirst();
        realm.beginTransaction();
        toEdited.setRcImg(address);
        realm.commitTransaction();

    }

    public void updateInsuranceAddress(String id, String address, String type) {
        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).equalTo("internalType", type).findFirst();
        realm.beginTransaction();
        toEdited.setInsuranceImg(address);
        realm.commitTransaction();

    }


    public void updateCarsBookmark(String id, Boolean isBookmark, String type) {
        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).equalTo("internalType", type).findFirst();
        realm.beginTransaction();
        toEdited.setBookmarked(isBookmark);
        realm.commitTransaction();

    }

    public void deleteCarImage(String id, String imageId, String type) {

        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).equalTo("internalType", type).findFirst();

        MediaRealm mediaRealm = toEdited.getMedia().where().equalTo("id", imageId).findFirst();

        if (mediaRealm != null) {

            if (!realm.isInTransaction()) {
                realm.beginTransaction();
            }

            mediaRealm.deleteFromRealm();

            realm.commitTransaction();
        }
    }


    public void clearBookingData() {

        realm.beginTransaction();
        realm.delete(SelectedBookingDataRealm.class);
        realm.commitTransaction();

    }

    public RealmResults<BookingCategoryRealm> getBookingSelectedCategory() {

        return realm.where(BookingCategoryRealm.class).equalTo("isSelected", Boolean.TRUE).findAll();
    }

    public RealmResults<SelectedBookingDataRealm> getBookingData() {

        return realm.where(SelectedBookingDataRealm.class).findAll();
    }


    public void updateBookingCategorySelected(String tag, Boolean isSelected, float cost) {

        BookingCategoryRealm toEdit = realm.where(BookingCategoryRealm.class)
                .equalTo("tag", tag).findFirst();
        RealmResults<BookingCategoryRealm> toEditList = realm.where(BookingCategoryRealm.class)
                .notEqualTo("tag", tag).findAll();

        if (null != toEdit) {
            try {
                realm.beginTransaction();
                toEdit.setSelected(isSelected);
                toEdit.setCost(cost);
                realm.commitTransaction();
            } catch (Exception e) {

            }
        }

        for (int i = 0; i < toEditList.size(); i++) {
            realm.beginTransaction();
            toEditList.get(i).setSelected(!isSelected);
            realm.commitTransaction();
        }
    }


    public void removeSelectedData(final String id) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SelectedBookingDataRealm> selectedBookingDataRealm = realm.where(SelectedBookingDataRealm.class)
                        .equalTo("id", id).findAll();
                selectedBookingDataRealm.deleteAllFromRealm();
            }
        });

    }

    public void updateQuantity(String id, int quantity) {

        SelectedBookingDataRealm toEdit = realm.where(SelectedBookingDataRealm.class)
                .equalTo("id", id).findFirst();

        if (null != toEdit) {
            try {
                realm.beginTransaction();
                toEdit.setQuantity(quantity);
                realm.commitTransaction();
            } catch (Exception e) {

            }
        }
    }

    public RealmResults<EstimateTaxCalculationRealm> getGSTInfo(String tax) {

        return realm.where(EstimateTaxCalculationRealm.class).equalTo("tax", tax).findAll();
    }

    public void clearGSTInfo() {

        realm.beginTransaction();
        realm.delete(EstimateTaxCalculationRealm.class);
        realm.commitTransaction();
    }

    public void updatePublishedStatus(String id, boolean publishedStatus) {

        CarStockRealm toEdited = realm.where(CarStockRealm.class).equalTo("id", id).findFirst();

        if (toEdited != null && !realm.isInTransaction()) {
            realm.beginTransaction();
            toEdited.setPublish(publishedStatus);
            realm.commitTransaction();
        }

    }

    public RealmResults<InsuranceDueRealm> getInsuranceDueData() {
        return realm.where(InsuranceDueRealm.class).findAll();
    }

    public RealmResults<ServiceDueRealm> getServiceDue() {
        return realm.where(ServiceDueRealm.class).findAll();
    }

}
