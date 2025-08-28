package com.theokanning.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The OpenAI resources used by a request
 */
@Data
public class Usage {
    /**
     * The number of prompt tokens used.
     */
    @JsonProperty("prompt_tokens")
    long promptTokens;

    /**
     * The number of completion tokens used.
     */
    @JsonProperty("completion_tokens")
    long completionTokens;

    /**
     * The number of total tokens used
     */
    @JsonProperty("total_tokens")
    long totalTokens;

    /**
     * Breakdown of tokens used in the prompt.
     */
    @JsonProperty("prompt_tokens_details")
    PromptTokensDetails promptTokensDetails;

    /**
     * Breakdown of tokens used in a completion.
     */
    @JsonProperty("completion_tokens_details")
    CompletionTokensDetails completionTokensDetails;

    public void add(Usage usage) {
        if(usage == null) {
            return;
        }
        this.promptTokens += usage.getPromptTokens();
        this.completionTokens += usage.getCompletionTokens();
        this.totalTokens += usage.getTotalTokens();
        if(usage.getPromptTokensDetails() != null) {
            if(this.promptTokensDetails == null) {
                this.promptTokensDetails = usage.getPromptTokensDetails();
            } else {
                this.promptTokensDetails.add(usage.getPromptTokensDetails());
            }
        }
        if(usage.getCompletionTokensDetails() != null) {
            if(this.completionTokensDetails == null) {
                this.completionTokensDetails = usage.getCompletionTokensDetails();
            } else {
                this.completionTokensDetails.add(usage.getCompletionTokensDetails());
            }
        }
    }
}
