package autroid.business.model.bean;

public class ManifestBE {

    private String discount_on;
    private int job_inspection_limit,qc_inspection_limit
            ;
    private Boolean skip_insurance_info,skip_store_approval,skip_qc,gst_invoice;

    public String getDiscount_on() {
        return discount_on;
    }

    public void setDiscount_on(String discount_on) {
        this.discount_on = discount_on;
    }

    public int getJob_inspection_limit() {
        return job_inspection_limit;
    }


    public void setJob_inspection_limit(int job_inspection_limit) {
        this.job_inspection_limit = job_inspection_limit;
    }

    public int getQc_inspection_limit() {
        return qc_inspection_limit;
    }

    public void setQc_inspection_limit(int qc_inspection_limit) {
        this.qc_inspection_limit = qc_inspection_limit;
    }

    public Boolean getSkip_insurance_info() {
        return skip_insurance_info;
    }

    public void setSkip_insurance_info(Boolean skip_insurance_info) {
        this.skip_insurance_info = skip_insurance_info;
    }

    public Boolean getSkip_store_approval() {
        return skip_store_approval;
    }

    public void setSkip_store_approval(Boolean skip_store_approval) {
        this.skip_store_approval = skip_store_approval;
    }

    public Boolean getSkip_qc() {
        return skip_qc;
    }

    public void setSkip_qc(Boolean skip_qc) {
        this.skip_qc = skip_qc;
    }

    public Boolean getGst_invoice() {
        return gst_invoice;
    }

    public void setGst_invoice(Boolean gst_invoice) {
        this.gst_invoice = gst_invoice;
    }
}
