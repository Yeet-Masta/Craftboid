package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.ai.states.FishingState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.RakNetPeerInterface;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.StringUtils;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleWindow;

/* loaded from: craftboid.jar:zombie/network/packets/EventPacket.class */
public class EventPacket implements INetworkPacket {
   public static final int MAX_PLAYER_EVENTS = 10;
   private static final long EVENT_TIMEOUT = 5000;
   private static final short EVENT_FLAGS_VAULT_OVER_SPRINT = 1;
   private static final short EVENT_FLAGS_VAULT_OVER_RUN = 2;
   private static final short EVENT_FLAGS_BUMP_FALL = 4;
   private static final short EVENT_FLAGS_BUMP_STAGGERED = 8;
   private static final short EVENT_FLAGS_ACTIVATE_ITEM = 16;
   private static final short EVENT_FLAGS_CLIMB_SUCCESS = 32;
   private static final short EVENT_FLAGS_CLIMB_STRUGGLE = 64;
   private static final short EVENT_FLAGS_BUMP_FROM_BEHIND = 128;
   private static final short EVENT_FLAGS_BUMP_TARGET_TYPE = 256;
   private static final short EVENT_FLAGS_PRESSED_MOVEMENT = 512;
   private static final short EVENT_FLAGS_PRESSED_CANCEL_ACTION = 1024;
   private static final short EVENT_FLAGS_SMASH_CAR_WINDOW = 2048;
   private static final short EVENT_FLAGS_FITNESS_FINISHED = 4096;
   private short id;
   public float x;
   public float y;
   public float z;
   private byte eventID;
   private String type1;
   private String type2;
   private String type3;
   private String type4;
   private float strafeSpeed;
   private float walkSpeed;
   private float walkInjury;
   private int booleanVariables;
   private short flags;
   private IsoPlayer player;
   private EventType event;
   private long timestamp;

   /* loaded from: craftboid.jar:zombie/network/packets/EventPacket$EventType.class */
   public enum EventType {
      EventSetActivatedPrimary,
      EventSetActivatedSecondary,
      EventFishing,
      EventFitness,
      EventEmote,
      EventClimbFence,
      EventClimbDownRope,
      EventClimbRope,
      EventClimbWall,
      EventClimbWindow,
      EventOpenWindow,
      EventCloseWindow,
      EventSmashWindow,
      EventSitOnGround,
      wasBumped,
      collideWithWall,
      EventUpdateFitness,
      EventFallClimb,
      EventOverrideItem,
      ChargeSpearConnect,
      Update,
      Unknown
   }

   @Override // zombie.network.packets.INetworkPacket
   public String getDescription() {
      short var10000 = this.id;
      return "[ player=" + var10000 + " \"" + (this.player == null ? "?" : this.player.getUsername()) + "\" | name=\"" + (this.event == null ? "?" : this.event.name()) + "\" | pos=( " + this.x + " ; " + this.y + " ; " + this.z + " ) | type1=\"" + this.type1 + "\" | type2=\"" + this.type2 + "\" | type3=\"" + this.type3 + "\" | type4=\"" + this.type4 + "\" | flags=" + this.flags + "\" | variables=" + this.booleanVariables + " ]";
   }

   @Override // zombie.network.packets.INetworkPacket
   public boolean isConsistent() {
      boolean var1 = (this.player == null || this.event == null) ? false : true;
      if (!var1 && Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, "[Event] is not consistent " + getDescription());
      }
      return var1;
   }

   @Override // zombie.network.packets.INetworkPacket
   public void parse(ByteBuffer var1, UdpConnection var2) {
      this.id = var1.getShort();
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.getFloat();
      this.eventID = var1.get();
      this.type1 = GameWindow.ReadString(var1);
      this.type2 = GameWindow.ReadString(var1);
      this.type3 = GameWindow.ReadString(var1);
      this.type4 = GameWindow.ReadString(var1);
      this.strafeSpeed = var1.getFloat();
      this.walkSpeed = var1.getFloat();
      this.walkInjury = var1.getFloat();
      this.booleanVariables = var1.getInt();
      this.flags = var1.getShort();
      if (this.eventID >= 0 && this.eventID < EventType.values().length) {
         this.event = EventType.values()[this.eventID];
      } else {
         DebugLog.Multiplayer.warn("Unknown event=" + this.eventID);
         this.event = null;
      }
      if (GameServer.bServer) {
         this.player = GameServer.IDToPlayerMap.get(Short.valueOf(this.id));
      } else if (GameClient.bClient) {
         this.player = (IsoPlayer) GameClient.IDToPlayerMap.get(Short.valueOf(this.id));
      } else {
         this.player = null;
      }
      this.timestamp = System.currentTimeMillis() + EVENT_TIMEOUT;
   }

   @Override // zombie.network.packets.INetworkPacket
   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putFloat(this.z);
      var1.putByte(this.eventID);
      var1.putUTF(this.type1);
      var1.putUTF(this.type2);
      var1.putUTF(this.type3);
      var1.putUTF(this.type4);
      var1.putFloat(this.strafeSpeed);
      var1.putFloat(this.walkSpeed);
      var1.putFloat(this.walkInjury);
      var1.putInt(this.booleanVariables);
      var1.putShort(this.flags);
   }

   public boolean isRelevant(UdpConnection var1) {
      return var1.RelevantTo(this.x, this.y);
   }

   public boolean isMovableEvent() {
      if (isConsistent()) {
         return EventType.EventClimbFence.equals(this.event) || EventType.EventFallClimb.equals(this.event);
      }
      return false;
   }

   private boolean requireNonMoving() {
      return isConsistent() && (EventType.EventClimbWindow.equals(this.event) || EventType.EventClimbFence.equals(this.event) || EventType.EventClimbDownRope.equals(this.event) || EventType.EventClimbRope.equals(this.event) || EventType.EventClimbWall.equals(this.event));
   }

   private IsoWindow getWindow(IsoPlayer var1) {
      IsoDirections[] var2 = IsoDirections.values();
      int length = var2.length;
      for (IsoDirections var5 : var2) {
         IsoWindow contextDoorOrWindowOrWindowFrame = (IsoWindow) var1.getContextDoorOrWindowOrWindowFrame(var5);
         if (contextDoorOrWindowOrWindowFrame instanceof IsoWindow) {
            return contextDoorOrWindowOrWindowFrame;
         }
      }
      return null;
   }

   private IsoObject getObject(IsoPlayer var1) {
      IsoDirections[] var2 = IsoDirections.values();
      int length = var2.length;
      for (IsoDirections var5 : var2) {
         IsoObject var6 = var1.getContextDoorOrWindowOrWindowFrame(var5);
         if ((var6 instanceof IsoWindow) || (var6 instanceof IsoThumpable) || IsoWindowFrame.isWindowFrame(var6)) {
            return var6;
         }
      }
      return null;
   }

   private IsoDirections checkCurrentIsEventGridSquareFence(IsoPlayer var1) {
      IsoDirections var2;
      IsoGridSquare var3 = var1.getCell().getGridSquare(this.x, this.y, this.z);
      IsoGridSquare var4 = var1.getCell().getGridSquare(this.x, this.y + 1.0f, this.z);
      IsoGridSquare var5 = var1.getCell().getGridSquare(this.x + 1.0f, this.y, this.z);
      if (var3 != null && var3.Is(IsoFlagType.HoppableN)) {
         var2 = IsoDirections.N;
      } else if (var3 != null && var3.Is(IsoFlagType.HoppableW)) {
         var2 = IsoDirections.W;
      } else if (var4 != null && var4.Is(IsoFlagType.HoppableN)) {
         var2 = IsoDirections.S;
      } else if (var5 != null && var5.Is(IsoFlagType.HoppableW)) {
         var2 = IsoDirections.E;
      } else {
         var2 = IsoDirections.Max;
      }
      return var2;
   }

   public boolean isTimeout() {
      return System.currentTimeMillis() > this.timestamp;
   }

   public void tryProcess() {
      if (isConsistent()) {
         if (this.player.networkAI.events.size() < 10) {
            this.player.networkAI.events.add(this);
         } else {
            DebugLog.Multiplayer.warn("Event skipped: " + getDescription());
         }
      }
   }

   public boolean process(IsoPlayer var1) {
      IsoGameCharacter var3;
      VehiclePart var4;
      boolean var2 = false;
      if (isConsistent()) {
         var1.overridePrimaryHandModel = null;
         var1.overrideSecondaryHandModel = null;
         if ((var1.getCurrentSquare() == var1.getCell().getGridSquare(this.x, this.y, this.z) && !var1.isPlayerMoving()) || !requireNonMoving()) {
            switch (AnonymousClass1.$SwitchMap$zombie$network$packets$EventPacket$EventType[this.event.ordinal()]) {
               case 1:
                  if (var1.getPrimaryHandItem() != null && var1.getPrimaryHandItem().canEmitLight()) {
                     var1.getPrimaryHandItem().setActivatedRemote((this.flags & 16) != 0);
                     var2 = true;
                     break;
                  }
                  break;
               case 2:
                  if (var1.getSecondaryHandItem() != null && var1.getSecondaryHandItem().canEmitLight()) {
                     var1.getSecondaryHandItem().setActivatedRemote((this.flags & 16) != 0);
                     var2 = true;
                     break;
                  }
                  break;
               case 3:
                  var1.setVariable("ClimbFenceOutcome", "fall");
                  var1.setVariable("BumpDone", true);
                  var1.setFallOnFront(true);
                  var2 = true;
                  break;
               case 4:
                  var1.setCollideType(this.type1);
                  var1.actionContext.reportEvent("collideWithWall");
                  var2 = true;
                  break;
               case RakNetPeerInterface.PacketReliability_UNRELIABLE_WITH_ACK_RECEIPT /* 5 */:
                  var1.setVariable("FishingStage", this.type1);
                  if (!FishingState.instance().equals(var1.getCurrentState())) {
                     var1.setVariable("forceGetUp", true);
                     var1.actionContext.reportEvent("EventFishing");
                  }
                  var2 = true;
                  break;
               case RakNetPeerInterface.PacketReliability_RELIABLE_WITH_ACK_RECEIPT /* 6 */:
                  var1.setVariable("ExerciseType", this.type1);
                  var1.setVariable("FitnessFinished", false);
                  var1.actionContext.reportEvent("EventFitness");
                  var2 = true;
                  break;
               case RakNetPeerInterface.PacketReliability_RELIABLE_ORDERED_WITH_ACK_RECEIPT /* 7 */:
                  var1.clearVariable("ExerciseHand");
                  var1.setVariable("ExerciseType", this.type2);
                  if (!StringUtils.isNullOrEmpty(this.type1)) {
                     var1.setVariable("ExerciseHand", this.type1);
                  }
                  var1.setFitnessSpeed();
                  if ((this.flags & EVENT_FLAGS_FITNESS_FINISHED) != 0) {
                     var1.setVariable("ExerciseStarted", false);
                     var1.setVariable("ExerciseEnded", true);
                  }
                  var1.setPrimaryHandItem((InventoryItem) null);
                  var1.setSecondaryHandItem((InventoryItem) null);
                  var1.overridePrimaryHandModel = this.type3;
                  var1.overrideSecondaryHandModel = this.type4;
                  var1.resetModelNextFrame();
                  var2 = true;
                  break;
               case EVENT_FLAGS_BUMP_STAGGERED /* 8 */:
                  var1.setVariable("emote", this.type1);
                  var1.actionContext.reportEvent("EventEmote");
                  var2 = true;
                  break;
               case 9:
                  var1.actionContext.reportEvent("EventSitOnGround");
                  var2 = true;
                  break;
               case 10:
                  var1.climbSheetRope();
                  var2 = true;
                  break;
               case 11:
                  var1.climbDownSheetRope();
                  var2 = true;
                  break;
               case 12:
                  IsoDirections var13 = checkCurrentIsEventGridSquareFence(var1);
                  if (var13 != IsoDirections.Max) {
                     var1.climbOverFence(var13);
                     if (var1.isSprinting()) {
                        var1.setVariable("VaultOverSprint", true);
                     }
                     if (var1.isRunning()) {
                        var1.setVariable("VaultOverRun", true);
                     }
                     var2 = true;
                     break;
                  }
                  break;
               case 13:
                  var1.setClimbOverWallStruggle((this.flags & EVENT_FLAGS_CLIMB_STRUGGLE) != 0);
                  var1.setClimbOverWallSuccess((this.flags & 32) != 0);
                  IsoDirections[] var12 = IsoDirections.values();
                  int length = var12.length;
                  for (IsoDirections var6 : var12) {
                     if (var1.climbOverWall(var6)) {
                        return true;
                     }
                  }
                  return false;
               case 14:
                  IsoObject object = getObject(var1);
                  if (object instanceof IsoWindow) {
                     var1.climbThroughWindow((IsoWindow) object);
                     var2 = true;
                  } else if (object instanceof IsoThumpable) {
                     var1.climbThroughWindow((IsoThumpable) object);
                     var2 = true;
                  }
                  if (IsoWindowFrame.isWindowFrame(object)) {
                     var1.climbThroughWindowFrame(object);
                     var2 = true;
                     break;
                  }
                  break;
               case 15:
                  IsoWindow var9 = getWindow(var1);
                  if (var9 != null) {
                     var1.openWindow(var9);
                     var2 = true;
                     break;
                  }
                  break;
               case 16:
                  IsoWindow var92 = getWindow(var1);
                  if (var92 != null) {
                     var1.closeWindow(var92);
                     var2 = true;
                     break;
                  }
                  break;
               case RakNetPeerInterface.ID_CONNECTION_ATTEMPT_FAILED /* 17 */:
                  if ((this.flags & EVENT_FLAGS_SMASH_CAR_WINDOW) != 0) {
                     BaseVehicle var8 = VehicleManager.instance.getVehicleByID(Short.parseShort(this.type1));
                     if (var8 != null && (var4 = var8.getPartById(this.type2)) != null) {
                        VehicleWindow var5 = var4.getWindow();
                        if (var5 != null) {
                           var1.smashCarWindow(var4);
                           var2 = true;
                           break;
                        }
                     }
                  } else {
                     IsoWindow var93 = getWindow(var1);
                     if (var93 != null) {
                        var1.smashWindow(var93);
                        var2 = true;
                        break;
                     }
                  }
                  break;
               case RakNetPeerInterface.ID_ALREADY_CONNECTED /* 18 */:
                  var1.setBumpDone(false);
                  var1.setVariable("BumpFallAnimFinished", false);
                  var1.setBumpType(this.type1);
                  var1.setBumpFallType(this.type2);
                  var1.setBumpFall((this.flags & 4) != 0);
                  var1.setBumpStaggered((this.flags & EVENT_FLAGS_BUMP_STAGGERED) != 0);
                  var1.reportEvent("wasBumped");
                  if (!StringUtils.isNullOrEmpty(this.type3) && !StringUtils.isNullOrEmpty(this.type4)) {
                     if ((this.flags & EVENT_FLAGS_BUMP_TARGET_TYPE) != 0) {
                        var3 = (IsoGameCharacter) GameClient.IDToZombieMap.get(Short.parseShort(this.type3));
                     } else {
                        var3 = (IsoGameCharacter) GameClient.IDToPlayerMap.get(Short.valueOf(Short.parseShort(this.type3)));
                     }
                     if (var3 != null) {
                        var3.setBumpType(this.type4);
                        var3.setHitFromBehind((this.flags & EVENT_FLAGS_BUMP_FROM_BEHIND) != 0);
                     }
                  }
                  var2 = true;
                  break;
               case RakNetPeerInterface.ID_NEW_INCOMING_CONNECTION /* 19 */:
                  if (var1.getNetworkCharacterAI().getAction() != null) {
                     var1.getNetworkCharacterAI().setOverride(true, this.type1, this.type2);
                  }
                  var2 = true;
                  break;
               case RakNetPeerInterface.ID_NO_FREE_INCOMING_CONNECTIONS /* 20 */:
                  var2 = true;
                  break;
               case RakNetPeerInterface.ID_DISCONNECTION_NOTIFICATION /* 21 */:
                  var1.networkAI.setPressedMovement((this.flags & 512) != 0);
                  var1.networkAI.setPressedCancelAction((this.flags & EVENT_FLAGS_PRESSED_CANCEL_ACTION) != 0);
                  var2 = true;
                  break;
               default:
                  DebugLog.Multiplayer.warn("[Event] unknown: " + getDescription());
                  var2 = true;
                  break;
            }
         }
      }
      return var2;
   }

   /* renamed from: zombie.network.packets.EventPacket$1, reason: invalid class name */
   /* loaded from: craftboid.jar:zombie/network/packets/EventPacket$1.class */
   static /* synthetic */ class AnonymousClass1 {
      static final /* synthetic */ int[] $SwitchMap$zombie$network$packets$EventPacket$EventType = new int[EventType.values().length];

      static {
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventSetActivatedPrimary.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventSetActivatedSecondary.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventFallClimb.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.collideWithWall.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventFishing.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventFitness.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventUpdateFitness.ordinal()] = 7;
         } catch (NoSuchFieldError e7) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventEmote.ordinal()] = EventPacket.EVENT_FLAGS_BUMP_STAGGERED;
         } catch (NoSuchFieldError e8) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventSitOnGround.ordinal()] = 9;
         } catch (NoSuchFieldError e9) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventClimbRope.ordinal()] = 10;
         } catch (NoSuchFieldError e10) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventClimbDownRope.ordinal()] = 11;
         } catch (NoSuchFieldError e11) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventClimbFence.ordinal()] = 12;
         } catch (NoSuchFieldError e12) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventClimbWall.ordinal()] = 13;
         } catch (NoSuchFieldError e13) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventClimbWindow.ordinal()] = 14;
         } catch (NoSuchFieldError e14) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventOpenWindow.ordinal()] = 15;
         } catch (NoSuchFieldError e15) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventCloseWindow.ordinal()] = 16;
         } catch (NoSuchFieldError e16) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventSmashWindow.ordinal()] = 17;
         } catch (NoSuchFieldError e17) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.wasBumped.ordinal()] = 18;
         } catch (NoSuchFieldError e18) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.EventOverrideItem.ordinal()] = 19;
         } catch (NoSuchFieldError e19) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.ChargeSpearConnect.ordinal()] = 20;
         } catch (NoSuchFieldError e20) {
         }
         try {
            $SwitchMap$zombie$network$packets$EventPacket$EventType[EventType.Update.ordinal()] = 21;
         } catch (NoSuchFieldError e21) {
         }
      }
   }

   /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
   /* JADX WARN: Removed duplicated region for block: B:101:0x03e0  */
   /* JADX WARN: Removed duplicated region for block: B:104:0x03f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
   public boolean set(zombie.characters.IsoPlayer r6, java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 1019
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
      throw new UnsupportedOperationException("Method not decompiled: zombie.network.packets.EventPacket.set(zombie.characters.IsoPlayer, java.lang.String):boolean");
   }

   public short getId() {
      return this.id;
   }

   public void setId(short id) {
      this.id = id;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getZ() {
      return this.z;
   }

   public void setZ(float z) {
      this.z = z;
   }

   public byte getEventID() {
      return this.eventID;
   }

   public void setEventID(byte eventID) {
      this.eventID = eventID;
   }

   public String getType1() {
      return this.type1;
   }

   public void setType1(String type1) {
      this.type1 = type1;
   }

   public String getType2() {
      return this.type2;
   }

   public void setType2(String type2) {
      this.type2 = type2;
   }

   public String getType3() {
      return this.type3;
   }

   public void setType3(String type3) {
      this.type3 = type3;
   }

   public String getType4() {
      return this.type4;
   }

   public void setType4(String type4) {
      this.type4 = type4;
   }

   public float getStrafeSpeed() {
      return this.strafeSpeed;
   }

   public void setStrafeSpeed(float strafeSpeed) {
      this.strafeSpeed = strafeSpeed;
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public void setWalkSpeed(float walkSpeed) {
      this.walkSpeed = walkSpeed;
   }

   public float getWalkInjury() {
      return this.walkInjury;
   }

   public void setWalkInjury(float walkInjury) {
      this.walkInjury = walkInjury;
   }

   public int getBooleanVariables() {
      return this.booleanVariables;
   }

   public void setBooleanVariables(int booleanVariables) {
      this.booleanVariables = booleanVariables;
   }

   public short getFlags() {
      return this.flags;
   }

   public void setFlags(short flags) {
      this.flags = flags;
   }

   public IsoPlayer getPlayer() {
      return this.player;
   }

   public void setPlayer(IsoPlayer player) {
      this.player = player;
   }

   public EventType getEvent() {
      return this.event;
   }

   public void setEvent(EventType event) {
      this.event = event;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }
}