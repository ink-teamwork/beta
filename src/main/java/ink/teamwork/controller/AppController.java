package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.App;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AppRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController extends BaseController {

    @Autowired
    private AppRepository repository;

    @GetMapping("/list")
    public JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<App> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.like(root.get("params"), "%" + keywords + "%");
                return predicate;
            } else {
                return null;
            }
        };

        Page<App> page = repository.findAll(specification, pageable);
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/add")
    public JSONObject add(@RequestBody JSONObject params) {
        JSONObject result = new JSONObject();
        long id = params.getLongValue("id");
        int sort = params.getIntValue("sort");
        String type = params.getString("type");
        String appParams = params.getString("params");
        JSONObject jsonParams;
        try {
            jsonParams = JSONObject.parseObject(appParams);
            String title = jsonParams.getString("title");
            if (StringUtils.isBlank(title)) {
                result.put("success", false);
                result.put("msg", "参数格式不正确，解析失败");
                return result;
            }
            try {
                App app = App.builder()
                        .status(1)
                        .build();
                app.setParams(appParams);
                app.setTitle(title);
                app.setSort(sort);
                app.setType(type);
                repository.save(app);
                result.put("success", true);
                return result;
            }catch (Exception e){
                result.put("success", false);
                result.put("msg", "添加失败");
                return result;
            }
        }catch (Exception e){
            result.put("success", false);
            result.put("msg", "参数格式不正确，解析失败");
            return result;
        }

    }

    @PostMapping("/update")
    public JSONObject update(@RequestBody JSONObject params) {
        JSONObject result = new JSONObject();
        long id = params.getLongValue("id");
        int sort = params.getIntValue("sort");
        String type = params.getString("type");
        String appParams = params.getString("params");
        JSONObject jsonParams;
        try {
            jsonParams = JSONObject.parseObject(appParams);
            String title = jsonParams.getString("title");
            if (StringUtils.isBlank(title)) {
                result.put("success", false);
                result.put("msg", "参数格式不正确，解析失败");
                return result;
            }
            try {
                App app = repository.getOne(id);
                app.setParams(appParams);
                app.setTitle(title);
                app.setSort(sort);
                app.setType(type);
                repository.save(app);
                result.put("success", true);
                return result;
            }catch (Exception e){
                result.put("success", false);
                result.put("msg", "修改失败");
                return result;
            }
        }catch (Exception e){
            result.put("success", false);
            result.put("msg", "参数格式不正确，解析失败");
            return result;
        }

    }

    @PostMapping("/changeStatus")
    public JSONObject changeStatus(@RequestBody JSONObject params) {
        long id = params.getLongValue("id");
        int status = params.getIntValue("status");
        JSONObject result = new JSONObject();
        try {
            App app = repository.getOne(id);
            app.setStatus(status);
            repository.save(app);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
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

}
