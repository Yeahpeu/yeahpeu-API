package com.yeahpeu.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SWAGGER 이용 및 컨트롤러 이외의 API 명세를 위한 설정 파일
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Yeahpeu 서버 API 👰‍♂️🤵‍♂️", version = "1.0.0",
                description = "# 예쀼 \n\n"
                        + "## 예비 부부를 위한 예싼 및 일정관리 서비스 입니다. \n\n"
                        + "### 제공 API : \n\n"
                        + "1. 로그인 및 인증 관련 서비스 \n\n"
                        + "2. __Wedding__ 결혼관련 일정 선택 서비스 \n\n"
                        + "3. __Task__ 일정별 세부확인항목 관리 서비스  \n\n"
                        + "4. __Budget__ 부부 예산 관리 서비스  \n\n"
                        + "5. __Event__ 부부 일정 관리 서비스 \n\n"
                        + "6. __Chat__ 타 부부 간 채팅 서비스 \n\n"
                        + "7. __WishList__ 혼수 저장 및 공유 서비스 \n\n"
                        + "8. __User__ 사용자 정보 관리 서비스  "
        )
)
@Configuration
public class SwaggerConfig {


    /**
     * 로그인을 Swagger Docs에 노출 시키기 위한 메서드
     * <p>
     * Login의 경우 Controller 에 접근하지 않고 Spring Security Filter Chain 에서 로그인을 진행하므로 API 로 노출되지 않는다.
     * <p>
     * 그러나 별도 설정을 통해 노출이 가능하다.
     */
    @Bean
    public OpenAPI openAPI() {

        OpenAPI openAPI = new OpenAPI();
        /**
         * 태그를 통해서 각 컨트롤러마다 탭을 나눌 수 있다
         * 태그 자체를 추가하는 과정
         */
        //openAPI.addTagsItem(new Tag().name("Authentication Controller").description("인증 및 인가"));

        PathItem loginPath = new PathItem();

        Operation loginOperation = new Operation()
                .addTagsItem("Auth Controller")
                .summary("로그인 성공 시 JWT 발급")
                .description("### 로그인에 성공하면 사용자에게 JWT를 발급합니다. \n\n"
                        + "1. Authorization > access token 추가 \n\n "
                        + "2. Cookie        > refresh token 추가 ")
                .requestBody(new RequestBody()
                        .content(new Content()
                                .addMediaType("application/x-www-form-urlencoded",
                                        new MediaType()
                                                .schema(new Schema()
                                                        .type("object")
                                                        .addProperties("username", new Schema<>().type("string")
                                                                .description("사용자의 아이디")
                                                                .example("test9@test.com"))
                                                        .addProperties("password", new Schema<>().type("string")
                                                                .description("사용자의 비밀번호")
                                                                .example("password")
                                                        )
                                                )
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse()
                                .description("로그인 성공")
                                .addHeaderObject("Set-Cookie", new Header()
                                        .description("Refresh token")
                                        .schema(new Schema<>().type("string"))
                                )
                                .addHeaderObject("Authorization", new Header()
                                        .description("Access token")
                                        .schema(new Schema<>().type("string"))
                                )
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .type("object")
                                                                .addProperties("message",
                                                                        new Schema<>().type("string").example("로그인 성공"))
                                                        )
                                        )
                                )
                        )
                );
        loginPath.setPost(loginOperation);
        openAPI.path("/api/v1/auth/login", loginPath);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return openAPI.components(
                new Components().addSecuritySchemes("bearerAuth", securityScheme)
        ).security(Collections.singletonList(securityRequirement));
//        return new OpenAPI().components(
//                new Components().addSecuritySchemes("bearerAuth", securityScheme)
//        ).security(Collections.singletonList(securityRequirement));
    }
}

