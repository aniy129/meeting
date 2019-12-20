package  com.meeting.meeting.model.dbo;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author jie
* @date 2019-11-03
*/
@Entity
@Data
@Table(name="meeting")
public class Meeting implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 资源主键，自增
     */
    @Column(name = "res_id")
    private Integer resId;

    /**
     * 主键，自增
     */
    @Column(name = "cor_id")
    private Integer corId;

    /**
     * 会议主标题
     */
    @Column(name = "title",nullable = false)
    private String title;

    /**
     * 会议副标题
     */
    @Column(name = "subtitle")
    private String subtitle;

    /**
     * 对会议的简略介绍，仅限文本，长度<=20
     */
    @Column(name = "intro")
    private String intro;

    /**
     * 对会议的详细介绍，可文本+图片等，长度无上限
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 会议的参会人数上限（不包括工作人员）
     */
    @Column(name = "max_num")
    private Integer maxNum;

    /**
     * 天数>0，以1天为单位
     */
    @Column(name = "days")
    private Integer days;

    /**
     * 0-待审核，1-审核通过，2-审核未通过
     */
    @Column(name = "audit",nullable = false)
    private Integer audit;

    /**
     * 0-待举办，1-举办中，2-已结束
     */
    @Column(name = "state",nullable = false)
    private Integer state;

    /**
     * 会议开始时间
     */
    @Column(name = "start_time")
    private Timestamp startTime;

    /**
     * 会议结束时间，须迟于会议开始时间
     */
    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * 会议在平台的申请时间，须早于会议开始时间
     */
    @Column(name = "apply_time")
    private Timestamp applyTime;

    /**
     * 会议的总费用，人民币
     */
    @Column(name = "cost")
    private BigDecimal cost;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    @Column(name = "enterprise_name")
    @Transient
    private String enterpriseName;
}
