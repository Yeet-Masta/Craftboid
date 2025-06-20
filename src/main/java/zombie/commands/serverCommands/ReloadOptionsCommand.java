package zombie.commands.serverCommands;

import com.asledgehammer.craftnail.packet.security.PacketChecker;
import com.asledgehammer.craftnail.util.TextFilter;
import zombie.characters.SafetySystemManager;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.core.znet.SteamGameServer;
import zombie.core.znet.SteamUtils;
import zombie.network.CoopSlave;
import zombie.network.GameServer;
import zombie.network.ServerOptions;
import zombie.popman.ZombiePopulationManager;

@CommandName(
   name = "reloadoptions"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_ReloadOptions"
)
@RequiredRight(
   requiredRights = 32
)
public class ReloadOptionsCommand extends CommandBase {
   public ReloadOptionsCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      TextFilter.INSTANCE.init();
      PacketChecker.INSTANCE.init();
      ServerOptions.instance.init();
      GameServer.initClientCommandFilter();
      ZombiePopulationManager.instance.onConfigReloaded();
      GameServer.sendOptionsToClients();
      SafetySystemManager.updateOptions();
      GameServer.udpEngine.SetServerPassword(GameServer.udpEngine.hashServerPassword(ServerOptions.instance.Password.getValue()));
      if (SteamUtils.isSteamModeEnabled()) {
         SteamGameServer.SetServerName(ServerOptions.instance.PublicName.getValue());
         SteamGameServer.SetKeyValue("description", ServerOptions.instance.PublicDescription.getValue());
         SteamGameServer.SetKeyValue("open", ServerOptions.instance.Open.getValue() ? "1" : "0");
         SteamGameServer.SetKeyValue("public", ServerOptions.instance.Public.getValue() ? "1" : "0");
         SteamGameServer.SetKeyValue("mods", ServerOptions.instance.Mods.getValue());
         SteamGameServer.SetKeyValue("pvp", ServerOptions.instance.PVP.getValue() ? "1" : "0");
         if (ServerOptions.instance.Public.getValue()) {
            SteamGameServer.SetGameTags(CoopSlave.instance != null ? "hosted" : "");
         } else {
            SteamGameServer.SetGameTags("hidden" + (CoopSlave.instance != null ? ";hosted" : ""));
         }
      }

      LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " reloaded options");
      return "Options reloaded";
   }
}
