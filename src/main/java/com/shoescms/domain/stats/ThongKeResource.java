package com.shoescms.domain.stats;


import com.shoescms.domain.stats.dtos.StatsProductRevenueDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/stats")
public class ThongKeResource {
    private final ThongKeService thongKeService;

    public ThongKeResource(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    @RequestMapping("doanh-thu-theo-thoi-gian")
    public Object doanhThuTheoNgay(@RequestParam String startDate, @RequestParam String endDate){
        return thongKeService.doanhThuTheoNgay(startDate, endDate);
    }

    @RequestMapping("doanh-thu-hom-nay")
    public Object doanhThuHomNay(@RequestParam String startDate, @RequestParam String endDate) {
        return thongKeService.doanhThuHomNay(startDate, endDate);
    }

    @RequestMapping("doanh-thu-tung-san-pham")
    public Page<StatsProductRevenueDto> doanhThuTungSanPham(@RequestParam String startDate, @RequestParam String endDate, Pageable pageable) {
        return thongKeService.doanhThuTungSanPham(startDate, endDate, pageable);
    }
    @RequestMapping("dashboard")
    public Object dashboard(@RequestParam String startDate, @RequestParam String endDate){
        return thongKeService.dashboard(startDate, endDate);
    }
}
