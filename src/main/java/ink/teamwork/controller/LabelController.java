package ink.teamwork.controller;

import ink.teamwork.entity.Label;
import ink.teamwork.model.Result;
import ink.teamwork.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/label")
public class LabelController extends BaseController {

    @Autowired
    private LabelRepository repository;

    @PostMapping("/list")
    public Result list(int draw, int start, int length) {
        Pageable pageable = PageRequest.of(start, length, Sort.Direction.DESC, "priority");
        Page<Label> page = repository.findAll(pageable);
        return Result.page(draw, page.getTotalElements(), page.getTotalElements(), page.getContent());
    }

    @PostMapping("/save")
    public Result update(@RequestParam(required = false) Long id, String name, @RequestParam(defaultValue = "0") int priority) {
        Label label = Label.builder().build();
        if (id != null) {
            label = repository.findById(id).orElse(null);
            if (label == null) {
                return Result.of(false, "修改数据不存在");
            }
        }
        label.setName(name);
        label.setPriority(priority);
        repository.save(label);
        return Result.of();
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable long id) {
        return Result.of();
    }

}
