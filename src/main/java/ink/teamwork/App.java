package ink.teamwork;

import ink.teamwork.entity.Admin;
import ink.teamwork.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@SpringBootApplication
public class App {

    @Autowired
    private AdminRepository repository;

    public static void main(String[] args) {
        new SpringApplicationBuilder(App.class).run(args);
    }

    @PostConstruct
    private void init() {
        List<Admin> list = repository.findAll();
        if (list.isEmpty()) {
            Admin admin = Admin.builder()
                    .name("管理员")
                    .username("admin")
                    .password(DigestUtils.sha1Hex("111111"))
                    .status(1)
                    .build();
            admin = repository.save(admin);
            if (admin == null){
                log.error("管理员信息初始化失败");
            } else {
                log.info("管理员信息初始化成功");
            }
        }
    }
}
