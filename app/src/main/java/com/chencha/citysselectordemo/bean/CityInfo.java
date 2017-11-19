package com.chencha.citysselectordemo.bean;

import java.util.List;

/**
 * Desc:
 * Author: chencha
 * Date: 17/11/13
 */

public class CityInfo {

    private String ssqid;
    private String ssqname;
    private String ssqename;
    private CitiesBean cities;

    public String getSsqid() {
        return ssqid;
    }

    public void setSsqid(String ssqid) {
        this.ssqid = ssqid;
    }

    public String getSsqname() {
        return ssqname;
    }

    public void setSsqname(String ssqname) {
        this.ssqname = ssqname;
    }

    public String getSsqename() {
        return ssqename;
    }

    public void setSsqename(String ssqename) {
        this.ssqename = ssqename;
    }

    public CitiesBean getCities() {
        return cities;
    }

    public void setCities(CitiesBean cities) {
        this.cities = cities;
    }

    public static class CitiesBean {
        private List<CityBean> city;

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }

        public static class CityBean {

            private String ssqid;
            private String ssqname;
            private String ssqename;
            private AreasBean areas;

            public String getSsqid() {
                return ssqid;
            }

            public void setSsqid(String ssqid) {
                this.ssqid = ssqid;
            }

            public String getSsqname() {
                return ssqname;
            }

            public void setSsqname(String ssqname) {
                this.ssqname = ssqname;
            }

            public String getSsqename() {
                return ssqename;
            }

            public void setSsqename(String ssqename) {
                this.ssqename = ssqename;
            }

            public AreasBean getAreas() {
                return areas;
            }

            public void setAreas(AreasBean areas) {
                this.areas = areas;
            }

            public static class AreasBean {
                private List<AreaBean> area;

                public List<AreaBean> getArea() {
                    return area;
                }

                public void setArea(List<AreaBean> area) {
                    this.area = area;
                }

                public static class AreaBean {

                    private String ssqid;
                    private String ssqname;
                    private String ssqename;

                    public String getSsqid() {
                        return ssqid;
                    }

                    public void setSsqid(String ssqid) {
                        this.ssqid = ssqid;
                    }

                    public String getSsqname() {
                        return ssqname;
                    }

                    public void setSsqname(String ssqname) {
                        this.ssqname = ssqname;
                    }

                    public String getSsqename() {
                        return ssqename;
                    }

                    public void setSsqename(String ssqename) {
                        this.ssqename = ssqename;
                    }
                }
            }
        }
    }
}
