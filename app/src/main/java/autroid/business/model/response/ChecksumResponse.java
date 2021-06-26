package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class ChecksumResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetChecksumData get;

    public GetChecksumData getGet() {
        return get;
    }

    public void setGet(GetChecksumData get) {
        this.get = get;
    }

    public class GetChecksumData{
        private String CALLBACK_URL,CHANNEL_ID,CHECKSUMHASH,CUST_ID,EMAIL,INDUSTRY_TYPE_ID,MID,MOBILE_NO,WEBSITE,MERCHANT_PARAM1;
        private String ORDER_ID,ACCESS_CODE,MERCHANT_ID,TXN_AMOUNT,REDIRECT_URL,CANCEL_URL,RSA_KEY_URL,CURRENCY;

        public String getMERCHANT_PARAM1() {
            return MERCHANT_PARAM1;
        }

        public void setMERCHANT_PARAM1(String MERCHANT_PARAM1) {
            this.MERCHANT_PARAM1 = MERCHANT_PARAM1;
        }

        public String getACCESS_CODE() {
            return ACCESS_CODE;
        }

        public void setACCESS_CODE(String ACCESS_CODE) {
            this.ACCESS_CODE = ACCESS_CODE;
        }

        public String getMERCHANT_ID() {
            return MERCHANT_ID;
        }

        public void setMERCHANT_ID(String MERCHANT_ID) {
            this.MERCHANT_ID = MERCHANT_ID;
        }

        public String getREDIRECT_URL() {
            return REDIRECT_URL;
        }

        public void setREDIRECT_URL(String REDIRECT_URL) {
            this.REDIRECT_URL = REDIRECT_URL;
        }

        public String getCANCEL_URL() {
            return CANCEL_URL;
        }

        public void setCANCEL_URL(String CANCEL_URL) {
            this.CANCEL_URL = CANCEL_URL;
        }

        public String getRSA_KEY_URL() {
            return RSA_KEY_URL;
        }

        public void setRSA_KEY_URL(String RSA_KEY_URL) {
            this.RSA_KEY_URL = RSA_KEY_URL;
        }

        public String getCURRENCY() {
            return CURRENCY;
        }

        public void setCURRENCY(String CURRENCY) {
            this.CURRENCY = CURRENCY;
        }

        public String getCALLBACK_URL() {
            return CALLBACK_URL;
        }

        public void setCALLBACK_URL(String CALLBACK_URL) {
            this.CALLBACK_URL = CALLBACK_URL;
        }

        public String getCHANNEL_ID() {
            return CHANNEL_ID;
        }

        public void setCHANNEL_ID(String CHANNEL_ID) {
            this.CHANNEL_ID = CHANNEL_ID;
        }

        public String getCHECKSUMHASH() {
            return CHECKSUMHASH;
        }

        public void setCHECKSUMHASH(String CHECKSUMHASH) {
            this.CHECKSUMHASH = CHECKSUMHASH;
        }

        public String getCUST_ID() {
            return CUST_ID;
        }

        public void setCUST_ID(String CUST_ID) {
            this.CUST_ID = CUST_ID;
        }

        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }

        public String getINDUSTRY_TYPE_ID() {
            return INDUSTRY_TYPE_ID;
        }

        public void setINDUSTRY_TYPE_ID(String INDUSTRY_TYPE_ID) {
            this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
        }

        public String getMID() {
            return MID;
        }

        public void setMID(String MID) {
            this.MID = MID;
        }

        public String getMOBILE_NO() {
            return MOBILE_NO;
        }

        public void setMOBILE_NO(String MOBILE_NO) {
            this.MOBILE_NO = MOBILE_NO;
        }

        public String getORDER_ID() {
            return ORDER_ID;
        }

        public void setORDER_ID(String ORDER_ID) {
            this.ORDER_ID = ORDER_ID;
        }

        public String getTXN_AMOUNT() {
            return TXN_AMOUNT;
        }

        public void setTXN_AMOUNT(String TXN_AMOUNT) {
            this.TXN_AMOUNT = TXN_AMOUNT;
        }

        public String getWEBSITE() {
            return WEBSITE;
        }

        public void setWEBSITE(String WEBSITE) {
            this.WEBSITE = WEBSITE;
        }
    }
}
