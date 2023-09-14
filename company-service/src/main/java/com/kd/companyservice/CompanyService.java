package com.kd.companyservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
@CacheConfig(cacheNames = {"Company"})
@Service
public class CompanyService {
  @Autowired private CompanyRepo companyRepo;

  public Company getCompanyId(Integer id) {
    return companyRepo.getReferenceById(id);
  }

  public Company create(Company company) {
    return companyRepo.saveAndFlush(company);
  }
  @Cacheable
  public List<Company> companies() {
    List<Company> companyList = companyRepo.findAll();
    return companyList;
  }

  public void delete(int id) {
    companyRepo.deleteById(id);
  }
}
