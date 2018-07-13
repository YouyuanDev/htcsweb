package com.htcsweb.entity;

public class CoatingPowderInfo {

    private  int id; //流水号
    private String coating_powder_name;    //涂层粉末名称
    private String powder_type;  //原料类型
    private  String manufacturer_name;
    private  String manufacturer_name_en;


    public CoatingPowderInfo() {
    }

    public CoatingPowderInfo(int id, String coating_powder_name, String powder_type, String manufacturer_name, String manufacturer_name_en) {
        this.id = id;
        this.coating_powder_name = coating_powder_name;
        this.powder_type = powder_type;
        this.manufacturer_name = manufacturer_name;
        this.manufacturer_name_en = manufacturer_name_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoating_powder_name() {
        return coating_powder_name;
    }

    public void setCoating_powder_name(String coating_powder_name) {
        this.coating_powder_name = coating_powder_name;
    }

    public String getPowder_type() {
        return powder_type;
    }

    public void setPowder_type(String powder_type) {
        this.powder_type = powder_type;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getManufacturer_name_en() {
        return manufacturer_name_en;
    }

    public void setManufacturer_name_en(String manufacturer_name_en) {
        this.manufacturer_name_en = manufacturer_name_en;
    }
}
