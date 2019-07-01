package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import ink.teamwork.entity.User;
import ink.teamwork.model.Result;
import ink.teamwork.repository.UserRepository;
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

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/getUser/{id}")
    public Result getUser(@PathVariable Long id){
        User user = repository.getOne(id);
        return Result.of(user != null, "", user);
    }

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String username = params.getString("username");
        String name = params.getString("name");
        String password = params.getString("password");
        String description = params.getString("description");

        User user = User.builder()
                .password(DigestUtils.sha1Hex(password))
                .name(name)
                .status(1)
                .type(User.TYPE_ADMIN)
                .username(username)
                .description(description)
                .createdTime(new Date())
                .build();
        JSONObject result = new JSONObject();
        User temp = repository.findByUsernameEquals(username);
        if (temp != null) {
            result.put("success", false);
            result.put("msg", "添加失败！用户名已存在");
            return result;
        }
        try {
            repository.save(user);
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
        String username = params.getString("username");
        String name = params.getString("name");
        String password = params.getString("password");
        String description = params.getString("description");
        User user = repository.getOne(id);
        if (StringUtils.isNotBlank(password)){
            user.setPassword(DigestUtils.sha1Hex(password));
        }
        user.setUsername(username);
        user.setName(name);
        user.setDescription(description );
        repository.save(user);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");
        Specification<User> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.or(builder.like(root.get("username"), "%" + keywords + "%"),
                        builder.like(root.get("name"), "%" +keywords + "%"));
                return predicate;
            } else {
                return null;
            }
        };
        Page<User> page = repository.findAll(specification, pageable);
        page.getContent().forEach(user -> user.setPassword(null));
        return Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
    }

    @PostMapping("/changeStatus")
    public JSONObject changeStatus(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        int status = params.getIntValue("status");
        User user = repository.getOne(id);
        user.setStatus(status);
        repository.save(user);
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
