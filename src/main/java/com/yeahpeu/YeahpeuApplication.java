package com.yeahpeu;

import com.yeahpeu.category.repository.CategoryRepository;
import com.yeahpeu.event.repository.EventRepository;
import com.yeahpeu.task.repository.TaskRepository;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.service.WeddingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class YeahpeuApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeahpeuApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            WeddingService weddingService,
            EventRepository eventRepository,
            TaskRepository taskRepository,
            CategoryRepository categoryRepository
    ) {
        return args -> {
//
//            // 온보딩 데이터 생성 및 처리
//            List<Long> categoryIds = List.of(
//                    101l, // 결혼식장 선정
//                    202l,      // 상견례
//                    303l,     // 웨딩 어시스트 선정
//                    106L,     // 본식 촬영 예약
//                    401L,
//                    111L// 청첩장 제작
//            );
//
//            ZonedDateTime weddingDay = ZonedDateTime.now().plusDays(365);
//
//            // User 10명 생성
//            for (int i = 0; i < 10; i++) {
//                UserEntity user = new UserEntity();
//                user.setEmailAddress("test" + i + "@test.com");
//                user.setPassword(passwordEncoder.encode("password"));
//                user.setNickname("test" + i);
//                user.setName("test" + i);
//                user.setMyCode("1234" + user.getName());
//                user.setRole(RoleType.USER);
//                userRepository.save(user);
//
//                if (i < 3) {
//                    OnboardingCommand command = new OnboardingCommand(
//                            user.getId(),
//                            WeddingRole.GROOM, // 예시로 신랑으로 설정
//                            weddingDay,
//                            123456000L, // 예산
//                            categoryIds
//                    );
//                    // 온보딩 처리 실행
//                    weddingService.processOnboarding(command);
//                }
//            }

//
//            UserEntity user1 = userRepository.findById(1L).get();
//            ZonedDateTime date = ZonedDateTime.now();

//            //이벤트 추가
//            for (int i = 0; i < 365; i++) {
//                EventCreateCommand eventCreateCommand = new EventCreateCommand(1L, "test테스트test테스트123",
//                        date.plusDays(i), "SSAFY", 1L, 6L, 2000 + i * 20);
//                CategoryEntity main = categoryRepository.findById(1L).get();
//                CategoryEntity sub = categoryRepository.findById(101L).get();
//
//                EventEntity event = EventEntity.of(user1.getWedding(), eventCreateCommand, main, sub);
//                EventEntity saved = eventRepository.save(event);
//
//                for (int j = 0; j < i / 20 + 1; j++) {
//                    TaskEntity task = TaskEntity.of(saved, "task" + j);
//                    taskRepository.save(task);
//                }
//            }
//
//            for (int i = 0; i < 40; i++) {
//                EventCreateCommand eventCreateCommand = new EventCreateCommand(1L, "test테스트test테스트123",
//                        date.plusDays(i * 3), "SSAFY", 1L, 6L, 2000 + i * 20);
//                CategoryEntity main = categoryRepository.findById(2L).get();
//                CategoryEntity sub = categoryRepository.findById(202L).get();
//
//                EventEntity event = EventEntity.of(user1.getWedding(), eventCreateCommand, main, sub);
//                EventEntity saved = eventRepository.save(event);
//
//                for (int j = 0; j < i / 5 + 1; j++) {
//                    TaskEntity task = TaskEntity.of(saved, "task" + j);
//                    taskRepository.save(task);
//                }
//            }
//
//            for (int i = 0; i < 30; i++) {
//                EventCreateCommand eventCreateCommand = new EventCreateCommand(1L, "test테스트test테스트123",
//                        date.plusDays(i * 4), "SSAFY", 1L, 6L, 2000 + i * 20);
//                CategoryEntity main = categoryRepository.findById(3L).get();
//                CategoryEntity sub = categoryRepository.findById(303L).get();
//
//                EventEntity event = EventEntity.of(user1.getWedding(), eventCreateCommand, main, sub);
//                EventEntity saved = eventRepository.save(event);
//
//                for (int j = 0; j < i / 5 + 1; j++) {
//                    TaskEntity task = TaskEntity.of(saved, "task" + j);
//                    taskRepository.save(task);
//                }
//            }
//
//            for (int i = 1; i < 365; i++) {
//                EventCreateCommand eventCreateCommand = new EventCreateCommand(1L, "test테스트test테스트123",
//                        date.minusDays(i), "SSAFY", 1L, 6L, 2000 + i * 20);
//                CategoryEntity main = categoryRepository.findById(1L).get();
//                CategoryEntity sub = categoryRepository.findById(106L).get();
//
//                EventEntity event = EventEntity.of(user1.getWedding(), eventCreateCommand, main, sub);
//                EventEntity saved = eventRepository.save(event);
//
//                for (int j = 0; j < i / 5 + 1; j++) {
//                    TaskEntity task = TaskEntity.of(saved, "task" + j);
//                    taskRepository.save(task);
//                }
//            }
        };
    }


}
