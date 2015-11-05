package com.ledboot.wegirls.bean;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class Girl {

    private String des;
    private String coverUrl;
    private String httpUrl;

    public Girl(String des, String coverUrl, String httpUrl) {
        this.des = des;
        this.coverUrl = coverUrl;
        this.httpUrl = httpUrl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
}
