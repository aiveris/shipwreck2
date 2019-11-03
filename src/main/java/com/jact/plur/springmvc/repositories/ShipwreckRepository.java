package com.jact.plur.springmvc.repositories;

import com.jact.plur.springmvc.models.Shipwreck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipwreckRepository extends JpaRepository<Shipwreck, Long> {

}
