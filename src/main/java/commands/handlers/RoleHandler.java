package commands.handlers;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.function.Consumer;

public class RoleHandler implements Consumer<ChatInputInteractionEvent> {
    @Override
    public void accept(ChatInputInteractionEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title("Role")
                .description("Jeżeli chcesz dostać nową role wypełnij formularz weryfikacyjny")
                .addField("Zarząd Sekcji", "<@&1161706269682434168> -  Po prostu zarząd. Jeżeli masz problem, to właśnie do nich możesz się zwrócić.", false)
                .addField("Aktywny Działacz", "<@&1292154983587909652> - Drugi stopień wtajemniczenia. Rola dla aktywnych członków sekcji, którzy udzielają się w organizacji koła i pomagają prowadzić Sekcję.", false)
                .addField("Goście", "<@&1187531821064409279> - Rola dla zewnętrznych gości sekcji, n.p. wykładowców, przedstawicieli firm itd.", false)
                .addField("Specjalista Dziedzinowy", "<@&1292154754725711995> - Rola dla osób które są zainteresowane sztuczną inteligencją i pracą nad projektami, ale niekoniecznie zajmują się stricte Informatyką. Takie osoby posiadają za to wiedzę z innych dziedzin, n.p. matematyki, fizyki, lingwistyki, biologii ", false)
                .addField("Informatyk", "<@&1292154685691793551> -  Jak nazwa wskazuje", false)
                .addField("Projekt!", "<@&1191730598482554941> - Rola dla osób, które chcą angażować w pracę nad projektami ", false)
                .addField("AI enjoyers", "<@&1087822600463462493> -  Zweryfikowany członek sekcji", false)
                .addField("Politechnika Łódzka", "<@&1292156397399707748> - Student/Doktorant/Absolwent/Pracownik Politechniki Łódzkiej\n", false)
                .addField("Uniwersytet Łódzki", "<@&1292156608234655854> - Student/Doktorant/Absolwent/Pracownik Uniwersytetu Łódzkiego", false)
                .build();
        Button verification = Button.link("https://docs.google.com/forms/d/e/1FAIpQLSd0QivKoErs0QVrnCwmEDaiWRnKz62_5Vh4E2Oz7jOeIew7lA/viewform?usp=sf_link", "Weryfikacja");
        event.reply().withEmbeds(embed).withEphemeral(true).withComponents(ActionRow.of(verification)).block();
    }
}
