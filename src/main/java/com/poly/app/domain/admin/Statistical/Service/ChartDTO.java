package com.poly.app.domain.admin.Statistical.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartDTO {
    private double choXacNhanPercent;
    private double daXacNhanPercent;
    private double choVanChuyenPercent;
    private double dangVanChuyenPercent;
    private double daThanhToanPercent;
    private double daHoanThanhPercent;
    private double daHuyPercent;

    public ChartDTO(double choXacNhanPercent, double daXacNhanPercent, double choVanChuyenPercent,
                    double dangVanChuyenPercent, double daThanhToanPercent, double daHoanThanhPercent,
                    double daHuyPercent) {
        this.choXacNhanPercent = choXacNhanPercent;
        this.daXacNhanPercent = daXacNhanPercent;
        this.choVanChuyenPercent = choVanChuyenPercent;
        this.dangVanChuyenPercent = dangVanChuyenPercent;
        this.daThanhToanPercent = daThanhToanPercent;
        this.daHoanThanhPercent = daHoanThanhPercent;
        this.daHuyPercent = daHuyPercent;
    }
}
