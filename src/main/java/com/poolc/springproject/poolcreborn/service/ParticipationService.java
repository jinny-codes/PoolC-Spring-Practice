package com.poolc.springproject.poolcreborn.service;

import com.poolc.springproject.poolcreborn.model.activity.Activity;
import com.poolc.springproject.poolcreborn.model.participation.Participation;
import com.poolc.springproject.poolcreborn.model.user.User;
import com.poolc.springproject.poolcreborn.payload.request.participation.ParticipationRequest;
import com.poolc.springproject.poolcreborn.payload.response.RequestedParticipationDto;
import com.poolc.springproject.poolcreborn.repository.ActivityRepository;
import com.poolc.springproject.poolcreborn.repository.ParticipationRepository;
import com.poolc.springproject.poolcreborn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    public boolean saveParticipation(User user, Activity activity) {
         if (!user.isClubMember()) {
             return false;
         }
         else {
             Participation participation = new Participation();
             participation.setUser(user);
             participation.setActivity(activity);
             activity.addParticipant(user);
             user.addParticipating(activity);
             participationRepository.save(participation);
             return true;
         }
    }
    public void approveParticipationRequest(RequestedParticipationDto requestedParticipationDto) {
        User user = userRepository.findByUsername(requestedParticipationDto.getUsername()).get();
        Activity activity = activityRepository.findByTitle(requestedParticipationDto.getActivityTitle()).get();
        if (user != null && activity != null) {
            Participation participation = new Participation(user, activity);
            participationRepository.save(participation);
        }
    }
    public void approveParticipationRequestList(List<RequestedParticipationDto> requestedParticipationDtoList) {
        requestedParticipationDtoList.stream()
                .filter(r -> userRepository.findByUsername(r.getUsername()).get().isClubMember())
                .forEach(this::approveParticipationRequest);
    }

    public boolean signupRequestAvailable(String username, String activityTitle, ParticipationRequest request) {
        Activity activity = activityRepository.findByTitle(activityTitle).get();
        User user = userRepository.findByUsername(username).get();
        if (!participationRepository.existsByActivityAndUser(activity, user)) {
            if (request.getIsApproved()) {
                return saveParticipation(user, activity);
            }
            else {
                return saveParticipation(user, activity);
            }
        }
        return false;
    }
    public RequestedParticipationDto buildRequestedParticipationDtoFromRequestedParticipation(Participation participation) {
        if (participation == null || !participation.isApproved()) {
            return null;
        }
        RequestedParticipationDto requestedParticipationDto = new RequestedParticipationDto();

        requestedParticipationDto.setActivityTitle(participation.getActivity().getTitle());
        requestedParticipationDto.setUsername(participation.getActivity().getUser().getUsername());
        requestedParticipationDto.setMajor(userRepository.findByUsername(participation.getUser().getUsername()).get().getMajor());
        requestedParticipationDto.setReason(participation.getReason());

        return requestedParticipationDto;
    }
    public List<RequestedParticipationDto> viewRequestedParticipation(Long activityId) {
        String activityTitle = activityRepository.findById(activityId).get().getTitle();
        List<Participation> requests = participationRepository.findByActivityTitleAndIsApproved(activityTitle, false);
        return requests.stream()
                .map(r -> buildRequestedParticipationDtoFromRequestedParticipation(r))
                .collect(Collectors.toList());

    }
}
