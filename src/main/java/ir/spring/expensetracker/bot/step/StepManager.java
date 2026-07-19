package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.state.ConversationState;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StepManager {

    private final List<ConversationStepHandler> steps;

    public StepManager(List<ConversationStepHandler> steps) {
        this.steps = steps;
    }

    public Optional<ConversationStepHandler> findSteps(ConversationState state) {
        return steps.stream().filter(s -> s.getConversationState().equals(state)).findFirst();
    }
}
