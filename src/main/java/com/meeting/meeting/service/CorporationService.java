package com.meeting.meeting.service;


import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dto.request.EnterpriseRegisterRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;

/**
 * @author jie
 * @date 2019-11-03
 */
public interface CorporationService {

    /**
     * findById
     *
     * @param id
     * @return
     */
    Corporation findById(Integer id);

    /**
     * create
     *
     * @param resources
     * @return
     */
    Corporation create(Corporation resources);

    /**
     * update
     *
     * @param resources
     */
    void update(Corporation resources);

    /**
     * delete
     *
     * @param id
     */
    void delete(Integer id);

    BaseResponse register(EnterpriseRegisterRequest request);
}
