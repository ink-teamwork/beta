package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.User;
import ink.teamwork.model.Result;
import ink.teamwork.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject params){
        log.info("method:[{}], params:[{}]", "login", params);
        String username = params.getString("username");
        String mobile = params.getString("mobile");
        String password = params.getString("password");
        String client = params.getString("client");
        if (StringUtils.isNoneBlank(username, mobile)){
            return Result.of(false, "用户不存在");
        }

        User user;

        if (StringUtils.isNotBlank(username) && User.TYPE_ADMIN.equals(client)){
            user = repository.findByUsernameEquals(username);
        } else if (StringUtils.isNotBlank(mobile) && User.TYPE_USER.equals(client)) {
            user = repository.findByMobileEquals(mobile);
        } else {
            return Result.of(false, "用户不存在");
        }

        if (!DigestUtils.sha1Hex(password).equals(user.getPassword())){
            log.error("登录密码错误:[{}]", username);
            return Result.of(false, "用户名或密码错误");
        }
        if (user.getStatus() != 1){
            log.error("用户被禁用:[{}]", username);
            return Result.of(false, "用户被禁用");
        }
        user.setPassword(null);
        return Result.of(true, "登录成功", user);
    }

}
