package com.baemin.emoji.generator;

import com.baemin.emoji.generator.dto.Emoji;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class EmojiJsonGenerator {

    public static void main (String[] args) throws IOException {

        // read file
        final InputStream is = EmojiJsonGenerator.class.getClassLoader().getResourceAsStream("emoji-test.txt");


        // convert
        Map<String, String> emojiMap = EmojiTestDataReader.getEmojiList(is);

        List<Emoji> emojiList = emojiMap.entrySet().stream()
                .map(data -> {
                    String emojiChar = data.getKey();
                    String emoji = data.getValue();

                    return Emoji.builder()
                            .emojiChar(emojiChar)
                            .emoji(emoji)
                            .description("")
                            .aliases(new ArrayList<>())
                            .tags(new ArrayList<>())
                            .build();
                })
                .collect(toList());

        // write file
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("emojis.json");
        String emojiJson = objectMapper.writeValueAsString(emojiList);

        System.out.println(emojiJson);

        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] strToBytes = emojiJson.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }


    private static class EmojiTestDataReader {
        static Map<String, String> getEmojiList(final InputStream emojiFileStream) throws IOException {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(emojiFileStream));
            //final List<String> result = new LinkedList<>();

            final Map<String, String> resultMap = new LinkedHashMap<>();

            String line = reader.readLine();
            String [] lineSplit;

            while (line != null) {
                if (!line.startsWith("#") && !line.startsWith(" ") && !line.startsWith("\n") && line.length() != 0) {
                    lineSplit = line.split(";");
                    String codpen = lineSplit[0].trim();

                    resultMap.put(convertToEmoji(codpen), convertToEmojiUnicode(codpen));
                }
                line = reader.readLine();
            }

            return resultMap;
        }



        private static String convertToEmoji(final String input) {
            String[] emojiCodepoints = input.split(" ");
            StringBuilder sb = new StringBuilder();

            for (String emojiCodepoint : emojiCodepoints) {
                int codePoint = convertFromCodepoint(emojiCodepoint);
                sb.append(Character.toChars(codePoint));
            }
            return sb.toString();
        }

        private static String convertToEmojiUnicode(final String input) {
            String[] emojiCodepoints = input.split(" ");
            StringBuilder emojiUni = new StringBuilder();

            for (String emojiCodepoint : emojiCodepoints) {
                int codePoint = convertFromCodepoint(emojiCodepoint);
                char[] chars = Character.toChars(codePoint);

                for (char aChar : chars) {
                    emojiUni.append(String.format("\\u%04X", (int) aChar));
                }
            }

             return emojiUni.toString();
        }

        static int convertFromCodepoint(String emojiCodepointAsString) {
            return Integer.parseInt(emojiCodepointAsString, 16);
        }

    }

}
