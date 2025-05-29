package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.IVAType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

/**
 * Repository interface for managing IVAType entities.
 * Provides methods to perform CRUD operations on IVAType entities.
 */
public interface IVATypeRepository extends JpaRepository<IVAType, Integer> {
    boolean existsByPercentage(BigDecimal percentage);

}
