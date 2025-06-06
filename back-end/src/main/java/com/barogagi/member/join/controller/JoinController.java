package com.barogagi.member.join.controller;

import com.barogagi.member.join.service.JoinService;
import com.barogagi.member.join.vo.JoinVO;
import com.barogagi.member.join.vo.UserIdCheckVO;
import com.barogagi.response.ApiResponse;
import com.barogagi.util.EncryptUtil;
import com.barogagi.util.InputValidate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일반 회원가입", description = "일반 회원가입 관련 API")
@RestController
@RequestMapping("/membership/join")
public class JoinController {
    private static final Logger logger = LoggerFactory.getLogger(JoinController.class);

    private final JoinService joinService;
    private final InputValidate inputValidate;
    private final EncryptUtil encryptUtil;

    private final String API_SECRET_KEY;

    @Autowired
    public JoinController(Environment environment,
                          JoinService joinService,
                          InputValidate inputValidate,
                          EncryptUtil encryptUtil){
        this.API_SECRET_KEY = environment.getProperty("api.secret-key");
        this.joinService = joinService;
        this.inputValidate = inputValidate;
        this.encryptUtil = encryptUtil;
    }

    @Operation(summary = "아이디 중복 체크 기능", description = "아이디 중복 체크 기능입니다.")
    @PostMapping("/basic/membership/userId/check")
    public ApiResponse checkUserId(@RequestBody UserIdCheckVO userIdCheckVO) {

        logger.info("CALL /membership/join/userId/check");
        logger.info("[input] API_SECRET_KEY={}", userIdCheckVO.getApiSecretKey());

        ApiResponse apiResponse = new ApiResponse();
        String resultCode = "";
        String message = "";

        try {

            if(userIdCheckVO.getApiSecretKey().equals(API_SECRET_KEY)){

                if(inputValidate.isEmpty(userIdCheckVO.getUserId())) {
                    resultCode = "101";
                    message = "아이디를 입력해주세요.";
                } else{
                    JoinVO joinVO = new JoinVO();
                    joinVO.setUserId(userIdCheckVO.getUserId());

                    int checkUserId = joinService.checkUserId(joinVO);
                    logger.info("@@ checkUserId={}", checkUserId);

                    if(checkUserId > 0){
                        resultCode = "300";
                        message = "해당 아이디 사용이 불가능합니다.";

                    } else{
                        resultCode = "200";
                        message = "해당 아이디 사용이 가능합니다.";
                    }
                }

            } else {
                resultCode = "100";
                message = "잘못된 접근입니다.";
            }

        } catch (Exception e) {
            resultCode = "400";
            message = "오류가 발생하였습니다.";
            throw new RuntimeException(e);

        } finally {
            apiResponse.setResultCode(resultCode);
            apiResponse.setMessage(message);
        }
        return apiResponse;
    }

    @Operation(summary = "회원가입 정보 저장 기능", description = "회원가입 정보 저장 기능입니다.")
    @PostMapping("/basic/membership/insert")
    public ApiResponse membershipJoinInsert(@RequestBody JoinVO joinVO){

        logger.info("CALL /membership/join/insert");
        logger.info("[input] API_SECRET_KEY={}", joinVO.getApiSecretKey());

        ApiResponse apiResponse = new ApiResponse();
        String resultCode = "";
        String message = "";

        try {

            if(joinVO.getApiSecretKey().equals(API_SECRET_KEY)){

                // 약관 동의 여부 확인
                /*
                서비스 이용약관(필수), 개인정보 수집 및 이용(필수), 마케팅 정보 수신(선택)
                 */
                if(joinVO.getServiceTermAgreeYn().equals("Y") && joinVO.getPersonalInfoAgreeYn().equals("Y")
                        && (joinVO.getMarketingAgreeYn().equals("Y") || joinVO.getMarketingAgreeYn().equals("N"))){

                    // 필수 입력값(아이디, 비밀번호, 생년월일, 휴대전화번호, 성별 값이 빈 값이 아닌지 확인)
                    // 선택 입력값(이메일)
                    if(inputValidate.isEmpty(joinVO.getUserId()) || inputValidate.isEmpty(joinVO.getPassword())
                            || inputValidate.isEmpty(joinVO.getEmail()) || inputValidate.isEmpty(joinVO.getBirth())
                            || inputValidate.isEmpty(joinVO.getTel()) || inputValidate.isEmpty(joinVO.getGender())){

                        // 필수 입력값 중 빈 값이 존재. insert 중지
                        resultCode = "101";
                        message = "회원가입에 필요한 정보를 입력해주세요.";

                    } else{
                        // 입력값 암호화 & 값 세팅
                        // 휴대전화번호, 이메일, 비밀번호 암호화
                        joinVO.setTel(encryptUtil.encrypt(joinVO.getTel()));
                        joinVO.setEmail(encryptUtil.encrypt(joinVO.getEmail()));

                        joinVO.setPassword(encryptUtil.hashEncodeString(joinVO.getPassword()));

                        // 회원 정보 저장(회원가입)
                        int insertResult = joinService.insertMemberInfo(joinVO);
                        logger.info("@@ insertResult={}", insertResult);

                        if(insertResult > 0){
                            resultCode = "200";
                            message = "회원가입에 성공하였습니다.";
                        } else{
                            resultCode = "300";
                            message = "회원가입에 실패하였습니다.";
                        }
                    }

                } else {
                    resultCode = "102";
                    message = "약관에 동의를 해주셔야 회원가입이 가능합니다.";
                }

            } else {
                resultCode = "100";
                message = "잘못된 접근입니다.";
            }

        } catch (Exception e) {
            resultCode = "400";
            message = "오류가 발생하였습니다.";
            throw new RuntimeException(e);
        } finally {
            apiResponse.setResultCode(resultCode);
            apiResponse.setMessage(message);
        }

        return apiResponse;
    }
}
