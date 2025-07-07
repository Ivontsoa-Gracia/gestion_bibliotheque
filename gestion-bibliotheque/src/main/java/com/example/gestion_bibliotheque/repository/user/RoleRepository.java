package com.example.gestion_bibliotheque.repository.user;

import com.example.gestion_bibliotheque.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // aucun besoin d’ajouter findByName si tu utilises uniquement findById
}
