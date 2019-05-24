package ink.teamwork.controller;

import ink.teamwork.model.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController extends BaseController {

    @PostMapping("/list")
    public Result list(int draw, int start, int length){
        return Result.page(0, 0, 0, null);
    }

    //@PostMapping("/")

}
