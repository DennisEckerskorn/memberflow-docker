package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.MembershipRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing memberships.
 */
@Service
public class MembershipService extends AbstractService<Membership, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MembershipService.class);
    private final MembershipRepository membershipRepository;

    /**
     * Constructor for MembershipService.
     *
     * @param membershipRepository the membership repository
     */
    public MembershipService(MembershipRepository membershipRepository) {
        super(membershipRepository);
        this.membershipRepository = membershipRepository;
    }

    /**
     * Saves a new membership in the database.
     *
     * @param membership the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the membership already exists
     * @throws InvalidDataException     if the membership is invalid
     */
    @Override
    public Membership save(Membership membership) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving membership: {}", membership);
        validateMembership(membership);
        boolean exists = membershipRepository.existsByType(membership.getType());
        if (exists) {
            logger.error("Membership with type {} already exists", membership.getType());
            throw new DuplicateEntityException("Membership with this type already exists");
        }

        return super.save(membership);
    }

    /**
     * Updates an existing membership in the database.
     *
     * @param membership the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the membership does not exist
     * @throws InvalidDataException    if the membership is invalid
     */
    @Override
    @Transactional
    public Membership update(Membership membership) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating membership: {}", membership);
        validateMembership(membership);
        return super.update(membership);
    }

    /**
     * Deletes a membership by its ID.
     *
     * @param id the ID of the membership to delete
     * @throws EntityNotFoundException if the membership does not exist
     */
    @Override
    @Transactional
    public void deleteById(Integer id) throws EntityNotFoundException {
        logger.info("Deleting membership by ID: {}", id);
        Membership membership = findById(id);

        if (!membership.getStudents().isEmpty()) {
            logger.error("Cannot delete membership. It is assigned to {} student(s)", membership.getStudents().size());
            throw new InvalidDataException("Cannot delete membership because it is assigned to one or more students");
        }


        super.deleteById(id);
        logger.info("Membership with ID {} deleted", id);
    }

    /**
     * Checks if a membership exists in the database.
     *
     * @param membership the entity to check
     * @return true if the membership exists, false otherwise
     */
    @Override
    protected boolean exists(Membership membership) {
        return membership.getType() != null &&
                membershipRepository.findByType(membership.getType()) != null;
    }

    /**
     * Finds a membership by its ID.
     *
     * @param id the ID of the membership to find
     * @return the found membership
     * @throws EntityNotFoundException if the membership does not exist
     * @throws InvalidDataException    if the ID is invalid
     */
    @Override
    public Membership findById(Integer id) throws EntityNotFoundException {
        logger.info("Finding membership by ID: {}", id);
        if (id == null) {
            logger.error("ID cannot be null");
            throw new InvalidDataException("ID cannot be null");
        }
        logger.info("Membership found: {}", id);
        return super.findById(id);
    }

    /**
     * Finds all memberships in the database.
     *
     * @return a list of all memberships
     */
    @Override
    public List<Membership> findAll() {
        logger.info("Finding all memberships");
        return super.findAll();
    }

    /**
     * Validates the membership entity.
     *
     * @param membership the membership to validate
     * @throws InvalidDataException if the membership is invalid
     */
    private void validateMembership(Membership membership) throws InvalidDataException {
        if (membership.getType() == null || membership.getType().toString().isEmpty()) {
            logger.error("Membership type cannot be null or empty");
            throw new InvalidDataException("Membership type cannot be null or empty");
        }
        if (membership.getStartDate() == null || membership.getEndDate() == null) {
            throw new InvalidDataException("Start and end date cannot be null");
        }
        if (membership.getStartDate().isAfter(membership.getEndDate())) {
            throw new InvalidDataException("Start date must be before end date");
        }
        if (membership.getStatus() == null) {
            throw new InvalidDataException("Status must be provided");
        }
    }
}
