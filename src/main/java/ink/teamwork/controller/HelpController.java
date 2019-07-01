package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.HelpContent;
import ink.teamwork.entity.HelpType;
import ink.teamwork.entity.User;
import ink.teamwork.model.Result;
import ink.teamwork.repository.HelpContentRepository;
import ink.teamwork.repository.HelpTypeRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/help")
public class HelpController {

    @Autowired
    private HelpTypeRepository typeRepository;

    @Autowired
    private HelpContentRepository contentRepository;

    @PostMapping("/type/add")
    private JSONObject addType(@RequestBody JSONObject params){
        String name = params.getString("name");
        int sort = params.getIntValue("sort");
        String icon = params.getString("icon");

        HelpType type = HelpType.builder()
                .name(name)
                .sort(sort)
                .icon(icon)
                .build();
        JSONObject result = new JSONObject();
        HelpType temp = typeRepository.findByNameEquals(name);
        if (temp != null) {
            result.put("success", false);
            result.put("msg", "添加失败！分类名称已存在");
            return result;
        }
        try {
            typeRepository.save(type);
            result.put("success", true);
            return result;
        } catch (Exception e){
            result.put("success", false);
            result.put("msg", "添加失败");
            return result;
        }

    }

    @PostMapping("/type/update")
    private JSONObject updateType(@RequestBody JSONObject params){
        JSONObject result = new JSONObject();
        long id = params.getLongValue("id");
        String name = params.getString("name");
        int sort = params.getIntValue("sort");
        String icon = params.getString("icon");
        HelpType type = typeRepository.getOne(id);
        type.setName(name);
        type.setSort(sort);
        type.setIcon(icon);
        HelpType temp = typeRepository.findByNameEquals(name);
        if (temp != null && !temp.getId().equals(type.getId())) {
            result.put("success", false);
            result.put("msg", "添加失败！分类名称已存在");
            return result;
        }
        typeRepository.save(type);
        result.put("success", true);
        return result;
    }

    @GetMapping("/type/list")
    private JSONObject listType(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "sort");
        Page<HelpType> page = typeRepository.findAll(pageable);
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/type/delete")
    public JSONObject deleteType(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        JSONObject result = new JSONObject();
        try {
            typeRepository.deleteById(id);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
    }

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String title = params.getString("title");
        long typeId = params.getLongValue("typeId");
        String content = params.getString("content");
        Date releaseTime = params.getDate("releaseTime");
        HelpContent helpContent = HelpContent.builder()
                .title(title)
                .typeId(typeId)
                .content(content)
                .releaseTime(releaseTime)
                .createdTime(new Date())
                .build();
        JSONObject result = new JSONObject();
        try {
            contentRepository.save(helpContent);
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
        String title = params.getString("title");
        long typeId = params.getLongValue("typeId");
        String content = params.getString("content");
        Date releaseTime = params.getDate("releaseTime");
        HelpContent helpContent = contentRepository.getOne(id);

        helpContent.setContent(content);
        helpContent.setReleaseTime(releaseTime);
        helpContent.setTitle(title);
        helpContent.setTypeId(typeId);

        contentRepository.save(helpContent);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("/list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<HelpContent> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.or(builder.like(root.get("title"), "%" + keywords + "%"),
                        builder.like(root.get("content"), "%" +keywords + "%"));
                return predicate;
            } else {
                return null;
            }
        };
        Page<HelpContent> page = contentRepository.findAll(specification, pageable);
        JSONObject result = Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
        result.put("data", typeRepository.findAll());
        return result;
    }

    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        JSONObject result = new JSONObject();
        try {
            contentRepository.deleteById(id);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
    }

    @GetMapping("/type")
    public List list(){
        return typeRepository.findAll();
    }

}
