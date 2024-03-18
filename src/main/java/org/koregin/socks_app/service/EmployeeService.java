package org.koregin.socks_app.service;

import org.koregin.socks_app.database.entity.Employee;

public interface EmployeeService {

    Employee findById(Integer id);
}
