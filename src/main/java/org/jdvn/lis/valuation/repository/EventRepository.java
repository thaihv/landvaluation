package org.jdvn.lis.valuation.repository;

import org.jdvn.lis.valuation.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
