package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @Test
    void 질문자_확인_성공() {
        assertThatNoException().isThrownBy(() -> Q1.checkAuthority(NsUserTest.JAVAJIGI));
    }

    @Test
    void 질문자_확인_실패() {
        assertThatThrownBy(() -> Q2.checkAuthority(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 모든_답변자가_질문자() {
        Q1.addAnswer(new Answer(NsUserTest.JAVAJIGI, Q1, "q1 a1"));
        Q1.addAnswer(new Answer(NsUserTest.JAVAJIGI, Q1, "q1 a2"));

        assertThatNoException().isThrownBy(() -> Q1.checkAuthority(NsUserTest.JAVAJIGI));
    }

    @Test
    void 질문자가_아닌_답변자가_존재() {
        Q1.addAnswer(new Answer(NsUserTest.JAVAJIGI, Q1, "q1 a1"));
        Q1.addAnswer(new Answer(NsUserTest.SANJIGI, Q1, "q1 a2"));

        assertThatThrownBy(() -> Q1.checkAuthority(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
