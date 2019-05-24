package ink.teamwork.controller;

import ink.teamwork.entity.Admin;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AdminRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private AdminRepository repository;

    @PostMapping("/list")
    public Result list(int draw, int start, int length){
        Pageable pageable = PageRequest.of(start, length, Sort.Direction.DESC, "id");
        Page<Admin> page = repository.findAll(pageable);
        List<Admin> list = page.getContent();
        if (!list.isEmpty()){
            list.forEach(admin -> {
                admin.setPassword(null);
            });
        }
        return Result.page(draw, page.getTotalElements(), page.getTotalElements(), list);
    }

    @PostMapping("/save")
    public Result save(@RequestParam(required = false) Long id, String username, String password, String name){
        Admin admin = repository.findByUsername(username);
        if (admin != null) {
            return Result.of(false, "用户名已存在");
        }

        admin = Admin.builder()
                .username(username)
                .password(DigestUtils.sha1Hex(password))
                .name(name)
                .status(1)
                .build();
        admin = repository.save(admin);
        return Result.of(admin != null);

    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        Admin admin = repository.findById(id).orElse(null);
        if (admin == null){
            return Result.of(false, "用户不存在");
        }
        Admin sessionUser = (Admin) session.getAttribute("admin");
        if (sessionUser.getId().equals(id)){
            return Result.of(false, "不能删除当前登录用户");
        }
        repository.delete(admin);
        return Result.of(true);
    }

    @PostMapping("/changeStatus/{id}")
    public Result changeStatus(@PathVariable Long id){
        Admin admin = repository.findById(id).orElse(null);
        if (admin == null){
            return Result.of(false, "用户不存在");
        }
        admin.setStatus(Math.abs(admin.getStatus() - 1));
        admin = repository.save(admin);
        return Result.of(admin != null);
    }

    @PostMapping("/password")
    public Result password(){
        return Result.of(true);
    }
}
