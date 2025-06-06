package com.barogagi.member.join.vo;

import com.barogagi.config.vo.DefailtVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinVO extends DefailtVO {

    // 아이디
    private String userId = "";

    // 비밀번호
    private String password = "";

    // 이메일 주소
    private String email = "";

    // 생년월일 (YYYYMMDD)
    private String birth = "";

    // 휴대전화번호
    private String tel = "";

    // 성별 (M : 남 / W : 여)
    private String gender = "";

    // 서비스 이용약관 동의 여부(필수) (Y : 동의 /  N : 비동의)
    private String serviceTermAgreeYn = "";

    // 개인정보 수집 및 이용 동의(필수) (Y : 동의 / N : 비동의)
    private String personalInfoAgreeYn = "";

    // 마케팅 정보 수신 동의(선택) (Y : 동의 / N : 비동의)
    private String marketingAgreeYn = "";
}
