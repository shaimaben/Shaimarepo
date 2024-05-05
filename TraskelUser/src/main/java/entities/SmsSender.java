package entities;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.math.BigDecimal;
public class SmsSender {
    public static final String ACCOUNT_SID = "TWILIO_ACCOUNT_SID";
    public static final String AUTH_TOKEN = "TWILIO_AUTH_TOKEN";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+21651371144"),//receiver
                new com.twilio.type.PhoneNumber("+13347589348"),//sender
                "Bienvenue dans notre platforme Traskel!") .create();

        System.out.println(message.getSid());
    }
}
