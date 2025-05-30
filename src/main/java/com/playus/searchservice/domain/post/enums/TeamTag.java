package com.playus.searchservice.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamTag {
    DOOSAN_BEARS("두산 베어스"),
    HANWHA_EAGLES("한화 이글스"),
    KIA_TIGERS("KIA 타이거즈"),
    KIWOOM_HEROES("키움 히어로즈"),
    LG_TWINS("LG 트윈스"),
    LOTTE_GIANTS("롯데 자이언츠"),
    NC_DINOS("NC 다이노스"),
    SSG_LANDERS("SSG 랜더스"),
    SAMSUNG_LIONS("삼성 라이온즈"),
    KT_WIZ("KT 위즈"),;

    private final String teamName;

    public static TeamTag fromString(String name){
        for (TeamTag teamTag : TeamTag.values()) {
            if (teamTag.teamName.equals(name)) {
                return teamTag;
            }
        }
        throw new IllegalArgumentException("No enum constant " + name);
    }
}
