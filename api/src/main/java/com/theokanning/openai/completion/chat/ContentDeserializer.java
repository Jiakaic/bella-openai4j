package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.theokanning.openai.assistants.message.content.AudioURL;
import com.theokanning.openai.assistants.message.content.ImageFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiangTao
 * @date 2024年04月10 11:17
 **/
public class ContentDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            return jsonParser.getText();
        }
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            // 处理数组的情况
            List<MultiMediaContent> contents = new ArrayList<>();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                // 判断数组内元素类型并进行相应的反序列化
                MultiMediaContent content = parseContent(jsonParser);
                if (content != null) {
                    contents.add(content);
                }
            }
            return contents;
        }
        //抛出异常
        return null;
    }

    MultiMediaContent parseContent(JsonParser jsonParser) throws IOException {
        MultiMediaContent content = new MultiMediaContent();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("type".equals(fieldName)) {
                content.setType(jsonParser.getText());
            } else if ("text".equals(fieldName)) {
                content.setText(jsonParser.getText());
            } else if ("image_url".equals(fieldName)) {
                content.setImageUrl(parseImageUrl(jsonParser));
            } else if ("image_file".equals(fieldName)) {
                content.setImageFile(parseImageFile(jsonParser));
            } else if ("input_audio".equals(fieldName)) {
                content.setInputAudio(parseInputAudio(jsonParser));
            } else if ("audio_url".equals(fieldName)) {
                content.setAudioUrl(parseAudioUrl(jsonParser));
            }
        }
        return content;
    }

    private ImageFile parseImageFile(JsonParser jsonParser) throws IOException {
        String fileId = null;
        String detail = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("file_id".equals(fieldName)) {
                fileId = jsonParser.getText();
            } else if ("detail".equals(fieldName)) {
                detail = jsonParser.getText();
            }
        }
        return new ImageFile(fileId, detail);
    }

    private ImageUrl parseImageUrl(JsonParser jsonParser) throws IOException {
        String url = null;
        String detail = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("url".equals(fieldName)) {
                url = jsonParser.getText();
            } else if ("detail".equals(fieldName)) {
                detail = jsonParser.getText();
            }
        }
        return new ImageUrl(url, detail);
    }

    private InputAudio parseInputAudio(JsonParser jsonParser) throws IOException {
        String data = null;
        String format = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("data".equals(fieldName)) {
                data = jsonParser.getText();
            } else if ("format".equals(fieldName)) {
                format = jsonParser.getText();
            }
        }
        return new InputAudio(data, format);
    }

    private AudioURL parseAudioUrl(JsonParser jsonParser) throws IOException {
        String url = null;
        String audioTranscript = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("url".equals(fieldName)) {
                url = jsonParser.getText();
            } else if ("audio_transcript".equals(fieldName)) {
                audioTranscript = jsonParser.getText();
            }
        }
        return new AudioURL(url, audioTranscript);
    }
}
