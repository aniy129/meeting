package  com.meeting.meeting.model.dbo;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author jie
* @date 2019-11-03
*/
@Entity
@Data
@Table(name="resource")
public class Resource implements Serializable {

    /**
     * 主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 0-会场资源，1-住宿资源
     */
    @Column(name = "type",nullable = false)
    private String type;

    /**
     * 对资源的简单描述，仅限文本，长度<=20字
     */
    @Column(name = "intro")
    private String intro;
}
