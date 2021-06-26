package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class PaymentRecheckResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetPaymentStatus getPaymentStatus;

    public GetPaymentStatus getGetPaymentStatus() {
        return getPaymentStatus;
    }

    public void setGetPaymentStatus(GetPaymentStatus getPaymentStatus) {
        this.getPaymentStatus = getPaymentStatus;
    }

    public class GetPaymentStatus{
        private String TXNID,BANKTXNID,ORDERID,TXNAMOUNT,STATUS,TXNTYPE,GATEWAYNAME,RESPCODE,RESPMSG,BANKNAME,MID,PAYMENTMODE,REFUNDAMT,TXNDATE;

        public String getTXNID() {
            return TXNID;
        }

        public void setTXNID(String TXNID) {
            this.TXNID = TXNID;
        }

        public String getBANKTXNID() {
            return BANKTXNID;
        }

        public void setBANKTXNID(String BANKTXNID) {
            this.BANKTXNID = BANKTXNID;
        }

        public String getORDERID() {
            return ORDERID;
        }

        public void setORDERID(String ORDERID) {
            this.ORDERID = ORDERID;
        }

        public String getTXNAMOUNT() {
            return TXNAMOUNT;
        }

        public void setTXNAMOUNT(String TXNAMOUNT) {
            this.TXNAMOUNT = TXNAMOUNT;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getTXNTYPE() {
            return TXNTYPE;
        }

        public void setTXNTYPE(String TXNTYPE) {
            this.TXNTYPE = TXNTYPE;
        }

        public String getGATEWAYNAME() {
            return GATEWAYNAME;
        }

        public void setGATEWAYNAME(String GATEWAYNAME) {
            this.GATEWAYNAME = GATEWAYNAME;
        }

        public String getRESPCODE() {
            return RESPCODE;
        }

        public void setRESPCODE(String RESPCODE) {
            this.RESPCODE = RESPCODE;
        }

        public String getRESPMSG() {
            return RESPMSG;
        }

        public void setRESPMSG(String RESPMSG) {
            this.RESPMSG = RESPMSG;
        }

        public String getBANKNAME() {
            return BANKNAME;
        }

        public void setBANKNAME(String BANKNAME) {
            this.BANKNAME = BANKNAME;
        }

        public String getMID() {
            return MID;
        }

        public void setMID(String MID) {
            this.MID = MID;
        }

        public String getPAYMENTMODE() {
            return PAYMENTMODE;
        }

        public void setPAYMENTMODE(String PAYMENTMODE) {
            this.PAYMENTMODE = PAYMENTMODE;
        }

        public String getREFUNDAMT() {
            return REFUNDAMT;
        }

        public void setREFUNDAMT(String REFUNDAMT) {
            this.REFUNDAMT = REFUNDAMT;
        }

        public String getTXNDATE() {
            return TXNDATE;
        }

        public void setTXNDATE(String TXNDATE) {
            this.TXNDATE = TXNDATE;
        }
    }
}
