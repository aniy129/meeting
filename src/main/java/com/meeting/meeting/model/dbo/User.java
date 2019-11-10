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
@Table(name="user")
public class User implements Serializable {

    /**
     * 主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 真实姓名
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 昵称,作为登录账号
     */
    @Column(name = "nickname",nullable = false)
    private String nickname;

    /**
     * 登录密码，限制30个字符长度
     */
    @Column(name = "password",nullable = false)
    private String password;

    /**
     * 登录身份：0-企业管理员，1-企业用户
     */
    @Column(name = "identity",nullable = false)
    private Integer identity;

    /**
     * 18位数身份证号码
     */
    @Column(name = "identify_num",nullable = false)
    private String identifyNum;

    /**
     * 11位数电话号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;
}
