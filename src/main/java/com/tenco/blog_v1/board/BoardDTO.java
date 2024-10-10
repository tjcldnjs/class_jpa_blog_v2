package com.tenco.blog_v1.board;

import com.tenco.blog_v1.user.User;
import lombok.Data;

public class BoardDTO {

    @Data
    public static class SaveDTO {
        private String title;
        private String content;

        public Board toEnetity(User user) {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .build();
        }

    }
}
