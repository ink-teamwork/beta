package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Label;
import ink.teamwork.entity.User;
import ink.teamwork.model.Result;
import ink.teamwork.repository.LabelRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.Date;

@RestController
@RequestMapping("/label")
public class LabelController extends BaseController {

    @Autowired
    private LabelRepository repository;


    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String name = params.getString("name");
        int sort = params.getIntValue("sort");

        Label label = Label.builder()
                .name(name)
                .sort(sort)
                .build();
        JSONObject result = new JSONObject();
        try {
            repository.save(label);
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
        String name = params.getString("name");
        int sort = params.getIntValue("sort");
        Label label = repository.getOne(id);

        label.setName(name);
        label.setSort(sort );
        repository.save(label);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "sort");
        Page<Label> page = repository.findAll(pageable);
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

}
