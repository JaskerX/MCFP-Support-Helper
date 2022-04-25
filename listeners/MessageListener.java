package de.jaskerx.listeners;

import java.util.List;

import de.jaskerx.main.InfoMessage;
import de.jaskerx.main.MCFPSupportHelper;
import de.jaskerx.main.Status;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public class MessageListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if (event.getChannelType().equals(ChannelType.TEXT)) {
			
			Member member = event.getMember();
			User user = event.getAuthor();
			String content = event.getMessage().getContentDisplay();
			TextChannel channel = event.getTextChannel();
																			// Velourship								// JaskerX
			if (content.equals("!sendTicketSelect") && (user.getId().equals("836678232484216933") || user.getId().equals("511297223901052949"))) {
				
				SelectMenu selMenu = SelectMenu.create("firstMenu")
						.setRequiredRange(0, 1)
						.setPlaceholder("Thema auswählen")
						.addOption("Minecraft Server", "mc")
						.addOption("Discord Server", "dc")
						.addOption("Website", "website")
						.addOption("Sonstige", "other")
						.build();
				ActionRow actionRow = ActionRow.of(selMenu);
				channel.sendMessage(new MessageBuilder().setContent("Bitte wähle das Thema deines Anliegens aus.").setActionRows(actionRow).build()).queue();
			}
			
			
			
			if (content.equals(MCFPSupportHelper.stopCmnd) && user.getId().equals("511297223901052949")) {
				
				Status.Stop();
			}
			
			if (content.equals("!close") && channel.getName().startsWith("ticket-") && !channel.getName().equals("ticket-updates")) {
				channel.delete().queue();
				closeTicket(Integer.valueOf(channel.getName().split("ticket-")[1]), user, event.getGuild());
			}
		
		}
	}
	
	private void closeTicket(int ticketNum, User closer, Guild guild) {
		
		SelectMenuListener.tickets.get(ticketNum).close(closer);
		SelectMenuListener.tickets.get(ticketNum).sendClosingMessagePrivat();
		List<TextChannel> channels = guild.getCategoriesByName("tickets", false).get(0).getTextChannels();
		channels.forEach(t -> {
			if (t.getName().equals("ticket-updates")) {
				SelectMenuListener.tickets.get(ticketNum).editUpdateMessage(t);
			};
		});
		SelectMenuListener.tickets.remove(ticketNum);
		MCFPSupportHelper.refreshTicketsInConfig();
	}
	
}
