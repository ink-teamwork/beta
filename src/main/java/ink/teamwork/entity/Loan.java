package ink.teamwork.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slogan;

    private String loanTime;

    private String trait;

    private String link;

    private String labels;

    private double quotaBegin;

    private double quotaEnd;

    private double interestRate;

    private String periodType;

    private int termBegin;

    private int termEnd;

    private int applyCount;

    private double successRate;

    @Column(name="pic", columnDefinition="TEXT")
    private String pic;

    private Date updateTime;

    private String process;

    private String conditions;

    private String note;

    private int status;

    private int sort;

    private int homePageRecommend;

    private Date createdTime;

}
