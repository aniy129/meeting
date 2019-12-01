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
@Table(name="manager")
public class Manager implements Serializable {

    /**
     * 主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 管理员编码
     */
    @Column(name = "code",nullable = false)
    private String code;

    /**
     * 登录账号，限制15个字符长度
     */
    @Column(name = "account")
    private String account;

    /**
     * 登录密码，限制30个字符长度
     */
    @Column(name = "password",nullable = false)
    private String password;

    /**
     * 管理员姓名
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 0-男，1-女
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 年龄限制18-100岁
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 仅限11位电话号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 管理员邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 管理员照片
     */
    @Column(name = "photo")
    private String photo;
    /**
     * 是否可用
     */
    @Column(name = "available")
    private Boolean available;

}
