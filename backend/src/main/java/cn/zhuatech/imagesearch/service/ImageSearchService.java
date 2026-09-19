/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.imagesearch.service;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class ImageSearchService {
    private static final List<Asset> CATALOG = List.of(
        new Asset("IMG-24018", "智能制造产线巡检", "制造", "上海工厂", List.of("工厂", "产线", "设备", "工程师"), true),
        new Asset("IMG-23806", "研发团队产品评审", "办公", "产品中心", List.of("会议", "团队", "屏幕", "产品"), true),
        new Asset("IMG-23172", "仓储拣货作业", "物流", "苏州仓", List.of("仓库", "货架", "拣货", "物流"), true),
        new Asset("IMG-22941", "机房设备维护", "运维", "数据中心", List.of("机房", "服务器", "维护", "设备"), false),
        new Asset("IMG-21813", "客户服务中心", "服务", "客服中心", List.of("客服", "坐席", "沟通", "办公"), true)
    );

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result search(Request request) {
        List<Match> matches = new ArrayList<>();
        for (Asset asset : CATALOG) {
            if (request.onlyAuthorized() && !asset.authorized()) continue;
            double score = score(request.query(), asset);
            matches.add(new Match(asset.id(), asset.title(), asset.category(), asset.source(),
                Math.round(score * 1000.0) / 1000.0, explain(request.query(), asset), asset.authorized()));
        }
        List<Match> ranked = matches.stream()
            .sorted(Comparator.comparingDouble(Match::similarity).reversed())
            .limit(request.topK()).toList();
        return new Result("READY", request.query(), ranked.size(), ranked,
            List.of("本地演示使用标签与文本相似度", "生产环境可替换为向量数据库与视觉嵌入", "检索结果保留素材授权状态"),
            "LOCAL_EXPLAINABLE_RANKER");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private double score(String query, Asset asset) {
        String normalized = query.toLowerCase();
        long hits = asset.tags().stream().filter(normalized::contains).count();
        if (normalized.contains(asset.category().toLowerCase())) hits += 2;
        int stable = Math.abs((query + asset.id()).hashCode() % 16);
        return Math.min(.98, .63 + hits * .09 + stable / 100.0);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private String explain(String query, Asset asset) {
        List<String> hitTags = asset.tags().stream().filter(query::contains).toList();
        return hitTags.isEmpty() ? "构图与企业场景接近" : "命中标签：" + String.join("、", hitTags);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank @Size(max = 500) String query,
                          @Min(1) @Max(20) int topK,
                          boolean onlyAuthorized) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Asset(String id, String title, String category, String source, List<String> tags, boolean authorized) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Match(String assetId, String title, String category, String source,
                        double similarity, String explanation, boolean authorized) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String status, String query, int resultCount, List<Match> matches,
                         List<String> notes, String executionMode) {}
}
