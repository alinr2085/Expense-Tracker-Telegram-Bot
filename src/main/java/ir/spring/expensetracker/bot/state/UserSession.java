package ir.spring.expensetracker.bot.state;

import lombok.Data;

@Data
public class UserSession {
    private ConversationState conversationState;
    private MaintainingDraft maintainingDraft;

    public UserSession(ConversationState conversationState, MaintainingDraft maintainingDraft) {
        this.conversationState = conversationState;
        this.maintainingDraft = maintainingDraft;
    }
}
