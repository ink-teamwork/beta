package ink.teamwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }


    @GetMapping("/loan")
    public String loan(){
        return "/loan/list";
    }

    @GetMapping("/loan/label")
    public String label(){
        return "/loan/label";
    }

    @GetMapping("/loan/detail")
    public String detail(){
        return "/loan/detail";
    }

    @GetMapping("/loan/statistics")
    public String statistics(){
        return "/loan/statistics";
    }

    @GetMapping("/user")
    public String user(){
        return "/user/list";
    }

    @GetMapping("/user/report")
    public String report(){
        return "/user/report";
    }

    @GetMapping("/channel")
    public String channel(){
        return "/channel/list";
    }

    @GetMapping("/channel/detail")
    public String channelDetail(){
        return "/channel/detail";
    }

    @GetMapping("/topic")
    public String topic(){
        return "/topic";
    }

    @GetMapping("/ad/app/nav")
    public String appNav(){
        return "/ad/app_nav";
    }

    @GetMapping("/ad/app/content")
    public String appContent(){
        return "/ad/app_content";
    }

    @GetMapping("/help/type")
    public String helpType(){
        return "/help/type";
    }

    @GetMapping("/help/content")
    public String helpContent(){
        return "/help/content";
    }

    @GetMapping("")
    public String list() {
        return "admin/list";
    }

    @GetMapping("/add")
    public String add() {
        return "/admin/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id) {
        return "/admin/edit";
    }

    @GetMapping("/app")
    public String app(){
        return "/admin/app";
    }

    @GetMapping("/menu")
    public String menu(){
        return "/admin/menu";
    }

    @GetMapping("/password")
    public String password(){
        return "/admin/password";
    }


}
