package com.chencha.citysselectordemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 * Author: chencha
 * Date: 17/11/13
 */

public class RegionInfo {

    private int id;
    private int parent;
    private String name;
    private List<AreasBean> mCitylistList;

    public RegionInfo() {
        super();
    }

    public RegionInfo(int id, int parent, String name) {
        super();
        this.id = id;
        this.parent = parent;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static class AreasBean implements Serializable {

        private String ssqid;
        private String ssqname;
        private String ssqename;


        public String getSsqid() {
            return this.ssqid;
        }

        public void setSsqid(String ssqid) {
            this.ssqid = ssqid;
        }

        public String getSsqname() {
            return this.ssqname;
        }

        public void setSsqname(String ssqname) {
            this.ssqname = ssqname;
        }

        public String getSsqename() {
            return this.ssqename;
        }

        public void setSsqename(String ssqename) {
            this.ssqename = ssqename;
        }
    }


}
