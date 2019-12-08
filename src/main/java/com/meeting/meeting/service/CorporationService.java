package com.meeting.meeting.service;


import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.model.dto.request.CorporationListRequest;
import com.meeting.meeting.model.dto.request.EditCorporationRequest;
import com.meeting.meeting.model.dto.request.EnterpriseRegisterRequest;
import com.meeting.meeting.model.dto.request.ManagerEditCorporationRequest;
import com.meeting.meeting.model.dto.response.BaseResponse;
import org.springframework.data.domain.Page;

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

    BaseResponse editCorporation(EditCorporationRequest request);

    Page<Corporation> list(CorporationListRequest request);

    BaseResponse edit(ManagerEditCorporationRequest request);
}
