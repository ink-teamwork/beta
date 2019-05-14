package ink.teamwork.controller;

import ink.teamwork.entity.Admin;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private AdminRepository repository;

    @PostMapping("/login")
    public Result login(String username, String password){
        log.info("username:[{}], password:[{}]", username, password);
        Admin admin = repository.findByUsername(username);
        System.out.println(admin);
        return Result.of(true, "登录成功");
    }

}
