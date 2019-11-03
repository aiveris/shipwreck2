package com.jact.plur.springmvc.repositories;

import com.jact.plur.springmvc.models.Shipwreck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

@Repository("shipwreckRepository")
public class ShipwreckRepositoryImpl {

    private static final Logger log = LoggerFactory.getLogger( ShipwreckRepositoryImpl.class );

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create
     */
    public Shipwreck create(Shipwreck ship) {
        entityManager.persist( ship );
        entityManager.flush();
        return ship;
    }

    /**
     * Update
     */
    public Shipwreck update(Shipwreck ship) {
        ship = entityManager.merge( ship );
        entityManager.flush();
        return ship;
    }

    /**
     * Delete
     */
    public void delete( Shipwreck mt) {
        entityManager.remove(mt);
        entityManager.flush();
    }

    /**
     * Find
     */
    public Shipwreck find(Long id) {

        log.info( String.format( "FIND #%d in", id ) + this.getClass().getCanonicalName() );
        return entityManager.find( Shipwreck.class, id);
    }

    public Shipwreck findById(long id) {
        return this.find( id );
    }

    public Shipwreck findAll(Long id) {
        return entityManager.find( Shipwreck.class, id);
    }

}
