package wanandroid.fy.com.main.fragment;

/**
 *
 * Created by Administrator on 2018/4/10.
 */
public class User {

    private String name;

    private int itemType;

    public User(String name, int itemType) {
        this.name = name;
        this.itemType = itemType;
    }

    public User(String name) {
        this(name, 11);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
