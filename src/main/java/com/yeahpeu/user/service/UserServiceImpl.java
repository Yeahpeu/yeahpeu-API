package com.yeahpeu.user.service;

import com.yeahpeu.auth.domain.EmailAuthEntity;
import com.yeahpeu.auth.repository.EmailAuthRepository;
import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.budget.repository.BudgetRepository;
import com.yeahpeu.chat.domain.ChatRoomEntity;
import com.yeahpeu.chat.domain.ChatRoomUserEntity;
import com.yeahpeu.chat.repository.ChatRoomRepository;
import com.yeahpeu.chat.repository.ChatRoomUserRepository;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.common.validation.AmountValidator;
import com.yeahpeu.user.controller.request.UserRequest;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.user.service.command.UpdateUserProfileCommand;
import com.yeahpeu.user.service.dto.UserDTO;
import com.yeahpeu.user.service.dto.UserProfileDTO;
import com.yeahpeu.user.service.dto.WeddingInfoDTO;
import com.yeahpeu.user.util.AuthCodeUtil;
import com.yeahpeu.wedding.domain.WeddingEntity;
import com.yeahpeu.wedding.repository.WeddingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yeahpeu.common.exception.ExceptionCode.*;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WeddingRepository weddingRepository;
    private final BudgetRepository budgetRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;


    @Override
    @Transactional()
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserDTO::fromEntity).toList();
    }

    @Override
    public UserDTO getMe(Long userId) {
        return userRepository.findById(userId).map(UserDTO::fromEntity).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER_ID)
        );
    }

    @Override
    @Transactional
    public UserDTO addUser(UserRequest body) {

        // 1. Email 중복 체크
        userRepository.findByEmailAddress(body.getEmailAddress()).ifPresent(user -> {
            throw new BadRequestException(DUPLICATE_EMAIL);
        });

        // 2. 이메일 인증 여부 확인
        EmailAuthEntity emailAuth = emailAuthRepository.findByEmailAddress(body.getEmailAddress());
        if (emailAuth == null || !emailAuth.getAuthStatus()) {
            throw new BadRequestException(EMAIL_NOT_VERIFIED);
        }

        body.setPassword(passwordEncoder.encode(body.getPassword()));

        // 3. User 가입
        //UserEntity userEntity = UserEntity.from(body);
        UserEntity userEntity = new UserEntity();
        userEntity.setName(body.getUsername());
        userEntity.setNickname(body.getUsername());
        userEntity.setEmailAddress(body.getEmailAddress());
        userEntity.setPassword(body.getPassword());
        userEntity.setRole(RoleType.USER);
        // 초대코드 생성
        userEntity.setMyCode(AuthCodeUtil.generateCode());

        // 4. 저장
        return UserDTO.fromEntity(userRepository.save(userEntity));

    }

    @Override
    public UserProfileDTO getMyProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_ID));

        if (user.getWedding() == null || user.getWedding().getId() == null) {
            throw new NotFoundException(NOT_FOUND_WEDDING_ID);
        }

        WeddingEntity wedding = weddingRepository.getWeddingWithCouple(user.getWedding().getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WEDDING_ID));

        BudgetEntity budget = budgetRepository.findByWeddingId(wedding.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BUDGET));

        UserProfileDTO dto = UserProfileDTO.from(user);
        dto.setWeddingInfoDTO(WeddingInfoDTO.from(user, wedding, budget));

        return dto;
    }

    @Override
    @Transactional
    public UserProfileDTO updateMyProfile(UpdateUserProfileCommand command) {
        // 1. 사용자 조회
        UserEntity user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_ID));

        if (user.getWedding() == null || user.getWedding().getId() == null) {
            throw new NotFoundException(NOT_FOUND_WEDDING_ID);
        }

        // 2. 사용자 정보 업데이트 (무조건 덮어쓰기)
        user.setName(command.getName());
        user.setAvatarUrl(command.getAvatarUrl());
        user.setNickname(command.getNickname());
//        user.setMyCode(AuthCodeUtil.generateCode());

        // 3. 결혼 정보 조회 및 업데이트
        WeddingEntity wedding = weddingRepository.findById(user.getWedding().getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_WEDDING_ID));

        wedding.setWeddingDay(command.getWeddingDay());

        // 4. 예산 정보 조회 및 업데이트
        BudgetEntity budget = budgetRepository.findByWeddingId(wedding.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BUDGET));

        if (!AmountValidator.validate(command.getBudget())) {
            throw new BadRequestException(INVALID_AMOUNT);
        }

        budget.setTotalBudget(command.getBudget());

        // 5. 업데이트된 데이터로 UserProfileDTO 생성
        WeddingInfoDTO weddingInfoDTO = WeddingInfoDTO.from(user, wedding, budget);
        UserProfileDTO userProfileDTO = UserProfileDTO.from(user);
        userProfileDTO.setWeddingInfoDTO(weddingInfoDTO);

        return userProfileDTO;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_ID));

        // 채팅방 나가기 처리
        leaveChatRooms(user);

        // 웨딩 정보 처리
        WeddingEntity wedding = user.getWedding();
        if (wedding != null && wedding.getId() != null) {
            if (wedding.getBride() != null && wedding.getBride().equals(user)) {
                wedding.setBride(null);
            }
            if (wedding.getGroom() != null && wedding.getGroom().equals(user)) {
                wedding.setGroom(null);
            }

            user.setWedding(null);

            if (wedding.getGroom() == null && wedding.getBride() == null) {
                weddingRepository.delete(wedding);
            }
        }

        userRepository.delete(user);
    }

    @Override
    public UserProfileDTO getOpponent(String opponentCode) {
        UserEntity opponent = userRepository.findByMyCode(opponentCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_OPPONENT));

        return UserProfileDTO.from(opponent);
    }

    private void leaveChatRooms(UserEntity user) {
        // 1. 사용자의 채팅방 참여 정보를 가져온다.
        List<ChatRoomUserEntity> userRooms = chatRoomUserRepository.findByUserId(user.getId());

        for (ChatRoomUserEntity cru : userRooms) {
            ChatRoomEntity chatRoom = cru.getChatRoom();

            // 만약 탈퇴하는 사용자가 해당 채팅방의 생성자라면...
            if (chatRoom.getCreator() != null && chatRoom.getCreator().getId().equals(user.getId())) {
                // 해당 채팅방의 모든 참여 정보를 조회
                List<ChatRoomUserEntity> participants = chatRoomUserRepository.findByChatRoomId(chatRoom.getId());
                // 탈퇴하는 사용자를 목록에서 제거
                participants.removeIf(p -> p.getUser().getId().equals(user.getId()));

                if (participants.isEmpty()) {
                    // 남은 참여자가 없다면,
                    // 해당 채팅방과 연관된 모든 참여 정보를 삭제한 후,
                    chatRoomUserRepository.deleteByUser_IdAndChatRoom_Id(user.getId(), chatRoom.getId());
                    // 채팅방 자체를 삭제한다.
                    chatRoomRepository.delete(chatRoom);
                    // 다음 참여 정보 처리로 넘어간다.
                    continue;
                } else {
                    // 남은 참여자가 있다면, 그 중 첫 번째 사용자를 새 생성자로 지정
                    chatRoom.setCreator(participants.getFirst().getUser());
                }
            }

            // 2. 탈퇴하는 사용자의 참여 정보 삭제 (이미 개별 참여 정보를 가지고 있으므로)
            chatRoomUserRepository.delete(cru);

            // 3. 사용 멤버 수 감소 처리
            int updatedCount = chatRoomRepository.decreaseUsedMemberCount(chatRoom.getId());

            // 4. 남은 멤버 수가 0이면 채팅방 삭제
            if (updatedCount == 0) {
                // 혹은 만약 채팅방에 남은 참여 정보가 있다면 삭제 전에 모두 제거
                chatRoomUserRepository.deleteByChatRoomId(chatRoom.getId());
                chatRoomRepository.delete(chatRoom);
            }
        }
    }
}
