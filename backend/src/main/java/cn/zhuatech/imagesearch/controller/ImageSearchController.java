/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.imagesearch.controller;

import cn.zhuatech.imagesearch.service.ImageSearchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imagesearch")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class ImageSearchController {
    private final ImageSearchService service;
    public ImageSearchController(ImageSearchService service) { this.service = service; }
    @PostMapping("/search") public ImageSearchService.Result search(@Valid @RequestBody ImageSearchService.Request request) { return service.search(request); }
}
