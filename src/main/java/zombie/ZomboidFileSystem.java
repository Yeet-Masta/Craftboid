package zombie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.debug.DebugLog;
import zombie.debug.LogSeverity;
import zombie.gameStates.ChooseGameInfo;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.modding.ActiveMods;
import zombie.modding.ActiveModsFile;
import zombie.network.CoopMaster;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.StringUtils;

/* loaded from: craftboid.jar:zombie/ZomboidFileSystem.class */
public final class ZomboidFileSystem {
   public static final ZomboidFileSystem instance;
   private ArrayList<String> modFolders;
   private ArrayList<String> modFoldersOrder;
   public File base;
   public URI baseURI;
   private File workdir;
   private URI workdirURI;
   private File localWorkdir;
   private File anims;
   private URI animsURI;
   private File animsX;
   private URI animsXURI;
   private File animSets;
   private URI animSetsURI;
   private File actiongroups;
   private URI actiongroupsURI;
   private File cacheDir;
   static final /* synthetic */ boolean $assertionsDisabled;
   private final ArrayList<String> loadList = new ArrayList<>();
   private final Map<String, String> modIdToDir = new HashMap();
   private final Map<String, ChooseGameInfo.Mod> modDirToMod = new HashMap();
   public final HashMap<String, String> ActiveFileMap = new HashMap<>();
   private final HashSet<String> AllAbsolutePaths = new HashSet<>();
   private final ConcurrentHashMap<String, String> RelativeMap = new ConcurrentHashMap<>();
   public final ThreadLocal<Boolean> IgnoreActiveFileMap = ThreadLocal.withInitial(() -> {
      return Boolean.FALSE;
   });
   private final ConcurrentHashMap<String, URI> CanonicalURIMap = new ConcurrentHashMap<>();
   private final ArrayList<String> mods = new ArrayList<>();
   private final HashSet<String> LoadedPacks = new HashSet<>();
   private FileGuidTable m_fileGuidTable = null;
   private boolean m_fileGuidTableWatcherActive = false;
   private final PredicatedFileWatcher m_modFileWatcher = new PredicatedFileWatcher(this::isModFile, this::onModFileChanged);
   private final HashSet<String> m_watchedModFolders = new HashSet<>();
   private long m_modsChangedTime = 0;

   /* loaded from: craftboid.jar:zombie/ZomboidFileSystem$IWalkFilesVisitor.class */
   public interface IWalkFilesVisitor {
      void visit(File file, String str);
   }

   static {
      $assertionsDisabled = !ZomboidFileSystem.class.desiredAssertionStatus();
      instance = new ZomboidFileSystem();
   }

   private ZomboidFileSystem() {
   }

   public void init() throws IOException {
      this.base = new File("./").getAbsoluteFile().getCanonicalFile();
      this.baseURI = this.base.toURI();
      this.workdir = new File(this.base, "media").getAbsoluteFile().getCanonicalFile();
      this.workdirURI = this.workdir.toURI();
      this.localWorkdir = this.base.toPath().relativize(this.workdir.toPath()).toFile();
      this.anims = new File(this.workdir, "anims");
      this.animsURI = this.anims.toURI();
      this.animsX = new File(this.workdir, "anims_X");
      this.animsXURI = this.animsX.toURI();
      this.animSets = new File(this.workdir, "AnimSets");
      this.animSetsURI = this.animSets.toURI();
      this.actiongroups = new File(this.workdir, "actiongroups");
      this.actiongroupsURI = this.actiongroups.toURI();
      searchFolders(this.workdir);
      Iterator<String> it = this.loadList.iterator();
      while (it.hasNext()) {
         String s = it.next();
         String var2 = getRelativeFile(s);
         File var3 = new File(s).getAbsoluteFile();
         String var4 = var3.getAbsolutePath();
         if (var3.isDirectory()) {
            var4 = var4 + File.separator;
         }
         this.ActiveFileMap.put(var2.toLowerCase(Locale.ENGLISH), var4);
         this.AllAbsolutePaths.add(var4);
      }
      this.loadList.clear();
   }

   public File getCanonicalFile(File var1, String var2) {
      if (!var1.isDirectory()) {
         return new File(var1, var2);
      }
      File[] var3 = var1.listFiles((var1x, var2x) -> {
         return var2x.equalsIgnoreCase(var2);
      });
      return (var3 == null || var3.length == 0) ? new File(var1, var2) : var3[0];
   }

   public String getGameModeCacheDir() {
      if (Core.GameMode == null) {
         Core.GameMode = "Sandbox";
      }
      String var1 = getSaveDir();
      return var1 + File.separator + Core.GameMode;
   }

   public String getCurrentSaveDir() {
      String var10000 = getGameModeCacheDir();
      return var10000 + File.separator + Core.GameSaveWorld;
   }

   public String getFileNameInCurrentSave(String var1) {
      String var10000 = getCurrentSaveDir();
      return var10000 + File.separator + var1;
   }

   public String getFileNameInCurrentSave(String var1, String var2) {
      return getFileNameInCurrentSave(var1 + File.separator + var2);
   }

   public String getFileNameInCurrentSave(String var1, String var2, String var3) {
      return getFileNameInCurrentSave(var1 + File.separator + var2 + File.separator + var3);
   }

   public File getFileInCurrentSave(String var1) {
      return new File(getFileNameInCurrentSave(var1));
   }

   public File getFileInCurrentSave(String var1, String var2) {
      return new File(getFileNameInCurrentSave(var1, var2));
   }

   public File getFileInCurrentSave(String var1, String var2, String var3) {
      return new File(getFileNameInCurrentSave(var1, var2, var3));
   }

   public String getSaveDir() {
      String var1 = getCacheDirSub("Saves");
      ensureFolderExists(var1);
      return var1;
   }

   public String getSaveDirSub(String var1) {
      String var10000 = getSaveDir();
      return var10000 + File.separator + var1;
   }

   public String getScreenshotDir() {
      String var1 = getCacheDirSub("Screenshots");
      ensureFolderExists(var1);
      return var1;
   }

   public String getScreenshotDirSub(String var1) {
      String var10000 = getScreenshotDir();
      return var10000 + File.separator + var1;
   }

   public void setCacheDir(String var1) {
      this.cacheDir = new File(var1.replace("/", File.separator)).getAbsoluteFile();
      ensureFolderExists(this.cacheDir);
   }

   public String getCacheDir() {
      if (this.cacheDir == null) {
         String var1 = System.getProperty("deployment.user.cachedir");
         if (var1 == null || System.getProperty("os.name").startsWith("Win")) {
            var1 = System.getProperty("user.home");
         }
         String var2 = var1 + File.separator + "Zomboid";
         setCacheDir(var2);
      }
      return this.cacheDir.getPath();
   }

   public String getCacheDirSub(String var1) {
      String var10000 = getCacheDir();
      return var10000 + File.separator + var1;
   }

   public String getMessagingDir() {
      String var1 = getCacheDirSub("messaging");
      ensureFolderExists(var1);
      return var1;
   }

   public String getMessagingDirSub(String var1) {
      String var10000 = getMessagingDir();
      return var10000 + File.separator + var1;
   }

   public File getMediaRootFile() {
      if ($assertionsDisabled || this.workdir != null) {
         return this.workdir;
      }
      throw new AssertionError();
   }

   public String getMediaRootPath() {
      return this.workdir.getPath();
   }

   public File getMediaFile(String var1) {
      if ($assertionsDisabled || this.workdir != null) {
         return new File(this.workdir, var1);
      }
      throw new AssertionError();
   }

   public String getMediaPath(String var1) {
      return getMediaFile(var1).getPath();
   }

   public String getAbsoluteWorkDir() {
      return this.workdir.getPath();
   }

   public String getLocalWorkDir() {
      return this.localWorkdir.getPath();
   }

   public String getLocalWorkDirSub(String var1) {
      String var10000 = getLocalWorkDir();
      return var10000 + File.separator + var1;
   }

   public String getAnimSetsPath() {
      return this.animSets.getPath();
   }

   public String getActionGroupsPath() {
      return this.actiongroups.getPath();
   }

   public static boolean ensureFolderExists(String var0) {
      return ensureFolderExists(new File(var0).getAbsoluteFile());
   }

   public static boolean ensureFolderExists(File var0) {
      return var0.exists() || var0.mkdirs();
   }

   public void searchFolders(File var1) {
      if (!GameServer.bServer) {
         Thread.yield();
         Core.getInstance().DoFrameReady();
      }
      if (var1.isDirectory()) {
         String var2 = var1.getAbsolutePath().replace("\\", "/").replace("./", "");
         if (var2.contains("media/maps/")) {
            this.loadList.add(var2);
         }
         String[] var3 = var1.list();
         for (String s : var3) {
            String var10003 = var1.getAbsolutePath();
            searchFolders(new File(var10003 + File.separator + s));
         }
         return;
      }
      this.loadList.add(var1.getAbsolutePath().replace("\\", "/").replace("./", ""));
   }

   public Object[] getAllPathsContaining(String var1) {
      ArrayList var2 = new ArrayList();
      for (Map.Entry<String, String> stringStringEntry : this.ActiveFileMap.entrySet()) {
         if (stringStringEntry.getKey().contains(var1)) {
            var2.add(stringStringEntry.getValue());
         }
      }
      return var2.toArray();
   }

   public Object[] getAllPathsContaining(String var1, String var2) {
      ArrayList var3 = new ArrayList();
      for (Map.Entry<String, String> stringStringEntry : this.ActiveFileMap.entrySet()) {
         if (stringStringEntry.getKey().contains(var1) && stringStringEntry.getKey().contains(var2)) {
            var3.add(stringStringEntry.getValue());
         }
      }
      return var3.toArray();
   }

   public synchronized String getString(String var1) {
      String var2;
      if (this.IgnoreActiveFileMap.get().booleanValue()) {
         return var1;
      }
      String var22 = var1.toLowerCase(Locale.ENGLISH);
      String var3 = this.RelativeMap.get(var22);
      if (var3 != null) {
         var2 = var3;
      } else {
         var2 = getRelativeFile(var1).toLowerCase(Locale.ENGLISH);
         this.RelativeMap.put(var22, var2);
      }
      String var4 = this.ActiveFileMap.get(var2);
      return var4 != null ? var4 : var1;
   }

   public synchronized boolean isKnownFile(String var1) {
      String var2;
      if (this.AllAbsolutePaths.contains(var1)) {
         return true;
      }
      String var22 = var1.toLowerCase(Locale.ENGLISH);
      String var3 = this.RelativeMap.get(var22);
      if (var3 != null) {
         var2 = var3;
      } else {
         var2 = getRelativeFile(var1).toLowerCase(Locale.ENGLISH);
         this.RelativeMap.put(var22, var2);
      }
      String var4 = this.ActiveFileMap.get(var2);
      return var4 != null;
   }

   public String getAbsolutePath(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      return this.ActiveFileMap.get(var2);
   }

   public void Reset() {
      this.loadList.clear();
      this.ActiveFileMap.clear();
      this.AllAbsolutePaths.clear();
      this.CanonicalURIMap.clear();
      this.modIdToDir.clear();
      this.modDirToMod.clear();
      this.mods.clear();
      this.modFolders = null;
      ActiveMods.Reset();
      if (this.m_fileGuidTable != null) {
         this.m_fileGuidTable.clear();
         this.m_fileGuidTable = null;
      }
   }

   public File getCanonicalFile(File var1) {
      try {
         return var1.getCanonicalFile();
      } catch (Exception e) {
         return var1.getAbsoluteFile();
      }
   }

   public File getCanonicalFile(String var1) {
      return getCanonicalFile(new File(var1));
   }

   public String getCanonicalPath(File var1) {
      try {
         return var1.getCanonicalPath();
      } catch (Exception e) {
         return var1.getAbsolutePath();
      }
   }

   public String getCanonicalPath(String var1) {
      return getCanonicalPath(new File(var1));
   }

   public URI getCanonicalURI(String var1) {
      URI var2 = this.CanonicalURIMap.get(var1);
      if (var2 == null) {
         var2 = getCanonicalFile(var1).toURI();
         this.CanonicalURIMap.put(var1, var2);
      }
      return var2;
   }

   public void resetModFolders() {
      this.modFolders = null;
   }

   public void getInstalledItemModsFolders(ArrayList<String> var1) {
      String[] var2;
      if (SteamUtils.isSteamModeEnabled() && (var2 = SteamWorkshop.instance.GetInstalledItemFolders()) != null) {
         for (String var6 : var2) {
            File var7 = new File(var6 + File.separator + "mods");
            if (var7.exists()) {
               var1.add(var7.getAbsolutePath());
            }
         }
      }
   }

   public void getStagedItemModsFolders(ArrayList<String> var1) {
      if (SteamUtils.isSteamModeEnabled()) {
         ArrayList var2 = SteamWorkshop.instance.getStageFolders();
         Iterator it = var2.iterator();
         while (it.hasNext()) {
            Object o = it.next();
            String var10002 = (String) o;
            File var4 = new File(var10002 + File.separator + "Contents" + File.separator + "mods");
            if (var4.exists()) {
               var1.add(var4.getAbsolutePath());
            }
         }
      }
   }

   private void getAllModFoldersAux(String var1, List<String> var2) {
      DirectoryStream.Filter<Path> var3 = var112 -> {
         return Files.isDirectory(var112, new LinkOption[0]) && Files.exists(var112.resolve("mod.info"), new LinkOption[0]);
      };
      Path var4 = FileSystems.getDefault().getPath(var1, new String[0]);
      if (Files.exists(var4, new LinkOption[0])) {
         try {
            DirectoryStream<Path> var5 = Files.newDirectoryStream(var4, var3);
            try {
               for (Path o : var5) {
                  if (o.getFileName().toString().equalsIgnoreCase("examplemod")) {
                     DebugLog.Mod.println("refusing to list " + o.getFileName());
                  } else {
                     String var8 = o.toAbsolutePath().toString();
                     if (!this.m_watchedModFolders.contains(var8)) {
                        this.m_watchedModFolders.add(var8);
                        DebugFileWatcher.instance.addDirectory(var8);
                        Path var9 = o.resolve("media");
                        if (Files.exists(var9, new LinkOption[0])) {
                           DebugFileWatcher.instance.addDirectoryRecurse(var9.toAbsolutePath().toString());
                        }
                     }
                     var2.add(var8);
                  }
               }
               var5.close();
            } finally {
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }
      }
   }

   public void setModFoldersOrder(String var1) {
      this.modFoldersOrder = new ArrayList<>(Arrays.asList(var1.split(",")));
   }

   public void getAllModFolders(List<String> var1) {
      if (this.modFolders == null) {
         this.modFolders = new ArrayList<>();
         if (this.modFoldersOrder == null) {
            setModFoldersOrder("workshop,steam,mods");
         }
         ArrayList<String> var2 = new ArrayList<>();
         for (int var3 = 0; var3 < this.modFoldersOrder.size(); var3++) {
            String var4 = this.modFoldersOrder.get(var3);
            if ("workshop".equals(var4)) {
               getStagedItemModsFolders(var2);
            }
            if ("steam".equals(var4)) {
               getInstalledItemModsFolders(var2);
            }
            if ("mods".equals(var4)) {
               String var10001 = Core.getMyDocumentFolder();
               var2.add(var10001 + File.separator + "mods");
            }
         }
         for (int var32 = 0; var32 < var2.size(); var32++) {
            String var42 = var2.get(var32);
            if (!this.m_watchedModFolders.contains(var42)) {
               this.m_watchedModFolders.add(var42);
               DebugFileWatcher.instance.addDirectory(var42);
            }
            getAllModFoldersAux(var42, this.modFolders);
         }
         DebugFileWatcher.instance.add(this.m_modFileWatcher);
      }
      var1.clear();
      var1.addAll(this.modFolders);
   }

   public ArrayList<ChooseGameInfo.Mod> getWorkshopItemMods(long var1) {
      ChooseGameInfo.Mod var11;
      ArrayList<ChooseGameInfo.Mod> var3 = new ArrayList<>();
      if (!SteamUtils.isSteamModeEnabled()) {
         return var3;
      }
      String var4 = SteamWorkshop.instance.GetItemInstallFolder(var1);
      if (var4 == null) {
         return var3;
      }
      File var5 = new File(var4 + File.separator + "mods");
      if (var5.exists() && var5.isDirectory()) {
         File[] var6 = var5.listFiles();
         int length = var6.length;
         for (File var10 : var6) {
            if (var10.isDirectory() && (var11 = ChooseGameInfo.readModInfo(var10.getAbsolutePath())) != null) {
               var3.add(var11);
            }
         }
         return var3;
      }
      return var3;
   }

   public ChooseGameInfo.Mod searchForModInfo(File var1, String var2, ArrayList<ChooseGameInfo.Mod> var3) {
      ChooseGameInfo.Mod var8;
      if (var1.isDirectory()) {
         String[] var4 = var1.list();
         if (var4 == null) {
            return null;
         }
         for (String s : var4) {
            String var10002 = var1.getAbsolutePath();
            File var6 = new File(var10002 + File.separator + s);
            ChooseGameInfo.Mod var7 = searchForModInfo(var6, var2, var3);
            if (var7 != null) {
               return var7;
            }
         }
         return null;
      }
      if (!var1.getAbsolutePath().endsWith("mod.info") || (var8 = ChooseGameInfo.readModInfo(var1.getAbsoluteFile().getParent())) == null) {
         return null;
      }
      if (!StringUtils.isNullOrWhitespace(var8.getId())) {
         this.modIdToDir.put(var8.getId(), var8.getDir());
         var3.add(var8);
      }
      if (var8.getId().equals(var2)) {
         return var8;
      }
      return null;
   }

   public void loadMod(String var1) {
      if (getModDir(var1) != null) {
         CoopMaster.instance.update();
         DebugLog.Mod.println("loading " + var1);
         File var2 = new File(getModDir(var1));
         URI var3 = var2.toURI();
         this.loadList.clear();
         searchFolders(var2);
         for (int var4 = 0; var4 < this.loadList.size(); var4++) {
            String var5 = getRelativeFile(var3, this.loadList.get(var4)).toLowerCase(Locale.ENGLISH);
            if (this.ActiveFileMap.containsKey(var5) && !var5.endsWith("mod.info") && !var5.endsWith("poster.png")) {
               DebugLog.Mod.println("mod \"" + var1 + "\" overrides " + var5);
            }
            String var6 = new File(this.loadList.get(var4)).getAbsolutePath();
            this.ActiveFileMap.put(var5, var6);
            this.AllAbsolutePaths.add(var6);
         }
         this.loadList.clear();
      }
   }

   private ArrayList<String> readLoadedDotTxt() {
      String var10000 = Core.getMyDocumentFolder();
      String var1 = var10000 + File.separator + "mods" + File.separator + "loaded.txt";
      File var2 = new File(var1);
      if (!var2.exists()) {
         return null;
      }
      ArrayList<String> var3 = new ArrayList<>();
      try {
         FileReader var4 = new FileReader(var1);
         try {
            BufferedReader var5 = new BufferedReader(var4);
            try {
               for (String var6 = var5.readLine(); var6 != null; var6 = var5.readLine()) {
                  String var62 = var6.trim();
                  if (!var62.isEmpty()) {
                     var3.add(var62);
                  }
               }
               var5.close();
               var4.close();
            } catch (Throwable var11) {
               try {
                  var5.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
               throw var11;
            }
         } finally {
         }
      } catch (Exception var13) {
         ExceptionLogger.logException(var13);
         var3 = null;
      }
      try {
         var2.delete();
      } catch (Exception var8) {
         ExceptionLogger.logException(var8);
      }
      return var3;
   }

   private ActiveMods readDefaultModsTxt() {
      ActiveMods var1 = ActiveMods.getById("default");
      ArrayList<String> var2 = readLoadedDotTxt();
      if (var2 != null) {
         var1.getMods().addAll(var2);
         saveModsFile();
      }
      var1.clear();
      String var10000 = Core.getMyDocumentFolder();
      String var3 = var10000 + File.separator + "mods" + File.separator + "default.txt";
      try {
         ActiveModsFile var4 = new ActiveModsFile();
         if (var4.read(var3, var1)) {
         }
      } catch (Exception var5) {
         ExceptionLogger.logException(var5);
      }
      return var1;
   }

   public void loadMods(String var1) {
      if (Core.OptionModsEnabled) {
         if (GameClient.bClient) {
            ArrayList<String> var5 = new ArrayList<>();
            loadTranslationMods(var5);
            var5.addAll(GameClient.instance.ServerMods);
            loadMods(var5);
            return;
         }
         ActiveMods var2 = ActiveMods.getById(var1);
         if (!"default".equalsIgnoreCase(var1)) {
            ActiveMods.setLoadedMods(var2);
            loadMods(var2.getMods());
            return;
         }
         try {
            ActiveMods var22 = readDefaultModsTxt();
            var22.checkMissingMods();
            var22.checkMissingMaps();
            ActiveMods.setLoadedMods(var22);
            loadMods(var22.getMods());
         } catch (Exception var4) {
            ExceptionLogger.logException(var4);
         }
      }
   }

   private boolean isTranslationMod(String var1) {
      ChooseGameInfo.Mod var2 = ChooseGameInfo.getAvailableModDetails(var1);
      if (var2 == null) {
         return false;
      }
      boolean var3 = false;
      File var4 = new File(var2.getDir());
      URI var5 = var4.toURI();
      this.loadList.clear();
      searchFolders(var4);
      Iterator<String> it = this.loadList.iterator();
      while (it.hasNext()) {
         String s = it.next();
         String var7 = getRelativeFile(var5, s);
         if (var7.endsWith(".lua") || var7.startsWith("media/maps/") || var7.startsWith("media/scripts/")) {
            return false;
         }
         if (var7.startsWith("media/lua/")) {
            if (!var7.startsWith("media/lua/shared/Translate/")) {
               return false;
            }
            var3 = true;
         }
      }
      this.loadList.clear();
      return var3;
   }

   private void loadTranslationMods(ArrayList<String> var1) {
      if (GameClient.bClient) {
         ActiveMods var2 = readDefaultModsTxt();
         ArrayList<String> var3 = new ArrayList<>();
         if (loadModsAux(var2.getMods(), var3) == null) {
            Iterator<String> it = var3.iterator();
            while (it.hasNext()) {
               String var5 = it.next();
               if (isTranslationMod(var5)) {
                  DebugLog.Mod.println("loading translation mod \"" + var5 + "\"");
                  if (!var1.contains(var5)) {
                     var1.add(var5);
                  }
               }
            }
         }
      }
   }

   private String loadModAndRequired(String var1, ArrayList<String> var2) {
      String var4;
      if (var1.isEmpty()) {
         return null;
      }
      if (var1.equalsIgnoreCase("examplemod")) {
         DebugLog.Mod.warn("refusing to load " + var1);
         return null;
      }
      if (var2.contains(var1)) {
         return null;
      }
      ChooseGameInfo.Mod var3 = ChooseGameInfo.getAvailableModDetails(var1);
      if (var3 == null) {
         if (GameServer.bServer) {
            GameServer.ServerMods.remove(var1);
         }
         DebugLog.Mod.warn("required mod \"" + var1 + "\" not found");
         return var1;
      }
      if (var3.getRequire() != null && (var4 = loadModsAux(var3.getRequire(), var2)) != null) {
         return var4;
      }
      var2.add(var1);
      return null;
   }

   public String loadModsAux(ArrayList<String> var1, ArrayList<String> var2) {
      Iterator<String> var3 = var1.iterator();
      while (var3.hasNext()) {
         String var4 = var3.next();
         String var5 = loadModAndRequired(var4, var2);
         if (var5 != null) {
            return var5;
         }
      }
      return null;
   }

   public void loadMods(ArrayList<String> var1) {
      this.mods.clear();
      Iterator<String> var2 = var1.iterator();
      while (var2.hasNext()) {
         String var3 = var2.next();
         loadModAndRequired(var3, this.mods);
      }
      Iterator<String> var22 = this.mods.iterator();
      while (var22.hasNext()) {
         String var32 = var22.next();
         loadMod(var32);
      }
   }

   public ArrayList<String> getModIDs() {
      return this.mods;
   }

   public String getModDir(String var1) {
      return this.modIdToDir.get(var1);
   }

   public ChooseGameInfo.Mod getModInfoForDir(String var1) {
      ChooseGameInfo.Mod var2 = this.modDirToMod.get(var1);
      if (var2 == null) {
         var2 = new ChooseGameInfo.Mod(var1);
         this.modDirToMod.put(var1, var2);
      }
      return var2;
   }

   public String getRelativeFile(File var1) {
      return getRelativeFile(this.baseURI, var1.getAbsolutePath());
   }

   public String getRelativeFile(String var1) {
      return getRelativeFile(this.baseURI, var1);
   }

   public String getRelativeFile(URI var1, File var2) {
      return getRelativeFile(var1, var2.getAbsolutePath());
   }

   public String getRelativeFile(URI var1, String var2) {
      URI var3 = getCanonicalURI(var2);
      URI var4 = getCanonicalURI(var1.getPath()).relativize(var3);
      if (var4.equals(var3)) {
         return var2;
      }
      String var5 = var4.getPath();
      if (var2.endsWith("/") && !var5.endsWith("/")) {
         var5 = var5 + "/";
      }
      return var5;
   }

   public String getAnimName(URI var1, File var2) {
      String var3 = getRelativeFile(var1, var2);
      String var4 = var3.toLowerCase(Locale.ENGLISH);
      int var5 = var4.lastIndexOf(46);
      if (var5 > -1) {
         var4 = var4.substring(0, var5);
      }
      if (var4.startsWith("anims/")) {
         var4 = var4.substring("anims/".length());
      } else if (var4.startsWith("anims_x/")) {
         var4 = var4.substring("anims_x/".length());
      }
      return var4;
   }

   public String resolveRelativePath(String var1, String var2) {
      Path var3 = Paths.get(var1, new String[0]);
      Path var4 = var3.getParent();
      Path var5 = var4.resolve(var2);
      String var6 = var5.toString();
      return getRelativeFile(var6);
   }

   public void saveModsFile() {
      try {
         String var10000 = Core.getMyDocumentFolder();
         ensureFolderExists(var10000 + File.separator + "mods");
         String var100002 = Core.getMyDocumentFolder();
         String var1 = var100002 + File.separator + "mods" + File.separator + "default.txt";
         ActiveModsFile var2 = new ActiveModsFile();
         var2.write(var1, ActiveMods.getById("default"));
      } catch (Exception var3) {
         ExceptionLogger.logException(var3);
      }
   }

   public void loadModPackFiles() {
      Iterator<String> it = this.mods.iterator();
      while (it.hasNext()) {
         String var2 = it.next();
         try {
            ChooseGameInfo.Mod var3 = ChooseGameInfo.getAvailableModDetails(var2);
            if (var3 != null) {
               Iterator it2 = var3.getPacks().iterator();
               while (it2.hasNext()) {
                  ChooseGameInfo.PackFile var5 = (ChooseGameInfo.PackFile) it2.next();
                  String var6 = getRelativeFile("media/texturepacks/" + var5.name + ".pack");
                  if (!this.ActiveFileMap.containsKey(var6.toLowerCase(Locale.ENGLISH))) {
                     DebugLog.Mod.warn("pack file \"" + var5.name + "\" needed by " + var2 + " not found");
                  } else {
                     String var7 = instance.getString("media/texturepacks/" + var5.name + ".pack");
                     if (!this.LoadedPacks.contains(var7)) {
                        GameWindow.LoadTexturePack(var5.name, var5.flags, var2);
                        this.LoadedPacks.add(var7);
                     }
                  }
               }
            }
         } catch (Exception var8) {
            ExceptionLogger.logException(var8);
         }
      }
      GameWindow.setTexturePackLookup();
   }

   public void loadModTileDefs() {
      HashSet<Integer> var1 = new HashSet<>();
      Iterator<String> it = this.mods.iterator();
      while (it.hasNext()) {
         String var3 = it.next();
         try {
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
            if (var4 != null) {
               Iterator it2 = var4.getTileDefs().iterator();
               while (it2.hasNext()) {
                  ChooseGameInfo.TileDef var6 = (ChooseGameInfo.TileDef) it2.next();
                  if (var1.contains(Integer.valueOf(var6.fileNumber))) {
                     DebugLog.Mod.error("tiledef fileNumber " + var6.fileNumber + " used by more than one mod");
                  } else {
                     String var7 = var6.name;
                     String var8 = getRelativeFile("media/" + var7 + ".tiles").toLowerCase(Locale.ENGLISH);
                     if (!this.ActiveFileMap.containsKey(var8)) {
                        DebugLog.Mod.error("tiledef file \"" + var6.name + "\" needed by " + var3 + " not found");
                     } else {
                        String var72 = this.ActiveFileMap.get(var8);
                        IsoWorld.instance.LoadTileDefinitions(IsoSpriteManager.instance, var72, var6.fileNumber);
                        var1.add(Integer.valueOf(var6.fileNumber));
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }
   }

   public void loadModTileDefPropertyStrings() {
      HashSet<Integer> var1 = new HashSet<>();
      Iterator<String> it = this.mods.iterator();
      while (it.hasNext()) {
         String var3 = it.next();
         try {
            ChooseGameInfo.Mod var4 = ChooseGameInfo.getAvailableModDetails(var3);
            if (var4 != null) {
               Iterator it2 = var4.getTileDefs().iterator();
               while (it2.hasNext()) {
                  ChooseGameInfo.TileDef var6 = (ChooseGameInfo.TileDef) it2.next();
                  if (var1.contains(Integer.valueOf(var6.fileNumber))) {
                     DebugLog.Mod.error("tiledef fileNumber " + var6.fileNumber + " used by more than one mod");
                  } else {
                     String var7 = var6.name;
                     String var8 = getRelativeFile("media/" + var7 + ".tiles").toLowerCase(Locale.ENGLISH);
                     if (!this.ActiveFileMap.containsKey(var8)) {
                        DebugLog.Mod.error("tiledef file \"" + var6.name + "\" needed by " + var3 + " not found");
                     } else {
                        String var72 = this.ActiveFileMap.get(var8);
                        IsoWorld.instance.LoadTileDefinitionsPropertyStrings(IsoSpriteManager.instance, var72, var6.fileNumber);
                        var1.add(Integer.valueOf(var6.fileNumber));
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }
   }

   public void loadFileGuidTable() {
      File var1 = instance.getMediaFile("fileGuidTable.xml");
      try {
         FileInputStream var2 = new FileInputStream(var1);
         try {
            JAXBContext var3 = JAXBContext.newInstance(new Class[]{FileGuidTable.class});
            Unmarshaller var4 = var3.createUnmarshaller();
            this.m_fileGuidTable = (FileGuidTable) var4.unmarshal(var2);
            this.m_fileGuidTable.setModID("game");
            var2.close();
            try {
               JAXBContext var18 = JAXBContext.newInstance(new Class[]{FileGuidTable.class});
               Unmarshaller var19 = var18.createUnmarshaller();
               Iterator<String> it = getModIDs().iterator();
               while (it.hasNext()) {
                  String var5 = it.next();
                  ChooseGameInfo.Mod var6 = ChooseGameInfo.getAvailableModDetails(var5);
                  if (var6 != null) {
                     try {
                        String var10002 = getModDir(var5);
                        FileInputStream var7 = new FileInputStream(var10002 + "/media/fileGuidTable.xml");
                        try {
                           FileGuidTable var8 = (FileGuidTable) var19.unmarshal(var7);
                           var8.setModID(var5);
                           this.m_fileGuidTable.mergeFrom(var8);
                           var7.close();
                        } catch (Throwable var12) {
                           try {
                              var7.close();
                           } catch (Throwable var11) {
                              var12.addSuppressed(var11);
                           }
                           throw var12;
                        }
                     } catch (FileNotFoundException e) {
                     } catch (Exception var14) {
                        ExceptionLogger.logException(var14);
                     }
                  }
               }
            } catch (Exception var17) {
               ExceptionLogger.logException(var17);
            }
            this.m_fileGuidTable.loaded();
            if (!this.m_fileGuidTableWatcherActive) {
               DebugFileWatcher.instance.add(new PredicatedFileWatcher("media/fileGuidTable.xml", var1x -> {
                  loadFileGuidTable();
               }));
               this.m_fileGuidTableWatcherActive = true;
            }
         } finally {
         }
      } catch (IOException | JAXBException var16) {
         System.err.println("Failed to load file Guid table.");
         ExceptionLogger.logException(var16);
      }
   }

   public FileGuidTable getFileGuidTable() {
      if (this.m_fileGuidTable == null) {
         loadFileGuidTable();
      }
      return this.m_fileGuidTable;
   }

   public String getFilePathFromGuid(String var1) {
      FileGuidTable var2 = getFileGuidTable();
      if (var2 != null) {
         return var2.getFilePathFromGuid(var1);
      }
      return null;
   }

   public String getGuidFromFilePath(String var1) {
      FileGuidTable var2 = getFileGuidTable();
      if (var2 != null) {
         return var2.getGuidFromFilePath(var1);
      }
      return null;
   }

   public String resolveFileOrGUID(String var1) {
      String var2 = var1;
      String var3 = getFilePathFromGuid(var1);
      if (var3 != null) {
         var2 = var3;
      }
      String var4 = var2.toLowerCase(Locale.ENGLISH);
      return this.ActiveFileMap.getOrDefault(var4, var2);
   }

   public boolean isValidFilePathGuid(String var1) {
      return getFilePathFromGuid(var1) != null;
   }

   public static File[] listAllDirectories(String var0, FileFilter var1, boolean var2) {
      File var3 = new File(var0).getAbsoluteFile();
      return listAllDirectories(var3, var1, var2);
   }

   public static File[] listAllDirectories(File var0, FileFilter var1, boolean var2) {
      if (!var0.isDirectory()) {
         return new File[0];
      }
      ArrayList<File> var3 = new ArrayList<>();
      listAllDirectoriesInternal(var0, var1, var2, var3);
      return (File[]) var3.toArray(new File[0]);
   }

   private static void listAllDirectoriesInternal(File var0, FileFilter var1, boolean var2, ArrayList<File> var3) {
      File[] var4 = var0.listFiles();
      if (var4 != null) {
         int length = var4.length;
         for (File var8 : var4) {
            if (!var8.isFile() && var8.isDirectory()) {
               if (var1.accept(var8)) {
                  var3.add(var8);
               }
               if (var2) {
                  listAllFilesInternal(var8, var1, true, var3);
               }
            }
         }
      }
   }

   public static File[] listAllFiles(String var0, FileFilter var1, boolean var2) {
      File var3 = new File(var0).getAbsoluteFile();
      return listAllFiles(var3, var1, var2);
   }

   public static File[] listAllFiles(File var0, FileFilter var1, boolean var2) {
      if (!var0.isDirectory()) {
         return new File[0];
      }
      ArrayList<File> var3 = new ArrayList<>();
      listAllFilesInternal(var0, var1, var2, var3);
      return (File[]) var3.toArray(new File[0]);
   }

   private static void listAllFilesInternal(File var0, FileFilter var1, boolean var2, ArrayList<File> var3) {
      File[] var4 = var0.listFiles();
      if (var4 != null) {
         int length = var4.length;
         for (File var8 : var4) {
            if (var8.isFile()) {
               if (var1.accept(var8)) {
                  var3.add(var8);
               }
            } else if (var8.isDirectory() && var2) {
               listAllFilesInternal(var8, var1, true, var3);
            }
         }
      }
   }

   public void walkGameAndModFiles(String var1, boolean var2, IWalkFilesVisitor var3) {
      walkGameAndModFilesInternal(this.base, var1, var2, var3);
      ArrayList<String> var4 = getModIDs();
      Iterator<String> it = var4.iterator();
      while (it.hasNext()) {
         String o = it.next();
         String var6 = getModDir(o);
         if (var6 != null) {
            walkGameAndModFilesInternal(new File(var6), var1, var2, var3);
         }
      }
   }

   private void walkGameAndModFilesInternal(File var1, String var2, boolean var3, IWalkFilesVisitor var4) {
      File[] var6;
      File var5 = new File(var1, var2);
      if (var5.isDirectory() && (var6 = var5.listFiles()) != null) {
         int length = var6.length;
         for (File var10 : var6) {
            var4.visit(var10, var2);
            if (var3 && var10.isDirectory()) {
               walkGameAndModFilesInternal(var1, var2 + "/" + var10.getName(), true, var4);
            }
         }
      }
   }

   public String[] resolveAllDirectories(String var1, FileFilter var2, boolean var3) {
      ArrayList<String> var4 = new ArrayList<>();
      walkGameAndModFiles(var1, var3, (var2x, var3x) -> {
         if (var2x.isDirectory() && var2.accept(var2x)) {
            String var4x = var3x + "/" + var2x.getName();
            if (!var4.contains(var4x)) {
               var4.add(var4x);
            }
         }
      });
      return (String[]) var4.toArray(new String[0]);
   }

   public String[] resolveAllFiles(String var1, FileFilter var2, boolean var3) {
      ArrayList<String> var4 = new ArrayList<>();
      walkGameAndModFiles(var1, var3, (var2x, var3x) -> {
         if (var2x.isFile() && var2.accept(var2x)) {
            String var4x = var3x + "/" + var2x.getName();
            if (!var4.contains(var4x)) {
               var4.add(var4x);
            }
         }
      });
      return (String[]) var4.toArray(new String[0]);
   }

   public String normalizeFolderPath(String var1) {
      return (var1.toLowerCase(Locale.ENGLISH).replace('\\', '/') + "/").replace("///", "/").replace("//", "/");
   }

   public static String processFilePath(String var0, char var1) {
      if (var1 != '\\') {
         var0 = var0.replace('\\', var1);
      }
      if (var1 != '/') {
         var0 = var0.replace('/', var1);
      }
      return var0;
   }

   public boolean tryDeleteFile(String var1) {
      if (StringUtils.isNullOrWhitespace(var1)) {
         return false;
      }
      try {
         return deleteFile(var1);
      } catch (IOException | AccessControlException var3) {
         ExceptionLogger.logException(var3, String.format("Failed to delete file: \"%s\"", var1), DebugLog.FileIO, LogSeverity.General);
         return false;
      }
   }

   public boolean deleteFile(String var1) throws IOException {
      File var2 = new File(var1).getAbsoluteFile();
      if (!var2.isFile()) {
         throw new FileNotFoundException(String.format("File path not found: \"%s\"", var1));
      }
      if (var2.delete()) {
         DebugLog.FileIO.debugln("File deleted successfully: \"%s\"", var1);
         return true;
      }
      DebugLog.FileIO.debugln("Failed to delete file: \"%s\"", var1);
      return false;
   }

   public void update() {
      if (this.m_modsChangedTime != 0) {
         long var1 = System.currentTimeMillis();
         if (this.m_modsChangedTime <= var1) {
            this.m_modsChangedTime = 0L;
            this.modFolders = null;
            this.modIdToDir.clear();
            this.modDirToMod.clear();
            ChooseGameInfo.Reset();
            Iterator<String> it = getModIDs().iterator();
            while (it.hasNext()) {
               String var4 = it.next();
               ChooseGameInfo.getModDetails(var4);
            }
            LuaEventManager.triggerEvent("OnModsModified");
         }
      }
   }

   private boolean isModFile(String var1) {
      if (this.m_modsChangedTime > 0 || this.modFolders == null) {
         return false;
      }
      String var12 = var1.toLowerCase().replace('\\', '/');
      if (var12.endsWith("/mods/default.txt")) {
         return false;
      }
      Iterator<String> it = this.modFolders.iterator();
      while (it.hasNext()) {
         String modFolder = it.next();
         String var3 = modFolder.toLowerCase().replace('\\', '/');
         if (var12.startsWith(var3)) {
            return true;
         }
      }
      return false;
   }

   private void onModFileChanged(String var1) {
      this.m_modsChangedTime = System.currentTimeMillis() + 2000;
   }

   public void cleanMultiplayerSaves() {
      DebugLog.FileIO.println("Start cleaning save fs");
      String var1 = getSaveDir();
      String var2 = var1 + File.separator + "Multiplayer" + File.separator;
      File var3 = new File(var2);
      if (!var3.exists()) {
         var3.mkdir();
      }
      try {
         File[] var4 = var3.listFiles();
         int length = var4.length;
         for (File var8 : var4) {
            DebugLog.FileIO.println("Checking " + var8.getAbsoluteFile() + " dir");
            if (var8.isDirectory()) {
               String var10002 = var8.toString();
               File var9 = new File(var10002 + File.separator + "map.bin");
               if (var9.exists()) {
                  DebugLog.FileIO.println("Processing " + var8.getAbsoluteFile() + " dir");
                  try {
                     Stream<Path> var10 = Files.walk(var8.toPath(), new FileVisitOption[0]);
                     var10.forEach(var0 -> {
                        if (var0.getFileName().toString().matches("map_\\d+_\\d+.bin")) {
                           DebugLog.FileIO.println("Delete " + var0.getFileName());
                           var0.toFile().delete();
                        }
                     });
                  } catch (IOException var11) {
                     throw new RuntimeException(var11);
                  }
               } else {
                  continue;
               }
            }
         }
      } catch (RuntimeException var12) {
         var12.printStackTrace();
      }
   }

   public void resetDefaultModsForNewRelease(String var1) {
      ensureFolderExists(getCacheDirSub("mods"));
      String var10000 = getCacheDirSub("mods");
      String var2 = var10000 + File.separator + "reset-mods-" + var1 + ".txt";
      File var3 = new File(var2);
      if (!var3.exists()) {
         try {
            FileWriter var4 = new FileWriter(var3);
            try {
               BufferedWriter var5 = new BufferedWriter(var4);
               try {
                  var5.write("If this file does not exist, default.txt will be reset to empty (no mods active).");
                  var5.close();
                  var4.close();
                  ActiveMods var13 = ActiveMods.getById("default");
                  var13.clear();
                  saveModsFile();
               } catch (Throwable var10) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
                  throw var10;
               }
            } finally {
            }
         } catch (Exception var12) {
            ExceptionLogger.logException(var12);
         }
      }
   }
}