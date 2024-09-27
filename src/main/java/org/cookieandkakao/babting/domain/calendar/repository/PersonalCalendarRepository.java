package org.cookieandkakao.babting.domain.calendar.repository;

import java.util.Optional;
import org.cookieandkakao.babting.domain.calendar.entity.PersonalCalendar;
import org.cookieandkakao.babting.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCalendarRepository extends JpaRepository<PersonalCalendar, Long> {

    Optional<PersonalCalendar> findByMember(Member member);

}
