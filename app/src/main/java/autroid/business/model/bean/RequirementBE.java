package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class RequirementBE {

    @SerializedName("requirement")
    String text;

    Boolean isFocused=false;

    public Boolean getFocused() {
        return isFocused;
    }

    public void setFocused(Boolean focused) {
        isFocused = focused;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
