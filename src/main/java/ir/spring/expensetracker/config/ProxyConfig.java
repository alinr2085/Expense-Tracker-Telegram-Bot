package ir.spring.expensetracker.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class ProxyConfig {

    @Bean(value = "okClient")
    public OkHttpClient okClient(
            @Value("${proxy.hostname}") String hostname,
            @Value("${proxy.port}") int port
    ) {
        return new TelegramOkHttpClientFactory.SocksProxyOkHttpClientCreator(
                () -> new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostname, port))
        ).get();
    }

    @Bean
    public TelegramClient telegramClient(
            @Qualifier("okClient") OkHttpClient okClient,
            @Value("${telegram.bot.token}") String botToken
    ) {
        return new OkHttpTelegramClient(okClient, botToken);
    }

    @Bean(value = "telegramBotsApplication")
    public TelegramBotsLongPollingApplication telegramBotsApplication(
            @Qualifier("okClient") OkHttpClient okClient
    ) {
        return new TelegramBotsLongPollingApplication(ObjectMapper::new, () -> okClient);
    }
}
