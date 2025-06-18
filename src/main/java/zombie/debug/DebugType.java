package zombie.debug;

import com.asledgehammer.crafthammer.util.console.ANSIUtils;
import zombie.util.StringUtils;

public enum DebugType {
   Packet,
   NetworkFileDebug,
   Network,
   General,
   Lua(ANSIUtils.ansiRGB8(135)),
   Mod,
   Sound,
   Zombie,
   Combat,
   Objects,
   Fireplace,
   Radio,
   MapLoading,
   Clothing,
   Animation,
   Asset,
   Script,
   Shader,
   Input,
   Recipe,
   ActionSystem,
   IsoRegion,
   UnitTests,
   FileIO,
   Multiplayer,
   Ownership,
   Death,
   Damage,
   Statistic,
   CraftHammer(ANSIUtils.ansiRGB8(248)),
   Security(ANSIUtils.ansiRGB8(218)),
   Sledgehammer(ANSIUtils.ansiRGB8(231)),
   Vehicle,
   Voice,
   Checksum;

   private final String label = StringUtils.leftJustify(this.toString(), 12);
   private final String color;

   private DebugType() {
      this.color = "";
   }

   private DebugType(String color) {
      this.color = color;
   }

   public String getLabel() {
      return this.label;
   }

   public String getColor() {
      return this.color;
   }

   public static boolean Do(DebugType var0) {
      return DebugLog.isEnabled(var0);
   }

   // $FF: synthetic method | What the fuck was this for???
   /*private static DebugType[] $values() {
      return new DebugType[]{Packet, NetworkFileDebug, Network, General, Lua, Mod, Sound, Zombie, Combat, Objects, Fireplace, Radio, MapLoading, Clothing, Animation, Asset, Script, Shader, Input, Recipe, ActionSystem, IsoRegion, UnitTests, FileIO, Multiplayer, Ownership, Death, Damage, Statistic, CraftHammer, Security, Sledgehammer, Vehicle, Voice, Checksum};
   }*/
}
