package ru.vkbot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

public class Bot {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException
    {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
//        Keyboard keyboard = new Keyboard();
//
//        List<List<KeyboardButton>> allKey = new ArrayList<>();
//        List<KeyboardButton> line1 = new ArrayList<>();
//        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Привет").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
//        line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Кто я?").setType(KeyboardButtonActionType.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
//        allKey.add(line1);
//        keyboard.setButtons(allKey);
        GroupActor actor = new GroupActor(208208851, "5ba085e0313c4d6cd6ad92c10782dc440e8e6a22f8a243f884c70bcffdcb2c49b7f2e3eef779217e90b11");
        Integer ts = vk.messages().getLongPollServer(actor).execute().getTs();
        while (true){
            MessagesGetLongPollHistoryQuery historyQuery =  vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()){
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if (message.getText().equals("Начать")){
                            vk.messages().send(actor).message("Напишите номер группы.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else if (message.getText().toUpperCase().matches(" /^[А-Я]{1}[-]{1}[а-я]{2}[-]{1}[а-я]{1}[-]{1}[0-9]{4}$/")) {
                            Data data = new Data();
                            vk.messages().send(actor).message(data.find(message.getText().toUpperCase())).userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else {
                            vk.messages().send(actor).message("Неверный формат группы.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                    }
                    catch (ApiException | ClientException | IOException e) {e.printStackTrace();}
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}
