package zombie.debug;

import com.asledgehammer.crafthammer.api.event.log.LogEntry;
import com.asledgehammer.crafthammer.api.event.log.LogListener;
import com.asledgehammer.crafthammer.api.event.log.LogType;
import com.asledgehammer.crafthammer.util.console.ANSIUtils;
import com.asledgehammer.craftnail.util.log.SecurityLogger;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import zombie.ZomboidFileSystem;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigFile;
import zombie.config.ConfigOption;
import zombie.core.Core;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.network.GameServer;
import zombie.ui.UIDebugConsole;
import zombie.util.StringUtils;

public final class DebugLog {
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");
   public static final DebugLogStream Craftboid;
   public static final DebugLogStream Security;
   public static final DebugLogStream Sledgehammer;
   public static final Map listeners = new HashMap();
   private static final boolean[] m_enabledDebugTypes = new boolean[DebugType.values().length];
   private static final HashMap logLevels = new HashMap();
   private static boolean s_initialized = false;
   public static boolean printServerTime = false;
   private static final OutputStreamWrapper s_stdout;
   private static final OutputStreamWrapper s_stderr;
   private static final PrintStream m_originalOut;
   private static final PrintStream m_originalErr;
   private static final PrintStream GeneralErr;
   private static ZLogger s_logFileLogger;
   public static final DebugLogStream ActionSystem;
   public static final DebugLogStream Animation;
   public static final DebugLogStream Asset;
   public static final DebugLogStream Clothing;
   public static final DebugLogStream Combat;
   public static final DebugLogStream Damage;
   public static final DebugLogStream Death;
   public static final DebugLogStream FileIO;
   public static final DebugLogStream Fireplace;
   public static final DebugLogStream General;
   public static final DebugLogStream Input;
   public static final DebugLogStream IsoRegion;
   public static final DebugLogStream Lua;
   public static final DebugLogStream MapLoading;
   public static final DebugLogStream Mod;
   public static final DebugLogStream Multiplayer;
   public static final DebugLogStream Network;
   public static final DebugLogStream NetworkFileDebug;
   public static final DebugLogStream Packet;
   public static final DebugLogStream Objects;
   public static final DebugLogStream Radio;
   public static final DebugLogStream Recipe;
   public static final DebugLogStream Script;
   public static final DebugLogStream Shader;
   public static final DebugLogStream Sound;
   public static final DebugLogStream Statistic;
   public static final DebugLogStream UnitTests;
   public static final DebugLogStream Vehicle;
   public static final DebugLogStream Voice;
   public static final DebugLogStream Zombie;
   public static final int VERSION = 1;
   private static boolean inListeners = false;

   public DebugLog() {
      super();
   }

   public static void addListener(UUID id, LogListener listener) {
      List list = (List<LogListener>)listeners.computeIfAbsent(id, (k) -> {
         return new ArrayList();
      });
      if (!list.contains(listener)) {
         list.add(listener);
      }

   }

   public static void removeListener(UUID id, LogListener listener) {
      List list = (List<LogListener>)listeners.computeIfAbsent(id, (k) -> {
         return new ArrayList();
      });
      list.remove(listener);
      if (list.isEmpty()) {
         listeners.remove(id);
      }

   }

   public static void removeListeners(UUID id) {
      listeners.remove(id);
   }

   private static DebugLogStream createDebugLogStream(DebugType var0) {
      return new DebugLogStream(m_originalOut, m_originalOut, m_originalErr, new GenericDebugLogFormatter(var0));
   }

   public static void enableLog(DebugType var0) {
      setLogEnabled(var0, true);
   }

   public static void enableLog(DebugType var0, LogSeverity var1) {
      setLogEnabled(var0, true);
      logLevels.put(var0, var1);
   }

   public static LogSeverity getLogLevel(DebugType var0) {
      return (LogSeverity)logLevels.get(var0);
   }

   public static boolean isLogEnabled(DebugType var0, LogSeverity var1) {
      return var1.ordinal() >= ((LogSeverity)logLevels.getOrDefault(var0, LogSeverity.Warning)).ordinal();
   }

   public static boolean isLogEnabled(LogSeverity var0, DebugType var1) {
      return var0.ordinal() >= LogSeverity.Warning.ordinal() || isEnabled(var1);
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, "%s", var4) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12) : null;
   }

   public static String formatString(DebugType var0, LogSeverity var1, String var2, Object var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13) {
      return isLogEnabled(var1, var0) ? formatStringVarArgs(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13) : null;
   }

   public static String formatStringVarArgs(DebugType type, LogSeverity severity, String var2, Object var3, String var4, Object... var5) {
      if (!isLogEnabled(severity, type)) {
         return null;
      } else {
         Date date = new Date();
         String formatted = String.format(var4, var5);
         String nonColoredCompiled = "" + var3 + formatted;
         String errColor = "";
         if (severity != LogSeverity.Error && severity != LogSeverity.Trace) {
            if (severity == LogSeverity.Warning) {
               errColor = ANSIUtils.brightYellow();
            }
         } else {
            errColor = ANSIUtils.brightRed();
         }

         String compiled = type.getColor() + type.getLabel() + "> " + errColor + nonColoredCompiled + ANSIUtils.reset();
         if (!inListeners && !listeners.isEmpty()) {
            inListeners = true;
            LogType eType = LogType.valueOf(type.name());
            LogEntry entry = new LogEntry(date.getTime(), eType, formatted);
            Iterator var13 = listeners.keySet().iterator();

            while(var13.hasNext()) {
               UUID id = (UUID)var13.next();
               List list = (List)listeners.get(id);
               Iterator var16 = list.iterator();

               while(var16.hasNext()) {
                  LogListener listener = (LogListener)var16.next();

                  try {
                     listener.onLogEntry(entry);
                  } catch (Exception var19) {
                     System.err.println("Failed to execute log listener.");
                     var19.printStackTrace(System.err);
                  }
               }
            }

            inListeners = false;
         }

         if (type == DebugType.Security) {
            SecurityLogger.INSTANCE.queue(date, nonColoredCompiled);
         } else {
            echoToLogFile(ANSIUtils.strip(compiled));
         }

         return dateFormat.format(date) + "> " + compiled;
      }
   }

   private static void echoToLogFile(String var0) {
      if (s_logFileLogger == null) {
         if (s_initialized) {
            return;
         }

         s_logFileLogger = new ZLogger(GameServer.bServer ? "DebugLog-server" : "DebugLog", false);
      }

      try {
         s_logFileLogger.writeUnsafe(var0, null, false);
      } catch (Exception var2) {
         m_originalErr.println("Exception thrown writing to log file.");
         m_originalErr.println(var2);
         var2.printStackTrace(m_originalErr);
      }

   }

   public static boolean isEnabled(DebugType var0) {
      return m_enabledDebugTypes[var0.ordinal()];
   }

   public static void log(DebugType var0, String var1) {
      String var2 = formatString(var0, LogSeverity.General, "LOG  : ", "", "%s", var1);
      if (var2 != null) {
         m_originalOut.println(var2);
      }

   }

   public static void setLogEnabled(DebugType var0, boolean var1) {
      m_enabledDebugTypes[var0.ordinal()] = var1;
   }

   public static void log(Object var0) {
      log(DebugType.General, String.valueOf(var0));
   }

   public static void log(String var0) {
      log(DebugType.General, var0);
   }

   public static ArrayList getDebugTypes() {
      ArrayList var0 = new ArrayList(Arrays.asList(DebugType.values()));
      var0.sort((var0x, var1) -> {
         return String.CASE_INSENSITIVE_ORDER.compare(((DebugType)var0x).name(), ((DebugType)var1).name());
      });
      return var0;
   }

   public static void save() {
      ArrayList var0 = new ArrayList();
      DebugType[] var1 = DebugType.values();
      int var2 = var1.length;
      DebugType[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         DebugType debugType = var3[var5];
         BooleanConfigOption configOption = new BooleanConfigOption(debugType.name(), false);
         configOption.setValue(isEnabled(debugType));
         var0.add(configOption);
      }

      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var6 = var10000 + File.separator + "debuglog.ini";
      ConfigFile var7 = new ConfigFile();
      var7.write(var6, 1, var0);
   }

   public static void load() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      String var0 = var10000 + File.separator + "debuglog.ini";
      ConfigFile var1 = new ConfigFile();
      if (var1.read(var0)) {
         for(int var2 = 0; var2 < var1.getOptions().size(); ++var2) {
            ConfigOption var3 = (ConfigOption)var1.getOptions().get(var2);

            try {
               setLogEnabled(DebugType.valueOf(var3.getName()), StringUtils.tryParseBoolean(var3.getValueAsString()));
            } catch (Exception var6) {
            }
         }
      }

   }

   public static void setStdOut(OutputStream var0) {
      s_stdout.setStream(var0);
   }

   public static void setStdErr(OutputStream var0) {
      s_stderr.setStream(var0);
   }

   public static void init() {
      if (!s_initialized) {
         s_initialized = true;
         setStdOut(System.out);
         setStdErr(System.err);
         System.setOut(General);
         System.setErr(GeneralErr);
         if (!GameServer.bServer) {
            load();
         }

         s_logFileLogger = LoggerManager.getLogger(GameServer.bServer ? "DebugLog-server" : "DebugLog");
      }

   }

   static {
      s_stdout = new OutputStreamWrapper(System.out);
      s_stderr = new OutputStreamWrapper(System.err);
      m_originalOut = new PrintStream(s_stdout, true);
      m_originalErr = new PrintStream(s_stderr, true);
      GeneralErr = new DebugLogStream(m_originalErr, m_originalErr, m_originalErr, new GeneralErrorDebugLogFormatter());
      ActionSystem = createDebugLogStream(DebugType.ActionSystem);
      Animation = createDebugLogStream(DebugType.Animation);
      Asset = createDebugLogStream(DebugType.Asset);
      Clothing = createDebugLogStream(DebugType.Clothing);
      Combat = createDebugLogStream(DebugType.Combat);
      Damage = createDebugLogStream(DebugType.Damage);
      Death = createDebugLogStream(DebugType.Death);
      FileIO = createDebugLogStream(DebugType.FileIO);
      Fireplace = createDebugLogStream(DebugType.Fireplace);
      General = createDebugLogStream(DebugType.General);
      Input = createDebugLogStream(DebugType.Input);
      IsoRegion = createDebugLogStream(DebugType.IsoRegion);
      Lua = createDebugLogStream(DebugType.Lua);
      MapLoading = createDebugLogStream(DebugType.MapLoading);
      Mod = createDebugLogStream(DebugType.Mod);
      Multiplayer = createDebugLogStream(DebugType.Multiplayer);
      Network = createDebugLogStream(DebugType.Network);
      NetworkFileDebug = createDebugLogStream(DebugType.NetworkFileDebug);
      Packet = createDebugLogStream(DebugType.Packet);
      Objects = createDebugLogStream(DebugType.Objects);
      Radio = createDebugLogStream(DebugType.Radio);
      Recipe = createDebugLogStream(DebugType.Recipe);
      Script = createDebugLogStream(DebugType.Script);
      Shader = createDebugLogStream(DebugType.Shader);
      Sound = createDebugLogStream(DebugType.Sound);
      Statistic = createDebugLogStream(DebugType.Statistic);
      UnitTests = createDebugLogStream(DebugType.UnitTests);
      Vehicle = createDebugLogStream(DebugType.Vehicle);
      Security = createDebugLogStream(DebugType.Security);
      Craftboid = createDebugLogStream(DebugType.CraftHammer);
      Sledgehammer = createDebugLogStream(DebugType.Sledgehammer);
      Voice = createDebugLogStream(DebugType.Voice);
      Zombie = createDebugLogStream(DebugType.Zombie);
      enableLog(DebugType.Checksum, LogSeverity.Debug);
      enableLog(DebugType.Combat, LogSeverity.Debug);
      enableLog(DebugType.General, LogSeverity.Debug);
      enableLog(DebugType.IsoRegion, LogSeverity.Debug);
      enableLog(DebugType.Lua, LogSeverity.Debug);
      enableLog(DebugType.Mod, LogSeverity.Debug);
      enableLog(DebugType.Multiplayer, LogSeverity.Debug);
      enableLog(DebugType.Network, LogSeverity.Debug);
      enableLog(DebugType.Vehicle, LogSeverity.Debug);
      enableLog(DebugType.Voice, LogSeverity.Debug);
      if (GameServer.bServer) {
         enableLog(DebugType.Damage, LogSeverity.Debug);
         enableLog(DebugType.Death, LogSeverity.Debug);
         enableLog(DebugType.Statistic, LogSeverity.Debug);
         enableLog(DebugType.CraftHammer);
         enableLog(DebugType.Security);
         enableLog(DebugType.Sledgehammer);
      }

   }

   private static final class OutputStreamWrapper extends FilterOutputStream {
      public OutputStreamWrapper(OutputStream var1) {
         super(var1);
      }

      public void write(@NotNull byte[] var1, int var2, int var3) throws IOException {
         this.out.write(var1, var2, var3);
         if (Core.bDebug && UIDebugConsole.instance != null && DebugOptions.instance.UIDebugConsoleDebugLog.getValue()) {
            UIDebugConsole.instance.addOutput(var1, var2, var3);
         }

      }

      public void setStream(OutputStream var1) {
         this.out = var1;
      }
   }
}
