package ink.teamwork.controller;

import ink.teamwork.entity.Admin;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    private AdminRepository repository;

    @PostMapping("/login")
    public Result login(String username, String password){
        log.info("username:[{}], password:[{}]", username, password);
        Admin admin = repository.findByUsername(username);
        if (admin == null){
            log.error("用户不存在:[{}]", username);
            return Result.of(false, "账号或密码错误");
        }
        if (!DigestUtils.sha1Hex(password).equals(admin.getPassword())){
            log.error("登录密码错误:[{}]", username);
            return Result.of(false, "用户名或密码错误");
        }
        if (admin.getStatus() != 1){
            log.error("用户被禁用:[{}]", username);
            return Result.of(false, "用户被禁用");
        }
        session.setAttribute("admin", admin);
        return Result.of(true, "登录成功");
    }

    @GetMapping("/logout")
    public void logout() throws IOException {
        session.removeAttribute("admin");
        response.sendRedirect("/admin/login");
    }

}
