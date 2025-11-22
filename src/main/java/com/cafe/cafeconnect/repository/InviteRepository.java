package com.cafe.cafeconnect.repository;

import com.cafe.cafeconnect.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    // no custom methods required right now
}


