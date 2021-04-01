package com.psikku.backend.service.submitanswer;


import com.psikku.backend.dto.testresult.TestFinalResultDto;
import com.psikku.backend.dto.useranswer.*;
import com.psikku.backend.entity.SubmittedAnswer;
import com.psikku.backend.entity.User;
import com.psikku.backend.entity.Voucher;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface SubmitAnswerService {
    SubmittedAnswer extractUserAnswer(SubmittedAnswerDto dto,
                                      LocalDateTime creationDate,
                                      Voucher voucher,
                                      User user);
    List<SubmittedAnswer> convertToSubmittedAnswerList(List<SubmittedAnswerDto> submittedAnswerDto, User user);
    List<SubmittedAnswerDto> convertToSubmittedAnswerDtoList(List<SubmittedAnswer> submittedAnswerList);
    TestFinalResultDto calculateGenericTest(UserAnswerDto submittedAnswerList, String voucherCode);
    TestFinalResultDto calculateResultTestV2(UserAnswerDto userAnswerDto, String voucherCode);
    void saveUserPictureAnswer(String voucher, String internalTestName, MultipartFile pictFile);
    void saveKraepelinResult(KraepelinResultDto dto);
    void savePauliResult(PauliResultDto dto);

    void saveRawAnswer(RawAnswerDto dto);
//    void getListTest(List<SubmittedAnswer> submittedAnswerList);
}
