package com.example.petstorespringrest.repository;

import com.example.petstorespringrest.entity.Pet;
import com.example.petstorespringrest.enums.PetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByStatus(PetStatus status);
}
