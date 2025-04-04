package com.ailab.assistant.services.impl;

import com.ailab.assistant.dtos.AIExplainRequest;
import com.ailab.assistant.dtos.AIGenerateRequest;
import com.ailab.assistant.dtos.LabManualGenerateRequest;
import com.ailab.assistant.services.AIModelService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiAIModelService implements AIModelService {
    private final WebClient webClient;
    private final String geminiApiKey = "AIzaSyCRP6SlpSDqD_JBqm5JnoF3cSLACV61n10"; // Replace with actual API key
    private final String geminiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

    public GeminiAIModelService() {
        this.webClient = WebClient.builder()
                .baseUrl(geminiUrl)
                .build();
    }

    @Override
    public Mono<String> generateContent(AIGenerateRequest request) {
        Map<String, Object> requestBody = new HashMap<>();

       if (request.category().toLowerCase().contains("textual")) {
           requestBody.put(
                   "contents", Map.of(
                           "parts", new Object[]{
                                   Map.of("text",
                                           "<p><strong>You are an expert educational assistant, providing structured explanations for learning purposes.</strong></p>" +

                                                   "<h2>📌 Request Details</h2>" +
                                                   "<ul>" +
                                                   (request.category().isEmpty() ? "" : "<li><strong>Category:</strong> " + request.category() + "</li>") +
                                                   (request.subject().isEmpty() ? "" : "<li><strong>Subject:</strong> " + request.subject() + "</li>") +
                                                   (request.topic().isEmpty() ? "" : "<li><strong>Topic:</strong> " + request.topic() + "</li>") +
                                                   (request.additionalKeywords().isEmpty() ? "" : "<li><strong>Keywords:</strong> " + request.additionalKeywords() + "</li>") +
                                                   (request.explanationLevel().isEmpty() ? "" : "<li><strong>Explanation Level:</strong> " + request.explanationLevel() + "</li>") +
                                                   "</ul>" +

                                                   "<h2>📖 Textual Explanation Guidelines</h2>" +
                                                   "<ul>" +
                                                   (request.explanationLevel().equalsIgnoreCase("detailed") ?
                                                           "<li>Provide a <strong>comprehensive, in-depth explanation</strong> with detailed analysis.</li>" +
                                                                   "<li>Include <strong>examples, real-world applications, historical context</strong>, and case studies if relevant.</li>" :
                                                           request.explanationLevel().equalsIgnoreCase("moderate") ?
                                                                   "<li>Give a <strong>balanced explanation</strong> covering key points with some examples.</li>" +
                                                                           "<li>Maintain clarity while avoiding excessive depth.</li>" :
                                                                   "<li>Summarize the topic <strong>concisely</strong> while covering core concepts.</li>" +
                                                                           "<li>Keep explanations <strong>short, direct, and impactful</strong>.</li>"
                                                   ) +
                                                   "</ul>" +

                                                   "<p><strong>IMPORTANT:</strong> The response must contain <strong>only valid HTML tags</strong> that hold content. <strong>Do NOT use Markdown formatting or unnecessary wrappers.</strong></p>"
                                   )
                           }
                   )
           );

       } else if (request.category().toLowerCase().contains("coding")) {
           requestBody.put(
                   "contents", Map.of(
                           "parts", new Object[]{
                                   Map.of("text",
                                           "<h2>🎯 Role: Expert Programming Tutor</h2>" +
                                                   "<p>You are a <strong>highly skilled programming tutor</strong>, specializing in <strong>conceptual clarity and deep coding insights</strong>. " +
                                                   "Your primary goal is to <strong>explain topics thoroughly, showcase examples, generate optimized code with comments, and provide a step-by-step breakdown</strong>.</p>" +

                                                   "<h2>📌 Request Details</h2>" +
                                                   "<ul>" +
                                                   "<li><strong>Category:</strong> " + request.category() + "</li>" +
                                                   "<li><strong>Subject:</strong> " + request.subject() + "</li>" +
                                                   "<li><strong>Topic:</strong> " + request.topic() + "</li>" +
                                                   "<li><strong>Keywords:</strong> " + request.additionalKeywords() + "</li>" +
                                                   "<li><strong>Explanation Level:</strong> " + request.explanationLevel() + "</li>" +
                                                   "</ul>" +

                                                   "<h2>🚀 Response Format</h2>" +
                                                   "<p><strong>IMPORTANT:</strong> The response must be in **pure HTML content** format. Do NOT use Markdown or include `<html>`, `<head>`, or `<body>` tags.</p>" +
                                                   "<p>Use valid HTML tags like <code>&lt;h2&gt;</code>, <code>&lt;p&gt;</code>, <code>&lt;ul&gt;</code>, <code>&lt;li&gt;</code>, <code>&lt;pre&gt;&lt;code&gt;</code>, etc.</p>" +

                                                   "<h2>📝 Response Structure</h2>" +
                                                   "<h3>1️⃣ Topic Overview</h3>" +
                                                   "<p>Provide a detailed explanation of the topic.</p>" +

                                                   "<h3>2️⃣ Example Snippets</h3>" +
                                                   "<pre><code class=\"language-" + request.subject().toLowerCase() + "\">" +
                                                   "// Example code snippets demonstrating core concepts" +
                                                   "</code></pre>" +

                                                   "<h3>3️⃣ Full Code Implementation</h3>" +
                                                   "<pre><code class=\"language-" + request.subject().toLowerCase() + "\">" +
                                                   "// Complete, optimized, well-structured code solution" +
                                                   "</code></pre>" +

                                                   "<h3>4️⃣ Step-by-Step Explanation</h3>" +
                                                   "<p>Break down the code line by line, explaining each step.</p>" +

                                                   "<h3>5️⃣ Additional Insights</h3>" +
                                                   "<p>Discuss best practices, optimizations, common mistakes, and real-world applications.</p>"
                                   )
                           }
                   )
           );


       }

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.value() == 429, response -> {
                    System.out.println("🔴 429 Too Many Requests – Retrying...");
                    return response.createException();
                })
                .bodyToMono(String.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))  // 3 retries with exponential backoff
                                .filter(e -> e instanceof WebClientResponseException.TooManyRequests)
                );
    }

    @Override
    public Mono<String> explainPartOfContent(AIExplainRequest request) {
        Map<String, Object> requestBody = Map.of(
                "contents", Map.of(
                        "parts", new Object[]{
                                Map.of("text",
                                        "You are an AI assistant that provides **clear and concise** explanations. " +
                                                "Follow the given guidelines to ensure accuracy and relevance.\n\n" +

                                                "### Context:\n" +
                                                "**Previously Generated Content:** " + request.generatedContent() + "\n\n" +

                                                "### Focus Area:\n" +
                                                "**Concept to Explain:** " + request.selectedLine() + "\n\n" +

                                                "### User's Specific Query:\n" +
                                                request.userQuestion() + "\n\n" +

                                                "### Explanation Guidelines:\n" +
                                                "- Keep it **short, precise, and to the point**.\n" +
                                                "- If explaining **code**, provide a **simple example** with an easy-to-understand breakdown.\n" +
                                                "- If **theoretical**, break it into **key bullet points** for clarity.\n" +
                                                "- **Do not add extra details beyond the given context.**\n" +
                                                "- Format the response (must consider) using **Markdown** for readability."
                                )
                        }
                )
        );




//        return webClient.post()
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.value() == 429, response -> {
                    System.out.println("🔴 429 Too Many Requests – Retrying...");
                    return response.createException();
                })
                .bodyToMono(String.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))  // 3 retries with exponential backoff
                                .filter(e -> e instanceof WebClientResponseException.TooManyRequests)
                );
    }

    public Mono<String> generateLabManual(LabManualGenerateRequest request) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(
                "contents", Map.of(
                        "parts", new Object[]{
                                Map.of("text",
                                        "Generate a well-structured lab manual for the subject '"+request.subjectName()+"' and experiment '"+request.experimentName()+"'. " +
                                                "Adhere strictly to the explanation level '"+request.explanationLevel()+"'. " +

                                                "The response must follow the exact structure of '"+request.responseFormat()+"', ensuring all content aligns correctly. " +
                                                "Format the response strictly in HTML, ensuring proper structure and hierarchy for rendering in Jodit Editor and smooth conversion to PDF using iText. " +

                                                "Strict Formatting Rules: " +
                                                "- Use proper heading tags (e.g., `<h2>` for sections, `<h3>` for subsections). " +
                                                "- Each paragraph must be inside `<p>` tags without excessive nesting or empty `<p>` elements. " +
                                                "- Lists must be formatted using `<ul>`/`<ol>` with `<li>` elements. " +
                                                "- Inline code snippets should use `<code>`, and block-level code should be inside `<pre><code>` for correct rendering. " +
                                                "- Avoid unnecessary `<br>` tags unless explicitly needed, and they must be self-closing (`<br />`). " +
                                                "- Do not include `<html>`, `<head>`, or `<body>` tags. " +
                                                "- Every HTML tag must be properly closed or self-closing to ensure valid syntax. " +

                                                "Content Structure Guidelines: " +
                                                "- Begin with an objective section in `<h2>` followed by a well-defined `<p>` explanation. " +
                                                "- Provide theoretical background in `<h2>Theory</h2>` followed by `<p>` content. " +
                                                "- Include a step-by-step experimental procedure using ordered (`<ol>`) or unordered (`<ul>`) lists. " +
                                                "- Ensure that all code snippets are wrapped inside `<pre><code>` to maintain structure when rendered. " +
                                                "- Include debugging tips and expected results in properly formatted sections. " +
                                                "- Conclude with `<h2>Conclusion</h2>` followed by a structured `<p>` summary." +

                                                "Ensure the output follows proper indentation and maintains a structured layout to prevent rendering issues in Jodit Editor and PDF conversion."
                                )
                        }
                )
        );


        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.value() == 429, response -> {
                    System.out.println("🔴 429 Too Many Requests – Retrying...");
                    return response.createException();
                })
                .bodyToMono(String.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))  // 3 retries with exponential backoff
                                .filter(e -> e instanceof WebClientResponseException.TooManyRequests)
                );
    }
}
