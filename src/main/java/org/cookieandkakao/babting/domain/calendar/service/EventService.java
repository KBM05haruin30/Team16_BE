package org.cookieandkakao.babting.domain.calendar.service;

import org.cookieandkakao.babting.domain.calendar.dto.response.EventGetResponseDto;
import org.cookieandkakao.babting.domain.calendar.entity.Event;
import org.cookieandkakao.babting.domain.calendar.entity.PersonalCalendar;
import org.cookieandkakao.babting.domain.calendar.entity.Reminder;
import org.cookieandkakao.babting.domain.calendar.entity.Time;
import org.cookieandkakao.babting.domain.calendar.repository.EventRepository;
import org.cookieandkakao.babting.domain.calendar.repository.PersonalCalendarRepository;
import org.cookieandkakao.babting.domain.calendar.repository.ReminderRepository;
import org.cookieandkakao.babting.domain.calendar.repository.TimeRepository;
import org.cookieandkakao.babting.domain.meeting.entity.Location;
import org.cookieandkakao.babting.domain.meeting.repository.LocationRepository;
import org.cookieandkakao.babting.domain.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final TimeRepository timeRepository;
    private final LocationRepository locationRepository;
    private final ReminderRepository reminderRepository;
    private final PersonalCalendarRepository personalCalendarRepository;


    public EventService(EventRepository eventRepository, TimeRepository timeRepository,
        LocationRepository locationRepository, ReminderRepository reminderRepository,
        PersonalCalendarRepository personalCalendarRepository) {
        this.eventRepository = eventRepository;
        this.timeRepository = timeRepository;
        this.locationRepository = locationRepository;
        this.reminderRepository = reminderRepository;
        this.personalCalendarRepository = personalCalendarRepository;
    }

    public void saveEvent(EventGetResponseDto eventGetResponseDto, Long memberId) {
        // 개인 캘린더 조회 또는 생성
        PersonalCalendar personalCalendar = personalCalendarRepository.findByMemberMemberId(memberId)
            .orElseGet(() -> personalCalendarRepository.save(new PersonalCalendar(new Member(memberId))));

        //Time 엔티티 저장
        Time time = timeRepository.save(eventGetResponseDto.time().toEntity());

        //Location 엔티티 저장
        Location location = null;
        if (eventGetResponseDto.location() != null) {
            location = locationRepository.save(eventGetResponseDto.location());
        }

        // Event 엔티티 저장
        Event event = new Event(personalCalendar, time, location, eventGetResponseDto.id(),
            eventGetResponseDto.title(), eventGetResponseDto.type(), eventGetResponseDto.isRecurEvent(),
            eventGetResponseDto.rrule(), eventGetResponseDto.dtStart(), eventGetResponseDto.description(),
            eventGetResponseDto.color(), eventGetResponseDto.memo());
        eventRepository.save(event);

        // Reminder 저장 (있을 경우)
        if (eventGetResponseDto.reminders() != null) {
            reminderRepository.save(new Reminder(event, eventGetResponseDto.reminders().getRemindTime()));
        }



    }


}
