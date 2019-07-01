package ink.teamwork.controller;

import com.alibaba.fastjson.JSONObject;
import ink.teamwork.entity.Loan;
import ink.teamwork.model.Result;
import ink.teamwork.repository.LabelRepository;
import ink.teamwork.repository.LoanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController extends BaseController {

    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private LabelRepository labelRepository;

    @PostMapping("/add")
    private JSONObject add(@RequestBody JSONObject params){
        String name = params.getString("name");
        String slogan = params.getString("slogan");
        String loanTime = params.getString("loanTime");
        String trait = params.getString("trait");
        String link = params.getString("link");
        String labels = params.getString("labels");
        double quotaBegin = params.getDoubleValue("quotaBegin");
        double quotaEnd = params.getDoubleValue("quotaEnd");
        double interestRate = params.getDoubleValue("interestRate");
        String periodType = params.getString("periodType");
        int termBegin = params.getIntValue("termBegin");
        int termEnd = params.getIntValue("termEnd");
        int applyCount = params.getIntValue("applyCount");
        double successRate = params.getDoubleValue("successRate");
        String pic = params.getString("pic");
        Date updateTime = params.getDate("updateTime");
        String process = params.getString("process");
        String conditions = params.getString("conditions");
        String note = params.getString("note");
        int sort = params.getIntValue("sort");
        int homePageRecommend = params.getIntValue("homePageRecommend");

        Loan loan = Loan.builder()
                .applyCount(applyCount)
                .conditions(conditions)
                .createdTime(new Date())
                .homePageRecommend(homePageRecommend)
                .interestRate(interestRate)
                .labels(labels)
                .link(link)
                .loanTime(loanTime)
                .name(name)
                .note(note)
                .periodType(periodType)
                .pic(pic)
                .process(process)
                .quotaBegin(quotaBegin)
                .quotaEnd(quotaEnd)
                .slogan(slogan)
                .sort(sort)
                .status(1)
                .successRate(successRate)
                .termBegin(termBegin)
                .termEnd(termEnd)
                .trait(trait)
                .updateTime(updateTime)
                .build();
        JSONObject result = new JSONObject();
        try {
            loanRepository.save(loan);
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
        String name = params.getString("name");
        String slogan = params.getString("slogan");
        String loanTime = params.getString("loanTime");
        String trait = params.getString("trait");
        String link = params.getString("link");
        String labels = params.getString("labels");
        double quotaBegin = params.getDoubleValue("quotaBegin");
        double quotaEnd = params.getDoubleValue("quotaEnd");
        double interestRate = params.getDoubleValue("interestRate");
        String periodType = params.getString("periodType");
        int termBegin = params.getIntValue("termBegin");
        int termEnd = params.getIntValue("termEnd");
        int applyCount = params.getIntValue("applyCount");
        double successRate = params.getDoubleValue("successRate");
        String pic = params.getString("pic");
        Date updateTime = params.getDate("updateTime");
        String process = params.getString("process");
        String conditions = params.getString("conditions");
        String note = params.getString("note");
        int sort = params.getIntValue("sort");
        int homePageRecommend = params.getIntValue("homePageRecommend");

        Loan loan = loanRepository.getOne(id);

        loan.setApplyCount(applyCount);
        loan.setConditions(conditions);
        loan.setHomePageRecommend(homePageRecommend);
        loan.setInterestRate(interestRate);
        loan.setLabels(labels);
        loan.setLink(link);
        loan.setLoanTime(loanTime);
        loan.setName(name);
        loan.setNote(note);
        loan.setPeriodType(periodType);
        loan.setPic(pic);
        loan.setProcess(process);
        loan.setQuotaBegin(quotaBegin);
        loan.setQuotaEnd(quotaEnd);
        loan.setSlogan(slogan);
        loan.setSort(sort);
        loan.setSuccessRate(successRate);
        loan.setTermBegin(termBegin);
        loan.setTermEnd(termEnd);
        loan.setTrait(trait);
        loan.setUpdateTime(updateTime);

        loanRepository.save(loan);
        JSONObject result = new JSONObject();
        result.put("success", true);
        return result;
    }

    @GetMapping("list")
    private JSONObject list(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String keywords){
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "sort");
        Specification<Loan> specification = (root, query, builder) -> {
            if (StringUtils.isNotBlank(keywords)){
                Predicate predicate = builder.like(root.get("name"), "%" + keywords + "%");
                return predicate;
            } else {
                return null;
            }
        };
        Page<Loan> page = loanRepository.findAll(specification, pageable);
        JSONObject result =  Result.page(currentPage, pageSize, page.getTotalElements(), page.getContent());
        result.put("labels", labelRepository.findAll());
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


    @GetMapping("/getLoans")
    public List<Loan> getLoans(){
        return loanRepository.findAll();
    }

//    @PostMapping("/delete")
//    public JSONObject delete(@RequestBody JSONObject params){
//        long id = params.getLongValue("id");
//        JSONObject result = new JSONObject();
//        try {
//            loanRepository.deleteById(id);
//            result.put("success", true);
//        }catch (Exception e){
//            result.put("success", false);
//        }
//        return result;
//    }


}
