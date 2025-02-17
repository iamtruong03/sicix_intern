package com.group1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group1.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByNameUser(String nameuser);
	Optional<User> findByNameUser(String nameuser);
}
