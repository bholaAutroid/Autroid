package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.response.BaseResponse;

public class BusinessPlanResponse extends BaseResponse {
     @SerializedName("responseData")
     private GetData getData;

    public GetData getGetData() {
        return getData;
    }

    public void setGetData(GetData getData) {
        this.getData = getData;
    }

    public class GetData{
        private UserBE business;
        private UserBE user;
        private ArrayList<NavigationGroupBE> navigation;
        private Boolean chat;
        private ManifestBE manifest;

        public ManifestBE getManifest() {
            return manifest;
        }

        public void setManifest(ManifestBE manifest) {
            this.manifest = manifest;
        }

        public Boolean getChat() {
            return chat;
        }

        public void setChat(Boolean chat) {
            this.chat = chat;
        }

        public UserBE getBusiness() {
            return business;
        }

        public void setBusiness(UserBE business) {
            this.business = business;
        }

        public UserBE getUser() {
            return user;
        }

        public void setUser(UserBE user) {
            this.user = user;
        }

        public ArrayList<NavigationGroupBE> getNavigation() {
            return navigation;
        }

        public void setNavigation(ArrayList<NavigationGroupBE> navigation) {
            this.navigation = navigation;
        }
    }
}
