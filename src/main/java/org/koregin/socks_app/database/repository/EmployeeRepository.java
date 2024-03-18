package org.koregin.socks_app.database.repository;

import org.koregin.socks_app.database.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
