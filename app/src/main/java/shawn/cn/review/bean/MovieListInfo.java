package shawn.cn.review.bean;

import java.util.List;

/**
 * Created by Shawn on 2017/7/11.
 */

public class MovieListInfo {


    private boolean sDefault;
    private int count;
    private int start;
    private int total;
    private String title;

    public MovieListInfo() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieListInfo(boolean sDefault, int count, int start, int total, String title) {
        this.sDefault = sDefault;
        this.count = count;
        this.start = start;
        this.total = total;
        this.title = title;
    }

    @Override
    public String toString() {
        return "MovieListInfo{" +
                "sDefault=" + sDefault +
                ", count=" + count +
                ", start=" + start +
                ", total=" + total +
                ", title='" + title + '\'' +
                '}';
    }
}
