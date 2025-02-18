package com.group1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group1.entities.Department;
import com.group1.entities.User;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByNameDepartment(String namedepartment);
	Optional<Department> findByNameDepartment(String namedepartment);
    @Query("SELECT d FROM Department d WHERE d.parentDepartment.departmentId = :parentId")
    List<Department> findSubDepartments(@Param("parentId") Long parentId);
    List<Department> findByParentDepartment(Department department);
}

