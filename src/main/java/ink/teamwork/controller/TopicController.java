package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Loan;
import ink.teamwork.entity.Topic;
import ink.teamwork.model.Result;
import ink.teamwork.repository.LoanRepository;
import ink.teamwork.repository.TopicRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TopicRepository topicRepository;

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String title = params.getString("title");
        int sort = params.getIntValue("sort");
        String pic = params.getString("pic");
        String bg = params.getString("bg");
        String summary = params.getString("summary");
        String loans = params.getString("loans");

        Topic topic = Topic.builder()
                .title(title)
                .sort(sort)
                .pic(pic)
                .bg(bg)
                .loans(loans)
                .summary(summary)
                .build();
        JSONObject result = new JSONObject();
        try {
            topicRepository.save(topic);
            result.put("success", true);
            return result;
        } catch (Exception e){
            result.put("success", false);
            result.put("msg", "添加失败");
            return result;
        }

    }

    @PostMapping("/update")
    private JSONObject update(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        String title = params.getString("title");
        int sort = params.getIntValue("sort");
        String pic = params.getString("pic");
        String bg = params.getString("bg");
        String summary = params.getString("summary");
        String loans = params.getString("loans");

        Topic topic = topicRepository.getOne(id);

        topic.setBg(bg);
        topic.setPic(pic);
        topic.setSort(sort);
        topic.setSummary(summary);
        topic.setTitle(title);
        topic.setLoans(loans);

        topicRepository.save(topic);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "sort");
        Specification<Topic> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.like(root.get("name"), "%" + keywords + "%");
                return predicate;
            } else {
                return null;
            }
        };
        Page<Topic> page = topicRepository.findAll(specification, pageable);
        JSONObject result =  Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
        result.put("loans", loanRepository.findAll());
        return result;
    }

    @PostMapping("/changeStatus")
    public JSONObject changeStatus(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        int status = params.getIntValue("status");
        Loan loan = loanRepository.getOne(id);
        loan.setStatus(status);
        loanRepository.save(loan);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @PostMapping("/homePageRecommend")
    public JSONObject homePageRecommend(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        int homePageRecommend = params.getIntValue("homePageRecommend");
        Loan loan = loanRepository.getOne(id);
        loan.setHomePageRecommend(homePageRecommend);
        loanRepository.save(loan);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject params){
        long id = params.getLongValue("id");
        JSONObject result = new JSONObject();
        try {
            loanRepository.deleteById(id);
            result.put("success", true);
        }catch (Exception e){
            result.put("success", false);
        }
        return result;
    }


}
