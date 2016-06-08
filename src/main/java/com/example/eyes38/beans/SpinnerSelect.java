package com.example.eyes38.beans;

import java.io.Serializable;

/**此类保存spinner选中的位置
 * Created by huangjiechun on 16/6/8.
 */
public class SpinnerSelect implements Serializable {
     private int province_select; //省选择的position
     private int city_select; //市选择的position
     private int area_select; //区选择的position
     private int plot_select; //小区选择的position

    public SpinnerSelect() {
    }

    public SpinnerSelect(int province_select, int city_select, int area_select, int plot_select) {
        this.province_select = province_select;
        this.city_select = city_select;
        this.area_select = area_select;
        this.plot_select = plot_select;
    }

    public int getProvince_select() {
        return province_select;
    }

    public void setProvince_select(int province_select) {
        this.province_select = province_select;
    }

    public int getCity_select() {
        return city_select;
    }

    public void setCity_select(int city_select) {
        this.city_select = city_select;
    }

    public int getArea_select() {
        return area_select;
    }

    public void setArea_select(int area_select) {
        this.area_select = area_select;
    }

    public int getPlot_select() {
        return plot_select;
    }

    public void setPlot_select(int plot_select) {
        this.plot_select = plot_select;
    }
}
