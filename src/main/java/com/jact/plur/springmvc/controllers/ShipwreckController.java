package com.jact.plur.springmvc.controllers;

import com.jact.plur.springmvc.repositories.ShipwreckRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.jact.plur.springmvc.models.Shipwreck;

@RestController
@RequestMapping("api/v1/")
public class ShipwreckController {

    @Autowired
    @Qualifier("shipwreckRepository")
    private ShipwreckRepository shipwreckRepository;

    @RequestMapping(value = "shipwrecks", method = RequestMethod.GET)
    public List<Shipwreck> list() {
        return shipwreckRepository.findAll();
    }

    @RequestMapping(value = "shipwrecks", method = RequestMethod.POST)
    public Shipwreck create(@RequestBody Shipwreck shipwreck) {
        return shipwreckRepository.saveAndFlush(shipwreck);
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.GET)
    public Shipwreck get(@PathVariable Long id) {
        Optional<Shipwreck> shipwreckOptional = shipwreckRepository.findById(id);
        return shipwreckOptional.orElse(null);
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.PUT)
    public Shipwreck update(@PathVariable Long id, @RequestBody Shipwreck shipwreck) {
        Optional<Shipwreck> shipwreckOptional = shipwreckRepository.findById(id);
        Shipwreck existShipwreck = shipwreckOptional.orElse(null);
        if (existShipwreck != null) {
            BeanUtils.copyProperties(shipwreck, existShipwreck);
            return shipwreckRepository.saveAndFlush(existShipwreck);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "shipwrecks/{id}", method = RequestMethod.DELETE)
    public Shipwreck delete(@PathVariable Long id) {
        Optional<Shipwreck> shipwreckOptional = shipwreckRepository.findById(id);
        Shipwreck existShipwreck = shipwreckOptional.orElse(null);
        if (existShipwreck != null) {
            shipwreckRepository.delete(existShipwreck);
        }
        return existShipwreck;
    }
}
