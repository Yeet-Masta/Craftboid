package zombie.commands.serverCommands;

import com.asledgehammer.craftnail.CraftNail;
import zombie.commands.CommandBase;
import zombie.commands.CommandHelp;
import zombie.commands.CommandName;
import zombie.commands.RequiredRight;
import zombie.core.logger.LoggerManager;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.network.ServerMap;

@CommandName(
   name = "quit"
)
@CommandHelp(
   helpText = "UI_ServerOptionDesc_Quit"
)
@RequiredRight(
   requiredRights = 32
)
public class QuitCommand extends CommandBase {
   public QuitCommand(String var1, String var2, String var3, UdpConnection var4) {
      super(var1, var2, var3, var4);
   }

   protected String Command() {
      DebugLog.Multiplayer.debugln(this.description);
      ServerMap.instance.QueueSaveAll();
      ServerMap.instance.QueueQuit();
      LoggerManager.getLogger("admin").write(this.getExecutorUsername() + " closed server");
      CraftNail.INSTANCE.stop(); //sure CraftNail should have been in java??? CraftNail.stop();
      return "Quit";
   }
}
