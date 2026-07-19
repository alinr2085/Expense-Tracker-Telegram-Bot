package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.entity.Transaction;
import ir.spring.expensetracker.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FileCommand implements CommandHandler {
    private final TransactionRepository transactionRepository;
    private final DisplayMessage displayMessage;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public FileCommand(TransactionRepository transactionRepository, DisplayMessage displayMessage) {
        this.transactionRepository = transactionRepository;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCommandName() {
        return "دانلود فایل \uD83D\uDDC2\uFE0F";
    }

    @Override
    @Transactional
    public void handleCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Transaction> transactionList = transactionRepository.
                findByUser_TelegramIdOrderByDateDesc(update.getMessage().getFrom().getId());
        if (transactionList.isEmpty()) {
            displayMessage.displayMessage(update.getMessage().getChatId(), "تراکنشی برای نمایش وجود نداره \n اول برو یه تراکنشی اضافه کن");
            return;
        }
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Transactions History");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"تاریخ", "نوع تراکنش", "دسته بندی", "مبلغ", "توضیحات"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Transaction tx : transactionList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(DATE_FORMAT.format(tx.getDate()));
                row.createCell(1).setCellValue(tx.getType().toString());
                row.createCell(2).setCellValue(tx.getCategory().getName());
                row.createCell(3).setCellValue(tx.getAmount().doubleValue());
                row.createCell(4).setCellValue(tx.getDescription() != null ? tx.getDescription() : "");
            }

            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            displayMessage.sendFile(chatId, "بیا اینم فایل اکسل کل تاریخچه‌ت 📄", in, "My_Expenses.xlsx");

        } catch (Exception e) {
            displayMessage.displayMessage(chatId, "موقع ساخت فایل یه گوهی بالا اومد، دوباره تلاش کن.");
            e.printStackTrace();
        }
    }
}
