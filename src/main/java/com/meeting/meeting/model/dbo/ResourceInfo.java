package  com.meeting.meeting.model.dbo;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author jie
* @date 2019-11-03
*/
@Entity
@Data
@Table(name="resource_info")
public class ResourceInfo implements Serializable {

    /**
     * 资源主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 主键，自增
     */
    @Column(name = "res_id")
    private Integer resId;

    /**
     * 0-会场资源，1-住宿资源
     */
    @Column(name = "type",nullable = false)
    private Integer type;

    /**
     * 资源的名称，例如xx会场，xx酒店，xx汽车租用公司
     */
    @Column(name = "name")
    private String name;

    /**
     * 0-占用中,不可用，1-空闲中,可用
     */
    @Column(name = "state",nullable = false)
    private Integer state;

    /**
     * 资源的具体位置（会场资源-会场详细到层数房间号，住宿资源-住宿地址）
     */
    @Column(name = "address",nullable = false)
    private String address;

    /**
     * 资源总共的数量（不考虑是否可用），例如，会场资源的会场数量，住宿资源的客房数量
     */
    @Column(name = "count")
    private Integer count;

    /**
     * 资源能够容纳安排的总人数
     */
    @Column(name = "toplimit")
    private Integer toplimit;

    /**
     * 资源的宣传图片，仅限一张
     */
    @Column(name = "image")
    private String image;

    /**
     * 对资源的简单描述，仅限文本，长度<=20字
     */
    @Column(name = "intro")
    private String intro;

    /**
     * 对资源的详细简述，可文本+图片等，长度不限
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 人民币为单位，会场资源的费用以会场平均价格为单位，住宿资源的费用以客房平均每间为单位
     */
    @Column(name = "cost")
    private BigDecimal cost;

    /**
     * 该资源的联系人姓名
     */
    @Column(name = "manager")
    private String manager;

    /**
     * 限定11位数手机号码
     */
    @Column(name = "phone",nullable = false)
    private String phone;

    /**
     * 格式为：xxx@xx.com
     */
    @Column(name = "email")
    private String email;
}
