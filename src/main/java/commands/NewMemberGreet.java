package commands;

import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class NewMemberGreet implements Consumer<MemberJoinEvent> {
    @Override
    public void accept(MemberJoinEvent memberJoinEvent) {
        EmbedCreateSpec spec = EmbedCreateSpec.builder().title("**Cześć!**")
                .description("Witaj na serwerze Discord Sekcji AI!\n\n" +
                        "Sekcja Sztucznej Inteligencji zajmuje się ogólnopojętą tematyką AI. Chcemy, aby każdy członek Sekcji miał możliwość rozwijania się na własnych zasadach i we własnym tempie. Na spotkaniach sekcji dzielimy się przemyśleniami, omawiamy postępy projektów oraz zapraszamy gości z zewnątrz, którzy dzielą się swoją wiedzą i doświadczeniem. Niezależnie od tego, czy dopiero zaczynasz swoją przygodę z AI, czy masz już doświadczenie w tym obszarze, jesteśmy miejscem dla Ciebie!\n\n" +
                        "**Aby rozpocząć wypełnij formularz weryfikacyjny.**\n\n" +
                        "(komendy na serwerze)\n" +
                        "Jeżeli potrzebujesz pomocy, napisz /help lub poproś o pomoc kogoś z Zarządu Sekcji\n" +
                        "Jeżeli chcesz zapoznać się z rolami na serwerze napisz /role\n\n" +
                        "Jeżeli chcesz dołączyć do projektu albo rozpocząć nowy projekt, wypełnij odpowiedni formularz. \n\n" +
                        "Zapraszamy też do zapoznania się z naszymi kontami na social mediach.")
                .thumbnail("https://scontent-waw2-1.xx.fbcdn.net/v/t39.30808-1/415127678_122115490724142773_470244489980226963_n.jpg?stp=c14.14.172.172a_dst-jpg_p200x200&_nc_cat=108&ccb=1-7&_nc_sid=f4b9fd&_nc_ohc=Eao8NnXJIDYQ7kNvgF94W-W&_nc_ht=scontent-waw2-1.xx&_nc_gid=AEMb51MINCiE1xCkzd39fPS&oh=00_AYDETcsqBqAXJqnDaodcdR55TITRJFkTG9hY3kMWNd10rw&oe=67080360")
                .addField("", "[Facebook](https://www.facebook.com/sekcja.ai.kino)", true)
                .addField("", "[LinkedIn](https://www.linkedin.com/company/sekcja-ai-kino)", true)
                .build();
        Button verification = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSd0QivKoErs0QVrnCwmEDaiWRnKz62_5Vh4E2Oz7jOeIew7lA/viewform?usp=sf_link", "Weryfikacja");
        Button newProject = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSeJDhzJo2wy9Op231N0Uda6ZZdFrzF39BxZgTafQ2ISxnAJ7g/viewform?usp=sf_link", "Nowy Projekt");
        Button join = Button.link("https://docs.google.com/forms/d/e/1FAIpQLScNwQMv_ie8SWmc2LFFMFqkkaItpa1QiU5V9H4KX0K6P-3XVA/viewform?usp=sf_link", "Dołącz do projektu");
        Button rules = Button.link("https://drive.google.com/file/d/159L_2ZfMwiTAQvhSdHXB08q_xSok3GMS/view?usp=drive_link", "Regulamin PŁ");
        Button rules_second = Button.link("https://drive.google.com/file/d/1H1gVk160c_vZcezslS7gxd7wCvKcT9ea/view?usp=drive_link", "Regulamin UŁ");
        Button rodo = Button.link("https://port.edu.p.lodz.pl/mod/page/view.php?id=1554&forceview=1", "Klauzula Informcyjna");
        memberJoinEvent.getMember().getPrivateChannel().block().createMessage(spec).withComponents(ActionRow.of(rules, rules_second, rodo), ActionRow.of(verification, newProject, join)).subscribe();
    }
}
