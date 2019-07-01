package ink.teamwork;

import ink.teamwork.entity.User;
import ink.teamwork.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootApplication
public class App {

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class).run(args);
    }

    @PostConstruct
    private void init() {
        List<User> list = repository.findAll();
        if (list.isEmpty()) {
            User user = User.builder()
                    .name("管理员")
                    .username("admin")
                    .password(DigestUtils.sha1Hex("111111"))
                    .status(1)
                    .type(User.TYPE_ADMIN)
                    .createdTime(new Date())
                    .build();
            user = repository.save(user);
            if (user == null){
                log.error("管理员信息初始化失败");
            } else {
                log.info("管理员信息初始化成功");
            }
        }
    }
}
