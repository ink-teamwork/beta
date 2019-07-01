package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Channel;
import ink.teamwork.model.Result;
import ink.teamwork.repository.ChannelRepository;
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

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelRepository repository;

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String name = params.getString("name");
        String password = params.getString("password");
        String code = params.getString("code");
        String backgroundUrl = params.getString("backgroundUrl");
        String popularizeUrl = params.getString("popularizeUrl");
        double registerPrice = params.getDoubleValue("registerPrice");
        JSONObject result = new JSONObject();
        Channel channel = Channel.builder()
                .backgroundUrl(backgroundUrl)
                .code(code)
                .name(name)
                .popularizeUrl(popularizeUrl)
                .status(1)
                .password(password)
                .registerPrice(registerPrice)
                .build();
        try {
            repository.save(channel);
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
        String password = params.getString("password");
        String code = params.getString("code");
        String backgroundUrl = params.getString("backgroundUrl");
        String popularizeUrl = params.getString("popularizeUrl");
        double registerPrice = params.getDoubleValue("registerPrice");
        Channel channel = repository.getOne(id);
        if (StringUtils.isNotBlank(password)){
            channel.setPassword(DigestUtils.sha1Hex(password));
        }
        channel.setPassword(password);
        channel.setName(name);
        channel.setCode(code);
        channel.setBackgroundUrl(backgroundUrl);
        channel.setPopularizeUrl(popularizeUrl);
        channel.setRegisterPrice(registerPrice);
        repository.save(channel);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<Channel> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.or(builder.like(root.get("name"), "%" + keywords + "%"),
                        builder.like(root.get("code"), "%" +keywords + "%"));
                return predicate;
            } else {
                return null;
            }
        };
        Page<Channel> page = repository.findAll(specification, pageable);
        page.getContent().forEach(user -> user.setPassword(null));
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/changeStatus")
    public JSONObject changeStatus(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        int status = params.getIntValue("status");
        Channel channel = repository.getOne(id);
        channel.setStatus(status);
        repository.save(channel);
        JSONObject result = new JSONObject();
        result.put("success", true);
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
