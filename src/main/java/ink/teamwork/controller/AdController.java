package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Ad;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AdRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ad")
public class AdController extends BaseController {

    @Autowired
    private AdRepository repository;

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String pic = params.getString("pic");
        int sort = params.getIntValue("sort");
        String adPparams = params.getString("params");
        String title = params.getString("title");
        JSONObject result = new JSONObject();
        Ad ad = Ad.builder()
                .title(title)
                .sort(sort)
                .pic(pic)
                .params(adPparams)
                .build();
        try {
            repository.save(ad);
            result.put("success", true);
            return result;
        } catch (Exception e){
            result.put("success", false);
            result.put("msg", "添加失败");
            return result;
        }

    }

    @PostMapping("/update")
    private JSONObject update(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        String pic = params.getString("pic");
        int sort = params.getIntValue("sort");
        String adPparams = params.getString("params");
        String title = params.getString("title");
        JSONObject result = new JSONObject();
        Ad ad = repository.getOne(id);
        ad.setParams(adPparams);
        ad.setSort(sort);
        ad.setPic(pic);
        ad.setTitle(title);
        repository.save(ad);
        result.put("success", true);
        return result;
    }

    @GetMapping("/list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<Ad> specification = (root, query, builder) -> {
            Predicate predicate = builder.isNull(root.get("type"));
            return predicate;
        };
        Page<Ad> page = repository.findAll(specification, pageable);
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        JSONObject result = new JSONObject();
        try {
            repository.deleteById(id);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
    }

    @PostMapping("/content/add")
    private JSONObject addContent(@RequestBody JSONObject params){
        String title = params.getString("title");
        String type = params.getString("type");
        int sort = params.getIntValue("sort");
        String pic = params.getString("pic");
        String link = params.getString("link");
        String adParams = params.getString("params");
        String summary = params.getString("summary");
        JSONObject result = new JSONObject();
        Ad ad = Ad.builder()
                .link(link)
                .params(adParams)
                .pic(pic)
                .sort(sort)
                .summary(summary)
                .title(title)
                .type(type)
                .build();
        try {
            repository.save(ad);
            result.put("success", true);
            return result;
        } catch (Exception e){
            result.put("success", false);
            result.put("msg", "添加失败");
            return result;
        }

    }

    @PostMapping("/content/update")
    private JSONObject updateContent(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        String title = params.getString("title");
        String type = params.getString("type");
        int sort = params.getIntValue("sort");
        String pic = params.getString("pic");
        String link = params.getString("link");
        String adParams = params.getString("params");
        String summary = params.getString("summary");
        JSONObject result = new JSONObject();
        Ad ad = repository.getOne(id);
        ad.setLink(link);
        ad.setParams(adParams);
        ad.setPic(pic);
        ad.setSort(sort);
        ad.setSummary(summary);
        ad.setTitle(title);
        ad.setType(type);
        repository.save(ad);
        result.put("success", true);
        return result;
    }

    @GetMapping("/content/list")
    private JSONObject listContent(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<Ad> specification = (root, query, builder) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(builder.isNotNull(root.get("type")));
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate =  builder.or(builder.like(root.get("title"), "%" + keywords + "%"),
                        builder.like(root.get("summary"), "%" +keywords + "%"));
                list.add(predicate);

            }
            query.where(list.toArray(new Predicate[list.size()]));
            return null;
        };
        Page<Ad> page = repository.findAll(specification, pageable);
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/content/delete")
    public JSONObject deleteContent(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        JSONObject result = new JSONObject();
        try {
            repository.deleteById(id);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
    }
    
}
