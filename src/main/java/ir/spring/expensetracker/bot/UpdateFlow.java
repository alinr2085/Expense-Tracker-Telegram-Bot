package ir.spring.expensetracker.bot;

import ir.spring.expensetracker.bot.command.CommandHandler;
import ir.spring.expensetracker.bot.command.CommandManager;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import ir.spring.expensetracker.bot.step.ConversationStepHandler;
import ir.spring.expensetracker.bot.step.StepManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class UpdateFlow {


    private final CommandManager commandManager;
    private final StepManager stepManager;
    private final DisplayMessage displayMessage;
    private final SessionManager sessionManager;

    public UpdateFlow(CommandManager commandManager, StepManager stepManager, DisplayMessage displayMessage, SessionManager sessionManager) {
        this.commandManager = commandManager;
        this.stepManager = stepManager;
        this.displayMessage = displayMessage;
        this.sessionManager = sessionManager;
    }

    public void dispatch(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Optional<CommandHandler> commandHandler = commandManager.findCommand(text);

            if (commandHandler.isPresent()) {
                commandHandler.get().handleCommand(update);
            } else {
                UserSession userSession = sessionManager.getUserSession(update.getMessage().getFrom().getId());
                if (userSession.getConversationState() == ConversationState.IDLE) {
                    displayMessage.displayMessage(update.getMessage().getChatId(), "حقیقتا نمیدونم چی میگی \uD83D\uDE10");
                } else {
                    Optional<ConversationStepHandler> stepHandler = stepManager.findSteps(userSession.getConversationState());
                    if (stepHandler.isPresent()) {
                        stepHandler.get().handle(update, userSession);
                    } else {
                        displayMessage.displayMessage(update.getMessage().getChatId(), "کیر میخوای ؟");
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            displayMessage.answerCallback(update.getCallbackQuery().getId());

            long telegramId = update.getCallbackQuery().getFrom().getId();
            UserSession userSession = sessionManager.getUserSession(telegramId);

            Optional<ConversationStepHandler> stepHandler = stepManager.findSteps(userSession.getConversationState());
            stepHandler.ifPresent(handler -> handler.handle(update, userSession));
        }
    }

}
