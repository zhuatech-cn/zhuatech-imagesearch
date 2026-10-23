/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.imagesearch.practical;

import static cn.zhuatech.imagesearch.practical.PracticalInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class PracticalService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "query", "minimumSimilarity", "topK", "candidates");
    var query = list(r, "query", 1, 256);
    double[] q = new double[query.size()];
    double norm = 0;
    for (int i = 0; i < q.length; i++) {
      q[i] = decimal(Collections.singletonMap("v", query.get(i)), "v", -1e6, 1e6).doubleValue();
      norm += q[i] * q[i];
    }
    if (norm == 0) throw new IllegalArgumentException("查询向量不能为零");
    double minimum = decimal(r, "minimumSimilarity", -1, 1).doubleValue();
    int top = integer(r, "topK", 1, 100);
    Set<String> ids = new HashSet<>();
    List<Map<String, Object>> out = new ArrayList<>();
    List<String> zero = new ArrayList<>();
    for (var c : rows(r, "candidates", 0, 100)) {
      keys(c, "id", "vector");
      String id = id(c, "id");
      unique(ids, id);
      var v = list(c, "vector", q.length, q.length);
      double dot = 0, n = 0;
      for (int i = 0; i < q.length; i++) {
        double x = decimal(Collections.singletonMap("v", v.get(i)), "v", -1e6, 1e6).doubleValue();
        dot += q[i] * x;
        n += x * x;
      }
      if (n == 0) {
        zero.add(id);
        continue;
      }
      double cosine = Math.max(-1, Math.min(1, dot / Math.sqrt(norm * n)));
      if (cosine >= minimum) out.add(Map.of("id", id, "similarity", round(cosine)));
    }
    out.sort(
        Comparator.<Map<String, Object>, BigDecimal>comparing(x -> (BigDecimal) x.get("similarity"))
            .reversed()
            .thenComparing(x -> x.get("id").toString()));
    return Map.of(
        "matches",
        out.subList(0, Math.min(top, out.size())),
        "zeroVectors",
        zero,
        "dimension",
        q.length,
        "embeddingPerformed",
        false);
  }
}
