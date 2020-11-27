package com.betbull.market.repository;

import com.betbull.market.model.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface ContractRepository declares methods for managing contracts in the database.
 */
@Repository
public interface ContractRepository extends CrudRepository<Contract, Long>
{
    /**
     * Find by player id and active true contract.
     *
     * @param id the id
     * @return the contract
     */
    Contract findByPlayerIdAndActiveTrue(Long id);

    /**
     * Find by player id list.
     *
     * @param id the id
     * @return the list
     */
    List<Contract> findByPlayerId(Long id);
}
