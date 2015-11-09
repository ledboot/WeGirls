package com.ledboot.wegirls.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class TaoModel {

    private String avatarUrl;
    private String city;
    private String height;
    private String weight;
    private List<String> imgList;
    private String realName;
    private String userId;
    private String link;
    private String totalFanNum;

    public TaoModel() {
    }

    public TaoModel(String avatarUrl, String city, String height, String weight, String realName, String userId, String link, String totalFanNum) {
        this.avatarUrl = avatarUrl;
        this.city = city;
        this.height = height;
        this.weight = weight;
        this.realName = realName;
        this.userId = userId;
        this.link = link;
        this.totalFanNum = totalFanNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTotalFanNum() {
        return totalFanNum;
    }

    public void setTotalFanNum(String totalFanNum) {
        this.totalFanNum = totalFanNum;
    }
}
