package commands;

import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class NewMemberGreet implements Consumer<MemberJoinEvent> {
    @Override
    public void accept(MemberJoinEvent memberJoinEvent) {
        EmbedCreateSpec spec = EmbedCreateSpec.builder().title("Ważne rzeczy")
                .description("Tekst powitalny")
                .thumbnail("https://scontent-waw2-1.xx.fbcdn.net/v/t39.30808-1/415127678_122115490724142773_470244489980226963_n.jpg?stp=c14.14.172.172a_dst-jpg_p200x200&_nc_cat=108&ccb=1-7&_nc_sid=f4b9fd&_nc_ohc=Eao8NnXJIDYQ7kNvgF94W-W&_nc_ht=scontent-waw2-1.xx&_nc_gid=AEMb51MINCiE1xCkzd39fPS&oh=00_AYDETcsqBqAXJqnDaodcdR55TITRJFkTG9hY3kMWNd10rw&oe=67080360")
                .addField("", "[Facebook](https://www.facebook.com/sekcja.ai.kino)", true)
                .addField("", "[LinkedIn](https://www.linkedin.com/company/sekcja-ai-kino)", true)
                .build();
        Button verification = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSd0QivKoErs0QVrnCwmEDaiWRnKz62_5Vh4E2Oz7jOeIew7lA/viewform?usp=sf_link", "Weryfikacja");
        Button newProject = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSeJDhzJo2wy9Op231N0Uda6ZZdFrzF39BxZgTafQ2ISxnAJ7g/viewform?usp=sf_link", "Nowy Projekt");
        Button join = Button.link("https://docs.google.com/forms/d/e/1FAIpQLScNwQMv_ie8SWmc2LFFMFqkkaItpa1QiU5V9H4KX0K6P-3XVA/viewform?usp=sf_link", "Dołącz do projektu");
        memberJoinEvent.getMember().getPrivateChannel().flatMap(x->x.createMessage(spec).withComponents(ActionRow.of(verification, newProject, join))).subscribe();
    }
}
