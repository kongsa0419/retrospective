package enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Major {
    // 0~6
    기계공학부(161, 255),
    컴퓨터공학부(136, 255),
    메카트로닉스공학부(168,255),
    전기전자통신공학부(151, 255),
    디자인건축공학부(163, 158),
    에너지신소재화학공학부(121,180),
    산업경영학부(111,200);

    private int majorCode; // 학과코드
    private int entireStudentCount; //총원

    Major(int majorCode, int entireStudentCount) {
        this.majorCode =majorCode;
        this.entireStudentCount = entireStudentCount;
    }

    public static Stream<Major> stream() {
        return Stream.of(Major.values());
    }
}
