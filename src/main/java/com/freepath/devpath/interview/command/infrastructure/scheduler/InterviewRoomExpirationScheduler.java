package com.freepath.devpath.interview.command.infrastructure.scheduler;

import com.freepath.devpath.interview.command.domain.aggregate.InterviewRoom;
import com.freepath.devpath.interview.command.domain.repository.InterviewRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewRoomExpirationScheduler {

    private final InterviewRoomRepository interviewRoomRepository;

    /* 매일 정시마다 진행 중이던 면접방을 expire 시키는 스케줄러*/
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void expireOldInterviewRooms() {
        List<InterviewRoom> progressRooms = interviewRoomRepository.findAllByInterviewRoomStatus(InterviewRoom.InterviewRoomStatus.PROGRESS);

        for (InterviewRoom room : progressRooms) {
            if (room.getInterviewRoomCreatedAt().isBefore(LocalDateTime.now().minusHours(24))) {
                room.updateStatus(InterviewRoom.InterviewRoomStatus.EXPIRED);
                System.out.println("만료 처리된 면접방 : "+room.getInterviewRoomId());
            }
        }
    }
}