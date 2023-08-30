package com.kd.companyservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepo companyRepo;
    public Company getCompanyId(Integer id){
        return companyRepo.getReferenceById(id);
    }
}
