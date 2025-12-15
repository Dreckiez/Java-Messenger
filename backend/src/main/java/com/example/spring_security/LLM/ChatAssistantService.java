package com.example.spring_security.LLM;

import com.openai.client.OpenAIClient;
import com.openai.models.*;
import com.openai.models.chat.completions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatAssistantService {

    private final OpenAIClient client;

    @Autowired
    public ChatAssistantService(OpenAIClient client) {
        this.client = client;
    }

    public String refineText(String rawText, String tone) {
        String systemInstruction = "Bạn là một công cụ chuẩn hóa văn bản backend (Text Normalizer). " +
                "Nhiệm vụ: Chuyển đổi văn bản người dùng nhập thành văn bản chuẩn: đúng chính tả, ngữ pháp và giọng văn '" + tone + "'. " +
                "QUY TẮC OUTPUT (BẮT BUỘC): " +
                "- CHỈ trả về duy nhất chuỗi văn bản kết quả. " +
                "- KHÔNG bao gồm bất kỳ lời chào, giải thích, hay dẫn dắt nào. " +
                "- KHÔNG định dạng Markdown. " +
                "- Nếu input đã đúng, hãy trả về y nguyên.";
        String userPrompt = String.format("Hãy viết lại đoạn văn sau sao cho giọng văn '%s': \"%s\"", tone, rawText);

        return callOpenAi(systemInstruction, userPrompt);
    }

    public List<String> suggestReplies(String incomingMessage) {
        // 1. Prompt ép format dùng dấu gạch đứng (|) để ngăn cách
        String systemInstruction =
                "Bạn là API gợi ý phản hồi nhanh (Smart Reply). " +
                        "Nhiệm vụ: Dựa vào tin nhắn đầu vào, hãy sinh ra 3 câu trả lời ngắn gọn, lịch sự và phù hợp ngữ cảnh. " +
                        "QUY TẮC OUTPUT BẮT BUỘC: " +
                        "- Trả về đúng 3 câu gợi ý. " +
                        "- Các câu ngăn cách nhau bởi dấu gạch đứng '|'. " +
                        "- KHÔNG đánh số (1, 2, 3), KHÔNG xuống dòng, KHÔNG có lời dẫn. " +
                        "- Ví dụ output chuẩn: Cảm ơn bạn|Tôi sẽ xem xét|Để tôi kiểm tra lại";

        String userPrompt = "Tin nhắn nhận được: \"" + incomingMessage + "\"";

        // 2. Gọi AI (nhớ để temperature khoảng 0.5 - 0.7 để có chút sáng tạo)
        String rawResponse = callOpenAi(systemInstruction, userPrompt);

        // rawResponse lúc này sẽ là: "Gợi ý A|Gợi ý B|Gợi ý C"

        // 3. Xử lý chuỗi (Split) để tạo thành List
        if (rawResponse != null && !rawResponse.isEmpty()) {
            // Cắt chuỗi dựa trên dấu |
            String[] suggestions = rawResponse.split("\\|");

            // Trim() từng phần tử để xóa khoảng trắng thừa
            List<String> result = new ArrayList<>();
            for (String s : suggestions) {
                result.add(s.trim());
            }
            return result;
        }

        return new ArrayList<>(); // Trả về list rỗng nếu lỗi
    }

    private String callOpenAi(String systemContent, String userContent) {
        List<ChatCompletionMessageParam> messages = new ArrayList<>();

        // 1. SỬA LỖI: Dùng Builder cho System Message
        // Sai: ChatCompletionMessageParam.ofSystem(systemContent)
        // Đúng: Phải build object ChatCompletionSystemMessageParam trước
        ChatCompletionSystemMessageParam systemMsg = ChatCompletionSystemMessageParam.builder()
                .content(systemContent)
                .build();
        messages.add(ChatCompletionMessageParam.ofSystem(systemMsg));

        // 2. SỬA LỖI: Dùng Builder cho User Message
        // Sai: ChatCompletionMessageParam.ofUser(userContent)
        // Đúng: Phải build object ChatCompletionUserMessageParam trước
        ChatCompletionUserMessageParam userMsg = ChatCompletionUserMessageParam.builder()
                .content(userContent)
                .build();
        messages.add(ChatCompletionMessageParam.ofUser(userMsg));

        // Các bước tiếp theo giữ nguyên
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("groq/compound")
                .messages(messages)
                .maxTokens(500)
                .temperature(0.3)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);

        return completion.choices().get(0).message().content().orElse("");
    }
}