package ir.spring.expensetracker.bot.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    private final Map<Long, UserSession> userSessionMap = new ConcurrentHashMap<Long, UserSession>();

    public UserSession getUserSession(long telegramId) {
            return userSessionMap.computeIfAbsent(telegramId, k -> new UserSession(ConversationState.IDLE, null));
    }

    public void updateState(long telegramId, ConversationState newState) {
        UserSession userSession = getUserSession(telegramId);
        userSession.setConversationState(newState);
    }

    public void clearSession(long telegramId) {
        userSessionMap.remove(telegramId);
    }
}
