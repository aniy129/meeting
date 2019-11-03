package com.meeting.meeting.service.impl;


import com.meeting.meeting.model.dbo.Corporation;
import com.meeting.meeting.repository.CorporationRepository;
import com.meeting.meeting.service.CorporationService;
import com.meeting.meeting.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
* @author jie
* @date 2019-11-03
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CorporationServiceImpl implements CorporationService {

    @Autowired
    private CorporationRepository corporationRepository;

    @Override
    public Corporation findById(Integer id) {
        Optional<Corporation> corporation = corporationRepository.findById(id);
        ValidationUtil.isNull(corporation,"Corporation","id",id);
        return corporation.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Corporation create(Corporation resources) {
        return corporationRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Corporation resources) {
        Optional<Corporation> optionalCorporation = corporationRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalCorporation,"Corporation","id",resources.getId());

        Corporation corporation = optionalCorporation.get();
        // 此处需自己修改
        resources.setId(corporation.getId());
        corporationRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        corporationRepository.deleteById(id);
    }
}
