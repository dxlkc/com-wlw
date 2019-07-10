package com.lkc.tool;

import com.lkc.model.IdCount;
import com.lkc.model.IdInfo;
import com.lkc.repository.IdCountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Connect implements CommandLineRunner {
    @Resource
    IdCountRepository idCountRepository;

    @Override
    public void run(String... args) throws Exception {
        IdCount idCount = IdCount.getIntance();
        IdInfo idInfo = idCountRepository.findByCountId("1");
        idCount.IndustryCount = Integer.valueOf(idInfo.getIndustryId());
        idCount.AcqUnitCount = Integer.valueOf(idInfo.getAcqunitId());
    }
}
