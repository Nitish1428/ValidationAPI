package com.validation.ValidationAPI.repository;

import com.validation.ValidationAPI.entity.EmployeModelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeDataRepo extends JpaRepository<EmployeModelData, Long> {

    // Query using Optional to handle null safety for single records
    Optional<EmployeModelData> findByEmpEmailIdIn(List<String> email);

    // Custom query to find employees by name using Optional
    //JPQL Query :JPQL queries should reference entity field names.
    //For JPQL query  not required to address  nativeQuery = true
    @Query(value = "SELECT e FROM EmployeModelData e WHERE e.empName = :name")
    Optional<EmployeModelData> findByName(@Param("name") String name);

    // Custom query to find employees by postal code
    //Native queries should reference database table and column names.
    //Since you're using nativeQuery, you need to reference the actual database column names
    @Query(value = "SELECT * FROM EMPLOYEE_DATA WHERE EMP_POSTAL_CODE = :postalCode", nativeQuery = true)
    List<EmployeModelData> findByPostalCode(@Param("postalCode") String postalCode);

    // Spring Data JPA's derived query method, you can follow the same pattern
    List<EmployeModelData> findByEmpAgeBetween(@Param("minAge") int minAge, @Param("maxAge") int maxAge);

    // Custom query to find employees by both name and address JPQL Valid Query
    // Hibernate-style query without the SELECT keyword
    //This version removes the explicit SELECT, starting with FROM, and works in a typical Hibernate scenario.
    @Query(value = " FROM EmployeModelData e WHERE e.empName = :name AND e.empAddress = :address")
    List<EmployeModelData> findByNameAndAddress(@Param("name") String name, @Param("address") String address);

    EmployeModelData findByEmpEmailIdAndEmpPhoneNumber(@Param("mail") String mail, @Param("phone") String phone);

}