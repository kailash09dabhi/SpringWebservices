package com.kd.companyservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
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

  @Cacheable("list")
  public List<Company> companies() {
    List<Company> companyList = companyRepo.findAll();
    return companyList;
  }

  public void delete(int id) {
    companyRepo.deleteById(id);
  }

  @CacheEvict(value = "list", allEntries = true)
  public Company updatedCompany(Company company) {
    Company savableCompany = company;
    if (companyRepo.existsById(company.getId())) {
      Company existingCompany = companyRepo.getReferenceById(company.getId());
      existingCompany.setName(company.getName());
      existingCompany.setAddress(company.getAddress());
      savableCompany = existingCompany;
    }
    return companyRepo.saveAndFlush(savableCompany);
  }
}
