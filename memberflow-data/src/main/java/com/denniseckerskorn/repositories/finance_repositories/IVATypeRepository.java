package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.IVAType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface IVATypeRepository extends JpaRepository<IVAType, Integer> {
    boolean existsByPercentage(BigDecimal percentage);

}
