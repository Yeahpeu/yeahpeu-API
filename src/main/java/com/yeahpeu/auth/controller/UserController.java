package com.yeahpeu.auth.controller;


import com.yeahpeu.user.dto.UserRequestDTO;
import com.yeahpeu.user.dto.UserResponseDTO;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //user domain 에 대한 엔드포인트, 가입. 수정. 삭제. 조회 등

    @GetMapping("/user/join")
    public String joinPage() {
        return "join"; // join page로 이동
    }

    @PostMapping
    public String join(@ModelAttribute("user") UserRequestDTO dto) {
        userService.createUser(dto);

        return "redirect:/login"; // 다른 컨트롤러로 이동
    }

    // model 인터페이스로 데이터 겟 가능
    @GetMapping("/user/update/{username}")
    public String updatePage(@PathVariable String username, Model model ) {
        if(userService.isAccess(username)){
            // 여기서 접근 페이지
            UserResponseDTO dto = userService.readOneUser(username);
            //정보 보내기
            return "update";
        }

        //접근 불가능
        return "redirect:/login";
    }

    //회원수정 : 수행
    @PostMapping("/user/update/{username}")
    public String update(@PathVariable String username, @ModelAttribute("user") UserRequestDTO dto) {

        if (userService.isAccess(username)){
            //업데이트
        }
        return "redirect:/user/update/"+username;
    }



}
