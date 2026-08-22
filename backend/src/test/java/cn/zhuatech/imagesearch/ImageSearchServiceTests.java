/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.imagesearch;

import cn.zhuatech.imagesearch.service.ImageSearchService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageSearchServiceTests {
    private final ImageSearchService service = new ImageSearchService();

    @Test void ranksManufacturingAssets() {
        var result = service.search(new ImageSearchService.Request("工厂产线设备工程师", 3, true));
        assertThat(result.matches()).hasSize(3);
        assertThat(result.matches().getFirst().assetId()).isEqualTo("IMG-24018");
        assertThat(result.matches()).allMatch(ImageSearchService.Match::authorized);
    }

    @Test void obeysTopK() {
        assertThat(service.search(new ImageSearchService.Request("办公团队", 2, false)).matches()).hasSize(2);
    }
}
