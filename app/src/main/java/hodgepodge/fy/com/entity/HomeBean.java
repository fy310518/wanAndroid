package hodgepodge.fy.com.entity;

import java.io.Serializable;

/**
 * Created by QKun on 2018/1/9.
 */

public class HomeBean implements Serializable {


    /**
     * bname : 3班
     * studentname : 许文俊
     * nname : 四年级
     * zongtidf : 95.8
     * zongtipd : 优秀
     */

    private String bname;
    private String studentname;
    private String nname;
    private String zongtidf;
    private String zongtipd;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getNname() {
        return nname;
    }

    public void setNname(String nname) {
        this.nname = nname;
    }

    public String getZongtidf() {
        return zongtidf;
    }

    public void setZongtidf(String zongtidf) {
        this.zongtidf = zongtidf;
    }

    public String getZongtipd() {
        return zongtipd;
    }

    public void setZongtipd(String zongtipd) {
        this.zongtipd = zongtipd;
    }
}
