package com.tms.a1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.a1.entity.User;

public interface UserRepo extends JpaRepository<User, String>{

}
