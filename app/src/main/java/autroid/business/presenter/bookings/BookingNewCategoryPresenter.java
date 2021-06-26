package autroid.business.presenter.bookings;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.ServiceRequest;
import autroid.business.model.response.BookedPackageResponse;
import autroid.business.model.response.BookingCategoryResponse;
import autroid.business.model.response.BookingServicesResponse;
import autroid.business.model.response.CarListResponse;
import autroid.business.model.response.ShowroomProfileResponse;
import autroid.business.model.response.VendorServicesResponse;
import autroid.business.view.fragment.carsales.BookingCategoryFragment;
import retrofit2.Response;

public class BookingNewCategoryPresenter {

    private BookingCategoryFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingNewCategoryPresenter(BookingCategoryFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void bookingCategory(String carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingCategoryResponse> myCall = apiRequest.getBookingCategory(carId);
        myCall.enqueue(new ApiCallback.MyCallback<BookingCategoryResponse>() {
            @Override
            public void success(final Response<BookingCategoryResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingCategoryResponse bookingCategoryResponse = response.body();
                            mFragment.onSuccess(bookingCategoryResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getVendors() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomProfileResponse> myCall = apiRequest.getOutlets(0);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomProfileResponse>() {
            @Override
            public void success(final Response<ShowroomProfileResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomProfileResponse searchResponse = response.body();
                            mFragment.onBusinessSuccess(searchResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getCarStock() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarListResponse> myCall = apiRequest.getCarStock(-1);
        myCall.enqueue(new ApiCallback.MyCallback<CarListResponse>() {
            @Override
            public void success(final Response<CarListResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarListResponse carStockResponse = response.body();
                            mFragment.onCarSuccess(carStockResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getPackages(AddBookingRequest addBookingRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookedPackageResponse> myCall = apiRequest.getPackages(addBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookedPackageResponse>() {
            @Override
            public void success(final Response<BookedPackageResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookedPackageResponse baseResponse = response.body();
                            mFragment.onPackageSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
/*

    public void addBooking(AddBookingRequest addBookingRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingResponse> myCall = apiRequest.addBooking(addBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingResponse>() {
            @Override
            public void success(final Response<AddBookingResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
//                            AddBookingResponse loginPostResponse = response.body();
//                            mFragment.onBookingSuccess(loginPostResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
//    }
//*/
//
    public void getNestedServices(ServiceRequest serviceRequest, final String tag) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<VendorServicesResponse> myCall = apiRequest.getVendorServices(serviceRequest);
        myCall.enqueue(new ApiCallback.MyCallback<VendorServicesResponse>() {
            @Override
            public void success(final Response<VendorServicesResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            VendorServicesResponse vendorServicesResponse = response.body();
                            mFragment.onNestedServicesSuccess(vendorServicesResponse,tag);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getServices(ServiceRequest serviceRequest, final String tag) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingServicesResponse> myCall = apiRequest.getDiagnosis(serviceRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingServicesResponse>() {
            @Override
            public void success(final Response<BookingServicesResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingServicesResponse bookingServicesResponse = response.body();
                            mFragment.onServicesSuccess(bookingServicesResponse,tag);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
