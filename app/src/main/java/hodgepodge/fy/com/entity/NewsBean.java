package hodgepodge.fy.com.entity;

/**
 * Created by Administrator on 2018/3/6.
 */
public class NewsBean {

    /**
     * ID : 187495
     * Title : 【科普】关于补钙，你问我答！
     * MainImage : http://5b0988e595225.cdn.sohucs.com/c_fill,w_150,h_100,g_faces,q_70/images/20180305/aa4f361f3ca441d9b4a1452d29a7cc25.jpeg
     * Body : （图片来源：网络）长期补钙是否会引起便秘？补钙要长期坚持。大多数人每天补充600mg左右剂量的钙剂，并不会引起便秘。相反的，钙对人体内腺激素的分泌有调节作用，参与维持消化、循环等系统器官的正常生理功能。过量补钙之所以会引起便秘，是因为钙在进入肠道后，遇上碱性环境，容易形成难以吸收的钙盐，抑制肠蠕动，造成顽固性便秘。要想避免这一点，当然不要“过量补钙”，另外，同时补充适量的维生素D，帮助更好地吸收钙，也有好处。对于平时有顽固性便秘的老年人，多食用富含植物纤维的谷类、蔬菜和水果等，都是很重要的预防便秘措施。（图片来源：网络）什么样的钙制剂吸收率最好？各种钙的吸收率差不所多，比较而言，碳酸钙的吸收率相对较高。研究表明，钙的净吸收率中，碳酸钙为36%~42%，乳酸钙、醋酸钙为28%~36%，柠檬酸钙为27%~33%，葡萄酸钙为24%~30%。碳酸钙制剂是当前使用最广泛、含钙量高、效果好的钙补充剂。（图片来源：网络）食补可以替代钙制剂吗？食物中的钙，并不能完全满足日常所需。中国营养调查显示，大多数中国人每日从食物中获得钙约为400mg左右，所以，对一般人群来讲，除了日常饮食，每日建议再额外补充500mg~600mg的钙，还是很有必要的。来源：中国疾控中心慢病中心免责声明：本文注明出处来源及作者，仅代表作者表达内容，不代表康美健康云立场。文章若有侵权，请联系删除。
     * Category :
     * HelpfulCount : 0
     * ReadingQuantity : 220214
     * ReleaseTime : 2018-03-05T10:32:15+08:00
     * LastModifiedTime : 2018-03-05T17:29:42+08:00
     * URL : http://www.jkbat.com/App/NewsDetail/187495
     */

    private int ID;
    private String Title;
    private String MainImage;
    private String Body;
    private String Category;
    private int HelpfulCount;
    private int ReadingQuantity;
    private String ReleaseTime;
    private String LastModifiedTime;
    private String URL;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getMainImage() {
        return MainImage;
    }

    public void setMainImage(String MainImage) {
        this.MainImage = MainImage;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String Body) {
        this.Body = Body;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public int getHelpfulCount() {
        return HelpfulCount;
    }

    public void setHelpfulCount(int HelpfulCount) {
        this.HelpfulCount = HelpfulCount;
    }

    public int getReadingQuantity() {
        return ReadingQuantity;
    }

    public void setReadingQuantity(int ReadingQuantity) {
        this.ReadingQuantity = ReadingQuantity;
    }

    public String getReleaseTime() {
        return ReleaseTime;
    }

    public void setReleaseTime(String ReleaseTime) {
        this.ReleaseTime = ReleaseTime;
    }

    public String getLastModifiedTime() {
        return LastModifiedTime;
    }

    public void setLastModifiedTime(String LastModifiedTime) {
        this.LastModifiedTime = LastModifiedTime;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "ID=" + ID +
                ", Title='" + Title + '\'' +
                ", MainImage='" + MainImage + '\'' +
                ", Body='" + Body + '\'' +
                ", Category='" + Category + '\'' +
                ", HelpfulCount=" + HelpfulCount +
                ", ReadingQuantity=" + ReadingQuantity +
                ", ReleaseTime='" + ReleaseTime + '\'' +
                ", LastModifiedTime='" + LastModifiedTime + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
