package ink.teamwork.controller;

import ink.teamwork.entity.App;
import ink.teamwork.model.Result;
import ink.teamwork.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController extends BaseController {

    @Autowired
    private AppRepository repository;

    @PostMapping("/list")
    public Result list(int draw, int start, int length){
        Pageable pageable = PageRequest.of(start, length, Sort.Direction.DESC, "id");
        Page<App> page = repository.findAll(pageable);
        List<App> list = page.getContent();
        if (!list.isEmpty()){

        }
        return Result.page(draw, page.getTotalElements(), page.getTotalElements(), list);
    }

}
