package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.TransactionsBE;


/**
 * Created by pranav.mittal on 12/28/17.
 */

public class CarEagerCoinsResponse extends BaseResponse{
    @SerializedName("responseData")
    private GetCoins getCoins;

    public GetCoins getGetCoins() {
        return getCoins;
    }

    public void setGetCoins(GetCoins getCoins) {
        this.getCoins = getCoins;
    }

    public class GetCoins{
        private String total,used,unused,referral_code,total_refferal;
        private ArrayList<GetCoinsData> list;

        public String getReferral_code() {
            return referral_code;
        }

        public void setReferral_code(String referral_code) {
            this.referral_code = referral_code;
        }

        public String getTotal_refferal() {
            return total_refferal;
        }

        public void setTotal_refferal(String total_refferal) {
            this.total_refferal = total_refferal;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getUnused() {
            return unused;
        }

        public void setUnused(String unused) {
            this.unused = unused;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public ArrayList<GetCoinsData> getList() {
            return list;
        }

        public void setList(ArrayList<GetCoinsData> list) {
            this.list = list;
        }
    }

    public class GetCoinsData{
        private String month;
        private ArrayList<TransactionsBE> transaction;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public ArrayList<TransactionsBE> getTransactions() {
            return transaction;
        }

        public void setTransactions(ArrayList<TransactionsBE> transactions) {
            this.transaction = transactions;
        }
    }
}
