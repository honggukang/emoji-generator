package com.baemin.emoji.generator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Emoji {

    private String emojiChar;
    private String emoji;
    private String description;
    private List<String> aliases;
    private List<String> tags;


    @Builder
    public Emoji(String emojiChar, String emoji, String description, List<String> aliases, List<String> tags) {
        this.emojiChar = emojiChar;
        this.emoji = emoji;
        this.description = description;
        this.aliases = aliases;
        this.tags = tags;
    }
}
