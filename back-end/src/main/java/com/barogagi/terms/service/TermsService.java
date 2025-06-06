package com.barogagi.terms.service;

import com.barogagi.terms.mapper.TermsMapper;
import com.barogagi.terms.vo.TermsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TermsService {

    private TermsMapper termsMapper;

    @Autowired
    public TermsService(TermsMapper termsMapper) {
        this.termsMapper = termsMapper;
    }

    // 사용중인 약관 목록 조회
    public List<TermsVO> selectTermsList(TermsVO vo) throws Exception {
        return termsMapper.selectTermsList(vo);
    }
}
