package com.meeting.meeting.model.dbo;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author jie
* @date 2019-11-03
*/
@Entity
@Data
@Table(name="corporation")
public class Corporation implements Serializable {

    /**
     * 主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 企业名称
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 企业联系人
     */
    @Column(name = "manager")
    private String manager;

    /**
     * 限11位数电话号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 格式为：xxx@xx.com
     */
    @Column(name = "email")
    private String email;

    /**
     * 详细地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 企业的宣传图片，仅限一张
     */
    @Column(name = "image")
    private String image;

    /**
     * 对企业的简略介绍，仅限文本，限20字
     */
    @Column(name = "intro")
    private String intro;

    /**
     * 对企业的详细介绍，可文本+图片等，长度无上限
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 根据企业在平台申请会议情况对企业有vip之分：0-普通会员，1-vip会员
     */
    @Column(name = "vip",nullable = false)
    private String vip;

    /**
     * 企业在该平台的注册时间
     */
    @Column(name = "reg_time",nullable = false)
    private Timestamp regTime;
}
