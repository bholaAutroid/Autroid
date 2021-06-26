package autroid.business.model.bean;

/**
 * Created by pranav.mittal on 06/30/17.
 */

public class TutorialBE {
    private String heading;
    private String lineOne;
    private String lineTwo;
    private int imgId;

    public TutorialBE(String heading,String lineOne,String lineTwo,int imgId){
        this.heading=heading;
        this.lineOne=lineOne;
        this.lineTwo=lineTwo;
        this.imgId=imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLineOne() {
        return lineOne;
    }

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }
}
