package zombie.Lua;

import fmod.fmod.EmitterType;
import fmod.fmod.FMODAudio;
import fmod.fmod.FMODManager;
import fmod.fmod.FMODSoundBank;
import fmod.fmod.FMODSoundEmitter;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.luaj.kahluafork.compiler.FuncState;
import org.lwjglx.input.Controller;
import org.lwjglx.input.Controllers;
import org.lwjglx.input.KeyCodes;
import org.lwjglx.input.Keyboard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import se.krka.kahlua.converter.KahluaConverterManager;
import se.krka.kahlua.integration.LuaCaller;
import se.krka.kahlua.integration.LuaReturn;
import se.krka.kahlua.integration.annotations.LuaMethod;
import se.krka.kahlua.integration.expose.LuaJavaClassExposer;
import se.krka.kahlua.j2se.J2SEPlatform;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.luaj.compiler.LuaCompiler;
import se.krka.kahlua.vm.Coroutine;
import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.LuaClosure;
import se.krka.kahlua.vm.Platform;
import zombie.AmbientStreamManager;
import zombie.BaseAmbientStreamManager;
import zombie.BaseSoundManager;
import zombie.DummySoundManager;
import zombie.GameSounds;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.MapGroups;
import zombie.SandboxOptions;
import zombie.SoundManager;
import zombie.SystemDisabler;
import zombie.VirtualZombieManager;
import zombie.WorldSoundManager;
import zombie.ZombieSpawnRecorder;
import zombie.ZomboidFileSystem;
import zombie.ai.GameCharacterAIBrain;
import zombie.ai.MapKnowledge;
import zombie.ai.sadisticAIDirector.SleepingEvent;
import zombie.ai.states.AttackState;
import zombie.ai.states.BurntToDeath;
import zombie.ai.states.ClimbDownSheetRopeState;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbOverWallState;
import zombie.ai.states.ClimbSheetRopeState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.CloseWindowState;
import zombie.ai.states.CrawlingZombieTurnState;
import zombie.ai.states.FakeDeadAttackState;
import zombie.ai.states.FakeDeadZombieState;
import zombie.ai.states.FishingState;
import zombie.ai.states.FitnessState;
import zombie.ai.states.IdleState;
import zombie.ai.states.LungeState;
import zombie.ai.states.OpenWindowState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.PlayerActionsState;
import zombie.ai.states.PlayerAimState;
import zombie.ai.states.PlayerEmoteState;
import zombie.ai.states.PlayerExtState;
import zombie.ai.states.PlayerFallDownState;
import zombie.ai.states.PlayerFallingState;
import zombie.ai.states.PlayerGetUpState;
import zombie.ai.states.PlayerHitReactionPVPState;
import zombie.ai.states.PlayerHitReactionState;
import zombie.ai.states.PlayerKnockedDown;
import zombie.ai.states.PlayerOnGroundState;
import zombie.ai.states.PlayerSitOnGroundState;
import zombie.ai.states.PlayerStrafeState;
import zombie.ai.states.SmashWindowState;
import zombie.ai.states.StaggerBackState;
import zombie.ai.states.SwipeStatePlayer;
import zombie.ai.states.ThumpState;
import zombie.ai.states.WalkTowardState;
import zombie.ai.states.ZombieFallDownState;
import zombie.ai.states.ZombieGetDownState;
import zombie.ai.states.ZombieGetUpState;
import zombie.ai.states.ZombieIdleState;
import zombie.ai.states.ZombieOnGroundState;
import zombie.ai.states.ZombieReanimateState;
import zombie.ai.states.ZombieSittingState;
import zombie.asset.Asset;
import zombie.asset.AssetPath;
import zombie.audio.BaseSoundBank;
import zombie.audio.BaseSoundEmitter;
import zombie.audio.DummySoundBank;
import zombie.audio.DummySoundEmitter;
import zombie.audio.GameSound;
import zombie.audio.GameSoundClip;
import zombie.audio.parameters.ParameterRoomType;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.characters.AttachedItems.AttachedItem;
import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.AttachedItems.AttachedLocation;
import zombie.characters.AttachedItems.AttachedLocationGroup;
import zombie.characters.AttachedItems.AttachedLocations;
import zombie.characters.AttachedItems.AttachedWeaponDefinitions;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.BodyDamage.Fitness;
import zombie.characters.BodyDamage.Metabolics;
import zombie.characters.BodyDamage.Nutrition;
import zombie.characters.BodyDamage.Thermoregulator;
import zombie.characters.CharacterActionAnims;
import zombie.characters.CharacterSoundEmitter;
import zombie.characters.CharacterTimedActions.LuaTimedAction;
import zombie.characters.CharacterTimedActions.LuaTimedActionNew;
import zombie.characters.DummyCharacterSoundEmitter;
import zombie.characters.Faction;
import zombie.characters.HairOutfitDefinitions;
import zombie.characters.HaloTextHelper;
import zombie.characters.IsoDummyCameraCharacter;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoSurvivor;
import zombie.characters.IsoZombie;
import zombie.characters.Moodles.Moodle;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.Moodles.Moodles;
import zombie.characters.Safety;
import zombie.characters.Stats;
import zombie.characters.SurvivorDesc;
import zombie.characters.SurvivorFactory;
import zombie.characters.WornItems.BodyLocation;
import zombie.characters.WornItems.BodyLocationGroup;
import zombie.characters.WornItems.BodyLocations;
import zombie.characters.WornItems.WornItem;
import zombie.characters.WornItems.WornItems;
import zombie.characters.ZombiesZoneDefinition;
import zombie.characters.action.ActionGroup;
import zombie.characters.professions.ProfessionFactory;
import zombie.characters.skills.PerkFactory;
import zombie.characters.traits.ObservationFactory;
import zombie.characters.traits.TraitCollection;
import zombie.characters.traits.TraitFactory;
import zombie.chat.ChatBase;
import zombie.chat.ChatManager;
import zombie.chat.ChatMessage;
import zombie.chat.ServerChatMessage;
import zombie.commands.PlayerType;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigOption;
import zombie.config.DoubleConfigOption;
import zombie.config.EnumConfigOption;
import zombie.config.IntegerConfigOption;
import zombie.config.StringConfigOption;
import zombie.core.BoxedStaticValues;
import zombie.core.Clipboard;
import zombie.core.Color;
import zombie.core.Colors;
import zombie.core.Core;
import zombie.core.GameVersion;
import zombie.core.ImmutableColor;
import zombie.core.IndieFileLoader;
import zombie.core.Language;
import zombie.core.PerformanceSettings;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.Translator;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.input.Input;
import zombie.core.logger.ExceptionLogger;
import zombie.core.logger.LoggerManager;
import zombie.core.logger.ZLogger;
import zombie.core.math.PZMath;
import zombie.core.network.ByteBufferWriter;
import zombie.core.opengl.RenderThread;
import zombie.core.physics.Bullet;
import zombie.core.physics.WorldSimulation;
import zombie.core.properties.PropertyContainer;
import zombie.core.raknet.UdpConnection;
import zombie.core.raknet.VoiceManager;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AnimNodeAssetManager;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.ModelAssetManager;
import zombie.core.skinnedmodel.model.WorldItemModelDrawer;
import zombie.core.skinnedmodel.population.BeardStyle;
import zombie.core.skinnedmodel.population.BeardStyles;
import zombie.core.skinnedmodel.population.ClothingDecalGroup;
import zombie.core.skinnedmodel.population.ClothingDecals;
import zombie.core.skinnedmodel.population.ClothingItem;
import zombie.core.skinnedmodel.population.DefaultClothing;
import zombie.core.skinnedmodel.population.HairStyle;
import zombie.core.skinnedmodel.population.HairStyles;
import zombie.core.skinnedmodel.population.Outfit;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.skinnedmodel.visual.HumanVisual;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.core.stash.Stash;
import zombie.core.stash.StashBuilding;
import zombie.core.stash.StashSystem;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureID;
import zombie.core.znet.GameServerDetails;
import zombie.core.znet.ISteamWorkshopCallback;
import zombie.core.znet.ServerBrowser;
import zombie.core.znet.SteamFriend;
import zombie.core.znet.SteamFriends;
import zombie.core.znet.SteamUGCDetails;
import zombie.core.znet.SteamUser;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.core.znet.SteamWorkshopItem;
import zombie.debug.BooleanDebugOption;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.debug.LineDrawer;
import zombie.erosion.ErosionConfig;
import zombie.erosion.ErosionData;
import zombie.erosion.ErosionMain;
import zombie.erosion.season.ErosionSeason;
import zombie.gameStates.AnimationViewerState;
import zombie.gameStates.AttachmentEditorState;
import zombie.gameStates.ChooseGameInfo;
import zombie.gameStates.ConnectToServerState;
import zombie.gameStates.DebugChunkState;
import zombie.gameStates.DebugGlobalObjectState;
import zombie.gameStates.GameLoadingState;
import zombie.gameStates.GameState;
import zombie.gameStates.IngameState;
import zombie.gameStates.LoadingQueueState;
import zombie.gameStates.MainScreenState;
import zombie.gameStates.TermsOfServiceState;
import zombie.globalObjects.CGlobalObject;
import zombie.globalObjects.CGlobalObjectSystem;
import zombie.globalObjects.CGlobalObjects;
import zombie.globalObjects.SGlobalObject;
import zombie.globalObjects.SGlobalObjectSystem;
import zombie.globalObjects.SGlobalObjects;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.inventory.FixingManager;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.inventory.ItemType;
import zombie.inventory.RecipeManager;
import zombie.inventory.types.AlarmClock;
import zombie.inventory.types.AlarmClockClothing;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.ComboItem;
import zombie.inventory.types.Drainable;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.Food;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.InventoryContainer;
import zombie.inventory.types.Key;
import zombie.inventory.types.KeyRing;
import zombie.inventory.types.Literature;
import zombie.inventory.types.MapItem;
import zombie.inventory.types.Moveable;
import zombie.inventory.types.Radio;
import zombie.inventory.types.WeaponPart;
import zombie.inventory.types.WeaponType;
import zombie.iso.BentFences;
import zombie.iso.BrokenFences;
import zombie.iso.BuildingDef;
import zombie.iso.CellLoader;
import zombie.iso.ContainerOverlays;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoDirectionSet;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoHeatSource;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoLot;
import zombie.iso.IsoLuaMover;
import zombie.iso.IsoMarkers;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaChunk;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoObjectPicker;
import zombie.iso.IsoPuddles;
import zombie.iso.IsoPushableObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWaterGeometry;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.LosUtil;
import zombie.iso.MetaObject;
import zombie.iso.MultiStageBuilding;
import zombie.iso.RoomDef;
import zombie.iso.SearchMode;
import zombie.iso.SliceY;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.TileOverlays;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.iso.WorldMarkers;
import zombie.iso.areas.IsoBuilding;
import zombie.iso.areas.IsoRoom;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.areas.SafeHouse;
import zombie.iso.areas.isoregion.IsoRegionLogType;
import zombie.iso.areas.isoregion.IsoRegions;
import zombie.iso.areas.isoregion.IsoRegionsLogger;
import zombie.iso.areas.isoregion.IsoRegionsRenderer;
import zombie.iso.areas.isoregion.data.DataCell;
import zombie.iso.areas.isoregion.data.DataChunk;
import zombie.iso.areas.isoregion.regions.IsoChunkRegion;
import zombie.iso.areas.isoregion.regions.IsoWorldRegion;
import zombie.iso.objects.BSFurnace;
import zombie.iso.objects.IsoBarbecue;
import zombie.iso.objects.IsoBarricade;
import zombie.iso.objects.IsoBrokenGlass;
import zombie.iso.objects.IsoCarBatteryCharger;
import zombie.iso.objects.IsoClothingDryer;
import zombie.iso.objects.IsoClothingWasher;
import zombie.iso.objects.IsoCombinationWasherDryer;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoCurtain;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoFire;
import zombie.iso.objects.IsoFireManager;
import zombie.iso.objects.IsoFireplace;
import zombie.iso.objects.IsoGenerator;
import zombie.iso.objects.IsoJukebox;
import zombie.iso.objects.IsoLightSwitch;
import zombie.iso.objects.IsoMannequin;
import zombie.iso.objects.IsoMolotovCocktail;
import zombie.iso.objects.IsoRadio;
import zombie.iso.objects.IsoStackedWasherDryer;
import zombie.iso.objects.IsoStove;
import zombie.iso.objects.IsoTelevision;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTrap;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWaveSignal;
import zombie.iso.objects.IsoWheelieBin;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.ObjectRenderEffects;
import zombie.iso.objects.RainManager;
import zombie.iso.objects.interfaces.BarricadeAble;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteGrid;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.iso.weather.ClimateColorInfo;
import zombie.iso.weather.ClimateForecaster;
import zombie.iso.weather.ClimateHistory;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.ClimateMoon;
import zombie.iso.weather.ClimateValues;
import zombie.iso.weather.Temperature;
import zombie.iso.weather.ThunderStorm;
import zombie.iso.weather.WeatherPeriod;
import zombie.iso.weather.WorldFlares;
import zombie.iso.weather.fog.ImprovedFog;
import zombie.iso.weather.fx.IsoWeatherFX;
import zombie.modding.ActiveMods;
import zombie.modding.ActiveModsFile;
import zombie.modding.ModUtilsJava;
import zombie.network.ConnectionManager;
import zombie.network.CoopMaster;
import zombie.network.DBResult;
import zombie.network.DBTicket;
import zombie.network.DesktopBrowser;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ItemTransactionManager;
import zombie.network.MPStatistic;
import zombie.network.MPStatistics;
import zombie.network.NetChecksum;
import zombie.network.NetworkAIParams;
import zombie.network.PacketTypes;
import zombie.network.Server;
import zombie.network.ServerOptions;
import zombie.network.ServerSettings;
import zombie.network.ServerSettingsManager;
import zombie.network.ServerWorldDatabase;
import zombie.network.Userlog;
import zombie.network.chat.ChatServer;
import zombie.network.chat.ChatType;
import zombie.popman.ZombiePopulationManager;
import zombie.popman.ZombiePopulationRenderer;
import zombie.profanity.ProfanityFilter;
import zombie.radio.ChannelCategory;
import zombie.radio.RadioAPI;
import zombie.radio.RadioData;
import zombie.radio.StorySounds.DataPoint;
import zombie.radio.StorySounds.EventSound;
import zombie.radio.StorySounds.SLSoundManager;
import zombie.radio.StorySounds.StorySound;
import zombie.radio.StorySounds.StorySoundEvent;
import zombie.radio.ZomboidRadio;
import zombie.radio.devices.DeviceData;
import zombie.radio.devices.DevicePresets;
import zombie.radio.devices.PresetEntry;
import zombie.radio.media.MediaData;
import zombie.radio.media.RecordedMedia;
import zombie.radio.scripting.DynamicRadioChannel;
import zombie.radio.scripting.RadioBroadCast;
import zombie.radio.scripting.RadioChannel;
import zombie.radio.scripting.RadioLine;
import zombie.radio.scripting.RadioScript;
import zombie.radio.scripting.RadioScriptManager;
import zombie.randomizedWorld.RandomizedWorldBase;
import zombie.randomizedWorld.randomizedBuilding.RBBar;
import zombie.randomizedWorld.randomizedBuilding.RBBasic;
import zombie.randomizedWorld.randomizedBuilding.RBBurnt;
import zombie.randomizedWorld.randomizedBuilding.RBBurntCorpse;
import zombie.randomizedWorld.randomizedBuilding.RBBurntFireman;
import zombie.randomizedWorld.randomizedBuilding.RBCafe;
import zombie.randomizedWorld.randomizedBuilding.RBClinic;
import zombie.randomizedWorld.randomizedBuilding.RBHairSalon;
import zombie.randomizedWorld.randomizedBuilding.RBKateAndBaldspot;
import zombie.randomizedWorld.randomizedBuilding.RBLooted;
import zombie.randomizedWorld.randomizedBuilding.RBOffice;
import zombie.randomizedWorld.randomizedBuilding.RBOther;
import zombie.randomizedWorld.randomizedBuilding.RBPileOCrepe;
import zombie.randomizedWorld.randomizedBuilding.RBPizzaWhirled;
import zombie.randomizedWorld.randomizedBuilding.RBSafehouse;
import zombie.randomizedWorld.randomizedBuilding.RBSchool;
import zombie.randomizedWorld.randomizedBuilding.RBShopLooted;
import zombie.randomizedWorld.randomizedBuilding.RBSpiffo;
import zombie.randomizedWorld.randomizedBuilding.RBStripclub;
import zombie.randomizedWorld.randomizedBuilding.RandomizedBuildingBase;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBandPractice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBathroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBedroomZed;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSBleach;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSCorpsePsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSDeadDrunk;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSFootballNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunmanInBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSGunslinger;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHenDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHockeyPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSHouseParty;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPokerNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPoliceAtHouse;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscape;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSPrisonEscapeWithPolice;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSkeletonPsycho;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSpecificProfession;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStagDo;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSStudentNight;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSSuicidePact;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSTinFoilHat;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombieLockedBathroom;
import zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombiesEating;
import zombie.randomizedWorld.randomizedDeadSurvivor.RandomizedDeadSurvivorBase;
import zombie.randomizedWorld.randomizedVehicleStory.RVSAmbulanceCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBanditRoad;
import zombie.randomizedWorld.randomizedVehicleStory.RVSBurntCar;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrashCorpse;
import zombie.randomizedWorld.randomizedVehicleStory.RVSChangingTire;
import zombie.randomizedWorld.randomizedVehicleStory.RVSConstructionSite;
import zombie.randomizedWorld.randomizedVehicleStory.RVSCrashHorde;
import zombie.randomizedWorld.randomizedVehicleStory.RVSFlippedCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockade;
import zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockadeShooting;
import zombie.randomizedWorld.randomizedVehicleStory.RVSTrailerCrash;
import zombie.randomizedWorld.randomizedVehicleStory.RVSUtilityVehicle;
import zombie.randomizedWorld.randomizedVehicleStory.RandomizedVehicleStoryBase;
import zombie.randomizedWorld.randomizedZoneStory.RZSBBQParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBaseball;
import zombie.randomizedWorld.randomizedZoneStory.RZSBeachParty;
import zombie.randomizedWorld.randomizedZoneStory.RZSBuryingCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSFishingTrip;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSForestCampEaten;
import zombie.randomizedWorld.randomizedZoneStory.RZSHunterCamp;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFest;
import zombie.randomizedWorld.randomizedZoneStory.RZSMusicFestStage;
import zombie.randomizedWorld.randomizedZoneStory.RZSSexyTime;
import zombie.randomizedWorld.randomizedZoneStory.RZSTrapperCamp;
import zombie.randomizedWorld.randomizedZoneStory.RandomizedZoneStoryBase;
import zombie.savefile.ClientPlayerDB;
import zombie.savefile.PlayerDBHelper;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.EvolvedRecipe;
import zombie.scripting.objects.Fixing;
import zombie.scripting.objects.GameSoundScript;
import zombie.scripting.objects.Item;
import zombie.scripting.objects.ItemRecipe;
import zombie.scripting.objects.MannequinScript;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.scripting.objects.MovableRecipe;
import zombie.scripting.objects.Recipe;
import zombie.scripting.objects.ScriptModule;
import zombie.scripting.objects.VehicleScript;
import zombie.spnetwork.SinglePlayerClient;
import zombie.text.templating.ReplaceProviderCharacter;
import zombie.text.templating.TemplateText;
import zombie.ui.ActionProgressBar;
import zombie.ui.Clock;
import zombie.ui.ModalDialog;
import zombie.ui.MoodlesUI;
import zombie.ui.NewHealthPanel;
import zombie.ui.ObjectTooltip;
import zombie.ui.RadarPanel;
import zombie.ui.RadialMenu;
import zombie.ui.RadialProgressBar;
import zombie.ui.SpeedControls;
import zombie.ui.TextDrawObject;
import zombie.ui.TextManager;
import zombie.ui.UI3DModel;
import zombie.ui.UIDebugConsole;
import zombie.ui.UIElement;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.ui.UIServerToolbox;
import zombie.ui.UITextBox2;
import zombie.ui.UITransition;
import zombie.ui.VehicleGauge;
import zombie.util.AddCoopPlayer;
import zombie.util.PZCalendar;
import zombie.util.PublicServerUtil;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.util.list.PZArrayList;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.EditVehicleState;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PathFindState2;
import zombie.vehicles.UI3DScene;
import zombie.vehicles.VehicleDoor;
import zombie.vehicles.VehicleLight;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleType;
import zombie.vehicles.VehicleWindow;
import zombie.vehicles.VehiclesDB2;
import zombie.world.moddata.ModData;
import zombie.worldMap.UIWorldMap;

/* loaded from: craftboid.jar:zombie/Lua/LuaManager.class */
public final class LuaManager {
    public static KahluaTable env;
    public static KahluaThread thread;
    public static KahluaThread debugthread;
    public static Exposer exposer;
    public static KahluaConverterManager converterManager = new KahluaConverterManager();
    public static J2SEPlatform platform = new J2SEPlatform();
    public static LuaCaller caller = new LuaCaller(converterManager);
    public static LuaCaller debugcaller = new LuaCaller(converterManager);
    public static ArrayList<String> loaded = new ArrayList<>();
    private static final HashSet<String> loading = new HashSet<>();
    public static HashMap<String, Object> loadedReturn = new HashMap<>();
    public static boolean checksumDone = false;
    public static ArrayList<String> loadList = new ArrayList<>();
    static ArrayList<String> paths = new ArrayList<>();
    private static final HashMap<String, Object> luaFunctionMap = new HashMap<>();
    private static final HashSet<KahluaTable> s_wiping = new HashSet<>();

    public static void outputTable(KahluaTable var0, int var1) {
    }

    private static void wipeRecurse(KahluaTable var0) {
        if (!var0.isEmpty() && !s_wiping.contains(var0)) {
            s_wiping.add(var0);
            KahluaTableIterator var1 = var0.iterator();
            while (var1.advance()) {
                KahluaTable var2 = (KahluaTable) Type.tryCastTo(var1.getValue(), KahluaTable.class);
                if (var2 != null) {
                    wipeRecurse(var2);
                }
            }
            s_wiping.remove(var0);
            var0.wipe();
        }
    }

    public static void init() {
        loaded.clear();
        loading.clear();
        loadedReturn.clear();
        paths.clear();
        luaFunctionMap.clear();
        platform = new J2SEPlatform();
        if (env != null) {
            s_wiping.clear();
            wipeRecurse(env);
        }
        env = platform.newEnvironment();
        converterManager = new KahluaConverterManager();
        if (thread != null) {
            thread.bReset = true;
        }
        thread = new KahluaThread(platform, env);
        debugthread = new KahluaThread(platform, env);
        UIManager.defaultthread = thread;
        caller = new LuaCaller(converterManager);
        debugcaller = new LuaCaller(converterManager);
        if (exposer != null) {
            exposer.destroy();
        }
        exposer = new Exposer(converterManager, platform, env);
        loaded = new ArrayList<>();
        checksumDone = false;
        GameClient.checksum = "";
        GameClient.checksumValid = false;
        KahluaNumberConverter.install(converterManager);
        LuaEventManager.register(platform, env);
        LuaHookManager.register(platform, env);
        CoopMaster.instance.register(platform, env);
        if (VoiceManager.instance != null) {
            VoiceManager.instance.LuaRegister(platform, env);
        }
        KahluaTable kahluaTable = env;
        exposer.exposeAll();
        exposer.TypeMap.put("function", LuaClosure.class);
        exposer.TypeMap.put("table", KahluaTable.class);
        outputTable(env, 0);
    }

    public static void LoadDir(String var0) throws URISyntaxException {
    }

    public static void LoadDirBase(String var0) throws Exception {
        LoadDirBase(var0, false);
    }

    public static void LoadDirBase(String var0, boolean var1) throws Exception {
        String var2 = "media/lua/" + var0 + "/";
        File var3 = ZomboidFileSystem.instance.getMediaFile("lua" + File.separator + var0);
        if (!paths.contains(var2)) {
            paths.add(var2);
        }
        try {
            searchFolders(ZomboidFileSystem.instance.baseURI, var3);
        } catch (IOException var14) {
            ExceptionLogger.logException(var14);
        }
        ArrayList<String> var15 = loadList;
        loadList = new ArrayList<>();
        ArrayList<String> var16 = ZomboidFileSystem.instance.getModIDs();
        Iterator<String> it = var16.iterator();
        while (it.hasNext()) {
            String o = it.next();
            String var5 = ZomboidFileSystem.instance.getModDir(o);
            if (var5 != null) {
                File var6 = new File(var5);
                URI var7 = var6.getCanonicalFile().toURI();
                File var8 = ZomboidFileSystem.instance.getCanonicalFile(var6, "media");
                File var9 = ZomboidFileSystem.instance.getCanonicalFile(var8, "lua");
                try {
                    searchFolders(var7, ZomboidFileSystem.instance.getCanonicalFile(var9, var0));
                } catch (IOException var13) {
                    ExceptionLogger.logException(var13);
                }
            }
        }
        Collections.sort(var15);
        Collections.sort(loadList);
        var15.addAll(loadList);
        loadList.clear();
        loadList = var15;
        HashSet<String> var17 = new HashSet<>();
        Iterator<String> var18 = loadList.iterator();
        while (var18.hasNext()) {
            String var19 = var18.next();
            if (!var17.contains(var19)) {
                var17.add(var19);
                String var20 = ZomboidFileSystem.instance.getAbsolutePath(var19);
                if (var20 == null) {
                    throw new IllegalStateException("couldn't find \"" + var19 + "\"");
                }
                if (!var1) {
                    RunLua(var20);
                }
                if (!checksumDone && !var19.contains("SandboxVars.lua") && (GameServer.bServer || GameClient.bClient)) {
                    NetChecksum.checksummer.addFile(var19, var20);
                }
                CoopMaster.instance.update();
            }
        }
        loadList.clear();
    }

    public static void initChecksum() throws Exception {
        if (!checksumDone) {
            if (GameClient.bClient || GameServer.bServer) {
                NetChecksum.checksummer.reset(false);
            }
        }
    }

    public static void finishChecksum() {
        if (GameServer.bServer) {
            GameServer.checksum = NetChecksum.checksummer.checksumToString();
            DebugLog.General.println("luaChecksum: " + GameServer.checksum);
        } else if (!GameClient.bClient) {
            return;
        } else {
            GameClient.checksum = NetChecksum.checksummer.checksumToString();
        }
        NetChecksum.GroupOfFiles.finishChecksum();
        checksumDone = true;
    }

    public static void LoadDirBase() throws Exception {
        initChecksum();
        LoadDirBase("shared");
        LoadDirBase("client");
    }

    public static void searchFolders(URI var0, File var1) throws IOException {
        if (!var1.isDirectory()) {
            if (var1.getAbsolutePath().toLowerCase().endsWith(".lua")) {
                String var4 = ZomboidFileSystem.instance.getRelativeFile(var0, var1.getAbsolutePath());
                loadList.add(var4.toLowerCase(Locale.ENGLISH));
                return;
            }
            return;
        }
        String[] var2 = var1.list();
        for (String s : var2) {
            String var10003 = var1.getCanonicalFile().getAbsolutePath();
            searchFolders(var0, new File(var10003 + File.separator + s));
        }
    }

    public static String getLuaCacheDir() {
        String var10000 = ZomboidFileSystem.instance.getCacheDir();
        String var0 = var10000 + File.separator + "Lua";
        File var1 = new File(var0);
        if (!var1.exists()) {
            var1.mkdir();
        }
        return var0;
    }

    public static String getSandboxCacheDir() {
        String var10000 = ZomboidFileSystem.instance.getCacheDir();
        String var0 = var10000 + File.separator + "Sandbox Presets";
        File var1 = new File(var0);
        if (!var1.exists()) {
            var1.mkdir();
        }
        return var0;
    }

    public static void fillContainer(ItemContainer var0, IsoPlayer var1) {
        ItemPickerJava.fillContainer(var0, var1);
    }

    public static void updateOverlaySprite(IsoObject var0) {
        ItemPickerJava.updateOverlaySprite(var0);
    }

    public static LuaClosure getDotDelimitedClosure(String var0) {
        String[] var1 = var0.split("\\.");
        KahluaTable var2 = env;
        for (int var3 = 0; var3 < var1.length - 1; var3++) {
            var2 = (KahluaTable) env.rawget(var1[var3]);
        }
        return (LuaClosure) var2.rawget(var1[var1.length - 1]);
    }

    public static void transferItem(IsoGameCharacter var0, InventoryItem var1, ItemContainer var2, ItemContainer var3) {
        LuaClosure var4 = (LuaClosure) env.rawget("javaTransferItems");
        caller.pcall(thread, var4, new Object[]{var0, var1, var2, var3});
    }

    public static void dropItem(InventoryItem var0) {
        LuaClosure var1 = getDotDelimitedClosure("ISInventoryPaneContextMenu.dropItem");
        caller.pcall(thread, var1, var0);
    }

    public static IsoGridSquare AdjacentFreeTileFinder(IsoGridSquare var0, IsoPlayer var1) {
        KahluaTable var2 = (KahluaTable) env.rawget("AdjacentFreeTileFinder");
        LuaClosure var3 = (LuaClosure) var2.rawget("Find");
        return (IsoGridSquare) caller.pcall(thread, var3, new Object[]{var0, var1})[1];
    }

    public static Object RunLua(String var0) {
        return RunLua(var0, false);
    }

    /* JADX WARN: Finally extract failed */
    public static Object RunLua(String var0, boolean var1) {
        String var2 = var0.replace("\\", "/");
        if (loading.contains(var2)) {
            DebugLog.Lua.warn("recursive require(): %s", new Object[]{var2});
            return null;
        }
        loading.add(var2);
        try {
            Object var3 = RunLuaInternal(var0, var1);
            loading.remove(var2);
            return var3;
        } catch (Throwable th) {
            loading.remove(var2);
            throw th;
        }
    }

    private static Object RunLuaInternal(String var0, boolean var1) {
        String var02 = var0.replace("\\", "/");
        if (loaded.contains(var02)) {
            return loadedReturn.get(var02);
        }
        FuncState.currentFile = var02.substring(var02.lastIndexOf(47) + 1);
        FuncState.currentfullFile = var02;
        String var03 = ZomboidFileSystem.instance.getString(var02.replace("\\", "/"));
        if (DebugLog.isEnabled(DebugType.Lua)) {
            DebugLog.Lua.println("Loading: " + ZomboidFileSystem.instance.getRelativeFile(var03));
        }
        try {
            InputStreamReader var3 = IndieFileLoader.getStreamReader(var03);
            LuaCompiler.rewriteEvents = var1;
            try {
                BufferedReader var5 = new BufferedReader(var3);
                try {
                    LuaClosure var4 = LuaCompiler.loadis(var5, var03.substring(var03.lastIndexOf(47) + 1), env);
                    var5.close();
                    luaFunctionMap.clear();
                    AttachedWeaponDefinitions.instance.m_dirty = true;
                    DefaultClothing.instance.m_dirty = true;
                    HairOutfitDefinitions.instance.m_dirty = true;
                    ZombiesZoneDefinition.bDirty = true;
                    LuaReturn var12 = caller.protectedCall(thread, var4, new Object[0]);
                    if (!var12.isSuccess()) {
                        Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getErrorString(), (Object) null);
                        if (var12.getJavaException() != null) {
                            Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getJavaException().toString(), (Object) null);
                        }
                        Logger.getLogger(IsoWorld.class.getName()).log(Level.SEVERE, var12.getLuaStackTrace(), (Object) null);
                    }
                    loaded.add(var02);
                    Object var6 = (!var12.isSuccess() || var12.size() <= 0) ? null : var12.getFirst();
                    if (var6 != null) {
                        loadedReturn.put(var02, var6);
                    } else {
                        loadedReturn.remove(var02);
                    }
                    LuaCompiler.rewriteEvents = false;
                    return var6;
                } finally {
                }
            } catch (Exception var10) {
                Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, "Error found in LUA file: " + var03, (Object) null);
                ExceptionLogger.logException(var10);
                thread.debugException(var10);
                return null;
            }
        } catch (FileNotFoundException var11) {
            ExceptionLogger.logException(var11);
            return null;
        }
    }

    public static Object getFunctionObject(String var0) {
        Object var1;
        if (var0 != null && !var0.isEmpty()) {
            Object var12 = luaFunctionMap.get(var0);
            if (var12 != null) {
                return var12;
            }
            KahluaTable var2 = env;
            if (var0.contains(".")) {
                String[] var3 = var0.split("\\.");
                for (int var4 = 0; var4 < var3.length - 1; var4++) {
                    KahluaTable var5 = (KahluaTable) Type.tryCastTo(var2.rawget(var3[var4]), KahluaTable.class);
                    if (var5 == null) {
                        DebugLog.General.error("no such function \"%s\"", new Object[]{var0});
                        return null;
                    }
                    var2 = var5;
                }
                var1 = var2.rawget(var3[var3.length - 1]);
            } else {
                var1 = var2.rawget(var0);
            }
            if (!(var1 instanceof JavaFunction) && !(var1 instanceof LuaClosure)) {
                DebugLog.General.error("no such function \"%s\"", new Object[]{var0});
                return null;
            }
            luaFunctionMap.put(var0, var1);
            return var1;
        }
        return null;
    }

    public static void Test() throws IOException {
    }

    public static Object get(Object var0) {
        return env.rawget(var0);
    }

    public static void call(String var0, Object var1) {
        caller.pcall(thread, env.rawget(var0), var1);
    }

    private static void exposeKeyboardKeys(KahluaTable var0) {
        Object var1 = var0.rawget("Keyboard");
        if (var1 instanceof KahluaTable) {
            KahluaTable var2 = (KahluaTable) var1;
            Field[] var3 = Keyboard.class.getFields();
            try {
                int length = var3.length;
                for (Field var7 : var3) {
                    if (Modifier.isStatic(var7.getModifiers()) && Modifier.isPublic(var7.getModifiers()) && Modifier.isFinal(var7.getModifiers()) && var7.getType().equals(Integer.TYPE) && var7.getName().startsWith("KEY_") && !var7.getName().endsWith("WIN")) {
                        var2.rawset(var7.getName(), Double.valueOf(var7.getInt(null)));
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private static void exposeLuaCalendar() {
        KahluaTable var0 = (KahluaTable) env.rawget("PZCalendar");
        if (var0 != null) {
            Field[] var1 = Calendar.class.getFields();
            try {
                int length = var1.length;
                for (Field var5 : var1) {
                    if (Modifier.isStatic(var5.getModifiers()) && Modifier.isPublic(var5.getModifiers()) && Modifier.isFinal(var5.getModifiers()) && var5.getType().equals(Integer.TYPE)) {
                        var0.rawset(var5.getName(), BoxedStaticValues.toDouble(var5.getInt(null)));
                    }
                }
            } catch (Exception e) {
            }
            env.rawset("Calendar", var0);
        }
    }

    public static String getHourMinuteJava() {
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(12);
        int hoursOfDay = calendar.get(11);
        return minutes < 10 ? hoursOfDay + ":0" + minutes : hoursOfDay + ":" + minutes;
    }

    public static KahluaTable copyTable(KahluaTable var0) {
        return copyTable(null, var0);
    }

    public static KahluaTable copyTable(KahluaTable var0, KahluaTable var1) {
        if (var0 == null) {
            var0 = platform.newTable();
        } else {
            var0.wipe();
        }
        if (var1 != null && !var1.isEmpty()) {
            KahluaTableIterator var2 = var1.iterator();
            while (var2.advance()) {
                Object var3 = var2.getKey();
                Object var4 = var2.getValue();
                if (var4 instanceof KahluaTable) {
                    var0.rawset(var3, copyTable(null, (KahluaTable) var4));
                } else {
                    var0.rawset(var3, var4);
                }
            }
        }
        return var0;
    }

    /* loaded from: craftboid.jar:zombie/Lua/LuaManager$Exposer.class */
    public static final class Exposer extends LuaJavaClassExposer {
        private final HashSet<Class<?>> exposed;

        public Exposer(KahluaConverterManager var1, Platform var2, KahluaTable var3) {
            super(var1, var2, var3);
            this.exposed = new HashSet<>();
        }

        public void exposeAll() {
            setExposed(BufferedReader.class);
            setExposed(BufferedWriter.class);
            setExposed(DataInputStream.class);
            setExposed(DataOutputStream.class);
            setExposed(Double.class);
            setExposed(Long.class);
            setExposed(Float.class);
            setExposed(Integer.class);
            setExposed(Math.class);
            setExposed(Void.class);
            setExposed(SimpleDateFormat.class);
            setExposed(ArrayList.class);
            setExposed(EnumMap.class);
            setExposed(HashMap.class);
            setExposed(LinkedList.class);
            setExposed(Stack.class);
            setExposed(Vector.class);
            setExposed(Iterator.class);
            setExposed(EmitterType.class);
            setExposed(FMODAudio.class);
            setExposed(FMODSoundBank.class);
            setExposed(FMODSoundEmitter.class);
            setExposed(Vector2f.class);
            setExposed(Vector3f.class);
            setExposed(KahluaUtil.class);
            setExposed(DummySoundBank.class);
            setExposed(DummySoundEmitter.class);
            setExposed(BaseSoundEmitter.class);
            setExposed(GameSound.class);
            setExposed(GameSoundClip.class);
            setExposed(AttackState.class);
            setExposed(BurntToDeath.class);
            setExposed(ClimbDownSheetRopeState.class);
            setExposed(ClimbOverFenceState.class);
            setExposed(ClimbOverWallState.class);
            setExposed(ClimbSheetRopeState.class);
            setExposed(ClimbThroughWindowState.class);
            setExposed(CloseWindowState.class);
            setExposed(CrawlingZombieTurnState.class);
            setExposed(FakeDeadAttackState.class);
            setExposed(FakeDeadZombieState.class);
            setExposed(FishingState.class);
            setExposed(FitnessState.class);
            setExposed(IdleState.class);
            setExposed(LungeState.class);
            setExposed(OpenWindowState.class);
            setExposed(PathFindState.class);
            setExposed(PlayerActionsState.class);
            setExposed(PlayerAimState.class);
            setExposed(PlayerEmoteState.class);
            setExposed(PlayerExtState.class);
            setExposed(PlayerFallDownState.class);
            setExposed(PlayerFallingState.class);
            setExposed(PlayerGetUpState.class);
            setExposed(PlayerHitReactionPVPState.class);
            setExposed(PlayerHitReactionState.class);
            setExposed(PlayerKnockedDown.class);
            setExposed(PlayerOnGroundState.class);
            setExposed(PlayerSitOnGroundState.class);
            setExposed(PlayerStrafeState.class);
            setExposed(SmashWindowState.class);
            setExposed(StaggerBackState.class);
            setExposed(SwipeStatePlayer.class);
            setExposed(ThumpState.class);
            setExposed(WalkTowardState.class);
            setExposed(ZombieFallDownState.class);
            setExposed(ZombieGetDownState.class);
            setExposed(ZombieGetUpState.class);
            setExposed(ZombieIdleState.class);
            setExposed(ZombieOnGroundState.class);
            setExposed(ZombieReanimateState.class);
            setExposed(ZombieSittingState.class);
            setExposed(GameCharacterAIBrain.class);
            setExposed(MapKnowledge.class);
            setExposed(BodyPartType.class);
            setExposed(BodyPart.class);
            setExposed(BodyDamage.class);
            setExposed(Thermoregulator.class);
            setExposed(Thermoregulator.ThermalNode.class);
            setExposed(Metabolics.class);
            setExposed(Fitness.class);
            setExposed(GameKeyboard.class);
            setExposed(LuaTimedAction.class);
            setExposed(LuaTimedActionNew.class);
            setExposed(Moodle.class);
            setExposed(Moodles.class);
            setExposed(MoodleType.class);
            setExposed(ProfessionFactory.class);
            setExposed(ProfessionFactory.Profession.class);
            setExposed(PerkFactory.class);
            setExposed(PerkFactory.Perk.class);
            setExposed(PerkFactory.Perks.class);
            setExposed(ObservationFactory.class);
            setExposed(ObservationFactory.Observation.class);
            setExposed(TraitFactory.class);
            setExposed(TraitFactory.Trait.class);
            setExposed(IsoDummyCameraCharacter.class);
            setExposed(Stats.class);
            setExposed(SurvivorDesc.class);
            setExposed(SurvivorFactory.class);
            setExposed(SurvivorFactory.SurvivorType.class);
            setExposed(IsoGameCharacter.class);
            setExposed(IsoGameCharacter.Location.class);
            setExposed(IsoGameCharacter.PerkInfo.class);
            setExposed(IsoGameCharacter.XP.class);
            setExposed(IsoGameCharacter.CharacterTraits.class);
            setExposed(TraitCollection.TraitSlot.class);
            setExposed(TraitCollection.class);
            setExposed(IsoPlayer.class);
            setExposed(IsoSurvivor.class);
            setExposed(IsoZombie.class);
            setExposed(CharacterActionAnims.class);
            setExposed(HaloTextHelper.class);
            setExposed(HaloTextHelper.ColorRGB.class);
            setExposed(NetworkAIParams.class);
            setExposed(BloodBodyPartType.class);
            setExposed(Clipboard.class);
            setExposed(AngelCodeFont.class);
            setExposed(ZLogger.class);
            setExposed(PropertyContainer.class);
            setExposed(ClothingItem.class);
            setExposed(AnimatorDebugMonitor.class);
            setExposed(ColorInfo.class);
            setExposed(Texture.class);
            setExposed(SteamFriend.class);
            setExposed(SteamUGCDetails.class);
            setExposed(SteamWorkshopItem.class);
            setExposed(Color.class);
            setExposed(Colors.class);
            setExposed(Core.class);
            setExposed(GameVersion.class);
            setExposed(ImmutableColor.class);
            setExposed(Language.class);
            setExposed(PerformanceSettings.class);
            setExposed(SpriteRenderer.class);
            setExposed(Translator.class);
            setExposed(PZMath.class);
            setExposed(DebugLog.class);
            setExposed(DebugOptions.class);
            setExposed(BooleanDebugOption.class);
            setExposed(DebugType.class);
            setExposed(ErosionConfig.class);
            setExposed(ErosionConfig.Debug.class);
            setExposed(ErosionConfig.Season.class);
            setExposed(ErosionConfig.Seeds.class);
            setExposed(ErosionConfig.Time.class);
            setExposed(ErosionMain.class);
            setExposed(ErosionSeason.class);
            setExposed(AnimationViewerState.class);
            setExposed(AnimationViewerState.BooleanDebugOption.class);
            setExposed(AttachmentEditorState.class);
            setExposed(ChooseGameInfo.Mod.class);
            setExposed(DebugChunkState.class);
            setExposed(DebugChunkState.BooleanDebugOption.class);
            setExposed(DebugGlobalObjectState.class);
            setExposed(GameLoadingState.class);
            setExposed(LoadingQueueState.class);
            setExposed(MainScreenState.class);
            setExposed(TermsOfServiceState.class);
            setExposed(CGlobalObject.class);
            setExposed(CGlobalObjects.class);
            setExposed(CGlobalObjectSystem.class);
            setExposed(SGlobalObject.class);
            setExposed(SGlobalObjects.class);
            setExposed(SGlobalObjectSystem.class);
            setExposed(Mouse.class);
            setExposed(AlarmClock.class);
            setExposed(AlarmClockClothing.class);
            setExposed(Clothing.class);
            setExposed(Clothing.ClothingPatch.class);
            setExposed(Clothing.ClothingPatchFabricType.class);
            setExposed(ComboItem.class);
            setExposed(Drainable.class);
            setExposed(DrainableComboItem.class);
            setExposed(Food.class);
            setExposed(HandWeapon.class);
            setExposed(InventoryContainer.class);
            setExposed(Key.class);
            setExposed(KeyRing.class);
            setExposed(Literature.class);
            setExposed(MapItem.class);
            setExposed(Moveable.class);
            setExposed(Radio.class);
            setExposed(WeaponPart.class);
            setExposed(ItemContainer.class);
            setExposed(ItemPickerJava.class);
            setExposed(InventoryItem.class);
            setExposed(InventoryItemFactory.class);
            setExposed(FixingManager.class);
            setExposed(RecipeManager.class);
            setExposed(IsoRegions.class);
            setExposed(IsoRegionsLogger.class);
            setExposed(IsoRegionsLogger.IsoRegionLog.class);
            setExposed(IsoRegionLogType.class);
            setExposed(DataCell.class);
            setExposed(DataChunk.class);
            setExposed(IsoChunkRegion.class);
            setExposed(IsoWorldRegion.class);
            setExposed(IsoRegionsRenderer.class);
            setExposed(IsoRegionsRenderer.BooleanDebugOption.class);
            setExposed(IsoBuilding.class);
            setExposed(IsoRoom.class);
            setExposed(SafeHouse.class);
            setExposed(BarricadeAble.class);
            setExposed(IsoBarbecue.class);
            setExposed(IsoBarricade.class);
            setExposed(IsoBrokenGlass.class);
            setExposed(IsoClothingDryer.class);
            setExposed(IsoClothingWasher.class);
            setExposed(IsoCombinationWasherDryer.class);
            setExposed(IsoStackedWasherDryer.class);
            setExposed(IsoCurtain.class);
            setExposed(IsoCarBatteryCharger.class);
            setExposed(IsoDeadBody.class);
            setExposed(IsoDoor.class);
            setExposed(IsoFire.class);
            setExposed(IsoFireManager.class);
            setExposed(IsoFireplace.class);
            setExposed(IsoGenerator.class);
            setExposed(IsoJukebox.class);
            setExposed(IsoLightSwitch.class);
            setExposed(IsoMannequin.class);
            setExposed(IsoMolotovCocktail.class);
            setExposed(IsoWaveSignal.class);
            setExposed(IsoRadio.class);
            setExposed(IsoTelevision.class);
            setExposed(IsoStackedWasherDryer.class);
            setExposed(IsoStove.class);
            setExposed(IsoThumpable.class);
            setExposed(IsoTrap.class);
            setExposed(IsoTree.class);
            setExposed(IsoWheelieBin.class);
            setExposed(IsoWindow.class);
            setExposed(IsoWindowFrame.class);
            setExposed(IsoWorldInventoryObject.class);
            setExposed(IsoZombieGiblets.class);
            setExposed(RainManager.class);
            setExposed(ObjectRenderEffects.class);
            setExposed(HumanVisual.class);
            setExposed(ItemVisual.class);
            setExposed(ItemVisuals.class);
            setExposed(IsoSprite.class);
            setExposed(IsoSpriteInstance.class);
            setExposed(IsoSpriteManager.class);
            setExposed(IsoSpriteGrid.class);
            setExposed(IsoFlagType.class);
            setExposed(IsoObjectType.class);
            setExposed(ClimateManager.class);
            setExposed(ClimateManager.DayInfo.class);
            setExposed(ClimateManager.ClimateFloat.class);
            setExposed(ClimateManager.ClimateColor.class);
            setExposed(ClimateManager.ClimateBool.class);
            setExposed(WeatherPeriod.class);
            setExposed(WeatherPeriod.WeatherStage.class);
            setExposed(WeatherPeriod.StrLerpVal.class);
            setExposed(ClimateManager.AirFront.class);
            setExposed(ThunderStorm.class);
            setExposed(ThunderStorm.ThunderCloud.class);
            setExposed(IsoWeatherFX.class);
            setExposed(Temperature.class);
            setExposed(ClimateColorInfo.class);
            setExposed(ClimateValues.class);
            setExposed(ClimateForecaster.class);
            setExposed(ClimateForecaster.DayForecast.class);
            setExposed(ClimateForecaster.ForecastValue.class);
            setExposed(ClimateHistory.class);
            setExposed(WorldFlares.class);
            setExposed(WorldFlares.Flare.class);
            setExposed(ImprovedFog.class);
            setExposed(ClimateMoon.class);
            setExposed(IsoPuddles.class);
            setExposed(IsoPuddles.PuddlesFloat.class);
            setExposed(BentFences.class);
            setExposed(BrokenFences.class);
            setExposed(ContainerOverlays.class);
            setExposed(IsoChunk.class);
            setExposed(BuildingDef.class);
            setExposed(IsoCamera.class);
            setExposed(IsoCell.class);
            setExposed(IsoChunkMap.class);
            setExposed(IsoDirections.class);
            setExposed(IsoDirectionSet.class);
            setExposed(IsoGridSquare.class);
            setExposed(IsoHeatSource.class);
            setExposed(IsoLightSource.class);
            setExposed(IsoLot.class);
            setExposed(IsoLuaMover.class);
            setExposed(IsoMetaChunk.class);
            setExposed(IsoMetaCell.class);
            setExposed(IsoMetaGrid.class);
            setExposed(IsoMetaGrid.Trigger.class);
            setExposed(IsoMetaGrid.VehicleZone.class);
            setExposed(IsoMetaGrid.Zone.class);
            setExposed(IsoMovingObject.class);
            setExposed(IsoObject.class);
            setExposed(IsoObjectPicker.class);
            setExposed(IsoPushableObject.class);
            setExposed(IsoUtils.class);
            setExposed(IsoWorld.class);
            setExposed(LosUtil.class);
            setExposed(MetaObject.class);
            setExposed(RoomDef.class);
            setExposed(SliceY.class);
            setExposed(TileOverlays.class);
            setExposed(Vector2.class);
            setExposed(Vector3.class);
            setExposed(WorldMarkers.class);
            setExposed(WorldMarkers.DirectionArrow.class);
            setExposed(WorldMarkers.GridSquareMarker.class);
            setExposed(WorldMarkers.PlayerHomingPoint.class);
            setExposed(SearchMode.class);
            setExposed(SearchMode.PlayerSearchMode.class);
            setExposed(SearchMode.SearchModeFloat.class);
            setExposed(IsoMarkers.class);
            setExposed(IsoMarkers.IsoMarker.class);
            setExposed(IsoMarkers.CircleIsoMarker.class);
            setExposed(LuaEventManager.class);
            setExposed(MapObjects.class);
            setExposed(ActiveMods.class);
            setExposed(Server.class);
            setExposed(ServerOptions.class);
            setExposed(ServerOptions.BooleanServerOption.class);
            setExposed(ServerOptions.DoubleServerOption.class);
            setExposed(ServerOptions.IntegerServerOption.class);
            setExposed(ServerOptions.StringServerOption.class);
            setExposed(ServerOptions.TextServerOption.class);
            setExposed(ServerSettings.class);
            setExposed(ServerSettingsManager.class);
            setExposed(ZombiePopulationRenderer.class);
            setExposed(ZombiePopulationRenderer.BooleanDebugOption.class);
            setExposed(RadioAPI.class);
            setExposed(DeviceData.class);
            setExposed(DevicePresets.class);
            setExposed(PresetEntry.class);
            setExposed(ZomboidRadio.class);
            setExposed(RadioData.class);
            setExposed(RadioScriptManager.class);
            setExposed(DynamicRadioChannel.class);
            setExposed(RadioChannel.class);
            setExposed(RadioBroadCast.class);
            setExposed(RadioLine.class);
            setExposed(RadioScript.class);
            setExposed(RadioScript.ExitOption.class);
            setExposed(ChannelCategory.class);
            setExposed(SLSoundManager.class);
            setExposed(StorySound.class);
            setExposed(StorySoundEvent.class);
            setExposed(EventSound.class);
            setExposed(DataPoint.class);
            setExposed(RecordedMedia.class);
            setExposed(MediaData.class);
            setExposed(MediaData.MediaLineData.class);
            setExposed(EvolvedRecipe.class);
            setExposed(Fixing.class);
            setExposed(Fixing.Fixer.class);
            setExposed(Fixing.FixerSkill.class);
            setExposed(GameSoundScript.class);
            setExposed(Item.class);
            setExposed(Item.Type.class);
            setExposed(ItemRecipe.class);
            setExposed(MannequinScript.class);
            setExposed(ModelAttachment.class);
            setExposed(ModelScript.class);
            setExposed(MovableRecipe.class);
            setExposed(Recipe.class);
            setExposed(Recipe.RequiredSkill.class);
            setExposed(Recipe.Result.class);
            setExposed(Recipe.Source.class);
            setExposed(ScriptModule.class);
            setExposed(VehicleScript.class);
            setExposed(VehicleScript.Area.class);
            setExposed(VehicleScript.Model.class);
            setExposed(VehicleScript.Part.class);
            setExposed(VehicleScript.Passenger.class);
            setExposed(VehicleScript.PhysicsShape.class);
            setExposed(VehicleScript.Position.class);
            setExposed(VehicleScript.Wheel.class);
            setExposed(ScriptManager.class);
            setExposed(TemplateText.class);
            setExposed(ReplaceProviderCharacter.class);
            setExposed(ActionProgressBar.class);
            setExposed(Clock.class);
            setExposed(UIDebugConsole.class);
            setExposed(ModalDialog.class);
            setExposed(MoodlesUI.class);
            setExposed(NewHealthPanel.class);
            setExposed(ObjectTooltip.class);
            setExposed(ObjectTooltip.Layout.class);
            setExposed(ObjectTooltip.LayoutItem.class);
            setExposed(RadarPanel.class);
            setExposed(RadialMenu.class);
            setExposed(RadialProgressBar.class);
            setExposed(SpeedControls.class);
            setExposed(TextManager.class);
            setExposed(UI3DModel.class);
            setExposed(UIElement.class);
            setExposed(UIFont.class);
            setExposed(UITransition.class);
            setExposed(UIManager.class);
            setExposed(UIServerToolbox.class);
            setExposed(UITextBox2.class);
            setExposed(VehicleGauge.class);
            setExposed(TextDrawObject.class);
            setExposed(PZArrayList.class);
            setExposed(PZCalendar.class);
            setExposed(BaseVehicle.class);
            setExposed(EditVehicleState.class);
            setExposed(PathFindBehavior2.BehaviorResult.class);
            setExposed(PathFindBehavior2.class);
            setExposed(PathFindState2.class);
            setExposed(UI3DScene.class);
            setExposed(VehicleDoor.class);
            setExposed(VehicleLight.class);
            setExposed(VehiclePart.class);
            setExposed(VehicleType.class);
            setExposed(VehicleWindow.class);
            setExposed(AttachedItem.class);
            setExposed(AttachedItems.class);
            setExposed(AttachedLocation.class);
            setExposed(AttachedLocationGroup.class);
            setExposed(AttachedLocations.class);
            setExposed(WornItems.class);
            setExposed(WornItem.class);
            setExposed(BodyLocation.class);
            setExposed(BodyLocationGroup.class);
            setExposed(BodyLocations.class);
            setExposed(DummySoundManager.class);
            setExposed(GameSounds.class);
            setExposed(GameTime.class);
            setExposed(GameWindow.class);
            setExposed(SandboxOptions.class);
            setExposed(SandboxOptions.BooleanSandboxOption.class);
            setExposed(SandboxOptions.DoubleSandboxOption.class);
            setExposed(SandboxOptions.StringSandboxOption.class);
            setExposed(SandboxOptions.EnumSandboxOption.class);
            setExposed(SandboxOptions.IntegerSandboxOption.class);
            setExposed(SoundManager.class);
            setExposed(SystemDisabler.class);
            setExposed(VirtualZombieManager.class);
            setExposed(WorldSoundManager.class);
            setExposed(WorldSoundManager.WorldSound.class);
            setExposed(DummyCharacterSoundEmitter.class);
            setExposed(CharacterSoundEmitter.class);
            setExposed(SoundManager.AmbientSoundEffect.class);
            setExposed(BaseAmbientStreamManager.class);
            setExposed(AmbientStreamManager.class);
            setExposed(Nutrition.class);
            setExposed(BSFurnace.class);
            setExposed(MultiStageBuilding.class);
            setExposed(MultiStageBuilding.Stage.class);
            setExposed(SleepingEvent.class);
            setExposed(IsoCompost.class);
            setExposed(Userlog.class);
            setExposed(Userlog.UserlogType.class);
            setExposed(ConfigOption.class);
            setExposed(BooleanConfigOption.class);
            setExposed(DoubleConfigOption.class);
            setExposed(EnumConfigOption.class);
            setExposed(IntegerConfigOption.class);
            setExposed(StringConfigOption.class);
            setExposed(Faction.class);
            setExposed(GlobalObject.LuaFileWriter.class);
            setExposed(Keyboard.class);
            setExposed(DBResult.class);
            setExposed(NonPvpZone.class);
            setExposed(DBTicket.class);
            setExposed(StashSystem.class);
            setExposed(StashBuilding.class);
            setExposed(Stash.class);
            setExposed(ItemType.class);
            setExposed(RandomizedWorldBase.class);
            setExposed(RandomizedBuildingBase.class);
            setExposed(RBBurntFireman.class);
            setExposed(RBBasic.class);
            setExposed(RBBurnt.class);
            setExposed(RBOther.class);
            setExposed(RBStripclub.class);
            setExposed(RBSchool.class);
            setExposed(RBSpiffo.class);
            setExposed(RBPizzaWhirled.class);
            setExposed(RBOffice.class);
            setExposed(RBHairSalon.class);
            setExposed(RBClinic.class);
            setExposed(RBPileOCrepe.class);
            setExposed(RBCafe.class);
            setExposed(RBBar.class);
            setExposed(RBLooted.class);
            setExposed(RBSafehouse.class);
            setExposed(RBBurntCorpse.class);
            setExposed(RBShopLooted.class);
            setExposed(RBKateAndBaldspot.class);
            setExposed(RandomizedDeadSurvivorBase.class);
            setExposed(RDSZombiesEating.class);
            setExposed(RDSBleach.class);
            setExposed(RDSDeadDrunk.class);
            setExposed(RDSGunmanInBathroom.class);
            setExposed(RDSGunslinger.class);
            setExposed(RDSZombieLockedBathroom.class);
            setExposed(RDSBandPractice.class);
            setExposed(RDSBathroomZed.class);
            setExposed(RDSBedroomZed.class);
            setExposed(RDSFootballNight.class);
            setExposed(RDSHenDo.class);
            setExposed(RDSStagDo.class);
            setExposed(RDSStudentNight.class);
            setExposed(RDSPokerNight.class);
            setExposed(RDSSuicidePact.class);
            setExposed(RDSPrisonEscape.class);
            setExposed(RDSPrisonEscapeWithPolice.class);
            setExposed(RDSSkeletonPsycho.class);
            setExposed(RDSCorpsePsycho.class);
            setExposed(RDSSpecificProfession.class);
            setExposed(RDSPoliceAtHouse.class);
            setExposed(RDSHouseParty.class);
            setExposed(RDSTinFoilHat.class);
            setExposed(RDSHockeyPsycho.class);
            setExposed(RandomizedVehicleStoryBase.class);
            setExposed(RVSCarCrash.class);
            setExposed(RVSBanditRoad.class);
            setExposed(RVSAmbulanceCrash.class);
            setExposed(RVSCrashHorde.class);
            setExposed(RVSCarCrashCorpse.class);
            setExposed(RVSPoliceBlockade.class);
            setExposed(RVSPoliceBlockadeShooting.class);
            setExposed(RVSBurntCar.class);
            setExposed(RVSConstructionSite.class);
            setExposed(RVSUtilityVehicle.class);
            setExposed(RVSChangingTire.class);
            setExposed(RVSFlippedCrash.class);
            setExposed(RVSTrailerCrash.class);
            setExposed(RandomizedZoneStoryBase.class);
            setExposed(RZSForestCamp.class);
            setExposed(RZSForestCampEaten.class);
            setExposed(RZSBuryingCamp.class);
            setExposed(RZSBeachParty.class);
            setExposed(RZSFishingTrip.class);
            setExposed(RZSBBQParty.class);
            setExposed(RZSHunterCamp.class);
            setExposed(RZSSexyTime.class);
            setExposed(RZSTrapperCamp.class);
            setExposed(RZSBaseball.class);
            setExposed(RZSMusicFestStage.class);
            setExposed(RZSMusicFest.class);
            setExposed(MapGroups.class);
            setExposed(BeardStyles.class);
            setExposed(BeardStyle.class);
            setExposed(HairStyles.class);
            setExposed(HairStyle.class);
            setExposed(BloodClothingType.class);
            setExposed(WeaponType.class);
            setExposed(IsoWaterGeometry.class);
            setExposed(ModData.class);
            setExposed(WorldMarkers.class);
            setExposed(ChatMessage.class);
            setExposed(ChatBase.class);
            setExposed(ServerChatMessage.class);
            setExposed(Safety.class);
            if (Core.bDebug) {
                setExposed(Field.class);
                setExposed(Method.class);
                setExposed(Coroutine.class);
            }
            UIWorldMap.setExposed(this);
            if (Core.bDebug) {
                try {
                    exposeMethod(Class.class, Class.class.getMethod("getName", new Class[0]), LuaManager.env);
                    exposeMethod(Class.class, Class.class.getMethod("getSimpleName", new Class[0]), LuaManager.env);
                } catch (NoSuchMethodException var3) {
                    var3.printStackTrace();
                }
            }
            Iterator<Class<?>> it = this.exposed.iterator();
            while (it.hasNext()) {
                Class<?> aClass = it.next();
                exposeLikeJavaRecursively(aClass, LuaManager.env);
            }
            exposeGlobalFunctions(new GlobalObject());
            LuaManager.exposeKeyboardKeys(LuaManager.env);
            LuaManager.exposeLuaCalendar();
        }

        public void setExposed(Class<?> var1) {
            this.exposed.add(var1);
        }

        public boolean shouldExpose(Class<?> var1) {
            return var1 != null && this.exposed.contains(var1);
        }
    }

    /* loaded from: craftboid.jar:zombie/Lua/LuaManager$GlobalObject.class */
    public static class GlobalObject {
        static FileOutputStream outStream;
        static FileInputStream inStream;
        static FileReader inFileReader;
        static BufferedReader inBufferedReader;
        static long timeLastRefresh;
        private static final TimSortComparator timSortComparator;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LuaManager.class.desiredAssertionStatus();
            inFileReader = null;
            inBufferedReader = null;
            timeLastRefresh = 0L;
            timSortComparator = new TimSortComparator();
        }

        @LuaMethod(name = "banPlayerFromServer", global = true)
        public static boolean banPlayerFromServer(IsoPlayer player, String reason) {
            if (reason == null) {
                reason = "";
            }
            try {
                ServerWorldDatabase db = ServerWorldDatabase.instance;
                String playerName = player.getName();
                db.banUser(playerName, true);
                db.addUserlog(playerName, Userlog.UserlogType.Banned, reason, "Server", 1);
                ZLogger logger = LoggerManager.getLogger("admin");
                logger.write("Server banned user " + playerName + reason, "IMPORTANT");
                UdpConnection conn = GameServer.getConnectionFromPlayer(player);
                if (conn == null) {
                    logger.write("User " + playerName + " doesn't exist.");
                    return false;
                }
                if (SteamUtils.isSteamModeEnabled()) {
                    long j = conn.steamID;
                    String msg = "Server banned steamid " + j + "(" + j + ")" + conn.username;
                    LoggerManager.getLogger("admin").write(msg, "IMPORTANT");
                    String steamID = SteamUtils.convertSteamIDToString(conn.steamID);
                    db.banSteamID(steamID, reason, true);
                } else {
                    String msg2 = "Server banned ip " + conn.ip + "(" + conn.username + ")" + reason;
                    LoggerManager.getLogger("admin").write(msg2, "IMPORTANT");
                    db.banIp(conn.ip, playerName, reason, true);
                }
                if ("".equals(reason)) {
                    GameServer.kick(conn, "UI_Policy_Ban", null);
                } else {
                    String msg3 = "You have been banned from this server for the following reason: " + reason;
                    GameServer.kick(conn, msg3, null);
                }
                conn.forceDisconnect("command-ban-ip");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @LuaMethod(name = "kickPlayerFromServer", global = true)
        public static void kickPlayerFromServer(IsoPlayer player, String reason) {
            if (reason == null) {
                reason = "";
            }
            ServerWorldDatabase db = ServerWorldDatabase.instance;
            String playerName = player.getName();
            ZLogger logger = LoggerManager.getLogger("admin");
            logger.write(" kicked user " + playerName);
            db.addUserlog(playerName, Userlog.UserlogType.Kicked, reason, "Server", 1);
            UdpConnection conn = GameServer.getConnectionFromPlayer(player);
            if (conn == null) {
                logger.write("User " + playerName + " doesn't exist.");
                return;
            }
            if ("".equals(reason)) {
                GameServer.kick(conn, "UI_Policy_Kick", null);
            } else {
                String msg = "You have been kicked from this server for the following reason: " + reason;
                GameServer.kick(conn, msg, null);
            }
            conn.forceDisconnect("command-kick");
            GameServer.addDisconnect(conn);
            logger.write("User " + playerName + " kicked.");
        }

        @LuaMethod(name = "loadVehicleModel", global = true)
        public static Model loadVehicleModel(String var0, String var1, String var2) {
            return loadZomboidModel(var0, var1, var2, "vehicle", true);
        }

        @LuaMethod(name = "loadStaticZomboidModel", global = true)
        public static Model loadStaticZomboidModel(String var0, String var1, String var2) {
            return loadZomboidModel(var0, var1, var2, null, true);
        }

        @LuaMethod(name = "loadSkinnedZomboidModel", global = true)
        public static Model loadSkinnedZomboidModel(String var0, String var1, String var2) {
            return loadZomboidModel(var0, var1, var2, null, false);
        }

        @LuaMethod(name = "loadZomboidModel", global = true)
        public static Model loadZomboidModel(String var0, String var1, String var2, String var3, boolean var4) {
            try {
                if (var1.startsWith("/")) {
                    var1 = var1.substring(1);
                }
                if (var2.startsWith("/")) {
                    var2 = var2.substring(1);
                }
                if (StringUtils.isNullOrWhitespace(var3)) {
                    var3 = "basicEffect";
                }
                if ("vehicle".equals(var3) && !Core.getInstance().getPerfReflectionsOnLoad()) {
                    var3 = var3 + "_noreflect";
                }
                Model var5 = ModelManager.instance.tryGetLoadedModel(var1, var2, var4, var3, false);
                if (var5 == null) {
                    ModelManager.instance.setModelMetaData(var0, var1, var2, var3, var4);
                    Model.ModelAssetParams var6 = new Model.ModelAssetParams();
                    var6.bStatic = var4;
                    var6.meshName = var1;
                    var6.shaderName = var3;
                    var6.textureName = var2;
                    var6.textureFlags = ModelManager.instance.getTextureFlags();
                    var5 = (Model) ModelAssetManager.instance.load(new AssetPath(var0), var6);
                    if (var5 != null) {
                        ModelManager.instance.putLoadedModel(var1, var2, var4, var3, var5);
                    }
                }
                return var5;
            } catch (Exception var7) {
                DebugLog.General.error("LuaManager.loadZomboidModel> Exception thrown loading model: " + var0 + " mesh:" + var1 + " tex:" + var2 + " shader:" + var3 + " isStatic:" + var4);
                var7.printStackTrace();
                return null;
            }
        }

        @LuaMethod(name = "setModelMetaData", global = true)
        public static void setModelMetaData(String var0, String var1, String var2, String var3, boolean var4) {
            if (var1.startsWith("/")) {
                var1 = var1.substring(1);
            }
            if (var2.startsWith("/")) {
                var2 = var2.substring(1);
            }
            ModelManager.instance.setModelMetaData(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "reloadModelsMatching", global = true)
        public static void reloadModelsMatching(String var0) {
            ModelManager.instance.reloadModelsMatching(var0);
        }

        @LuaMethod(name = "getSLSoundManager", global = true)
        public static SLSoundManager getSLSoundManager() {
            return null;
        }

        @LuaMethod(name = "getRadioAPI", global = true)
        public static RadioAPI getRadioAPI() {
            if (RadioAPI.hasInstance()) {
                return RadioAPI.getInstance();
            }
            return null;
        }

        @LuaMethod(name = "getRadioTranslators", global = true)
        public static ArrayList<String> getRadioTranslators(Language var0) {
            return RadioData.getTranslatorNames(var0);
        }

        @LuaMethod(name = "getTranslatorCredits", global = true)
        public static ArrayList<String> getTranslatorCredits(Language var0) {
            File var1 = new File(ZomboidFileSystem.instance.getString("media/lua/shared/Translate/" + var0.name() + "/credits.txt"));
            try {
                FileReader var2 = new FileReader(var1, Charset.forName(var0.charset()));
                try {
                    BufferedReader var3 = new BufferedReader(var2);
                    try {
                        ArrayList<String> var4 = new ArrayList<>();
                        while (true) {
                            String var5 = var3.readLine();
                            if (var5 != null) {
                                if (!StringUtils.isNullOrWhitespace(var5)) {
                                    var4.add(var5.trim());
                                }
                            } else {
                                var3.close();
                                var2.close();
                                return var4;
                            }
                        }
                    } catch (Throwable var9) {
                        try {
                            var3.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                        throw var9;
                    }
                } catch (Throwable var10) {
                    try {
                        var2.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                    throw var10;
                }
            } catch (FileNotFoundException e) {
                return null;
            } catch (Exception var12) {
                ExceptionLogger.logException(var12);
                return null;
            }
        }

        @LuaMethod(name = "getBehaviourDebugPlayer", global = true)
        public static IsoGameCharacter getBehaviourDebugPlayer() {
            return null;
        }

        @LuaMethod(name = "setBehaviorStep", global = true)
        public static void setBehaviorStep(boolean var0) {
        }

        @LuaMethod(name = "getPuddlesManager", global = true)
        public static IsoPuddles getPuddlesManager() {
            return IsoPuddles.getInstance();
        }

        @LuaMethod(name = "setPuddles", global = true)
        public static void setPuddles(float var0) {
            IsoPuddles.PuddlesFloat var1 = IsoPuddles.getInstance().getPuddlesFloat(3);
            var1.setEnableAdmin(true);
            var1.setAdminValue(var0);
            IsoPuddles.PuddlesFloat var12 = IsoPuddles.getInstance().getPuddlesFloat(1);
            var12.setEnableAdmin(true);
            var12.setAdminValue(PZMath.clamp_01(var0 * 1.2f));
        }

        @LuaMethod(name = "getZomboidRadio", global = true)
        public static ZomboidRadio getZomboidRadio() {
            if (ZomboidRadio.hasInstance()) {
                return ZomboidRadio.getInstance();
            }
            return null;
        }

        @LuaMethod(name = "getRandomUUID", global = true)
        public static String getRandomUUID() {
            return ModUtilsJava.getRandomUUID();
        }

        @LuaMethod(name = "sendItemListNet", global = true)
        public static boolean sendItemListNet(IsoPlayer var0, ArrayList<InventoryItem> var1, IsoPlayer var2, String var3, String var4) {
            return ModUtilsJava.sendItemListNet(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "instanceof", global = true)
        public static boolean instof(Object var0, String var1) {
            if (var0 == null) {
                return false;
            }
            if (LuaManager.exposer.TypeMap.containsKey(var1)) {
                Class<?> var3 = (Class) LuaManager.exposer.TypeMap.get(var1);
                return var3.isInstance(var0);
            }
            if (var1.equals("LuaClosure") && (var0 instanceof LuaClosure)) {
                return true;
            }
            return var1.equals("KahluaTableImpl") && (var0 instanceof KahluaTableImpl);
        }

        @LuaMethod(name = "serverConnect", global = true)
        public static void serverConnect(String var0, String var1, String var2, String var3, String var4, String var5, String var6, boolean var7) {
            Core.GameMode = "Multiplayer";
            Core.setDifficulty("Hardcore");
            if (GameClient.connection != null) {
                GameClient.connection.forceDisconnect("lua-connect");
            }
            GameClient.instance.resetDisconnectTimer();
            GameClient.bClient = true;
            GameClient.bCoopInvite = false;
            ZomboidFileSystem.instance.cleanMultiplayerSaves();
            GameClient.instance.doConnect(var0, var1, var2, var3, var4, var5, var6, var7);
        }

        @LuaMethod(name = "serverConnectCoop", global = true)
        public static void serverConnectCoop(String var0) {
            Core.GameMode = "Multiplayer";
            Core.setDifficulty("Hardcore");
            if (GameClient.connection != null) {
                GameClient.connection.forceDisconnect("lua-connect-coop");
            }
            GameClient.bClient = true;
            GameClient.bCoopInvite = true;
            GameClient.instance.doConnectCoop(var0);
        }

        @LuaMethod(name = "sendPing", global = true)
        public static void sendPing() {
            if (GameClient.bClient) {
                ByteBufferWriter var0 = GameClient.connection.startPingPacket();
                PacketTypes.doPingPacket(var0);
                var0.putLong(System.currentTimeMillis());
                GameClient.connection.endPingPacket();
            }
        }

        @LuaMethod(name = "connectionManagerLog", global = true)
        public static void connectionManagerLog(String var0, String var1) {
            ConnectionManager.log(var0, var1, (UdpConnection) null);
        }

        @LuaMethod(name = "forceDisconnect", global = true)
        public static void forceDisconnect() {
            if (GameClient.connection != null) {
                GameClient.connection.forceDisconnect("lua-force-disconnect");
            }
        }

        @LuaMethod(name = "backToSinglePlayer", global = true)
        public static void backToSinglePlayer() {
            if (GameClient.bClient) {
                GameClient.instance.doDisconnect("going back to single-player");
                GameClient.bClient = false;
                timeLastRefresh = 0L;
            }
        }

        @LuaMethod(name = "isIngameState", global = true)
        public static boolean isIngameState() {
            return GameWindow.states.current == IngameState.instance;
        }

        @LuaMethod(name = "requestPacketCounts", global = true)
        public static void requestPacketCounts() {
            if (GameClient.bClient) {
                GameClient.instance.requestPacketCounts();
            }
        }

        @LuaMethod(name = "canConnect", global = true)
        public static boolean canConnect() {
            return GameClient.instance.canConnect();
        }

        @LuaMethod(name = "getReconnectCountdownTimer", global = true)
        public static String getReconnectCountdownTimer() {
            return GameClient.instance.getReconnectCountdownTimer();
        }

        @LuaMethod(name = "getPacketCounts", global = true)
        public static KahluaTable getPacketCounts(int var0) {
            if (GameClient.bClient) {
                return PacketTypes.getPacketCounts(var0);
            }
            return null;
        }

        @LuaMethod(name = "getAllItems", global = true)
        public static ArrayList<Item> getAllItems() {
            return ScriptManager.instance.getAllItems();
        }

        @LuaMethod(name = "scoreboardUpdate", global = true)
        public static void scoreboardUpdate() {
            GameClient.instance.scoreboardUpdate();
        }

        @LuaMethod(name = "save", global = true)
        public static void save(boolean var0) {
            try {
                GameWindow.save(var0);
            } catch (Throwable var2) {
                ExceptionLogger.logException(var2);
            }
        }

        @LuaMethod(name = "saveGame", global = true)
        public static void saveGame() {
            save(true);
        }

        @LuaMethod(name = "getAllRecipes", global = true)
        public static ArrayList<Recipe> getAllRecipes() {
            return new ArrayList<>(ScriptManager.instance.getAllRecipes());
        }

        @LuaMethod(name = "requestUserlog", global = true)
        public static void requestUserlog(String var0) {
            if (GameClient.bClient) {
                GameClient.instance.requestUserlog(var0);
            }
        }

        @LuaMethod(name = "addUserlog", global = true)
        public static void addUserlog(String var0, String var1, String var2) {
            if (GameClient.bClient) {
                GameClient.instance.addUserlog(var0, var1, var2);
            }
        }

        @LuaMethod(name = "removeUserlog", global = true)
        public static void removeUserlog(String var0, String var1, String var2) {
            if (GameClient.bClient) {
                GameClient.instance.removeUserlog(var0, var1, var2);
            }
        }

        @LuaMethod(name = "tabToX", global = true)
        public static String tabToX(String var0, int var1) {
            StringBuilder var0Builder = new StringBuilder(var0);
            while (var0Builder.length() < var1) {
                var0Builder.append(" ");
            }
            String var02 = var0Builder.toString();
            return var02;
        }

        @LuaMethod(name = "istype", global = true)
        public static boolean isType(Object var0, String var1) {
            if (LuaManager.exposer.TypeMap.containsKey(var1)) {
                Class<?> var2 = (Class) LuaManager.exposer.TypeMap.get(var1);
                return var2.equals(var0.getClass());
            }
            return false;
        }

        @LuaMethod(name = "isoToScreenX", global = true)
        public static float isoToScreenX(int var0, float var1, float var2, float var3) {
            float var4 = IsoUtils.XToScreen(var1, var2, var3, 0) - IsoCamera.cameras[var0].getOffX();
            return IsoCamera.getScreenLeft(var0) + (var4 / Core.getInstance().getZoom(var0));
        }

        @LuaMethod(name = "isoToScreenY", global = true)
        public static float isoToScreenY(int var0, float var1, float var2, float var3) {
            float var4 = IsoUtils.YToScreen(var1, var2, var3, 0) - IsoCamera.cameras[var0].getOffY();
            return IsoCamera.getScreenTop(var0) + (var4 / Core.getInstance().getZoom(var0));
        }

        @LuaMethod(name = "screenToIsoX", global = true)
        public static float screenToIsoX(int var0, float var1, float var2, float var3) {
            float var4 = Core.getInstance().getZoom(var0);
            return IsoCamera.cameras[var0].XToIso((var1 - IsoCamera.getScreenLeft(var0)) * var4, (var2 - IsoCamera.getScreenTop(var0)) * var4, var3);
        }

        @LuaMethod(name = "screenToIsoY", global = true)
        public static float screenToIsoY(int var0, float var1, float var2, float var3) {
            float var4 = Core.getInstance().getZoom(var0);
            return IsoCamera.cameras[var0].YToIso((var1 - IsoCamera.getScreenLeft(var0)) * var4, (var2 - IsoCamera.getScreenTop(var0)) * var4, var3);
        }

        @LuaMethod(name = "getAmbientStreamManager", global = true)
        public static BaseAmbientStreamManager getAmbientStreamManager() {
            return AmbientStreamManager.instance;
        }

        @LuaMethod(name = "getSleepingEvent", global = true)
        public static SleepingEvent getSleepingEvent() {
            return SleepingEvent.instance;
        }

        @LuaMethod(name = "setPlayerMovementActive", global = true)
        public static void setPlayerMovementActive(int var0, boolean var1) {
            IsoPlayer.players[var0].bJoypadMovementActive = var1;
        }

        @LuaMethod(name = "setActivePlayer", global = true)
        public static void setActivePlayer(int var0) {
            if (!GameClient.bClient) {
                IsoPlayer.setInstance(IsoPlayer.players[var0]);
                IsoCamera.CamCharacter = IsoPlayer.getInstance();
            }
        }

        @LuaMethod(name = "getPlayer", global = true)
        public static IsoPlayer getPlayer() {
            return IsoPlayer.getInstance();
        }

        @LuaMethod(name = "getNumActivePlayers", global = true)
        public static int getNumActivePlayers() {
            return IsoPlayer.numPlayers;
        }

        @LuaMethod(name = "playServerSound", global = true)
        public static void playServerSound(String var0, IsoGridSquare var1) {
            GameServer.PlayWorldSoundServer(var0, false, var1, 0.2f, 5.0f, 1.1f, true);
        }

        @LuaMethod(name = "getMaxActivePlayers", global = true)
        public static int getMaxActivePlayers() {
            return 4;
        }

        @LuaMethod(name = "getPlayerScreenLeft", global = true)
        public static int getPlayerScreenLeft(int var0) {
            return IsoCamera.getScreenLeft(var0);
        }

        @LuaMethod(name = "getPlayerScreenTop", global = true)
        public static int getPlayerScreenTop(int var0) {
            return IsoCamera.getScreenTop(var0);
        }

        @LuaMethod(name = "getPlayerScreenWidth", global = true)
        public static int getPlayerScreenWidth(int var0) {
            return IsoCamera.getScreenWidth(var0);
        }

        @LuaMethod(name = "getPlayerScreenHeight", global = true)
        public static int getPlayerScreenHeight(int var0) {
            return IsoCamera.getScreenHeight(var0);
        }

        @LuaMethod(name = "getPlayerByOnlineID", global = true)
        public static IsoPlayer getPlayerByOnlineID(int var0) {
            if (GameServer.bServer) {
                return (IsoPlayer) GameServer.IDToPlayerMap.get(Short.valueOf((short) var0));
            }
            if (GameClient.bClient) {
                return (IsoPlayer) GameClient.IDToPlayerMap.get(Short.valueOf((short) var0));
            }
            return null;
        }

        @LuaMethod(name = "initUISystem", global = true)
        public static void initUISystem() {
            UIManager.init();
            LuaEventManager.triggerEvent("OnCreatePlayer", 0, IsoPlayer.players[0]);
        }

        @LuaMethod(name = "getPerformance", global = true)
        public static PerformanceSettings getPerformance() {
            return PerformanceSettings.instance;
        }

        @LuaMethod(name = "getDBSchema", global = true)
        public static void getDBSchema() {
            GameClient.instance.getDBSchema();
        }

        @LuaMethod(name = "getTableResult", global = true)
        public static void getTableResult(String var0, int var1) {
            GameClient.instance.getTableResult(var0, var1);
        }

        @LuaMethod(name = "getWorldSoundManager", global = true)
        public static WorldSoundManager getWorldSoundManager() {
            return WorldSoundManager.instance;
        }

        @LuaMethod(name = "AddWorldSound", global = true)
        public static void AddWorldSound(IsoPlayer var0, int var1, int var2) {
            WorldSoundManager.instance.addSound((Object) null, (int) var0.getX(), (int) var0.getY(), (int) var0.getZ(), var1, var2, false);
        }

        @LuaMethod(name = "AddNoiseToken", global = true)
        public static void AddNoiseToken(IsoGridSquare var0, int var1) {
        }

        @LuaMethod(name = "pauseSoundAndMusic", global = true)
        public static void pauseSoundAndMusic() {
            DebugLog.log("EXITDEBUG: pauseSoundAndMusic 1");
            SoundManager.instance.pauseSoundAndMusic();
            DebugLog.log("EXITDEBUG: pauseSoundAndMusic 2");
        }

        @LuaMethod(name = "resumeSoundAndMusic", global = true)
        public static void resumeSoundAndMusic() {
            SoundManager.instance.resumeSoundAndMusic();
        }

        @LuaMethod(name = "isDemo", global = true)
        public static boolean isDemo() {
            return false;
        }

        @LuaMethod(name = "getTimeInMillis", global = true)
        public static long getTimeInMillis() {
            return System.currentTimeMillis();
        }

        @LuaMethod(name = "getCurrentCoroutine", global = true)
        public static Coroutine getCurrentCoroutine() {
            return LuaManager.thread.getCurrentCoroutine();
        }

        @LuaMethod(name = "reloadLuaFile", global = true)
        public static void reloadLuaFile(String var0) {
            LuaManager.loaded.remove(var0);
            LuaManager.RunLua(var0, true);
        }

        @LuaMethod(name = "reloadServerLuaFile", global = true)
        public static void reloadServerLuaFile(String var0) {
            if (GameServer.bServer) {
                String var10000 = ZomboidFileSystem.instance.getCacheDir();
                String var02 = var10000 + File.separator + "Server" + File.separator + var0;
                LuaManager.loaded.remove(var02);
                LuaManager.RunLua(var02, true);
            }
        }

        @LuaMethod(name = "getServerSpawnRegions", global = true)
        public static KahluaTable getServerSpawnRegions() {
            if (GameClient.bClient) {
                return GameClient.instance.getServerSpawnRegions();
            }
            return null;
        }

        @LuaMethod(name = "getServerOptions", global = true)
        public static ServerOptions getServerOptions() {
            return ServerOptions.instance;
        }

        @LuaMethod(name = "getServerName", global = true)
        public static String getServerName() {
            if (GameServer.bServer) {
                return GameServer.ServerName;
            }
            return GameClient.bClient ? GameClient.ServerName : "";
        }

        @LuaMethod(name = "getServerIP", global = true)
        public static String getServerIP() {
            return GameServer.bServer ? GameServer.IPCommandline == null ? GameServer.ip : GameServer.IPCommandline : GameClient.bClient ? GameClient.ip : "";
        }

        @LuaMethod(name = "getServerPort", global = true)
        public static String getServerPort() {
            if (GameServer.bServer) {
                return String.valueOf(GameServer.DEFAULT_PORT);
            }
            return GameClient.bClient ? String.valueOf(GameClient.port) : "";
        }

        @LuaMethod(name = "isShowConnectionInfo", global = true)
        public static boolean isShowConnectionInfo() {
            return NetworkAIParams.isShowConnectionInfo();
        }

        @LuaMethod(name = "setShowConnectionInfo", global = true)
        public static void setShowConnectionInfo(boolean var0) {
            NetworkAIParams.setShowConnectionInfo(var0);
        }

        @LuaMethod(name = "isShowServerInfo", global = true)
        public static boolean isShowServerInfo() {
            return NetworkAIParams.isShowServerInfo();
        }

        @LuaMethod(name = "setShowServerInfo", global = true)
        public static void setShowServerInfo(boolean var0) {
            NetworkAIParams.setShowServerInfo(var0);
        }

        @LuaMethod(name = "isShowPingInfo", global = true)
        public static boolean isShowPingInfo() {
            return NetworkAIParams.isShowPingInfo();
        }

        @LuaMethod(name = "setShowPingInfo", global = true)
        public static void setShowPingInfo(boolean var0) {
            NetworkAIParams.setShowPingInfo(var0);
        }

        @LuaMethod(name = "getSpecificPlayer", global = true)
        public static IsoPlayer getSpecificPlayer(int var0) {
            return IsoPlayer.players[var0];
        }

        @LuaMethod(name = "getCameraOffX", global = true)
        public static float getCameraOffX() {
            return IsoCamera.getOffX();
        }

        @LuaMethod(name = "getLatestSave", global = true)
        public static KahluaTable getLatestSave() {
            KahluaTable var0 = LuaManager.platform.newTable();
            try {
                String var10006 = ZomboidFileSystem.instance.getCacheDir();
                BufferedReader var1 = new BufferedReader(new FileReader(var10006 + File.separator + "latestSave.ini"));
                int var3 = 1;
                while (true) {
                    try {
                        String var2 = var1.readLine();
                        if (var2 != null) {
                            var0.rawset(var3, var2);
                            var3++;
                        } else {
                            var1.close();
                            return var0;
                        }
                    } catch (Exception e) {
                        return var0;
                    }
                }
            } catch (FileNotFoundException e2) {
                return var0;
            }
        }

        @LuaMethod(name = "isCurrentExecutionPoint", global = true)
        public static boolean isCurrentExecutionPoint(String var0, int var1) {
            int var2 = LuaManager.thread.currentCoroutine.getCallframeTop() - 1;
            if (var2 < 0) {
                var2 = 0;
            }
            LuaCallFrame var3 = LuaManager.thread.currentCoroutine.getCallFrame(var2);
            return var3.closure != null && var3.closure.prototype.lines[var3.pc] == var1 && var0.equals(var3.closure.prototype.filename);
        }

        @LuaMethod(name = "toggleBreakOnChange", global = true)
        public static void toggleBreakOnChange(KahluaTable var0, Object var1) {
            if (Core.bDebug) {
                LuaManager.thread.toggleBreakOnChange(var0, var1);
            }
        }

        @LuaMethod(name = "isDebugEnabled", global = true)
        public static boolean isDebugEnabled() {
            return Core.bDebug;
        }

        @LuaMethod(name = "toggleBreakOnRead", global = true)
        public static void toggleBreakOnRead(KahluaTable var0, Object var1) {
            if (Core.bDebug) {
                LuaManager.thread.toggleBreakOnRead(var0, var1);
            }
        }

        @LuaMethod(name = "toggleBreakpoint", global = true)
        public static void toggleBreakpoint(String var0, int var1) {
            String var02 = var0.replace("\\", "/");
            if (Core.bDebug) {
                LuaManager.thread.breakpointToggle(var02, var1);
            }
        }

        @LuaMethod(name = "sendVisual", global = true)
        public static void sendVisual(IsoPlayer var0) {
            if (GameClient.bClient) {
                GameClient.instance.sendVisual(var0);
            }
        }

        @LuaMethod(name = "sendClothing", global = true)
        public static void sendClothing(IsoPlayer var0) {
            if (GameClient.bClient) {
                GameClient.instance.sendClothing(var0, "", (InventoryItem) null);
            }
        }

        @LuaMethod(name = "hasDataReadBreakpoint", global = true)
        public static boolean hasDataReadBreakpoint(KahluaTable var0, Object var1) {
            return LuaManager.thread.hasReadDataBreakpoint(var0, var1);
        }

        @LuaMethod(name = "hasDataBreakpoint", global = true)
        public static boolean hasDataBreakpoint(KahluaTable var0, Object var1) {
            return LuaManager.thread.hasDataBreakpoint(var0, var1);
        }

        @LuaMethod(name = "hasBreakpoint", global = true)
        public static boolean hasBreakpoint(String var0, int var1) {
            return LuaManager.thread.hasBreakpoint(var0, var1);
        }

        @LuaMethod(name = "getLoadedLuaCount", global = true)
        public static int getLoadedLuaCount() {
            return LuaManager.loaded.size();
        }

        @LuaMethod(name = "getLoadedLua", global = true)
        public static String getLoadedLua(int var0) {
            return LuaManager.loaded.get(var0);
        }

        @LuaMethod(name = "isServer", global = true)
        public static boolean isServer() {
            return GameServer.bServer;
        }

        @LuaMethod(name = "isServerSoftReset", global = true)
        public static boolean isServerSoftReset() {
            return GameServer.bServer && GameServer.bSoftReset;
        }

        @LuaMethod(name = "isClient", global = true)
        public static boolean isClient() {
            return GameClient.bClient;
        }

        @LuaMethod(name = "canModifyPlayerStats", global = true)
        public static boolean canModifyPlayerStats() {
            return !GameClient.bClient || GameClient.canModifyPlayerStats();
        }

        @LuaMethod(name = "executeQuery", global = true)
        public static void executeQuery(String var0, KahluaTable var1) {
            GameClient.instance.executeQuery(var0, var1);
        }

        @LuaMethod(name = "canSeePlayerStats", global = true)
        public static boolean canSeePlayerStats() {
            return GameClient.canSeePlayerStats();
        }

        @LuaMethod(name = "getAccessLevel", global = true)
        public static String getAccessLevel() {
            return PlayerType.toString(GameClient.connection.accessLevel);
        }

        @LuaMethod(name = "getOnlinePlayers", global = true)
        public static ArrayList<IsoPlayer> getOnlinePlayers() {
            if (GameServer.bServer) {
                return GameServer.getPlayers();
            }
            if (GameClient.bClient) {
                return GameClient.instance.getPlayers();
            }
            return null;
        }

        @LuaMethod(name = "getDebug", global = true)
        public static boolean getDebug() {
            return Core.bDebug || (GameServer.bServer && GameServer.bDebug);
        }

        @LuaMethod(name = "getCameraOffY", global = true)
        public static float getCameraOffY() {
            return IsoCamera.getOffY();
        }

        @LuaMethod(name = "createRegionFile", global = true)
        public static KahluaTable createRegionFile() {
            KahluaTable var0 = LuaManager.platform.newTable();
            String var1 = IsoWorld.instance.getMap();
            if (var1.equals("DEFAULT")) {
                MapGroups var2 = new MapGroups();
                var2.createGroups();
                if (var2.getNumberOfGroups() != 1) {
                    throw new RuntimeException("GameMap is DEFAULT but there are multiple worlds to choose from");
                }
                var2.setWorld(0);
                var1 = IsoWorld.instance.getMap();
            }
            if (!GameClient.bClient && !GameServer.bServer) {
                var1 = MapGroups.addMissingVanillaDirectories(var1);
            }
            String[] var10 = var1.split(";");
            int var3 = 1;
            int length = var10.length;
            for (String s : var10) {
                String var7 = s.trim();
                if (!var7.isEmpty()) {
                    File var8 = new File(ZomboidFileSystem.instance.getString("media/maps/" + var7 + "/spawnpoints.lua"));
                    if (var8.exists()) {
                        KahluaTable var9 = LuaManager.platform.newTable();
                        var9.rawset("name", var7);
                        var9.rawset("file", "media/maps/" + var7 + "/spawnpoints.lua");
                        var0.rawset(var3, var9);
                        var3++;
                    }
                }
            }
            return var0;
        }

        @LuaMethod(name = "getMapDirectoryTable", global = true)
        public static KahluaTable getMapDirectoryTable() {
            String[] var2;
            KahluaTable var0 = LuaManager.platform.newTable();
            String[] var22 = ZomboidFileSystem.instance.getMediaFile("maps").list();
            if (var22 != null) {
                int var3 = 1;
                for (String value : var22) {
                    if (!value.equals("challengemaps")) {
                        var0.rawset(var3, value);
                        var3++;
                    }
                }
                Iterator<String> it = ZomboidFileSystem.instance.getModIDs().iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    ChooseGameInfo.Mod var6 = null;
                    try {
                        var6 = ChooseGameInfo.getAvailableModDetails(s);
                    } catch (Exception e) {
                    }
                    if (var6 != null) {
                        File var1 = new File(var6.getDir() + "/media/maps/");
                        if (var1.exists() && (var2 = var1.list()) != null) {
                            for (String var8 : var2) {
                                ChooseGameInfo.Map var9 = ChooseGameInfo.getMapDetails(var8);
                                if (var9.getLotDirectories() != null && !var9.getLotDirectories().isEmpty() && !var8.equals("challengemaps")) {
                                    var0.rawset(var3, var8);
                                    var3++;
                                }
                            }
                        }
                    }
                }
            }
            return var0;
        }

        @LuaMethod(name = "deleteSave", global = true)
        public static void deleteSave(String var0) {
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator + var0);
            String[] var2 = var1.list();
            if (var2 != null) {
                for (String s : var2) {
                    File var4 = new File(ZomboidFileSystem.instance.getSaveDir() + File.separator + var0 + File.separator + s);
                    if (var4.isDirectory()) {
                        deleteSave(var0 + File.separator + var4.getName());
                    }
                    var4.delete();
                }
                var1.delete();
            }
        }

        @LuaMethod(name = "sendPlayerExtraInfo", global = true)
        public static void sendPlayerExtraInfo(IsoPlayer var0) {
            GameClient.sendPlayerExtraInfo(var0);
        }

        @LuaMethod(name = "getServerAddressFromArgs", global = true)
        public static String getServerAddressFromArgs() {
            if (System.getProperty("args.server.connect") != null) {
                String var0 = System.getProperty("args.server.connect");
                System.clearProperty("args.server.connect");
                return var0;
            }
            return null;
        }

        @LuaMethod(name = "getServerPasswordFromArgs", global = true)
        public static String getServerPasswordFromArgs() {
            if (System.getProperty("args.server.password") != null) {
                String var0 = System.getProperty("args.server.password");
                System.clearProperty("args.server.password");
                return var0;
            }
            return null;
        }

        @LuaMethod(name = "getServerListFile", global = true)
        public static String getServerListFile() {
            return SteamUtils.isSteamModeEnabled() ? "ServerListSteam.txt" : "ServerList.txt";
        }

        @LuaMethod(name = "getServerList", global = true)
        public static KahluaTable getServerList() {
            ArrayList<Server> var0 = new ArrayList<>();
            KahluaTable var1 = LuaManager.platform.newTable();
            BufferedReader var2 = null;
            try {
                try {
                    String var10002 = LuaManager.getLuaCacheDir();
                    File var3 = new File(var10002 + File.separator + getServerListFile());
                    if (!var3.exists()) {
                        var3.createNewFile();
                    }
                    var2 = new BufferedReader(new FileReader(var3, StandardCharsets.UTF_8));
                    Server var5 = null;
                    while (true) {
                        String var4 = var2.readLine();
                        if (var4 == null) {
                            break;
                        }
                        if (var4.startsWith("name=")) {
                            var5 = new Server();
                            var0.add(var5);
                            var5.setName(var4.replaceFirst("name=", ""));
                        } else if (var4.startsWith("ip=")) {
                            var5.setIp(var4.replaceFirst("ip=", ""));
                        } else if (var4.startsWith("localip=")) {
                            var5.setLocalIP(var4.replaceFirst("localip=", ""));
                        } else if (var4.startsWith("description=")) {
                            var5.setDescription(var4.replaceFirst("description=", ""));
                        } else if (var4.startsWith("port=")) {
                            var5.setPort(var4.replaceFirst("port=", ""));
                        } else if (var4.startsWith("user=")) {
                            var5.setUserName(var4.replaceFirst("user=", ""));
                        } else if (var4.startsWith("password=")) {
                            var5.setPwd(var4.replaceFirst("password=", ""));
                        } else if (var4.startsWith("serverpassword=")) {
                            var5.setServerPassword(var4.replaceFirst("serverpassword=", ""));
                        } else if (var4.startsWith("usesteamrelay=")) {
                            var5.setUseSteamRelay(Boolean.parseBoolean(var4.replaceFirst("usesteamrelay=", "")));
                        }
                    }
                    int var6 = 1;
                    Iterator<Server> it = var0.iterator();
                    while (it.hasNext()) {
                        Server o = it.next();
                        Double var9 = Double.valueOf(var6);
                        var1.rawset(var9, o);
                        var6++;
                    }
                    try {
                        var2.close();
                    } catch (Exception e) {
                    }
                } catch (Exception var18) {
                    var18.printStackTrace();
                    try {
                        var2.close();
                    } catch (Exception e2) {
                    }
                }
                return var1;
            } catch (Throwable th) {
                try {
                    var2.close();
                } catch (Exception e3) {
                }
                throw th;
            }
        }

        @LuaMethod(name = "ping", global = true)
        public static void ping(String var0, String var1, String var2, String var3) {
            GameClient.askPing = true;
            serverConnect(var0, var1, var2, "", var3, "", "", false);
        }

        @LuaMethod(name = "stopPing", global = true)
        public static void stopPing() {
            GameClient.askPing = false;
        }

        @LuaMethod(name = "transformIntoKahluaTable", global = true)
        public static KahluaTable transformIntoKahluaTable(HashMap<Object, Object> var0) {
            KahluaTable var1 = LuaManager.platform.newTable();
            for (Map.Entry<Object, Object> objectObjectEntry : var0.entrySet()) {
                var1.rawset(objectObjectEntry.getKey(), objectObjectEntry.getValue());
            }
            return var1;
        }

        @LuaMethod(name = "getSaveDirectory", global = true)
        public static ArrayList<File> getSaveDirectory(String var0) {
            File var1 = new File(var0 + File.separator);
            if (!var1.exists() && !Core.getInstance().isNoSave()) {
                var1.mkdir();
            }
            String[] var2 = var1.list();
            if (var2 == null) {
                return null;
            }
            ArrayList<File> var3 = new ArrayList<>();
            for (String s : var2) {
                File var5 = new File(var0 + File.separator + s);
                if (var5.isDirectory()) {
                    var3.add(var5);
                }
            }
            return var3;
        }

        @LuaMethod(name = "getFullSaveDirectoryTable", global = true)
        public static KahluaTable getFullSaveDirectoryTable() {
            KahluaTable var0 = LuaManager.platform.newTable();
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator);
            if (!var1.exists()) {
                var1.mkdir();
            }
            String[] var2 = var1.list();
            if (var2 != null) {
                ArrayList<File> var3 = new ArrayList<>();
                for (int var4 = 0; var4 < var2.length; var4++) {
                    String var100022 = ZomboidFileSystem.instance.getSaveDir();
                    File var5 = new File(var100022 + File.separator + var2[var4]);
                    if (var5.isDirectory() && !"Multiplayer".equals(var2[var4])) {
                        String var10000 = ZomboidFileSystem.instance.getSaveDir();
                        ArrayList<File> var6 = getSaveDirectory(var10000 + File.separator + var2[var4]);
                        var3.addAll(var6);
                    }
                }
                var3.sort((var11, var21) -> {
                    return Long.compare(var21.lastModified(), var11.lastModified());
                });
                int var42 = 1;
                Iterator<File> it = var3.iterator();
                while (it.hasNext()) {
                    File o = it.next();
                    String var7 = getSaveName(o);
                    Double var8 = Double.valueOf(var42);
                    var0.rawset(var8, var7);
                    var42++;
                }
            }
            return var0;
        }

        public static String getSaveName(File var0) {
            String[] var1 = var0.getAbsolutePath().split("\\" + File.separator);
            return var1[var1.length - 2] + File.separator + var0.getName();
        }

        @LuaMethod(name = "getSaveDirectoryTable", global = true)
        public static KahluaTable getSaveDirectoryTable() {
            return LuaManager.platform.newTable();
        }

        public static List<String> getMods() {
            ArrayList<String> var0 = new ArrayList<>();
            ZomboidFileSystem.instance.getAllModFolders(var0);
            return var0;
        }

        @LuaMethod(name = "doChallenge", global = true)
        public static void doChallenge(KahluaTable var0) {
            Core.GameMode = var0.rawget("gameMode").toString();
            Core.ChallengeID = var0.rawget("id").toString();
            Core.bLastStand = Core.GameMode.equals("LastStand");
            Core.getInstance().setChallenge(true);
            getWorld().setMap(var0.getString("world"));
            int var1 = Rand.Next(100000000);
            IsoWorld.instance.setWorld(Integer.toString(var1));
            getWorld().bDoChunkMapUpdate = false;
        }

        @LuaMethod(name = "doTutorial", global = true)
        public static void doTutorial(KahluaTable var0) {
            Core.GameMode = "Tutorial";
            Core.bLastStand = false;
            Core.ChallengeID = null;
            Core.getInstance().setChallenge(false);
            Core.bTutorial = true;
            getWorld().setMap(var0.getString("world"));
            getWorld().bDoChunkMapUpdate = false;
        }

        @LuaMethod(name = "deleteAllGameModeSaves", global = true)
        public static void deleteAllGameModeSaves(String var0) {
            String var1 = Core.GameMode;
            Core.GameMode = var0;
            Path var2 = Paths.get(ZomboidFileSystem.instance.getGameModeCacheDir(), new String[0]);
            if (Files.exists(var2, new LinkOption[0])) {
                try {
                    Files.walkFileTree(var2, new FileVisitor<Path>() { // from class: zombie.Lua.LuaManager.GlobalObject.1
                        @Override // java.nio.file.FileVisitor
                        public FileVisitResult preVisitDirectory(Path var12, BasicFileAttributes var22) throws IOException {
                            return FileVisitResult.CONTINUE;
                        }

                        @Override // java.nio.file.FileVisitor
                        public FileVisitResult visitFile(Path var12, BasicFileAttributes var22) throws IOException {
                            Files.delete(var12);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override // java.nio.file.FileVisitor
                        public FileVisitResult visitFileFailed(Path var12, IOException var22) throws IOException {
                            var22.printStackTrace();
                            return FileVisitResult.CONTINUE;
                        }

                        @Override // java.nio.file.FileVisitor
                        public FileVisitResult postVisitDirectory(Path var12, IOException var22) throws IOException {
                            Files.delete(var12);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException var4) {
                    var4.printStackTrace();
                }
            }
            Core.GameMode = var1;
        }

        @LuaMethod(name = "sledgeDestroy", global = true)
        public static void sledgeDestroy(IsoObject var0) {
            if (GameClient.bClient) {
                GameClient.destroy(var0);
            }
        }

        @LuaMethod(name = "getTickets", global = true)
        public static void getTickets(String var0) {
            if (GameClient.bClient) {
                GameClient.getTickets(var0);
            }
        }

        @LuaMethod(name = "addTicket", global = true)
        public static void addTicket(String var0, String var1, int var2) {
            if (GameClient.bClient) {
                GameClient.addTicket(var0, var1, var2);
            }
        }

        @LuaMethod(name = "removeTicket", global = true)
        public static void removeTicket(int var0) {
            if (GameClient.bClient) {
                GameClient.removeTicket(var0);
            }
        }

        @LuaMethod(name = "sendFactionInvite", global = true)
        public static void sendFactionInvite(Faction var0, IsoPlayer var1, String var2) {
            if (GameClient.bClient) {
                GameClient.sendFactionInvite(var0, var1, var2);
            }
        }

        @LuaMethod(name = "acceptFactionInvite", global = true)
        public static void acceptFactionInvite(Faction var0, String var1) {
            if (GameClient.bClient) {
                GameClient.acceptFactionInvite(var0, var1);
            }
        }

        @LuaMethod(name = "sendSafehouseInvite", global = true)
        public static void sendSafehouseInvite(SafeHouse var0, IsoPlayer var1, String var2) {
            if (GameClient.bClient) {
                GameClient.sendSafehouseInvite(var0, var1, var2);
            }
        }

        @LuaMethod(name = "acceptSafehouseInvite", global = true)
        public static void acceptSafehouseInvite(SafeHouse var0, String var1) {
            if (GameClient.bClient) {
                GameClient.acceptSafehouseInvite(var0, var1);
            }
        }

        @LuaMethod(name = "createHordeFromTo", global = true)
        public static void createHordeFromTo(float var0, float var1, float var2, float var3, int var4) {
            ZombiePopulationManager.instance.createHordeFromTo((int) var0, (int) var1, (int) var2, (int) var3, var4);
        }

        @LuaMethod(name = "createHordeInAreaTo", global = true)
        public static void createHordeInAreaTo(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
            ZombiePopulationManager.instance.createHordeInAreaTo(var0, var1, var2, var3, var4, var5, var6);
        }

        @LuaMethod(name = "spawnHorde", global = true)
        public static void spawnHorde(float var0, float var1, float var2, float var3, float var4, int var5) {
            for (int var6 = 0; var6 < var5; var6++) {
                VirtualZombieManager.instance.choices.clear();
                IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(Rand.Next(var0, var2), Rand.Next(var1, var3), var4);
                if (var7 != null) {
                    VirtualZombieManager.instance.choices.add(var7);
                    IsoZombie var8 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.fromIndex(Rand.Next(IsoDirections.Max.index())).index(), false);
                    var8.dressInRandomOutfit();
                    ZombieSpawnRecorder.instance.record(var8, "LuaManager.spawnHorde");
                }
            }
        }

        @LuaMethod(name = "createZombie", global = true)
        public static IsoZombie createZombie(float var0, float var1, float var2, SurvivorDesc var3, int var4, IsoDirections var5) {
            VirtualZombieManager.instance.choices.clear();
            IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var0, var1, var2);
            VirtualZombieManager.instance.choices.add(var6);
            IsoZombie var7 = VirtualZombieManager.instance.createRealZombieAlways(var5.index(), false);
            ZombieSpawnRecorder.instance.record(var7, "LuaManager.createZombie");
            return var7;
        }

        @LuaMethod(name = "triggerEvent", global = true)
        public static void triggerEvent(String var0) {
            LuaEventManager.triggerEvent(var0);
        }

        @LuaMethod(name = "triggerEvent", global = true)
        public static void triggerEvent(String var0, Object var1) {
            LuaEventManager.triggerEventGarbage(var0, var1);
        }

        @LuaMethod(name = "triggerEvent", global = true)
        public static void triggerEvent(String var0, Object var1, Object var2) {
            LuaEventManager.triggerEventGarbage(var0, var1, var2);
        }

        @LuaMethod(name = "triggerEvent", global = true)
        public static void triggerEvent(String var0, Object var1, Object var2, Object var3) {
            LuaEventManager.triggerEventGarbage(var0, var1, var2, var3);
        }

        @LuaMethod(name = "triggerEvent", global = true)
        public static void triggerEvent(String var0, Object var1, Object var2, Object var3, Object var4) {
            LuaEventManager.triggerEventGarbage(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "debugLuaTable", global = true)
        public static void debugLuaTable(Object var0, int var1) {
            if (var1 <= 1 && (var0 instanceof KahluaTable)) {
                KahluaTable var2 = (KahluaTable) var0;
                KahluaTableIterator var3 = var2.iterator();
                do {
                    Object var7 = var3.getKey();
                    Object var6 = var3.getValue();
                    if (var7 != null) {
                        if (var6 != null) {
                            DebugLog.Lua.debugln(("\t".repeat(Math.max(0, var1))) + var7 + " : " + var6);
                        }
                        if (var6 instanceof KahluaTable) {
                            debugLuaTable(var6, var1 + 1);
                        }
                    }
                } while (var3.advance());
                if (var2.getMetatable() != null) {
                    debugLuaTable(var2.getMetatable(), var1);
                }
            }
        }

        @LuaMethod(name = "debugLuaTable", global = true)
        public static void debugLuaTable(Object var0) {
            debugLuaTable(var0, 0);
        }

        @LuaMethod(name = "sendItemsInContainer", global = true)
        public static void sendItemsInContainer(IsoObject var0, ItemContainer var1) {
            GameServer.sendItemsInContainer(var0, var1 == null ? var0.getContainer() : var1);
        }

        @LuaMethod(name = "getModDirectoryTable", global = true)
        public static KahluaTable getModDirectoryTable() {
            KahluaTable var0 = LuaManager.platform.newTable();
            List<String> var1 = getMods();
            int var2 = 1;
            for (String o : var1) {
                Double var5 = Double.valueOf(var2);
                var0.rawset(var5, o);
                var2++;
            }
            return var0;
        }

        @LuaMethod(name = "getModInfoByID", global = true)
        public static ChooseGameInfo.Mod getModInfoByID(String var0) {
            try {
                return ChooseGameInfo.getModDetails(var0);
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }
        }

        @LuaMethod(name = "getModInfo", global = true)
        public static ChooseGameInfo.Mod getModInfo(String var0) {
            try {
                return ChooseGameInfo.readModInfo(var0);
            } catch (Exception var2) {
                ExceptionLogger.logException(var2);
                return null;
            }
        }

        @LuaMethod(name = "getMapFoldersForMod", global = true)
        public static ArrayList<String> getMapFoldersForMod(String var0) {
            try {
                ChooseGameInfo.Mod var1 = ChooseGameInfo.getModDetails(var0);
                if (var1 == null) {
                    return null;
                }
                String var10000 = var1.getDir();
                String var2 = var10000 + File.separator + "media" + File.separator + "maps";
                File var3 = new File(var2);
                if (var3.exists() && var3.isDirectory()) {
                    ArrayList<String> var4 = null;
                    DirectoryStream<Path> var5 = Files.newDirectoryStream(var3.toPath());
                    try {
                        for (Path o : var5) {
                            if (Files.isDirectory(o, new LinkOption[0]) && new File(var2 + File.separator + o.getFileName().toString() + File.separator + "map.info").exists()) {
                                if (var4 == null) {
                                    var4 = new ArrayList<>();
                                }
                                var4.add(o.getFileName().toString());
                            }
                        }
                        var5.close();
                        return var4;
                    } finally {
                    }
                }
                return null;
            } catch (Exception var10) {
                var10.printStackTrace();
                return null;
            }
        }

        @LuaMethod(name = "spawnpointsExistsForMod", global = true)
        public static boolean spawnpointsExistsForMod(String var0, String var1) {
            try {
                ChooseGameInfo.Mod var2 = ChooseGameInfo.getModDetails(var0);
                if (var2 == null) {
                    return false;
                }
                String var10000 = var2.getDir();
                String var3 = var10000 + File.separator + "media" + File.separator + "maps" + File.separator + var1 + File.separator + "spawnpoints.lua";
                return new File(var3).exists();
            } catch (Exception var4) {
                var4.printStackTrace();
                return false;
            }
        }

        @LuaMethod(name = "getFileSeparator", global = true)
        public static String getFileSeparator() {
            return File.separator;
        }

        @LuaMethod(name = "getScriptManager", global = true)
        public static ScriptManager getScriptManager() {
            return ScriptManager.instance;
        }

        @LuaMethod(name = "checkSaveFolderExists", global = true)
        public static boolean checkSaveFolderExists(String var0) {
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator + var0);
            return var1.exists();
        }

        @LuaMethod(name = "getAbsoluteSaveFolderName", global = true)
        public static String getAbsoluteSaveFolderName(String var0) {
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator + var0);
            return var1.getAbsolutePath();
        }

        @LuaMethod(name = "checkSaveFileExists", global = true)
        public static boolean checkSaveFileExists(String var0) {
            File var1 = new File(ZomboidFileSystem.instance.getFileNameInCurrentSave(var0));
            return var1.exists();
        }

        @LuaMethod(name = "checkSavePlayerExists", global = true)
        public static boolean checkSavePlayerExists() {
            if (GameClient.bClient) {
                return ClientPlayerDB.getInstance() != null && ClientPlayerDB.getInstance().clientLoadNetworkPlayer() && ClientPlayerDB.getInstance().isAliveMainNetworkPlayer();
            }
            return PlayerDBHelper.isPlayerAlive(ZomboidFileSystem.instance.getCurrentSaveDir(), 1);
        }

        @LuaMethod(name = "fileExists", global = true)
        public static boolean fileExists(String var0) {
            String var1 = var0.replace("/", File.separator);
            File var2 = new File(ZomboidFileSystem.instance.getString(var1.replace("\\", File.separator)));
            return var2.exists();
        }

        @LuaMethod(name = "serverFileExists", global = true)
        public static boolean serverFileExists(String var0) {
            String var1 = var0.replace("/", File.separator);
            String var12 = var1.replace("\\", File.separator);
            String var10002 = ZomboidFileSystem.instance.getCacheDir();
            File var2 = new File(var10002 + File.separator + "Server" + File.separator + var12);
            return var2.exists();
        }

        @LuaMethod(name = "takeScreenshot", global = true)
        public static void takeScreenshot() {
            Core.getInstance().TakeFullScreenshot((String) null);
        }

        @LuaMethod(name = "takeScreenshot", global = true)
        public static void takeScreenshot(String var0) {
            Core.getInstance().TakeFullScreenshot(var0);
        }

        @LuaMethod(name = "checkStringPattern", global = true)
        public static boolean checkStringPattern(String var0) {
            return !var0.contains("[");
        }

        @LuaMethod(name = "instanceItem", global = true)
        public static InventoryItem instanceItem(Item var0) {
            return InventoryItemFactory.CreateItem(var0.moduleDotType);
        }

        @LuaMethod(name = "instanceItem", global = true)
        public static InventoryItem instanceItem(String var0) {
            return InventoryItemFactory.CreateItem(var0);
        }

        @LuaMethod(name = "createNewScriptItem", global = true)
        public static Item createNewScriptItem(String var0, String var1, String var2, String var3, String var4) {
            Item var5 = new Item();
            var5.module = ScriptManager.instance.getModule(var0);
            var5.module.ItemMap.put(var1, var5);
            var5.Icon = "Item_" + var4;
            var5.DisplayName = var2;
            var5.name = var1;
            var5.moduleDotType = var5.module.name + "." + var1;
            try {
                var5.type = Item.Type.valueOf(var3);
            } catch (Exception e) {
            }
            return var5;
        }

        @LuaMethod(name = "cloneItemType", global = true)
        public static Item cloneItemType(String var0, String var1) {
            Item var2 = ScriptManager.instance.FindItem(var1);
            Item var3 = new Item();
            var3.module = var2.getModule();
            var3.module.ItemMap.put(var0, var3);
            return var3;
        }

        @LuaMethod(name = "moduleDotType", global = true)
        public static String moduleDotType(String var0, String var1) {
            return StringUtils.moduleDotType(var0, var1);
        }

        @LuaMethod(name = "require", global = true)
        public static Object require(String var0) {
            String var1 = var0;
            if (!var0.endsWith(".lua")) {
                var1 = var0 + ".lua";
            }
            for (int var2 = 0; var2 < LuaManager.paths.size(); var2++) {
                String var3 = LuaManager.paths.get(var2);
                String var4 = ZomboidFileSystem.instance.getAbsolutePath(var3 + var1);
                if (var4 != null) {
                    return LuaManager.RunLua(ZomboidFileSystem.instance.getString(var4));
                }
            }
            DebugLog.Lua.warn("require(\"" + var0 + "\") failed");
            return null;
        }

        @LuaMethod(name = "getRenderer", global = true)
        public static SpriteRenderer getRenderer() {
            return SpriteRenderer.instance;
        }

        @LuaMethod(name = "getGameTime", global = true)
        public static GameTime getGameTime() {
            return GameTime.instance;
        }

        @LuaMethod(name = "getMPStatistics", global = true)
        public static KahluaTable getStatistics() {
            return MPStatistics.getLuaStatistics();
        }

        @LuaMethod(name = "getMPStatus", global = true)
        public static KahluaTable getMPStatus() {
            return MPStatistics.getLuaStatus();
        }

        @LuaMethod(name = "getMaxPlayers", global = true)
        public static Double getMaxPlayers() {
            return Double.valueOf(GameClient.connection.maxPlayers);
        }

        @LuaMethod(name = "getWorld", global = true)
        public static IsoWorld getWorld() {
            return IsoWorld.instance;
        }

        @LuaMethod(name = "getCell", global = true)
        public static IsoCell getCell() {
            return IsoWorld.instance.getCell();
        }

        @LuaMethod(name = "getSandboxOptions", global = true)
        public static SandboxOptions getSandboxOptions() {
            return SandboxOptions.instance;
        }

        @LuaMethod(name = "getFileOutput", global = true)
        public static DataOutputStream getFileOutput(String var0) {
            if (StringUtils.containsDoubleDot(var0)) {
                DebugLog.Lua.warn("relative paths not allowed");
                return null;
            }
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = (var10000 + File.separator + var0).replace("/", File.separator).replace("\\", File.separator);
            String var2 = var1.substring(0, var1.lastIndexOf(File.separator));
            File var3 = new File(var2.replace("\\", "/"));
            if (!var3.exists()) {
                var3.mkdirs();
            }
            File var4 = new File(var1);
            try {
                outStream = new FileOutputStream(var4);
            } catch (FileNotFoundException var6) {
                Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var6);
            }
            return new DataOutputStream(outStream);
        }

        @LuaMethod(name = "getLastStandPlayersDirectory", global = true)
        public static String getLastStandPlayersDirectory() {
            return "LastStand";
        }

        @LuaMethod(name = "getLastStandPlayerFileNames", global = true)
        public static List<String> getLastStandPlayerFileNames() throws IOException {
            ArrayList<String> var0 = new ArrayList<>();
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = var10000 + File.separator + getLastStandPlayersDirectory();
            File var2 = new File(var1.replace("/", File.separator).replace("\\", File.separator));
            if (!var2.exists()) {
                var2.mkdir();
            }
            File[] var3 = var2.listFiles();
            if (var3 == null) {
                return var0;
            }
            int length = var3.length;
            for (File var6 : var3) {
                if (!var6.isDirectory() && var6.getName().endsWith(".txt")) {
                    String var10001 = getLastStandPlayersDirectory();
                    var0.add(var10001 + File.separator + var6.getName());
                }
            }
            return var0;
        }

        @Deprecated
        @LuaMethod(name = "getAllSavedPlayers", global = true)
        public static List<BufferedReader> getAllSavedPlayers() throws IOException {
            ArrayList<BufferedReader> var0 = new ArrayList<>();
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = var10000 + File.separator + getLastStandPlayersDirectory();
            File var2 = new File(var1.replace("/", File.separator).replace("\\", File.separator));
            if (!var2.exists()) {
                var2.mkdir();
            }
            File[] var3 = var2.listFiles();
            if (var3 == null) {
                return var0;
            }
            int length = var3.length;
            for (File var6 : var3) {
                var0.add(new BufferedReader(new FileReader(var6)));
            }
            return var0;
        }

        @LuaMethod(name = "getSandboxPresets", global = true)
        public static List<String> getSandboxPresets() throws IOException {
            ArrayList<String> var0 = new ArrayList<>();
            String var1 = LuaManager.getSandboxCacheDir();
            File var2 = new File(var1);
            if (!var2.exists()) {
                var2.mkdir();
            }
            File[] var3 = var2.listFiles();
            if (var3 == null) {
                return var0;
            }
            int length = var3.length;
            for (File var6 : var3) {
                if (var6.getName().endsWith(".cfg")) {
                    var0.add(var6.getName().replace(".cfg", ""));
                }
            }
            Collections.sort(var0);
            return var0;
        }

        @LuaMethod(name = "deleteSandboxPreset", global = true)
        public static void deleteSandboxPreset(String var0) {
            if (StringUtils.containsDoubleDot(var0)) {
                DebugLog.Lua.warn("relative paths not allowed");
                return;
            }
            String var10000 = LuaManager.getSandboxCacheDir();
            String var1 = var10000 + File.separator + var0 + ".cfg";
            File var2 = new File(var1);
            if (var2.exists()) {
                var2.delete();
            }
        }

        @LuaMethod(name = "getFileReader", global = true)
        public static BufferedReader getFileReader(String var0, boolean var1) throws IOException {
            if (StringUtils.containsDoubleDot(var0)) {
                DebugLog.Lua.warn("relative paths not allowed");
                return null;
            }
            String var10000 = LuaManager.getLuaCacheDir();
            String var2 = var10000 + File.separator + var0;
            File var3 = new File(var2.replace("/", File.separator).replace("\\", File.separator));
            if (!var3.exists() && var1) {
                var3.createNewFile();
            }
            if (var3.exists()) {
                BufferedReader var4 = null;
                try {
                    FileInputStream var5 = new FileInputStream(var3);
                    InputStreamReader var6 = new InputStreamReader(var5, StandardCharsets.UTF_8);
                    var4 = new BufferedReader(var6);
                } catch (IOException var7) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var7);
                }
                return var4;
            }
            return null;
        }

        @LuaMethod(name = "getModFileReader", global = true)
        public static BufferedReader getModFileReader(String var0, String var1, boolean var2) throws IOException {
            if (!var1.isEmpty() && !StringUtils.containsDoubleDot(var1) && !new File(var1).isAbsolute()) {
                String var10000 = ZomboidFileSystem.instance.getCacheDir();
                String var3 = var10000 + File.separator + "mods" + File.separator + var1;
                if (var0 != null) {
                    ChooseGameInfo.Mod var4 = ChooseGameInfo.getModDetails(var0);
                    if (var4 == null) {
                        return null;
                    }
                    String var100002 = var4.getDir();
                    var3 = var100002 + File.separator + var1;
                }
                String var32 = var3.replace("/", File.separator).replace("\\", File.separator);
                File var9 = new File(var32);
                if (!var9.exists() && var2) {
                    String var5 = var32.substring(0, var32.lastIndexOf(File.separator));
                    File var6 = new File(var5);
                    if (!var6.exists()) {
                        var6.mkdirs();
                    }
                    var9.createNewFile();
                }
                if (var9.exists()) {
                    BufferedReader var10 = null;
                    try {
                        FileInputStream var11 = new FileInputStream(var9);
                        InputStreamReader var7 = new InputStreamReader(var11, StandardCharsets.UTF_8);
                        var10 = new BufferedReader(var7);
                    } catch (IOException var8) {
                        Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var8);
                    }
                    return var10;
                }
                return null;
            }
            return null;
        }

        public static void refreshAnimSets(boolean var0) {
            if (var0) {
                try {
                    AnimationSet.Reset();
                    for (Asset var2 : AnimNodeAssetManager.instance.getAssetTable().values()) {
                        AnimNodeAssetManager.instance.reload(var2);
                    }
                } catch (Exception var3) {
                    ExceptionLogger.logException(var3);
                    return;
                }
            }
            AnimationSet.GetAnimationSet("player", true);
            AnimationSet.GetAnimationSet("player-vehicle", true);
            AnimationSet.GetAnimationSet("zombie", true);
            AnimationSet.GetAnimationSet("zombie-crawler", true);
            for (int var4 = 0; var4 < IsoPlayer.numPlayers; var4++) {
                IsoPlayer var5 = IsoPlayer.players[var4];
                if (var5 != null) {
                    var5.advancedAnimator.OnAnimDataChanged(var0);
                }
            }
            Iterator it = IsoWorld.instance.CurrentCell.getZombieList().iterator();
            while (it.hasNext()) {
                IsoZombie var6 = (IsoZombie) it.next();
                var6.advancedAnimator.OnAnimDataChanged(var0);
            }
        }

        public static void reloadActionGroups() {
            try {
                ActionGroup.reloadAll();
            } catch (Exception e) {
            }
        }

        @LuaMethod(name = "getModFileWriter", global = true)
        public static LuaFileWriter getModFileWriter(String var0, String var1, boolean var2, boolean var3) {
            ChooseGameInfo.Mod var4;
            if (var1.isEmpty() || StringUtils.containsDoubleDot(var1) || new File(var1).isAbsolute() || (var4 = ChooseGameInfo.getModDetails(var0)) == null) {
                return null;
            }
            String var10000 = var4.getDir();
            String var5 = (var10000 + File.separator + var1).replace("/", File.separator).replace("\\", File.separator);
            String var6 = var5.substring(0, var5.lastIndexOf(File.separator));
            File var7 = new File(var6);
            if (!var7.exists()) {
                var7.mkdirs();
            }
            File var8 = new File(var5);
            if (!var8.exists() && var2) {
                try {
                    var8.createNewFile();
                } catch (IOException var13) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var13);
                }
            }
            PrintWriter var9 = null;
            try {
                FileOutputStream var10 = new FileOutputStream(var8, var3);
                OutputStreamWriter var11 = new OutputStreamWriter(var10, StandardCharsets.UTF_8);
                var9 = new PrintWriter(var11);
            } catch (IOException var12) {
                Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var12);
            }
            return new LuaFileWriter(var9);
        }

        @LuaMethod(name = "updateFire", global = true)
        public static void updateFire() {
            IsoFireManager.Update();
        }

        @LuaMethod(name = "deletePlayerSave", global = true)
        public static void deletePlayerSave(String var0) {
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = var10000 + File.separator + "Players" + File.separator + "player" + var0 + ".txt";
            File var2 = new File(var1.replace("/", File.separator).replace("\\", File.separator));
            var2.delete();
        }

        @LuaMethod(name = "getControllerCount", global = true)
        public static int getControllerCount() {
            return GameWindow.GameInput.getControllerCount();
        }

        @LuaMethod(name = "isControllerConnected", global = true)
        public static boolean isControllerConnected(int var0) {
            return var0 >= 0 && var0 <= GameWindow.GameInput.getControllerCount() && GameWindow.GameInput.getController(var0) != null;
        }

        @LuaMethod(name = "getControllerGUID", global = true)
        public static String getControllerGUID(int var0) {
            Controller var1;
            return (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) ? "???" : var1.getGUID();
        }

        @LuaMethod(name = "getControllerName", global = true)
        public static String getControllerName(int var0) {
            Controller var1;
            return (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) ? "???" : var1.getGamepadName();
        }

        @LuaMethod(name = "getControllerAxisCount", global = true)
        public static int getControllerAxisCount(int var0) {
            Controller var1;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) {
                return 0;
            }
            return var1.getAxisCount();
        }

        @LuaMethod(name = "getControllerAxisValue", global = true)
        public static float getControllerAxisValue(int var0, int var1) {
            Controller var2;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var2 = GameWindow.GameInput.getController(var0)) == null || var1 < 0 || var1 >= var2.getAxisCount()) {
                return 0.0f;
            }
            return var2.getAxisValue(var1);
        }

        @LuaMethod(name = "getControllerDeadZone", global = true)
        public static float getControllerDeadZone(int var0, int var1) {
            if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount() && var1 >= 0 && var1 < GameWindow.GameInput.getAxisCount(var0)) {
                return JoypadManager.instance.getDeadZone(var0, var1);
            }
            return 0.0f;
        }

        @LuaMethod(name = "setControllerDeadZone", global = true)
        public static void setControllerDeadZone(int var0, int var1, float var2) {
            if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount() && var1 >= 0 && var1 < GameWindow.GameInput.getAxisCount(var0)) {
                JoypadManager.instance.setDeadZone(var0, var1, var2);
            }
        }

        @LuaMethod(name = "saveControllerSettings", global = true)
        public static void saveControllerSettings(int var0) {
            if (var0 >= 0 && var0 < GameWindow.GameInput.getControllerCount()) {
                JoypadManager.instance.saveControllerSettings(var0);
            }
        }

        @LuaMethod(name = "getControllerButtonCount", global = true)
        public static int getControllerButtonCount(int var0) {
            Controller var1;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) {
                return 0;
            }
            return var1.getButtonCount();
        }

        @LuaMethod(name = "getControllerPovX", global = true)
        public static float getControllerPovX(int var0) {
            Controller var1;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) {
                return 0.0f;
            }
            return var1.getPovX();
        }

        @LuaMethod(name = "getControllerPovY", global = true)
        public static float getControllerPovY(int var0) {
            Controller var1;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) {
                return 0.0f;
            }
            return var1.getPovY();
        }

        @LuaMethod(name = "reloadControllerConfigFiles", global = true)
        public static void reloadControllerConfigFiles() {
            JoypadManager.instance.reloadControllerFiles();
        }

        @LuaMethod(name = "isJoypadPressed", global = true)
        public static boolean isJoypadPressed(int var0, int var1) {
            return GameWindow.GameInput.isButtonPressedD(var1, var0);
        }

        @LuaMethod(name = "isJoypadDown", global = true)
        public static boolean isJoypadDown(int var0) {
            return JoypadManager.instance.isDownPressed(var0);
        }

        @LuaMethod(name = "isJoypadLTPressed", global = true)
        public static boolean isJoypadLTPressed(int var0) {
            return JoypadManager.instance.isLTPressed(var0);
        }

        @LuaMethod(name = "isJoypadRTPressed", global = true)
        public static boolean isJoypadRTPressed(int var0) {
            return JoypadManager.instance.isRTPressed(var0);
        }

        @LuaMethod(name = "isJoypadLeftStickButtonPressed", global = true)
        public static boolean isJoypadLeftStickButtonPressed(int var0) {
            return JoypadManager.instance.isL3Pressed(var0);
        }

        @LuaMethod(name = "isJoypadRightStickButtonPressed", global = true)
        public static boolean isJoypadRightStickButtonPressed(int var0) {
            return JoypadManager.instance.isR3Pressed(var0);
        }

        @LuaMethod(name = "getJoypadAimingAxisX", global = true)
        public static float getJoypadAimingAxisX(int var0) {
            return JoypadManager.instance.getAimingAxisX(var0);
        }

        @LuaMethod(name = "getJoypadAimingAxisY", global = true)
        public static float getJoypadAimingAxisY(int var0) {
            return JoypadManager.instance.getAimingAxisY(var0);
        }

        @LuaMethod(name = "getJoypadMovementAxisX", global = true)
        public static float getJoypadMovementAxisX(int var0) {
            return JoypadManager.instance.getMovementAxisX(var0);
        }

        @LuaMethod(name = "getJoypadMovementAxisY", global = true)
        public static float getJoypadMovementAxisY(int var0) {
            return JoypadManager.instance.getMovementAxisY(var0);
        }

        @LuaMethod(name = "getJoypadAButton", global = true)
        public static int getJoypadAButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getAButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadBButton", global = true)
        public static int getJoypadBButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getBButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadXButton", global = true)
        public static int getJoypadXButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getXButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadYButton", global = true)
        public static int getJoypadYButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getYButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadLBumper", global = true)
        public static int getJoypadLBumper(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getLBumper();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadRBumper", global = true)
        public static int getJoypadRBumper(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getRBumper();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadBackButton", global = true)
        public static int getJoypadBackButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getBackButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadStartButton", global = true)
        public static int getJoypadStartButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getStartButton();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadLeftStickButton", global = true)
        public static int getJoypadLeftStickButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getL3();
            }
            return -1;
        }

        @LuaMethod(name = "getJoypadRightStickButton", global = true)
        public static int getJoypadRightStickButton(int var0) {
            JoypadManager.Joypad var1 = JoypadManager.instance.getFromControllerID(var0);
            if (var1 != null) {
                return var1.getR3();
            }
            return -1;
        }

        @LuaMethod(name = "wasMouseActiveMoreRecentlyThanJoypad", global = true)
        public static boolean wasMouseActiveMoreRecentlyThanJoypad() {
            if (IsoPlayer.players[0] == null) {
                JoypadManager.Joypad var1 = GameWindow.ActivatedJoyPad;
                return var1 == null || var1.isDisabled() || JoypadManager.instance.getLastActivity(var1.getID()) < Mouse.lastActivity;
            }
            int var0 = IsoPlayer.players[0].getJoypadBind();
            return var0 == -1 || JoypadManager.instance.getLastActivity(var0) < Mouse.lastActivity;
        }

        @LuaMethod(name = "activateJoypadOnSteamDeck", global = true)
        public static void activateJoypadOnSteamDeck() {
            if (GameWindow.ActivatedJoyPad == null) {
                JoypadManager.instance.isAPressed(0);
                if (JoypadManager.instance.JoypadList.isEmpty()) {
                    return;
                } else {
                    GameWindow.ActivatedJoyPad = (JoypadManager.Joypad) JoypadManager.instance.JoypadList.get(0);
                }
            }
            if (IsoPlayer.getInstance() != null) {
                LuaEventManager.triggerEvent("OnJoypadActivate", Integer.valueOf(GameWindow.ActivatedJoyPad.getID()));
            } else {
                LuaEventManager.triggerEvent("OnJoypadActivateUI", Integer.valueOf(GameWindow.ActivatedJoyPad.getID()));
            }
        }

        @LuaMethod(name = "reactivateJoypadAfterResetLua", global = true)
        public static boolean reactivateJoypadAfterResetLua() {
            if (GameWindow.ActivatedJoyPad != null) {
                LuaEventManager.triggerEvent("OnJoypadActivateUI", Integer.valueOf(GameWindow.ActivatedJoyPad.getID()));
                return true;
            }
            return false;
        }

        @LuaMethod(name = "isJoypadConnected", global = true)
        public static boolean isJoypadConnected(int var0) {
            return JoypadManager.instance.isJoypadConnected(var0);
        }

        private static void addPlayerToWorld(int var0, IsoPlayer var1, boolean var2) {
            if (IsoPlayer.players[var0] != null) {
                IsoPlayer.players[var0].getEmitter().stopAll();
                IsoPlayer.players[var0].getEmitter().unregister();
                IsoPlayer.players[var0].updateUsername();
                IsoPlayer.players[var0].setSceneCulled(true);
                IsoPlayer.players[var0] = null;
            }
            var1.PlayerIndex = var0;
            if (GameClient.bClient && var0 != 0 && var1.serverPlayerIndex != 1) {
                ClientPlayerDB.getInstance().forgetPlayer(var1.serverPlayerIndex);
            }
            if (GameClient.bClient && var0 != 0 && var1.serverPlayerIndex == 1) {
                var1.serverPlayerIndex = ClientPlayerDB.getInstance().getNextServerPlayerIndex();
            }
            if (var0 == 0) {
                var1.sqlID = 1;
            }
            if (var2) {
                var1.applyTraits(IsoWorld.instance.getLuaTraits());
                var1.createKeyRing();
                ProfessionFactory.Profession var3 = ProfessionFactory.getProfession(var1.getDescriptor().getProfession());
                if (var3 != null && !var3.getFreeRecipes().isEmpty()) {
                    for (String s : var3.getFreeRecipes()) {
                        var1.getKnownRecipes().add(s);
                    }
                }
                Iterator<String> var4 = IsoWorld.instance.getLuaTraits().iterator();
                while (var4.hasNext()) {
                    String var5 = var4.next();
                    TraitFactory.Trait var6 = TraitFactory.getTrait(var5);
                    if (var6 != null && !var6.getFreeRecipes().isEmpty()) {
                        for (String var8 : var6.getFreeRecipes()) {
                            var1.getKnownRecipes().add(var8);
                        }
                    }
                }
                var1.setDir(IsoDirections.SE);
                LuaEventManager.triggerEvent("OnNewGame", var1, var1.getCurrentSquare());
            }
            IsoPlayer.numPlayers = Math.max(IsoPlayer.numPlayers, var0 + 1);
            IsoWorld.instance.AddCoopPlayers.add(new AddCoopPlayer(var1));
            if (var0 == 0) {
                IsoPlayer.setInstance(var1);
            }
        }

        @LuaMethod(name = "toInt", global = true)
        public static int toInt(double var0) {
            return (int) var0;
        }

        @LuaMethod(name = "getClientUsername", global = true)
        public static String getClientUsername() {
            if (GameClient.bClient) {
                return GameClient.username;
            }
            return null;
        }

        @LuaMethod(name = "setPlayerJoypad", global = true)
        public static void setPlayerJoypad(int var0, int var1, IsoPlayer var2, String var3) {
            if (IsoPlayer.players[var0] == null || IsoPlayer.players[var0].isDead()) {
                boolean var4 = var2 == null;
                if (var2 == null) {
                    IsoPlayer var5 = IsoPlayer.getInstance();
                    IsoWorld var6 = IsoWorld.instance;
                    int var7 = var6.getLuaPosX() + (300 * var6.getLuaSpawnCellX());
                    int var8 = var6.getLuaPosY() + (300 * var6.getLuaSpawnCellY());
                    int var9 = var6.getLuaPosZ();
                    DebugLog.Lua.debugln("coop player spawning at " + var7 + "," + var8 + "," + var9);
                    var2 = new IsoPlayer(var6.CurrentCell, var6.getLuaPlayerDesc(), var7, var8, var9);
                    IsoPlayer.setInstance(var5);
                    var6.CurrentCell.getAddList().remove(var2);
                    var6.CurrentCell.getObjectList().remove(var2);
                    var2.SaveFileName = IsoPlayer.getUniqueFileName();
                }
                if (GameClient.bClient) {
                    if (var3 != null) {
                        if (!$assertionsDisabled && var0 == 0) {
                            throw new AssertionError();
                        }
                        var2.username = var3;
                        var2.getModData().rawset("username", var3);
                    } else {
                        if (!$assertionsDisabled && var0 != 0) {
                            throw new AssertionError();
                        }
                        var2.username = GameClient.username;
                    }
                }
                addPlayerToWorld(var0, var2, var4);
            }
            var2.JoypadBind = var1;
            JoypadManager.instance.assignJoypad(var1, var0);
        }

        @LuaMethod(name = "setPlayerMouse", global = true)
        public static void setPlayerMouse(IsoPlayer var0) {
            boolean var2 = var0 == null;
            if (var0 == null) {
                IsoPlayer var3 = IsoPlayer.getInstance();
                IsoWorld var4 = IsoWorld.instance;
                int var5 = var4.getLuaPosX() + (300 * var4.getLuaSpawnCellX());
                int var6 = var4.getLuaPosY() + (300 * var4.getLuaSpawnCellY());
                int var7 = var4.getLuaPosZ();
                DebugLog.Lua.debugln("coop player spawning at " + var5 + "," + var6 + "," + var7);
                var0 = new IsoPlayer(var4.CurrentCell, var4.getLuaPlayerDesc(), var5, var6, var7);
                IsoPlayer.setInstance(var3);
                var4.CurrentCell.getAddList().remove(var0);
                var4.CurrentCell.getObjectList().remove(var0);
                var0.SaveFileName = null;
            }
            if (GameClient.bClient) {
                var0.username = GameClient.username;
            }
            addPlayerToWorld(0, var0, var2);
        }

        @LuaMethod(name = "revertToKeyboardAndMouse", global = true)
        public static void revertToKeyboardAndMouse() {
            JoypadManager.instance.revertToKeyboardAndMouse();
        }

        @LuaMethod(name = "isJoypadUp", global = true)
        public static boolean isJoypadUp(int var0) {
            return JoypadManager.instance.isUpPressed(var0);
        }

        @LuaMethod(name = "isJoypadLeft", global = true)
        public static boolean isJoypadLeft(int var0) {
            return JoypadManager.instance.isLeftPressed(var0);
        }

        @LuaMethod(name = "isJoypadRight", global = true)
        public static boolean isJoypadRight(int var0) {
            return JoypadManager.instance.isRightPressed(var0);
        }

        @LuaMethod(name = "isJoypadLBPressed", global = true)
        public static boolean isJoypadLBPressed(int var0) {
            return JoypadManager.instance.isLBPressed(var0);
        }

        @LuaMethod(name = "isJoypadRBPressed", global = true)
        public static boolean isJoypadRBPressed(int var0) {
            return JoypadManager.instance.isRBPressed(var0);
        }

        @LuaMethod(name = "getButtonCount", global = true)
        public static int getButtonCount(int var0) {
            Controller var1;
            if (var0 < 0 || var0 >= GameWindow.GameInput.getControllerCount() || (var1 = GameWindow.GameInput.getController(var0)) == null) {
                return 0;
            }
            return var1.getButtonCount();
        }

        @LuaMethod(name = "setDebugToggleControllerPluggedIn", global = true)
        public static void setDebugToggleControllerPluggedIn(int var0) {
            Controllers.setDebugToggleControllerPluggedIn(var0);
        }

        @LuaMethod(name = "getFileWriter", global = true)
        public static LuaFileWriter getFileWriter(String var0, boolean var1, boolean var2) {
            if (StringUtils.containsDoubleDot(var0)) {
                DebugLog.Lua.warn("relative paths not allowed");
                return null;
            }
            String var10000 = LuaManager.getLuaCacheDir();
            String var3 = (var10000 + File.separator + var0).replace("/", File.separator).replace("\\", File.separator);
            String var4 = var3.substring(0, var3.lastIndexOf(File.separator));
            File var5 = new File(var4.replace("\\", "/"));
            if (!var5.exists()) {
                var5.mkdirs();
            }
            File var6 = new File(var3);
            if (!var6.exists() && var1) {
                try {
                    var6.createNewFile();
                } catch (IOException var11) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var11);
                }
            }
            PrintWriter var7 = null;
            try {
                FileOutputStream var8 = new FileOutputStream(var6, var2);
                OutputStreamWriter var9 = new OutputStreamWriter(var8, StandardCharsets.UTF_8);
                var7 = new PrintWriter(var9);
            } catch (IOException var10) {
                Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var10);
            }
            return new LuaFileWriter(var7);
        }

        @LuaMethod(name = "getSandboxFileWriter", global = true)
        public static LuaFileWriter getSandboxFileWriter(String var0, boolean var1, boolean var2) {
            String var10000 = LuaManager.getSandboxCacheDir();
            String var3 = (var10000 + File.separator + var0).replace("/", File.separator).replace("\\", File.separator);
            String var4 = var3.substring(0, var3.lastIndexOf(File.separator));
            File var5 = new File(var4.replace("\\", "/"));
            if (!var5.exists()) {
                var5.mkdirs();
            }
            File var6 = new File(var3);
            if (!var6.exists() && var1) {
                try {
                    var6.createNewFile();
                } catch (IOException var11) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var11);
                }
            }
            PrintWriter var7 = null;
            try {
                FileOutputStream var8 = new FileOutputStream(var6, var2);
                OutputStreamWriter var9 = new OutputStreamWriter(var8, StandardCharsets.UTF_8);
                var7 = new PrintWriter(var9);
            } catch (IOException var10) {
                Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var10);
            }
            return new LuaFileWriter(var7);
        }

        @LuaMethod(name = "createStory", global = true)
        public static void createStory(String var0) {
            Core.GameMode = var0;
            String var1 = ZomboidFileSystem.instance.getGameModeCacheDir();
            String var12 = var1.replace("/", File.separator).replace("\\", File.separator);
            int var2 = 1;
            boolean var4 = false;
            while (!var4) {
                File var3 = new File(var12 + File.separator + "Game" + var2);
                if (!var3.exists()) {
                    var4 = true;
                } else {
                    var2++;
                }
            }
            Core.GameSaveWorld = "newstory";
        }

        @LuaMethod(name = "createWorld", global = true)
        public static void createWorld(String var0) {
            if (var0 == null || var0.isEmpty()) {
                var0 = "blah";
            }
            String var02 = sanitizeWorldName(var0);
            String var10000 = ZomboidFileSystem.instance.getGameModeCacheDir();
            String var1 = (var10000 + File.separator + var02 + File.separator).replace("/", File.separator).replace("\\", File.separator);
            String var2 = var1.substring(0, var1.lastIndexOf(File.separator));
            File var3 = new File(var2.replace("\\", "/"));
            if (!var3.exists() && !Core.getInstance().isNoSave()) {
                var3.mkdirs();
            }
            Core.GameSaveWorld = var02;
        }

        @LuaMethod(name = "sanitizeWorldName", global = true)
        public static String sanitizeWorldName(String var0) {
            return var0.replace(" ", "_").replace("/", "").replace("\\", "").replace("?", "").replace("*", "").replace("<", "").replace(">", "").replace(":", "").replace("|", "").trim();
        }

        @LuaMethod(name = "forceChangeState", global = true)
        public static void forceChangeState(GameState var0) {
            GameWindow.states.forceNextState(var0);
        }

        @LuaMethod(name = "endFileOutput", global = true)
        public static void endFileOutput() {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException var1) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var1);
                }
            }
            outStream = null;
        }

        @LuaMethod(name = "getFileInput", global = true)
        public static DataInputStream getFileInput(String var0) throws IOException {
            if (StringUtils.containsDoubleDot(var0)) {
                DebugLog.Lua.warn("relative paths not allowed");
                return null;
            }
            String var10000 = LuaManager.getLuaCacheDir();
            String var1 = var10000 + File.separator + var0;
            File var2 = new File(var1.replace("/", File.separator).replace("\\", File.separator));
            if (var2.exists()) {
                try {
                    inStream = new FileInputStream(var2);
                } catch (FileNotFoundException var4) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var4);
                }
                return new DataInputStream(inStream);
            }
            return null;
        }

        @LuaMethod(name = "getGameFilesInput", global = true)
        public static DataInputStream getGameFilesInput(String var0) {
            String var1 = var0.replace("/", File.separator).replace("\\", File.separator);
            if (!ZomboidFileSystem.instance.isKnownFile(var1)) {
                return null;
            }
            File var2 = new File(ZomboidFileSystem.instance.getString(var1));
            if (var2.exists()) {
                try {
                    inStream = new FileInputStream(var2);
                } catch (FileNotFoundException var4) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var4);
                }
                return new DataInputStream(inStream);
            }
            return null;
        }

        @LuaMethod(name = "getGameFilesTextInput", global = true)
        public static BufferedReader getGameFilesTextInput(String var0) {
            if (Core.getInstance().getDebug()) {
                String var1 = var0.replace("/", File.separator).replace("\\", File.separator);
                if (ZomboidFileSystem.instance.isKnownFile(var1)) {
                    File var2 = new File(ZomboidFileSystem.instance.getString(var1));
                    if (var2.exists()) {
                        try {
                            inFileReader = new FileReader(var0);
                            inBufferedReader = new BufferedReader(inFileReader);
                            return inBufferedReader;
                        } catch (FileNotFoundException var4) {
                            Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var4);
                            return null;
                        }
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        @LuaMethod(name = "endTextFileInput", global = true)
        public static void endTextFileInput() {
            if (inBufferedReader != null) {
                try {
                    inBufferedReader.close();
                    inFileReader.close();
                } catch (IOException var1) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var1);
                }
            }
            inBufferedReader = null;
            inFileReader = null;
        }

        @LuaMethod(name = "endFileInput", global = true)
        public static void endFileInput() {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException var1) {
                    Logger.getLogger(LuaManager.class.getName()).log(Level.SEVERE, (String) null, (Throwable) var1);
                }
            }
            inStream = null;
        }

        @LuaMethod(name = "getLineNumber", global = true)
        public static int getLineNumber(LuaCallFrame var0) {
            if (var0.closure == null) {
                return 0;
            }
            int var1 = var0.pc;
            if (var1 < 0) {
                var1 = 0;
            }
            if (var1 >= var0.closure.prototype.lines.length) {
                var1 = var0.closure.prototype.lines.length - 1;
            }
            return var0.closure.prototype.lines[var1];
        }

        @LuaMethod(name = "ZombRand", global = true)
        public static double ZombRand(double var0) {
            if (var0 == 0.0d) {
                return 0.0d;
            }
            if (var0 < 0.0d) {
                return -Rand.Next(-((long) var0), Rand.randlua);
            }
            return Rand.Next((long) var0, Rand.randlua);
        }

        @LuaMethod(name = "ZombRandBetween", global = true)
        public static double ZombRandBetween(double var0, double var2) {
            return Rand.Next((long) var0, (long) var2, Rand.randlua);
        }

        @LuaMethod(name = "ZombRand", global = true)
        public static double ZombRand(double var0, double var2) {
            return Rand.Next((int) var0, (int) var2, Rand.randlua);
        }

        @LuaMethod(name = "ZombRandFloat", global = true)
        public static float ZombRandFloat(float var0, float var1) {
            return Rand.Next(var0, var1, Rand.randlua);
        }

        @LuaMethod(name = "getShortenedFilename", global = true)
        public static String getShortenedFilename(String var0) {
            return var0.substring(var0.indexOf("lua/") + 4);
        }

        @LuaMethod(name = "isKeyDown", global = true)
        public static boolean isKeyDown(int var0) {
            return GameKeyboard.isKeyDown(var0);
        }

        @LuaMethod(name = "wasKeyDown", global = true)
        public static boolean wasKeyDown(int var0) {
            return GameKeyboard.wasKeyDown(var0);
        }

        @LuaMethod(name = "isKeyPressed", global = true)
        public static boolean isKeyPressed(int var0) {
            return GameKeyboard.isKeyPressed(var0);
        }

        @LuaMethod(name = "getFMODSoundBank", global = true)
        public static BaseSoundBank getFMODSoundBank() {
            return BaseSoundBank.instance;
        }

        @LuaMethod(name = "isSoundPlaying", global = true)
        public static boolean isSoundPlaying(Object var0) {
            return (var0 instanceof Double) && FMODManager.instance.isPlaying(((Double) var0).longValue());
        }

        @LuaMethod(name = "stopSound", global = true)
        public static void stopSound(long var0) {
            FMODManager.instance.stopSound(var0);
        }

        @LuaMethod(name = "isShiftKeyDown", global = true)
        public static boolean isShiftKeyDown() {
            return GameKeyboard.isKeyDown(42) || GameKeyboard.isKeyDown(54);
        }

        @LuaMethod(name = "isCtrlKeyDown", global = true)
        public static boolean isCtrlKeyDown() {
            return GameKeyboard.isKeyDown(29) || GameKeyboard.isKeyDown(157);
        }

        @LuaMethod(name = "isAltKeyDown", global = true)
        public static boolean isAltKeyDown() {
            return GameKeyboard.isKeyDown(56) || GameKeyboard.isKeyDown(184);
        }

        @LuaMethod(name = "getCore", global = true)
        public static Core getCore() {
            return Core.getInstance();
        }

        @LuaMethod(name = "getGameVersion", global = true)
        public static String getGameVersion() {
            return Core.getInstance().getGameVersion().toString();
        }

        @LuaMethod(name = "getSquare", global = true)
        public static IsoGridSquare getSquare(double var0, double var2, double var4) {
            return IsoCell.getInstance().getGridSquare(var0, var2, var4);
        }

        @LuaMethod(name = "getDebugOptions", global = true)
        public static DebugOptions getDebugOptions() {
            return DebugOptions.instance;
        }

        @LuaMethod(name = "setShowPausedMessage", global = true)
        public static void setShowPausedMessage(boolean var0) {
            DebugLog.log("EXITDEBUG: setShowPausedMessage 1");
            UIManager.setShowPausedMessage(var0);
            DebugLog.log("EXITDEBUG: setShowPausedMessage 2");
        }

        @LuaMethod(name = "getFilenameOfCallframe", global = true)
        public static String getFilenameOfCallframe(LuaCallFrame var0) {
            if (var0.closure == null) {
                return null;
            }
            return var0.closure.prototype.filename;
        }

        @LuaMethod(name = "getFilenameOfClosure", global = true)
        public static String getFilenameOfClosure(LuaClosure var0) {
            if (var0 == null) {
                return null;
            }
            return var0.prototype.filename;
        }

        @LuaMethod(name = "getFirstLineOfClosure", global = true)
        public static int getFirstLineOfClosure(LuaClosure var0) {
            if (var0 == null) {
                return 0;
            }
            return var0.prototype.lines[0];
        }

        @LuaMethod(name = "getLocalVarCount", global = true)
        public static int getLocalVarCount(Coroutine var0) {
            LuaCallFrame var1 = var0.currentCallFrame();
            if (var1 == null) {
                return 0;
            }
            return var1.LocalVarNames.size();
        }

        @LuaMethod(name = "isSystemLinux", global = true)
        public static boolean isSystemLinux() {
            return (isSystemMacOS() || isSystemWindows()) ? false : true;
        }

        @LuaMethod(name = "isSystemMacOS", global = true)
        public static boolean isSystemMacOS() {
            return System.getProperty("os.name").contains("OS X");
        }

        @LuaMethod(name = "isSystemWindows", global = true)
        public static boolean isSystemWindows() {
            return System.getProperty("os.name").startsWith("Win");
        }

        @LuaMethod(name = "isModActive", global = true)
        public static boolean isModActive(ChooseGameInfo.Mod var0) {
            String var1 = var0.getDir();
            if (!StringUtils.isNullOrWhitespace(var0.getId())) {
                var1 = var0.getId();
            }
            return ZomboidFileSystem.instance.getModIDs().contains(var1);
        }

        @LuaMethod(name = "openUrl", global = true)
        public static void openURl(String var0) {
            Desktop var1 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (var1 != null && var1.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URI var2 = new URI(var0);
                    var1.browse(var2);
                    return;
                } catch (Exception var3) {
                    ExceptionLogger.logException(var3);
                    return;
                }
            }
            DesktopBrowser.openURL(var0);
        }

        @LuaMethod(name = "isDesktopOpenSupported", global = true)
        public static boolean isDesktopOpenSupported() {
            return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN);
        }

        @LuaMethod(name = "showFolderInDesktop", global = true)
        public static void showFolderInDesktop(String var0) {
            File var1 = new File(var0);
            if (var1.exists() && var1.isDirectory()) {
                Desktop var2 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (var2 != null && var2.isSupported(Desktop.Action.OPEN)) {
                    try {
                        var2.open(var1);
                    } catch (Exception var4) {
                        ExceptionLogger.logException(var4);
                    }
                }
            }
        }

        @LuaMethod(name = "getActivatedMods", global = true)
        public static ArrayList<String> getActivatedMods() {
            return ZomboidFileSystem.instance.getModIDs();
        }

        @LuaMethod(name = "toggleModActive", global = true)
        public static void toggleModActive(ChooseGameInfo.Mod var0, boolean var1) {
            String var2 = var0.getDir();
            if (!StringUtils.isNullOrWhitespace(var0.getId())) {
                var2 = var0.getId();
            }
            ActiveMods.getById("default").setModActive(var2, var1);
        }

        @LuaMethod(name = "saveModsFile", global = true)
        public static void saveModsFile() {
            ZomboidFileSystem.instance.saveModsFile();
        }

        private static void deleteSavefileFilesMatching(File var0, String var1) {
            DirectoryStream.Filter<Path> var2 = var1x -> {
                return var1x.getFileName().toString().matches(var1);
            };
            try {
                DirectoryStream<Path> var3 = Files.newDirectoryStream(var0.toPath(), var2);
                try {
                    for (Path var5 : var3) {
                        System.out.println("DELETE " + var5);
                        Files.deleteIfExists(var5);
                    }
                    var3.close();
                } finally {
                }
            } catch (Exception var8) {
                ExceptionLogger.logException(var8);
            }
        }

        @LuaMethod(name = "manipulateSavefile", global = true)
        public static void manipulateSavefile(String var0, String var1) {
            if (!StringUtils.isNullOrWhitespace(var0) && !StringUtils.containsDoubleDot(var0)) {
                String var10000 = ZomboidFileSystem.instance.getSaveDir();
                String var2 = var10000 + File.separator + var0;
                File var3 = new File(var2);
                if (var3.exists() && var3.isDirectory()) {
                    switch (var1) {
                        case "DeleteChunkDataXYBin":
                            deleteSavefileFilesMatching(var3, "chunkdata_[0-9]+_[0-9]+\\.bin");
                            return;
                        case "DeleteMapXYBin":
                            deleteSavefileFilesMatching(var3, "map_[0-9]+_[0-9]+\\.bin");
                            return;
                        case "DeleteMapMetaBin":
                            deleteSavefileFilesMatching(var3, "map_meta\\.bin");
                            return;
                        case "DeleteMapTBin":
                            deleteSavefileFilesMatching(var3, "map_t\\.bin");
                            return;
                        case "DeleteMapZoneBin":
                            deleteSavefileFilesMatching(var3, "map_zone\\.bin");
                            return;
                        case "DeletePlayersDB":
                            deleteSavefileFilesMatching(var3, "players\\.db");
                            return;
                        case "DeleteReanimatedBin":
                            deleteSavefileFilesMatching(var3, "reanimated\\.bin");
                            return;
                        case "DeleteVehiclesDB":
                            deleteSavefileFilesMatching(var3, "vehicles\\.db");
                            return;
                        case "DeleteZOutfitsBin":
                            deleteSavefileFilesMatching(var3, "z_outfits\\.bin");
                            return;
                        case "DeleteZPopVirtualBin":
                            deleteSavefileFilesMatching(var3, "zpop_virtual\\.bin");
                            return;
                        case "DeleteZPopXYBin":
                            deleteSavefileFilesMatching(var3, "zpop_[0-9]+_[0-9]+\\.bin");
                            return;
                        case "WriteModsDotTxt":
                            ActiveMods var6 = ActiveMods.getById("currentGame");
                            ActiveModsFile var7 = new ActiveModsFile();
                            var7.write(var2 + File.separator + "mods.txt", var6);
                            return;
                        default:
                            throw new IllegalArgumentException("unknown action \"" + var1 + "\"");
                    }
                }
            }
        }

        @LuaMethod(name = "getLocalVarName", global = true)
        public static String getLocalVarName(Coroutine var0, int var1) {
            LuaCallFrame var2 = var0.currentCallFrame();
            return (String) var2.LocalVarNames.get(var1);
        }

        @LuaMethod(name = "getLocalVarStack", global = true)
        public static int getLocalVarStack(Coroutine var0, int var1) {
            LuaCallFrame var2 = var0.currentCallFrame();
            return ((Integer) var2.LocalVarToStackMap.get(var2.LocalVarNames.get(var1))).intValue();
        }

        @LuaMethod(name = "getCallframeTop", global = true)
        public static int getCallframeTop(Coroutine var0) {
            return var0.getCallframeTop();
        }

        @LuaMethod(name = "getCoroutineTop", global = true)
        public static int getCoroutineTop(Coroutine var0) {
            return var0.getTop();
        }

        @LuaMethod(name = "getCoroutineObjStack", global = true)
        public static Object getCoroutineObjStack(Coroutine var0, int var1) {
            return var0.getObjectFromStack(var1);
        }

        @LuaMethod(name = "getCoroutineObjStackWithBase", global = true)
        public static Object getCoroutineObjStackWithBase(Coroutine var0, int var1) {
            return var0.getObjectFromStack(var1 - var0.currentCallFrame().localBase);
        }

        @LuaMethod(name = "localVarName", global = true)
        public static String localVarName(Coroutine var0, int var1) {
            int var2 = var0.getCallframeTop() - 1;
            return var2 < 0 ? "" : "";
        }

        @LuaMethod(name = "getCoroutineCallframeStack", global = true)
        public static LuaCallFrame getCoroutineCallframeStack(Coroutine var0, int var1) {
            return var0.getCallFrame(var1);
        }

        @LuaMethod(name = "createTile", global = true)
        public static void createTile(String var0, IsoGridSquare var1) {
            synchronized (IsoSpriteManager.instance.NamedMap) {
                IsoSprite var3 = (IsoSprite) IsoSpriteManager.instance.NamedMap.get(var0);
                if (var3 != null) {
                    int var4 = 0;
                    int var5 = 0;
                    int var6 = 0;
                    if (var1 != null) {
                        var4 = var1.getX();
                        var5 = var1.getY();
                        var6 = var1.getZ();
                    }
                    CellLoader.DoTileObjectCreation(var3, var3.getType(), var1, IsoWorld.instance.CurrentCell, var4, var5, var6, var0);
                }
            }
        }

        @LuaMethod(name = "getNumClassFunctions", global = true)
        public static int getNumClassFunctions(Object var0) {
            return var0.getClass().getDeclaredMethods().length;
        }

        @LuaMethod(name = "getClassFunction", global = true)
        public static Method getClassFunction(Object var0, int var1) {
            return var0.getClass().getDeclaredMethods()[var1];
        }

        @LuaMethod(name = "getNumClassFields", global = true)
        public static int getNumClassFields(Object var0) {
            return var0.getClass().getDeclaredFields().length;
        }

        @LuaMethod(name = "getClassField", global = true)
        public static Field getClassField(Object var0, int var1) {
            Field var2 = var0.getClass().getDeclaredFields()[var1];
            var2.setAccessible(true);
            return var2;
        }

        @LuaMethod(name = "getDirectionTo", global = true)
        public static IsoDirections getDirectionTo(IsoGameCharacter var0, IsoObject var1) {
            Vector2 var2 = new Vector2(var1.getX(), var1.getY());
            var2.x -= var0.x;
            var2.y -= var0.y;
            return IsoDirections.fromAngle(var2);
        }

        @LuaMethod(name = "translatePointXInOverheadMapToWindow", global = true)
        public static float translatePointXInOverheadMapToWindow(float var0, UIElement var1, float var2, float var3) {
            IngameState.draww = var1.getWidth().intValue();
            return IngameState.translatePointX(var0, var3, var2, 0.0f);
        }

        @LuaMethod(name = "translatePointYInOverheadMapToWindow", global = true)
        public static float translatePointYInOverheadMapToWindow(float var0, UIElement var1, float var2, float var3) {
            IngameState.drawh = var1.getHeight().intValue();
            return IngameState.translatePointY(var0, var3, var2, 0.0f);
        }

        @LuaMethod(name = "translatePointXInOverheadMapToWorld", global = true)
        public static float translatePointXInOverheadMapToWorld(float var0, UIElement var1, float var2, float var3) {
            IngameState.draww = var1.getWidth().intValue();
            return IngameState.invTranslatePointX(var0, var3, var2, 0.0f);
        }

        @LuaMethod(name = "translatePointYInOverheadMapToWorld", global = true)
        public static float translatePointYInOverheadMapToWorld(float var0, UIElement var1, float var2, float var3) {
            IngameState.drawh = var1.getHeight().intValue();
            return IngameState.invTranslatePointY(var0, var3, var2, 0.0f);
        }

        @LuaMethod(name = "drawOverheadMap", global = true)
        public static void drawOverheadMap(UIElement var0, float var1, float var2, float var3) {
            IngameState.renderDebugOverhead2(getCell(), 0, var1, var0.getAbsoluteX().intValue(), var0.getAbsoluteY().intValue(), var2, var3, var0.getWidth().intValue(), var0.getHeight().intValue());
        }

        @LuaMethod(name = "assaultPlayer", global = true)
        public static void assaultPlayer() {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }

        @LuaMethod(name = "isoRegionsRenderer", global = true)
        public static IsoRegionsRenderer isoRegionsRenderer() {
            return new IsoRegionsRenderer();
        }

        @LuaMethod(name = "zpopNewRenderer", global = true)
        public static ZombiePopulationRenderer zpopNewRenderer() {
            return new ZombiePopulationRenderer();
        }

        @LuaMethod(name = "zpopSpawnTimeToZero", global = true)
        public static void zpopSpawnTimeToZero(int var0, int var1) {
            ZombiePopulationManager.instance.dbgSpawnTimeToZero(var0, var1);
        }

        @LuaMethod(name = "zpopClearZombies", global = true)
        public static void zpopClearZombies(int var0, int var1) {
            ZombiePopulationManager.instance.dbgClearZombies(var0, var1);
        }

        @LuaMethod(name = "zpopSpawnNow", global = true)
        public static void zpopSpawnNow(int var0, int var1) {
            ZombiePopulationManager.instance.dbgSpawnNow(var0, var1);
        }

        @LuaMethod(name = "addVirtualZombie", global = true)
        public static void addVirtualZombie(int var0, int var1) {
        }

        @LuaMethod(name = "luaDebug", global = true)
        public static void luaDebug() {
            try {
                throw new Exception("LuaDebug");
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

        @LuaMethod(name = "setAggroTarget", global = true)
        public static void setAggroTarget(int var0, int var1, int var2) {
            ZombiePopulationManager.instance.setAggroTarget(var0, var1, var2);
        }

        @LuaMethod(name = "debugFullyStreamedIn", global = true)
        public static void debugFullyStreamedIn(int var0, int var1) {
            IngameState.instance.debugFullyStreamedIn(var0, var1);
        }

        @LuaMethod(name = "getClassFieldVal", global = true)
        public static Object getClassFieldVal(Object var0, Field var1) {
            try {
                return var1.get(var0);
            } catch (Exception e) {
                return "<private>";
            }
        }

        @LuaMethod(name = "getMethodParameter", global = true)
        public static String getMethodParameter(Method var0, int var1) {
            return var0.getParameterTypes()[var1].getSimpleName();
        }

        @LuaMethod(name = "getMethodParameterCount", global = true)
        public static int getMethodParameterCount(Method var0) {
            return var0.getParameterTypes().length;
        }

        @LuaMethod(name = "breakpoint", global = true)
        public static void breakpoint() {
        }

        @LuaMethod(name = "getLuaDebuggerErrorCount", global = true)
        public static int getLuaDebuggerErrorCount() {
            KahluaThread kahluaThread = LuaManager.thread;
            return KahluaThread.m_error_count;
        }

        @LuaMethod(name = "getLuaDebuggerErrors", global = true)
        public static ArrayList<String> getLuaDebuggerErrors() {
            KahluaThread kahluaThread = LuaManager.thread;
            return new ArrayList<>(KahluaThread.m_errors_list);
        }

        @LuaMethod(name = "doLuaDebuggerAction", global = true)
        public static void doLuaDebuggerAction(String var0) {
            UIManager.luaDebuggerAction = var0;
        }

        @LuaMethod(name = "getGameSpeed", global = true)
        public static int getGameSpeed() {
            if (UIManager.getSpeedControls() != null) {
                return UIManager.getSpeedControls().getCurrentGameSpeed();
            }
            return 0;
        }

        @LuaMethod(name = "setGameSpeed", global = true)
        public static void setGameSpeed(int var0) {
            DebugLog.log("EXITDEBUG: setGameSpeed 1");
            if (UIManager.getSpeedControls() == null) {
                DebugLog.log("EXITDEBUG: setGameSpeed 2");
            } else {
                UIManager.getSpeedControls().SetCurrentGameSpeed(var0);
                DebugLog.log("EXITDEBUG: setGameSpeed 3");
            }
        }

        @LuaMethod(name = "isGamePaused", global = true)
        public static boolean isGamePaused() {
            return GameTime.isGamePaused();
        }

        @LuaMethod(name = "getMouseXScaled", global = true)
        public static int getMouseXScaled() {
            return Mouse.getX();
        }

        @LuaMethod(name = "getMouseYScaled", global = true)
        public static int getMouseYScaled() {
            return Mouse.getY();
        }

        @LuaMethod(name = "getMouseX", global = true)
        public static int getMouseX() {
            return Mouse.getXA();
        }

        @LuaMethod(name = "setMouseXY", global = true)
        public static void setMouseXY(int var0, int var1) {
            Mouse.setXY(var0, var1);
        }

        @LuaMethod(name = "isMouseButtonDown", global = true)
        public static boolean isMouseButtonDown(int var0) {
            return Mouse.isButtonDown(var0);
        }

        @LuaMethod(name = "getMouseY", global = true)
        public static int getMouseY() {
            return Mouse.getYA();
        }

        @LuaMethod(name = "getSoundManager", global = true)
        public static BaseSoundManager getSoundManager() {
            return SoundManager.instance;
        }

        @LuaMethod(name = "getLastPlayedDate", global = true)
        public static String getLastPlayedDate(String var0) {
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator + var0);
            if (!var1.exists()) {
                return Translator.getText("UI_LastPlayed") + "???";
            }
            Date var2 = new Date(var1.lastModified());
            SimpleDateFormat var3 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String var4 = var3.format(var2);
            String var10000 = Translator.getText("UI_LastPlayed");
            return var10000 + var4;
        }

        @LuaMethod(name = "getTextureFromSaveDir", global = true)
        public static Texture getTextureFromSaveDir(String var0, String var1) {
            TextureID.UseFiltering = true;
            String var2 = ZomboidFileSystem.instance.getSaveDir() + File.separator + var1 + File.separator + var0;
            Texture var3 = Texture.getSharedTexture(var2);
            TextureID.UseFiltering = false;
            return var3;
        }

        @LuaMethod(name = "getSaveInfo", global = true)
        public static KahluaTable getSaveInfo(String var0) {
            if (!var0.contains(File.separator)) {
                String var10000 = IsoWorld.instance.getGameMode();
                var0 = var10000 + File.separator + var0;
            }
            KahluaTable var1 = LuaManager.platform.newTable();
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var2 = new File(var10002 + File.separator + var0);
            if (var2.exists()) {
                Date var3 = new Date(var2.lastModified());
                SimpleDateFormat var4 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String var5 = var4.format(var3);
                var1.rawset("lastPlayed", var5);
                String[] var6 = var0.split("\\" + File.separator);
                var1.rawset("saveName", var2.getName());
                var1.rawset("gameMode", var6[var6.length - 2]);
            }
            String var100022 = ZomboidFileSystem.instance.getSaveDir();
            File var22 = new File(var100022 + File.separator + var0 + File.separator + "map_ver.bin");
            if (var22.exists()) {
                try {
                    FileInputStream var222 = new FileInputStream(var22);
                    try {
                        DataInputStream var24 = new DataInputStream(var222);
                        try {
                            int var26 = var24.readInt();
                            var1.rawset("worldVersion", Double.valueOf(var26));
                            if (var26 >= 18) {
                                try {
                                    String var28 = GameWindow.ReadString(var24);
                                    if (var28.equals("DEFAULT")) {
                                        var28 = "Muldraugh, KY";
                                    }
                                    var1.rawset("mapName", var28);
                                } catch (Exception e) {
                                }
                            }
                            if (var26 >= 74) {
                                try {
                                    var1.rawset("difficulty", GameWindow.ReadString(var24));
                                } catch (Exception e2) {
                                }
                            }
                            var24.close();
                            var222.close();
                        } catch (Throwable var19) {
                            try {
                                var24.close();
                            } catch (Throwable var15) {
                                var19.addSuppressed(var15);
                            }
                            throw var19;
                        }
                    } finally {
                    }
                } catch (Exception var21) {
                    ExceptionLogger.logException(var21);
                }
            }
            String var100002 = ZomboidFileSystem.instance.getSaveDir();
            String var23 = var100002 + File.separator + var0 + File.separator + "mods.txt";
            ActiveMods var25 = new ActiveMods(var0);
            ActiveModsFile var27 = new ActiveModsFile();
            if (var27.read(var23, var25)) {
                var1.rawset("activeMods", var25);
            }
            String var100003 = ZomboidFileSystem.instance.getSaveDir();
            String var282 = var100003 + File.separator + var0;
            var1.rawset("playerAlive", Boolean.valueOf(PlayerDBHelper.isPlayerAlive(var282, 1)));
            KahluaTable var7 = LuaManager.platform.newTable();
            try {
                ArrayList<Object> var8 = PlayerDBHelper.getPlayers(var282);
                for (int var9 = 0; var9 < var8.size(); var9 += 3) {
                    Double var10 = (Double) var8.get(var9);
                    String var11 = (String) var8.get(var9 + 1);
                    Boolean var12 = (Boolean) var8.get(var9 + 2);
                    KahluaTable var13 = LuaManager.platform.newTable();
                    var13.rawset("sqlID", var10);
                    var13.rawset("name", var11);
                    var13.rawset("isDead", var12);
                    var7.rawset((var9 / 3) + 1, var13);
                }
            } catch (Exception var18) {
                ExceptionLogger.logException(var18);
            }
            var1.rawset("players", var7);
            return var1;
        }

        @LuaMethod(name = "renameSavefile", global = true)
        public static boolean renameSaveFile(String var0, String var1, String var2) {
            if (var0 != null && !var0.contains("/") && !var0.contains("\\") && !var0.contains(File.separator) && !StringUtils.containsDoubleDot(var0) && var1 != null && !var1.contains("/") && !var1.contains("\\") && !var1.contains(File.separator) && !StringUtils.containsDoubleDot(var1) && var2 != null && !var2.contains("/") && !var2.contains("\\") && !var2.contains(File.separator) && !StringUtils.containsDoubleDot(var2)) {
                String var3 = sanitizeWorldName(var2);
                if (!var3.equals(var2) || var3.startsWith(".") || var3.endsWith(".") || !new File(ZomboidFileSystem.instance.getSaveDirSub(var0)).exists()) {
                    return false;
                }
                Path var4 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getSaveDirSub(var0 + File.separator + var1), new String[0]);
                Path var5 = FileSystems.getDefault().getPath(ZomboidFileSystem.instance.getSaveDirSub(var0 + File.separator + var3), new String[0]);
                try {
                    Files.move(var4, var5, new CopyOption[0]);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
            return false;
        }

        @LuaMethod(name = "setSavefilePlayer1", global = true)
        public static void setSavefilePlayer1(String var0, String var1, int var2) {
            String var3 = ZomboidFileSystem.instance.getSaveDirSub(var0 + File.separator + var1);
            try {
                PlayerDBHelper.setPlayer1(var3, var2);
            } catch (Exception var5) {
                ExceptionLogger.logException(var5);
            }
        }

        @LuaMethod(name = "getServerSavedWorldVersion", global = true)
        public static int getServerSavedWorldVersion(String var0) {
            String var10002 = ZomboidFileSystem.instance.getSaveDir();
            File var1 = new File(var10002 + File.separator + var0 + File.separator + "map_t.bin");
            if (var1.exists()) {
                try {
                    FileInputStream var2 = new FileInputStream(var1);
                    try {
                        DataInputStream var3 = new DataInputStream(var2);
                        try {
                            byte var4 = var3.readByte();
                            byte var5 = var3.readByte();
                            byte var6 = var3.readByte();
                            byte var7 = var3.readByte();
                            if (var4 == 71 && var5 == 77 && var6 == 84 && var7 == 77) {
                                int var8 = var3.readInt();
                                var3.close();
                                var2.close();
                                return var8;
                            }
                            var3.close();
                            var2.close();
                            return 1;
                        } catch (Throwable var11) {
                            try {
                                var3.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                            throw var11;
                        }
                    } finally {
                    }
                } catch (Exception var13) {
                    var13.printStackTrace();
                    return 0;
                }
            }
            return 0;
        }

        @LuaMethod(name = "getZombieInfo", global = true)
        public static KahluaTable getZombieInfo(IsoZombie var0) {
            KahluaTable var1 = LuaManager.platform.newTable();
            if (var0 != null) {
                var1.rawset("OnlineID", Short.valueOf(var0.OnlineID));
                var1.rawset("RealX", Float.valueOf(var0.realx));
                var1.rawset("RealY", Float.valueOf(var0.realy));
                var1.rawset("X", Float.valueOf(var0.x));
                var1.rawset("Y", Float.valueOf(var0.y));
                var1.rawset("TargetX", Float.valueOf(var0.networkAI.targetX));
                var1.rawset("TargetY", Float.valueOf(var0.networkAI.targetY));
                var1.rawset("PathLength", Float.valueOf(var0.getPathFindBehavior2().getPathLength()));
                var1.rawset("TargetLength", Double.valueOf(Math.sqrt(((var0.x - var0.getPathFindBehavior2().getTargetX()) * (var0.x - var0.getPathFindBehavior2().getTargetX())) + ((var0.y - var0.getPathFindBehavior2().getTargetY()) * (var0.y - var0.getPathFindBehavior2().getTargetY())))));
                var1.rawset("clientActionState", var0.getActionStateName());
                var1.rawset("clientAnimationState", var0.getAnimationStateName());
                var1.rawset("finderProgress", var0.getFinder().progress.name());
                var1.rawset("usePathFind", Boolean.toString(var0.networkAI.usePathFind));
                var1.rawset("owner", var0.authOwner.username);
                var0.networkAI.DebugInterfaceActive = true;
            }
            return var1;
        }

        @LuaMethod(name = "getPlayerInfo", global = true)
        public static KahluaTable getPlayerInfo(IsoPlayer var0) {
            KahluaTable var1 = LuaManager.platform.newTable();
            if (var0 != null) {
                long var2 = GameTime.getServerTime() / 1000000;
                var1.rawset("OnlineID", Short.valueOf(var0.OnlineID));
                var1.rawset("RealX", Float.valueOf(var0.realx));
                var1.rawset("RealY", Float.valueOf(var0.realy));
                var1.rawset("X", Float.valueOf(var0.x));
                var1.rawset("Y", Float.valueOf(var0.y));
                var1.rawset("TargetX", Float.valueOf(var0.networkAI.targetX));
                var1.rawset("TargetY", Float.valueOf(var0.networkAI.targetY));
                var1.rawset("TargetT", Integer.valueOf(var0.networkAI.targetZ));
                var1.rawset("ServerT", Long.valueOf(var2));
                var1.rawset("PathLength", Float.valueOf(var0.getPathFindBehavior2().getPathLength()));
                var1.rawset("TargetLength", Double.valueOf(Math.sqrt(((var0.x - var0.getPathFindBehavior2().getTargetX()) * (var0.x - var0.getPathFindBehavior2().getTargetX())) + ((var0.y - var0.getPathFindBehavior2().getTargetY()) * (var0.y - var0.getPathFindBehavior2().getTargetY())))));
                var1.rawset("clientActionState", var0.getActionStateName());
                var1.rawset("clientAnimationState", var0.getAnimationStateName());
                var1.rawset("finderProgress", var0.getFinder().progress.name());
                var1.rawset("usePathFind", Boolean.toString(var0.networkAI.usePathFind));
            }
            return var1;
        }

        @LuaMethod(name = "getMapInfo", global = true)
        public static KahluaTable getMapInfo(String var0) {
            if (var0.contains(";")) {
                var0 = var0.split(";")[0];
            }
            ChooseGameInfo.Map var1 = ChooseGameInfo.getMapDetails(var0);
            if (var1 == null) {
                return null;
            }
            KahluaTable var2 = LuaManager.platform.newTable();
            var2.rawset("description", var1.getDescription());
            var2.rawset("dir", var1.getDirectory());
            KahluaTable var3 = LuaManager.platform.newTable();
            Iterator it = var1.getLotDirectories().iterator();
            while (it.hasNext()) {
                String var6 = (String) it.next();
                var3.rawset(Double.valueOf(1), var6);
            }
            var2.rawset("lots", var3);
            var2.rawset("thumb", var1.getThumbnail());
            var2.rawset("title", var1.getTitle());
            return var2;
        }

        @LuaMethod(name = "getVehicleInfo", global = true)
        public static KahluaTable getVehicleInfo(BaseVehicle var0) {
            if (var0 == null) {
                return null;
            }
            KahluaTable var1 = LuaManager.platform.newTable();
            var1.rawset("name", var0.getScript().getName());
            var1.rawset("weight", Float.valueOf(var0.getMass()));
            var1.rawset("speed", Float.valueOf(var0.getMaxSpeed()));
            var1.rawset("frontEndDurability", Integer.toString(var0.frontEndDurability));
            var1.rawset("rearEndDurability", Integer.toString(var0.rearEndDurability));
            var1.rawset("currentFrontEndDurability", Integer.toString(var0.currentFrontEndDurability));
            var1.rawset("currentRearEndDurability", Integer.toString(var0.currentRearEndDurability));
            var1.rawset("engine_running", Boolean.valueOf(var0.isEngineRunning()));
            var1.rawset("engine_started", Boolean.valueOf(var0.isEngineStarted()));
            var1.rawset("engine_quality", Integer.valueOf(var0.getEngineQuality()));
            var1.rawset("engine_loudness", Integer.valueOf(var0.getEngineLoudness()));
            var1.rawset("engine_power", Integer.valueOf(var0.getEnginePower()));
            var1.rawset("battery_isset", Boolean.valueOf(var0.getBattery() != null));
            var1.rawset("battery_charge", Float.valueOf(var0.getBatteryCharge()));
            var1.rawset("gas_amount", Float.valueOf(var0.getPartById("GasTank").getContainerContentAmount()));
            var1.rawset("gas_capacity", Integer.valueOf(var0.getPartById("GasTank").getContainerCapacity()));
            VehiclePart var2 = var0.getPartById("DoorFrontLeft");
            var1.rawset("doorleft_exist", Boolean.valueOf(var2 != null));
            if (var2 != null) {
                var1.rawset("doorleft_open", Boolean.valueOf(var2.getDoor().isOpen()));
                var1.rawset("doorleft_locked", Boolean.valueOf(var2.getDoor().isLocked()));
                var1.rawset("doorleft_lockbroken", Boolean.valueOf(var2.getDoor().isLockBroken()));
                VehicleWindow var3 = var2.findWindow();
                var1.rawset("windowleft_exist", Boolean.valueOf(var3 != null));
                if (var3 != null) {
                    var1.rawset("windowleft_open", Boolean.valueOf(var3.isOpen()));
                    var1.rawset("windowleft_health", Integer.valueOf(var3.getHealth()));
                }
            }
            VehiclePart var5 = var0.getPartById("DoorFrontRight");
            var1.rawset("doorright_exist", Boolean.valueOf(var5 != null));
            if (var2 != null) {
                var1.rawset("doorright_open", Boolean.valueOf(var5.getDoor().isOpen()));
                var1.rawset("doorright_locked", Boolean.valueOf(var5.getDoor().isLocked()));
                var1.rawset("doorright_lockbroken", Boolean.valueOf(var5.getDoor().isLockBroken()));
                VehicleWindow var4 = var5.findWindow();
                var1.rawset("windowright_exist", Boolean.valueOf(var4 != null));
                if (var4 != null) {
                    var1.rawset("windowright_open", Boolean.valueOf(var4.isOpen()));
                    var1.rawset("windowright_health", Integer.valueOf(var4.getHealth()));
                }
            }
            var1.rawset("headlights_set", Boolean.valueOf(var0.hasHeadlights()));
            var1.rawset("headlights_on", Boolean.valueOf(var0.getHeadlightsOn()));
            if (var0.getPartById("Heater") != null) {
                var1.rawset("heater_isset", true);
                Object var6 = var0.getPartById("Heater").getModData().rawget("active");
                if (var6 == null) {
                    var1.rawset("heater_on", false);
                } else {
                    var1.rawset("heater_on", Boolean.valueOf(var6 == Boolean.TRUE));
                }
            } else {
                var1.rawset("heater_isset", false);
            }
            return var1;
        }

        @LuaMethod(name = "getLotDirectories", global = true)
        public static ArrayList<String> getLotDirectories() {
            if (IsoWorld.instance.MetaGrid != null) {
                return IsoWorld.instance.MetaGrid.getLotDirectories();
            }
            return null;
        }

        @LuaMethod(name = "useTextureFiltering", global = true)
        public static void useTextureFiltering(boolean var0) {
            TextureID.UseFiltering = var0;
        }

        @LuaMethod(name = "getTexture", global = true)
        public static Texture getTexture(String var0) {
            return Texture.getSharedTexture(var0);
        }

        @LuaMethod(name = "getTextManager", global = true)
        public static TextManager getTextManager() {
            return TextManager.instance;
        }

        @LuaMethod(name = "setProgressBarValue", global = true)
        public static void setProgressBarValue(IsoPlayer var0, int var1) {
            if (var0.isLocalPlayer()) {
                UIManager.getProgressBar(var0.getPlayerNum()).setValue(var1);
            }
        }

        @LuaMethod(name = "getText", global = true)
        public static String getText(String var0) {
            return Translator.getText(var0);
        }

        @LuaMethod(name = "getText", global = true)
        public static String getText(String var0, Object var1) {
            return Translator.getText(var0, var1);
        }

        @LuaMethod(name = "getText", global = true)
        public static String getText(String var0, Object var1, Object var2) {
            return Translator.getText(var0, var1, var2);
        }

        @LuaMethod(name = "getText", global = true)
        public static String getText(String var0, Object var1, Object var2, Object var3) {
            return Translator.getText(var0, var1, var2, var3);
        }

        @LuaMethod(name = "getText", global = true)
        public static String getText(String var0, Object var1, Object var2, Object var3, Object var4) {
            return Translator.getText(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "getTextOrNull", global = true)
        public static String getTextOrNull(String var0) {
            return Translator.getTextOrNull(var0);
        }

        @LuaMethod(name = "getTextOrNull", global = true)
        public static String getTextOrNull(String var0, Object var1) {
            return Translator.getTextOrNull(var0, var1);
        }

        @LuaMethod(name = "getTextOrNull", global = true)
        public static String getTextOrNull(String var0, Object var1, Object var2) {
            return Translator.getTextOrNull(var0, var1, var2);
        }

        @LuaMethod(name = "getTextOrNull", global = true)
        public static String getTextOrNull(String var0, Object var1, Object var2, Object var3) {
            return Translator.getTextOrNull(var0, var1, var2, var3);
        }

        @LuaMethod(name = "getTextOrNull", global = true)
        public static String getTextOrNull(String var0, Object var1, Object var2, Object var3, Object var4) {
            return Translator.getTextOrNull(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "getItemText", global = true)
        public static String getItemText(String var0) {
            return Translator.getDisplayItemName(var0);
        }

        @LuaMethod(name = "getRadioText", global = true)
        public static String getRadioText(String var0) {
            return Translator.getRadioText(var0);
        }

        @LuaMethod(name = "getTextMediaEN", global = true)
        public static String getTextMediaEN(String var0) {
            return Translator.getTextMediaEN(var0);
        }

        @LuaMethod(name = "getItemNameFromFullType", global = true)
        public static String getItemNameFromFullType(String var0) {
            return Translator.getItemNameFromFullType(var0);
        }

        @LuaMethod(name = "getRecipeDisplayName", global = true)
        public static String getRecipeDisplayName(String var0) {
            return Translator.getRecipeName(var0);
        }

        @LuaMethod(name = "getMyDocumentFolder", global = true)
        public static String getMyDocumentFolder() {
            return Core.getMyDocumentFolder();
        }

        @LuaMethod(name = "getSpriteManager", global = true)
        public static IsoSpriteManager getSpriteManager(String var0) {
            return IsoSpriteManager.instance;
        }

        @LuaMethod(name = "getSprite", global = true)
        public static IsoSprite getSprite(String var0) {
            return IsoSpriteManager.instance.getSprite(var0);
        }

        @LuaMethod(name = "getServerModData", global = true)
        public static void getServerModData() {
            GameClient.getCustomModData();
        }

        @LuaMethod(name = "isXBOXController", global = true)
        public static boolean isXBOXController() {
            for (int var0 = 0; var0 < GameWindow.GameInput.getControllerCount(); var0++) {
                Controller var1 = GameWindow.GameInput.getController(var0);
                if (var1 != null && var1.getGamepadName().contains("XBOX 360")) {
                    return true;
                }
            }
            return false;
        }

        @LuaMethod(name = "sendClientCommand", global = true)
        public static void sendClientCommand(String var0, String var1, KahluaTable var2) {
            if (GameClient.bClient && GameClient.bIngame) {
                GameClient.instance.sendClientCommand((IsoPlayer) null, var0, var1, var2);
            } else {
                if (GameServer.bServer) {
                    throw new IllegalStateException("can't call this function on the server");
                }
                SinglePlayerClient.sendClientCommand((IsoPlayer) null, var0, var1, var2);
            }
        }

        @LuaMethod(name = "sendClientCommand", global = true)
        public static void sendClientCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
            if (var0 != null && var0.isLocalPlayer()) {
                if (GameClient.bClient && GameClient.bIngame) {
                    GameClient.instance.sendClientCommand(var0, var1, var2, var3);
                } else {
                    if (GameServer.bServer) {
                        throw new IllegalStateException("can't call this function on the server");
                    }
                    SinglePlayerClient.sendClientCommand(var0, var1, var2, var3);
                }
            }
        }

        @LuaMethod(name = "sendServerCommand", global = true)
        public static void sendServerCommand(String var0, String var1, KahluaTable var2) {
            if (GameServer.bServer) {
                GameServer.sendServerCommand(var0, var1, var2);
            }
        }

        @LuaMethod(name = "sendServerCommand", global = true)
        public static void sendServerCommand(IsoPlayer var0, String var1, String var2, KahluaTable var3) {
            if (GameServer.bServer) {
                GameServer.sendServerCommand(var0, var1, var2, var3);
            }
        }

        @LuaMethod(name = "getOnlineUsername", global = true)
        public static String getOnlineUsername() {
            return IsoPlayer.getInstance().getDisplayName();
        }

        @LuaMethod(name = "isValidUserName", global = true)
        public static boolean isValidUserName(String var0) {
            return ServerWorldDatabase.isValidUserName(var0);
        }

        @LuaMethod(name = "getHourMinute", global = true)
        public static String getHourMinute() {
            return LuaManager.getHourMinuteJava();
        }

        @LuaMethod(name = "SendCommandToServer", global = true)
        public static void SendCommandToServer(String var0) {
            GameClient.SendCommandToServer(var0);
        }

        @LuaMethod(name = "isAdmin", global = true)
        public static boolean isAdmin() {
            return GameClient.bClient && GameClient.connection.accessLevel == 32;
        }

        @LuaMethod(name = "canModifyPlayerScoreboard", global = true)
        public static boolean canModifyPlayerScoreboard() {
            return GameClient.bClient && GameClient.connection.accessLevel != 1;
        }

        @LuaMethod(name = "isAccessLevel", global = true)
        public static boolean isAccessLevel(String var0) {
            return GameClient.bClient && GameClient.connection.accessLevel != 1 && GameClient.connection.accessLevel == PlayerType.fromString(var0);
        }

        @LuaMethod(name = "sendBandage", global = true)
        public static void sendBandage(int var0, int var1, boolean var2, float var3, boolean var4, String var5) {
            GameClient.instance.sendBandage(var0, var1, var2, var3, var4, var5);
        }

        @LuaMethod(name = "sendCataplasm", global = true)
        public static void sendCataplasm(int var0, int var1, float var2, float var3, float var4) {
            GameClient.instance.sendCataplasm(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "sendStitch", global = true)
        public static void sendStitch(IsoGameCharacter var0, IsoGameCharacter var1, BodyPart var2, InventoryItem var3, boolean var4) {
            GameClient.instance.sendStitch(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "sendDisinfect", global = true)
        public static void sendDisinfect(IsoGameCharacter var0, IsoGameCharacter var1, BodyPart var2, InventoryItem var3) {
            GameClient.instance.sendDisinfect(var0, var1, var2, var3);
        }

        @LuaMethod(name = "sendSplint", global = true)
        public static void sendSplint(int var0, int var1, boolean var2, float var3, String var4) {
            GameClient.instance.sendSplint(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "sendRemoveGlass", global = true)
        public static void sendRemoveGlass(IsoGameCharacter var0, IsoGameCharacter var1, BodyPart var2, boolean var3) {
            GameClient.instance.sendRemoveGlass(var0, var1, var2, var3);
        }

        @LuaMethod(name = "sendRemoveBullet", global = true)
        public static void sendRemoveBullet(IsoGameCharacter var0, IsoGameCharacter var1, BodyPart var2) {
            GameClient.instance.sendRemoveBullet(var0, var1, var2);
        }

        @LuaMethod(name = "sendCleanBurn", global = true)
        public static void sendCleanBurn(IsoGameCharacter var0, IsoGameCharacter var1, BodyPart var2, InventoryItem var3) {
            GameClient.instance.sendCleanBurn(var0, var1, var2, var3);
        }

        @LuaMethod(name = "getGameClient", global = true)
        public static GameClient getGameClient() {
            return GameClient.instance;
        }

        @LuaMethod(name = "sendRequestInventory", global = true)
        public static void sendRequestInventory(IsoPlayer var0) {
            GameClient.sendRequestInventory(var0);
        }

        @LuaMethod(name = "InvMngGetItem", global = true)
        public static void InvMngGetItem(long var0, String var2, IsoPlayer var3) {
            GameClient.invMngRequestItem((int) var0, var2, var3);
        }

        @LuaMethod(name = "InvMngRemoveItem", global = true)
        public static void InvMngRemoveItem(long var0, IsoPlayer var2) {
            GameClient.invMngRequestRemoveItem((int) var0, var2);
        }

        @LuaMethod(name = "getConnectedPlayers", global = true)
        public static ArrayList<IsoPlayer> getConnectedPlayers() {
            return GameClient.instance.getConnectedPlayers();
        }

        @LuaMethod(name = "getPlayerFromUsername", global = true)
        public static IsoPlayer getPlayerFromUsername(String var0) {
            return GameClient.instance.getPlayerFromUsername(var0);
        }

        @LuaMethod(name = "isCoopHost", global = true)
        public static boolean isCoopHost() {
            return GameClient.connection != null && GameClient.connection.isCoopHost;
        }

        @LuaMethod(name = "setAdmin", global = true)
        public static void setAdmin() {
            if (CoopMaster.instance.isRunning()) {
                String var0 = "admin";
                if (GameClient.connection.accessLevel == 32) {
                    var0 = "";
                }
                GameClient.connection.accessLevel = PlayerType.fromString(var0);
                IsoPlayer.getInstance().accessLevel = var0;
                GameClient.SendCommandToServer("/setaccesslevel \"" + IsoPlayer.getInstance().username + "\" \"" + (var0.equals("") ? "none" : var0) + "\"");
                if ((var0.equals("") && IsoPlayer.getInstance().isInvisible()) || (var0.equals("admin") && !IsoPlayer.getInstance().isInvisible())) {
                    GameClient.SendCommandToServer("/invisible");
                }
            }
        }

        @LuaMethod(name = "addWarningPoint", global = true)
        public static void addWarningPoint(String var0, String var1, int var2) {
            if (GameClient.bClient) {
                GameClient.instance.addWarningPoint(var0, var1, var2);
            }
        }

        @LuaMethod(name = "toggleSafetyServer", global = true)
        public static void toggleSafetyServer(IsoPlayer var0) {
        }

        @LuaMethod(name = "disconnect", global = true)
        public static void disconnect() {
            GameClient.connection.forceDisconnect("lua-disconnect");
        }

        @LuaMethod(name = "writeLog", global = true)
        public static void writeLog(String var0, String var1) {
            LoggerManager.getLogger(var0).write(var1);
        }

        @LuaMethod(name = "doKeyPress", global = true)
        public static void doKeyPress(boolean var0) {
            GameKeyboard.doLuaKeyPressed = var0;
        }

        @LuaMethod(name = "getEvolvedRecipes", global = true)
        public static Stack<EvolvedRecipe> getEvolvedRecipes() {
            return ScriptManager.instance.getAllEvolvedRecipes();
        }

        @LuaMethod(name = "getZone", global = true)
        public static IsoMetaGrid.Zone getZone(int var0, int var1, int var2) {
            return IsoWorld.instance.MetaGrid.getZoneAt(var0, var1, var2);
        }

        @LuaMethod(name = "getZones", global = true)
        public static ArrayList<IsoMetaGrid.Zone> getZones(int var0, int var1, int var2) {
            return IsoWorld.instance.MetaGrid.getZonesAt(var0, var1, var2);
        }

        @LuaMethod(name = "getVehicleZoneAt", global = true)
        public static IsoMetaGrid.VehicleZone getVehicleZoneAt(int var0, int var1, int var2) {
            return IsoWorld.instance.MetaGrid.getVehicleZoneAt(var0, var1, var2);
        }

        @LuaMethod(name = "replaceWith", global = true)
        public static String replaceWith(String var0, String var1, String var2) {
            return var0.replaceFirst(var1, var2);
        }

        @LuaMethod(name = "getTimestamp", global = true)
        public static long getTimestamp() {
            return System.currentTimeMillis() / 1000;
        }

        @LuaMethod(name = "getTimestampMs", global = true)
        public static long getTimestampMs() {
            return System.currentTimeMillis();
        }

        @LuaMethod(name = "forceSnowCheck", global = true)
        public static void forceSnowCheck() {
            ErosionMain.getInstance().snowCheck();
        }

        @LuaMethod(name = "getGametimeTimestamp", global = true)
        public static long getGametimeTimestamp() {
            return GameTime.instance.getCalender().getTimeInMillis() / 1000;
        }

        @LuaMethod(name = "canInviteFriends", global = true)
        public static boolean canInviteFriends() {
            if (GameClient.bClient && SteamUtils.isSteamModeEnabled()) {
                return CoopMaster.instance.isRunning() || !GameClient.bCoopInvite;
            }
            return false;
        }

        @LuaMethod(name = "inviteFriend", global = true)
        public static void inviteFriend(String var0) {
            if (CoopMaster.instance.isRunning()) {
                CoopMaster.instance.sendMessage("invite-add", var0);
            }
            SteamFriends.InviteUserToGame(SteamUtils.convertStringToSteamID(var0), "+connect " + GameClient.ip + ":" + GameClient.port);
        }

        @LuaMethod(name = "getFriendsList", global = true)
        public static KahluaTable getFriendsList() {
            KahluaTable var0 = LuaManager.platform.newTable();
            if (getSteamModeActive().booleanValue()) {
                List<SteamFriend> var1 = SteamFriends.GetFriendList();
                int var2 = 1;
                for (SteamFriend o : var1) {
                    Double var5 = Double.valueOf(var2);
                    var0.rawset(var5, o);
                    var2++;
                }
            }
            return var0;
        }

        @LuaMethod(name = "getSteamModeActive", global = true)
        public static Boolean getSteamModeActive() {
            return Boolean.valueOf(SteamUtils.isSteamModeEnabled());
        }

        @LuaMethod(name = "isValidSteamID", global = true)
        public static boolean isValidSteamID(String var0) {
            return (var0 == null || var0.isEmpty() || !SteamUtils.isValidSteamID(var0)) ? false : true;
        }

        @LuaMethod(name = "getCurrentUserSteamID", global = true)
        public static String getCurrentUserSteamID() {
            if (SteamUtils.isSteamModeEnabled() && !GameServer.bServer) {
                return SteamUser.GetSteamIDString();
            }
            return null;
        }

        @LuaMethod(name = "getCurrentUserProfileName", global = true)
        public static String getCurrentUserProfileName() {
            if (SteamUtils.isSteamModeEnabled() && !GameServer.bServer) {
                return SteamFriends.GetFriendPersonaName(SteamUser.GetSteamID());
            }
            return null;
        }

        @LuaMethod(name = "getSteamScoreboard", global = true)
        public static boolean getSteamScoreboard() {
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
                String var0 = ServerOptions.instance.SteamScoreboard.getValue();
                return "true".equals(var0) || (GameClient.connection.accessLevel == 32 && "admin".equals(var0));
            }
            return false;
        }

        @LuaMethod(name = "isSteamOverlayEnabled", global = true)
        public static boolean isSteamOverlayEnabled() {
            return SteamUtils.isOverlayEnabled();
        }

        @LuaMethod(name = "activateSteamOverlayToWorkshop", global = true)
        public static void activateSteamOverlayToWorkshop() {
            if (SteamUtils.isOverlayEnabled()) {
                SteamFriends.ActivateGameOverlayToWebPage("steam://url/SteamWorkshopPage/108600");
            }
        }

        @LuaMethod(name = "activateSteamOverlayToWorkshopUser", global = true)
        public static void activateSteamOverlayToWorkshopUser() {
            if (SteamUtils.isOverlayEnabled()) {
                SteamFriends.ActivateGameOverlayToWebPage("steam://url/SteamIDCommunityFilesPage/" + SteamUser.GetSteamIDString() + "/108600");
            }
        }

        @LuaMethod(name = "activateSteamOverlayToWorkshopItem", global = true)
        public static void activateSteamOverlayToWorkshopItem(String var0) {
            if (SteamUtils.isOverlayEnabled() && SteamUtils.isValidSteamID(var0)) {
                SteamFriends.ActivateGameOverlayToWebPage("steam://url/CommunityFilePage/" + var0);
            }
        }

        @LuaMethod(name = "activateSteamOverlayToWebPage", global = true)
        public static void activateSteamOverlayToWebPage(String var0) {
            if (SteamUtils.isOverlayEnabled()) {
                SteamFriends.ActivateGameOverlayToWebPage(var0);
            }
        }

        @LuaMethod(name = "getSteamProfileNameFromSteamID", global = true)
        public static String getSteamProfileNameFromSteamID(String var0) {
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
                long var1 = SteamUtils.convertStringToSteamID(var0);
                if (var1 != -1) {
                    return SteamFriends.GetFriendPersonaName(var1);
                }
                return null;
            }
            return null;
        }

        @LuaMethod(name = "getSteamAvatarFromSteamID", global = true)
        public static Texture getSteamAvatarFromSteamID(String var0) {
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient) {
                long var1 = SteamUtils.convertStringToSteamID(var0);
                if (var1 != -1) {
                    return Texture.getSteamAvatar(var1);
                }
                return null;
            }
            return null;
        }

        @LuaMethod(name = "getSteamIDFromUsername", global = true)
        public static String getSteamIDFromUsername(String var0) {
            IsoPlayer var1;
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient && (var1 = GameClient.instance.getPlayerFromUsername(var0)) != null) {
                return SteamUtils.convertSteamIDToString(var1.getSteamID());
            }
            return null;
        }

        @LuaMethod(name = "resetRegionFile", global = true)
        public static void resetRegionFile() {
            ServerOptions.getInstance().resetRegionFile();
        }

        @LuaMethod(name = "getSteamProfileNameFromUsername", global = true)
        public static String getSteamProfileNameFromUsername(String var0) {
            IsoPlayer var1;
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient && (var1 = GameClient.instance.getPlayerFromUsername(var0)) != null) {
                return SteamFriends.GetFriendPersonaName(var1.getSteamID());
            }
            return null;
        }

        @LuaMethod(name = "getSteamAvatarFromUsername", global = true)
        public static Texture getSteamAvatarFromUsername(String var0) {
            IsoPlayer var1;
            if (SteamUtils.isSteamModeEnabled() && GameClient.bClient && (var1 = GameClient.instance.getPlayerFromUsername(var0)) != null) {
                return Texture.getSteamAvatar(var1.getSteamID());
            }
            return null;
        }

        @LuaMethod(name = "getSteamWorkshopStagedItems", global = true)
        public static ArrayList<SteamWorkshopItem> getSteamWorkshopStagedItems() {
            if (SteamUtils.isSteamModeEnabled()) {
                return SteamWorkshop.instance.loadStagedItems();
            }
            return null;
        }

        @LuaMethod(name = "getSteamWorkshopItemIDs", global = true)
        public static ArrayList<String> getSteamWorkshopItemIDs() {
            if (SteamUtils.isSteamModeEnabled()) {
                ArrayList<String> var0 = new ArrayList<>();
                String[] var1 = SteamWorkshop.instance.GetInstalledItemFolders();
                if (var1 != null) {
                    for (String s : var1) {
                        String var3 = SteamWorkshop.instance.getIDFromItemInstallFolder(s);
                        if (var3 != null) {
                            var0.add(var3);
                        }
                    }
                }
                return var0;
            }
            return null;
        }

        @LuaMethod(name = "getSteamWorkshopItemMods", global = true)
        public static ArrayList<ChooseGameInfo.Mod> getSteamWorkshopItemMods(String var0) {
            if (SteamUtils.isSteamModeEnabled()) {
                long var1 = SteamUtils.convertStringToSteamID(var0);
                if (var1 > 0) {
                    return ZomboidFileSystem.instance.getWorkshopItemMods(var1);
                }
                return null;
            }
            return null;
        }

        @LuaMethod(name = "isSteamRunningOnSteamDeck", global = true)
        public static boolean isSteamRunningOnSteamDeck() {
            return SteamUtils.isSteamModeEnabled() && SteamUtils.isRunningOnSteamDeck();
        }

        @LuaMethod(name = "showSteamGamepadTextInput", global = true)
        public static boolean showSteamGamepadTextInput(boolean var0, boolean var1, String var2, int var3, String var4) {
            return SteamUtils.isSteamModeEnabled() && SteamUtils.showGamepadTextInput(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "showSteamFloatingGamepadTextInput", global = true)
        public static boolean showSteamFloatingGamepadTextInput(boolean var0, int var1, int var2, int var3, int var4) {
            return SteamUtils.isSteamModeEnabled() && SteamUtils.showFloatingGamepadTextInput(var0, var1, var2, var3, var4);
        }

        @LuaMethod(name = "isFloatingGamepadTextInputVisible", global = true)
        public static boolean isFloatingGamepadTextInputVisible() {
            return SteamUtils.isSteamModeEnabled() && SteamUtils.isFloatingGamepadTextInputVisible();
        }

        @LuaMethod(name = "sendPlayerStatsChange", global = true)
        public static void sendPlayerStatsChange(IsoPlayer var0) {
            if (GameClient.bClient) {
                GameClient.instance.sendChangedPlayerStats(var0);
            }
        }

        @LuaMethod(name = "sendPersonalColor", global = true)
        public static void sendPersonalColor(IsoPlayer var0) {
            if (GameClient.bClient) {
                GameClient.instance.sendPersonalColor(var0);
            }
        }

        @LuaMethod(name = "requestTrading", global = true)
        public static void requestTrading(IsoPlayer var0, IsoPlayer var1) {
            GameClient.instance.requestTrading(var0, var1);
        }

        @LuaMethod(name = "acceptTrading", global = true)
        public static void acceptTrading(IsoPlayer var0, IsoPlayer var1, boolean var2) {
            GameClient.instance.acceptTrading(var0, var1, var2);
        }

        @LuaMethod(name = "tradingUISendAddItem", global = true)
        public static void tradingUISendAddItem(IsoPlayer var0, IsoPlayer var1, InventoryItem var2) {
            GameClient.instance.tradingUISendAddItem(var0, var1, var2);
        }

        @LuaMethod(name = "tradingUISendRemoveItem", global = true)
        public static void tradingUISendRemoveItem(IsoPlayer var0, IsoPlayer var1, int var2) {
            GameClient.instance.tradingUISendRemoveItem(var0, var1, var2);
        }

        @LuaMethod(name = "tradingUISendUpdateState", global = true)
        public static void tradingUISendUpdateState(IsoPlayer var0, IsoPlayer var1, int var2) {
            GameClient.instance.tradingUISendUpdateState(var0, var1, var2);
        }

        @LuaMethod(name = "querySteamWorkshopItemDetails", global = true)
        public static void querySteamWorkshopItemDetails(ArrayList<String> var0, LuaClosure var1, Object var2) {
            if (var0 != null && var1 != null) {
                if (var0.isEmpty()) {
                    if (var2 == null) {
                        LuaManager.caller.pcall(LuaManager.thread, var1, new Object[]{"Completed", new ArrayList()});
                        return;
                    } else {
                        LuaManager.caller.pcall(LuaManager.thread, var1, new Object[]{var2, "Completed", new ArrayList()});
                        return;
                    }
                }
                new ItemQuery(var0, var1, var2);
                return;
            }
            throw new NullPointerException();
        }

        @LuaMethod(name = "connectToServerStateCallback", global = true)
        public static void connectToServerStateCallback(String var0) {
            if (ConnectToServerState.instance != null) {
                ConnectToServerState.instance.FromLua(var0);
            }
        }

        @LuaMethod(name = "getPublicServersList", global = true)
        public static KahluaTable getPublicServersList() {
            KahluaTable var0 = LuaManager.platform.newTable();
            if (!SteamUtils.isSteamModeEnabled() && !PublicServerUtil.isEnabled()) {
                return var0;
            }
            if (System.currentTimeMillis() - timeLastRefresh < UdpConnection.CONNECTION_GRACE_INTERVAL) {
                return var0;
            }
            ArrayList<Server> var1 = new ArrayList<>();
            try {
                if (getSteamModeActive().booleanValue()) {
                    ServerBrowser.RefreshInternetServers();
                    List<GameServerDetails> var2 = ServerBrowser.GetServerList();
                    for (GameServerDetails o : var2) {
                        Server var5 = new Server();
                        var5.setName(o.name);
                        var5.setDescription(o.gameDescription);
                        var5.setSteamId(Long.toString(o.steamId));
                        var5.setPing(Integer.toString(o.ping));
                        var5.setPlayers(Integer.toString(o.numPlayers));
                        var5.setMaxPlayers(Integer.toString(o.maxPlayers));
                        var5.setOpen(true);
                        var5.setIp(o.address);
                        var5.setPort(Integer.toString(o.port));
                        var5.setMods(o.tags);
                        var5.setVersion(Core.getInstance().getVersion());
                        var5.setLastUpdate(1);
                        var1.add(var5);
                    }
                    System.out.printf("%d servers\n", Integer.valueOf(var2.size()));
                } else {
                    URL var18 = new URL(PublicServerUtil.webSite + "servers.xml");
                    InputStreamReader var20 = new InputStreamReader(var18.openStream());
                    BufferedReader var22 = new BufferedReader(var20);
                    StringBuilder var6 = new StringBuilder();
                    while (true) {
                        String var24 = var22.readLine();
                        if (var24 == null) {
                            break;
                        }
                        var6.append(var24).append('\n');
                    }
                    var22.close();
                    DocumentBuilderFactory var7 = DocumentBuilderFactory.newInstance();
                    DocumentBuilder var8 = var7.newDocumentBuilder();
                    Document var9 = var8.parse(new InputSource(new StringReader(var6.toString())));
                    var9.getDocumentElement().normalize();
                    NodeList var10 = var9.getElementsByTagName("server");
                    for (int var11 = 0; var11 < var10.getLength(); var11++) {
                        Node var12 = var10.item(var11);
                        if (var12.getNodeType() == 1) {
                            Element var13 = (Element) var12;
                            Server var14 = new Server();
                            var14.setName(var13.getElementsByTagName("name").item(0).getTextContent());
                            if (var13.getElementsByTagName("desc").item(0) != null && !"".equals(var13.getElementsByTagName("desc").item(0).getTextContent())) {
                                var14.setDescription(var13.getElementsByTagName("desc").item(0).getTextContent());
                            }
                            var14.setIp(var13.getElementsByTagName("ip").item(0).getTextContent());
                            var14.setPort(var13.getElementsByTagName("port").item(0).getTextContent());
                            var14.setPlayers(var13.getElementsByTagName("players").item(0).getTextContent());
                            var14.setMaxPlayers(var13.getElementsByTagName("maxPlayers").item(0).getTextContent());
                            if (var13.getElementsByTagName("version").item(0) != null) {
                                var14.setVersion(var13.getElementsByTagName("version").item(0).getTextContent());
                            }
                            var14.setOpen(var13.getElementsByTagName("open").item(0).getTextContent().equals("1"));
                            int var15 = Integer.parseInt(var13.getElementsByTagName("lastUpdate").item(0).getTextContent());
                            if (var13.getElementsByTagName("mods").item(0) != null && !"".equals(var13.getElementsByTagName("mods").item(0).getTextContent())) {
                                var14.setMods(var13.getElementsByTagName("mods").item(0).getTextContent());
                            }
                            var14.setLastUpdate((int) Math.floor((getTimestamp() - var15) / 60));
                            NodeList var16 = var13.getElementsByTagName("password");
                            var14.setPasswordProtected(var16.getLength() != 0 && var16.item(0).getTextContent().equals("1"));
                            var1.add(var14);
                        }
                    }
                }
                int var19 = 1;
                Iterator<Server> it = var1.iterator();
                while (it.hasNext()) {
                    Server o2 = it.next();
                    Double var25 = Double.valueOf(var19);
                    var0.rawset(var25, o2);
                    var19++;
                }
                timeLastRefresh = Calendar.getInstance().getTimeInMillis();
                return var0;
            } catch (Exception var17) {
                var17.printStackTrace();
                return null;
            }
        }

        @LuaMethod(name = "steamRequestInternetServersList", global = true)
        public static void steamRequestInternetServersList() {
            ServerBrowser.RefreshInternetServers();
        }

        @LuaMethod(name = "steamReleaseInternetServersRequest", global = true)
        public static void steamReleaseInternetServersRequest() {
            ServerBrowser.Release();
        }

        @LuaMethod(name = "steamGetInternetServersCount", global = true)
        public static int steamRequestInternetServersCount() {
            return ServerBrowser.GetServerCount();
        }

        @LuaMethod(name = "steamGetInternetServerDetails", global = true)
        public static Server steamGetInternetServerDetails(int var0) {
            GameServerDetails var1;
            if (ServerBrowser.IsRefreshing() && (var1 = ServerBrowser.GetServerDetails(var0)) != null && !var1.tags.contains("hidden") && !var1.tags.contains("hosted") && !var1.tags.contains("hidden") && !var1.tags.contains("hosted")) {
                Server var2 = new Server();
                var2.setName(var1.name);
                var2.setDescription("");
                var2.setSteamId(Long.toString(var1.steamId));
                var2.setPing(Integer.toString(var1.ping));
                var2.setPlayers(Integer.toString(var1.numPlayers));
                var2.setMaxPlayers(Integer.toString(var1.maxPlayers));
                var2.setOpen(true);
                var2.setPublic(true);
                if (var1.tags.contains("hidden")) {
                    var2.setOpen(false);
                    var2.setPublic(false);
                }
                var2.setIp(var1.address);
                var2.setPort(Integer.toString(var1.port));
                var2.setMods("");
                if (!var1.tags.replace("hidden", "").replace("hosted", "").replace(";", "").isEmpty()) {
                    var2.setMods(var1.tags.replace(";hosted", "").replace("hidden", ""));
                }
                var2.setHosted(var1.tags.contains("hosted"));
                var2.setVersion("");
                var2.setLastUpdate(1);
                var2.setPasswordProtected(var1.passwordProtected);
                return var2;
            }
            return null;
        }

        @LuaMethod(name = "steamRequestServerRules", global = true)
        public static boolean steamRequestServerRules(String var0, int var1) {
            return ServerBrowser.RequestServerRules(var0, var1);
        }

        @LuaMethod(name = "steamRequestServerDetails", global = true)
        public static boolean steamRequestServerDetails(String var0, int var1) {
            return ServerBrowser.QueryServer(var0, var1);
        }

        @LuaMethod(name = "isPublicServerListAllowed", global = true)
        public static boolean isPublicServerListAllowed() {
            return SteamUtils.isSteamModeEnabled() || PublicServerUtil.isEnabled();
        }

        @LuaMethod(name = "is64bit", global = true)
        public static boolean is64bit() {
            return "64".equals(System.getProperty("sun.arch.data.model"));
        }

        @LuaMethod(name = "testSound", global = true)
        public static void testSound() {
            float var0 = Mouse.getX();
            float var1 = Mouse.getY();
            int var2 = (int) IsoPlayer.getInstance().getZ();
            int var3 = (int) IsoUtils.XToIso(var0, var1, var2);
            int var4 = (int) IsoUtils.YToIso(var0, var1, var2);
            AmbientStreamManager.Ambient var7 = new AmbientStreamManager.Ambient("Meta/House Alarm", var3, var4, 50.0f, 1.0f);
            var7.trackMouse = true;
            ((AmbientStreamManager) AmbientStreamManager.instance).ambient.add(var7);
        }

        @LuaMethod(name = "debugSetRoomType", global = true)
        public static void debugSetRoomType(Double var0) {
            ParameterRoomType.setRoomType(var0.intValue());
        }

        @LuaMethod(name = "copyTable", global = true)
        public static KahluaTable copyTable(KahluaTable var0) {
            return LuaManager.copyTable(var0);
        }

        @LuaMethod(name = "copyTable", global = true)
        public static KahluaTable copyTable(KahluaTable var0, KahluaTable var1) {
            return LuaManager.copyTable(var0, var1);
        }

        @LuaMethod(name = "getUrlInputStream", global = true)
        public static DataInputStream getUrlInputStream(String var0) {
            if (var0 == null) {
                return null;
            }
            if (var0.startsWith("https://") || var0.startsWith("http://")) {
                try {
                    return new DataInputStream(new URL(var0).openStream());
                } catch (IOException var2) {
                    var2.printStackTrace();
                    return null;
                }
            }
            return null;
        }

        @LuaMethod(name = "renderIsoCircle", global = true)
        public static void renderIsoCircle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, int var8) {
            double d = 0.0d;
            while (true) {
                double var11 = d;
                if (var11 < 6.283185307179586d) {
                    float var13 = var0 + (var3 * ((float) Math.cos(var11)));
                    float var14 = var1 + (var3 * ((float) Math.sin(var11)));
                    float var15 = var0 + (var3 * ((float) Math.cos(var11 + 0.3490658503988659d)));
                    float var16 = var1 + (var3 * ((float) Math.sin(var11 + 0.3490658503988659d)));
                    float var17 = IsoUtils.XToScreenExact(var13, var14, var2, 0);
                    float var18 = IsoUtils.YToScreenExact(var13, var14, var2, 0);
                    float var19 = IsoUtils.XToScreenExact(var15, var16, var2, 0);
                    float var20 = IsoUtils.YToScreenExact(var15, var16, var2, 0);
                    LineDrawer.drawLine(var17, var18, var19, var20, var4, var5, var6, var7, var8);
                    d = var11 + 0.3490658503988659d;
                } else {
                    return;
                }
            }
        }

        @LuaMethod(name = "configureLighting", global = true)
        public static void configureLighting(float var0) {
            if (LightingJNI.init) {
                LightingJNI.configure(var0);
            }
        }

        @LuaMethod(name = "testHelicopter", global = true)
        public static void testHelicopter() {
            if (GameClient.bClient) {
                GameClient.SendCommandToServer("/chopper start");
            } else {
                IsoWorld.instance.helicopter.pickRandomTarget();
            }
        }

        @LuaMethod(name = "endHelicopter", global = true)
        public static void endHelicopter() {
            if (GameClient.bClient) {
                GameClient.SendCommandToServer("/chopper stop");
            } else {
                IsoWorld.instance.helicopter.deactivate();
            }
        }

        @LuaMethod(name = "getServerSettingsManager", global = true)
        public static ServerSettingsManager getServerSettingsManager() {
            return ServerSettingsManager.instance;
        }

        @LuaMethod(name = "rainConfig", global = true)
        public static void rainConfig(String var0, int var1) {
            if ("alpha".equals(var0)) {
                IsoWorld.instance.CurrentCell.setRainAlpha(var1);
            }
            if ("intensity".equals(var0)) {
                IsoWorld.instance.CurrentCell.setRainIntensity(var1);
            }
            if ("speed".equals(var0)) {
                IsoWorld.instance.CurrentCell.setRainSpeed(var1);
            }
            if ("reloadTextures".equals(var0)) {
                IsoWorld.instance.CurrentCell.reloadRainTextures();
            }
        }

        @LuaMethod(name = "sendSwitchSeat", global = true)
        public static void sendSwitchSeat(BaseVehicle var0, IsoGameCharacter var1, int var2, int var3) {
            if (GameClient.bClient) {
                VehicleManager.instance.sendSwitchSeat(GameClient.connection, var0, var1, var2, var3);
            }
        }

        @LuaMethod(name = "getVehicleById", global = true)
        public static BaseVehicle getVehicleById(int var0) {
            return VehicleManager.instance.getVehicleByID((short) var0);
        }

        @LuaMethod(name = "addBloodSplat", global = true)
        public void addBloodSplat(IsoGridSquare var1, int var2) {
            for (int var3 = 0; var3 < var2; var3++) {
                var1.getChunk().addBloodSplat(var1.x + Rand.Next(-0.5f, 0.5f), var1.y + Rand.Next(-0.5f, 0.5f), var1.z, Rand.Next(8));
            }
        }

        @LuaMethod(name = "addCarCrash", global = true)
        public static void addCarCrash() {
            IsoChunk var1;
            IsoMetaGrid.Zone var2;
            IsoGridSquare var0 = IsoPlayer.getInstance().getCurrentSquare();
            if (var0 != null && (var1 = var0.getChunk()) != null && (var2 = var0.getZone()) != null && var1.canAddRandomCarCrash(var2, true)) {
                var0.chunk.addRandomCarCrash(var2, true);
            }
        }

        @LuaMethod(name = "createRandomDeadBody", global = true)
        public static IsoDeadBody createRandomDeadBody(IsoGridSquare var0, int var1) {
            if (var0 == null) {
                return null;
            }
            ItemPickerJava.ItemPickerRoom var2 = (ItemPickerJava.ItemPickerRoom) ItemPickerJava.rooms.get("all");
            RandomizedBuildingBase.HumanCorpse var3 = new RandomizedBuildingBase.HumanCorpse(IsoWorld.instance.getCell(), var0.x, var0.y, var0.z);
            var3.setDir(IsoDirections.getRandom());
            var3.setDescriptor(SurvivorFactory.CreateSurvivor());
            var3.setFemale(var3.getDescriptor().isFemale());
            var3.initWornItems("Human");
            var3.initAttachedItems("Human");
            Outfit var4 = var3.getRandomDefaultOutfit();
            var3.dressInNamedOutfit(var4.m_Name);
            var3.initSpritePartsEmpty();
            var3.Dressup(var3.getDescriptor());
            for (int var5 = 0; var5 < var1; var5++) {
                var3.addBlood((BloodBodyPartType) null, false, true, false);
            }
            IsoDeadBody var6 = new IsoDeadBody(var3, true);
            ItemPickerJava.fillContainerType(var2, var6.getContainer(), var3.isFemale() ? "inventoryfemale" : "inventorymale", (IsoGameCharacter) null);
            return var6;
        }

        @LuaMethod(name = "addZombieSitting", global = true)
        public void addZombieSitting(int var1, int var2, int var3) {
            IsoGridSquare var4 = IsoCell.getInstance().getGridSquare(var1, var2, var3);
            if (var4 != null) {
                VirtualZombieManager.instance.choices.clear();
                VirtualZombieManager.instance.choices.add(var4);
                IsoZombie var5 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
                var5.bDressInRandomOutfit = true;
                ZombiePopulationManager.instance.sitAgainstWall(var5, var4);
            }
        }

        @LuaMethod(name = "addZombiesEating", global = true)
        public void addZombiesEating(int var1, int var2, int var3, int var4, boolean var5) {
            IsoGridSquare var6 = IsoCell.getInstance().getGridSquare(var1, var2, var3);
            if (var6 != null) {
                VirtualZombieManager.instance.choices.clear();
                VirtualZombieManager.instance.choices.add(var6);
                IsoZombie var7 = VirtualZombieManager.instance.createRealZombieAlways(Rand.Next(8), false);
                var7.setX(var6.x);
                var7.setY(var6.y);
                var7.setFakeDead(false);
                var7.setHealth(0.0f);
                var7.upKillCount = false;
                if (!var5) {
                    var7.dressInRandomOutfit();
                    for (int var8 = 0; var8 < 10; var8++) {
                        var7.addHole((BloodBodyPartType) null);
                        var7.addBlood((BloodBodyPartType) null, false, true, false);
                    }
                    var7.DoZombieInventory();
                }
                var7.setSkeleton(var5);
                if (var5) {
                    var7.getHumanVisual().setSkinTextureIndex(2);
                }
                IsoDeadBody var9 = new IsoDeadBody(var7, true);
                VirtualZombieManager.instance.createEatingZombies(var9, var4);
            }
        }

        @LuaMethod(name = "addZombiesInOutfitArea", global = true)
        public ArrayList<IsoZombie> addZombiesInOutfitArea(int var1, int var2, int var3, int var4, int var5, int var6, String var7, Integer var8) {
            ArrayList<IsoZombie> var9 = new ArrayList<>();
            for (int var10 = 0; var10 < var6; var10++) {
                var9.addAll(addZombiesInOutfit(Rand.Next(var1, var3), Rand.Next(var2, var4), var5, 1, var7, var8));
            }
            return var9;
        }

        @LuaMethod(name = "addZombiesInOutfit", global = true)
        public static ArrayList<IsoZombie> addZombiesInOutfit(int var0, int var1, int var2, int var3, String var4, Integer var5) {
            return addZombiesInOutfit(var0, var1, var2, var3, var4, var5, false, false, false, false, 1.0f);
        }

        @LuaMethod(name = "addZombiesInOutfit", global = true)
        public static ArrayList<IsoZombie> addZombiesInOutfit(int var0, int var1, int var2, int var3, String var4, Integer var5, boolean var6, boolean var7, boolean var8, boolean var9, float var10) {
            IsoGridSquare var12;
            ArrayList<IsoZombie> var11 = new ArrayList<>();
            if (!IsoWorld.getZombiesDisabled() && (var12 = IsoCell.getInstance().getGridSquare(var0, var1, var2)) != null) {
                for (int var13 = 0; var13 < var3; var13++) {
                    if (var10 <= 0.0f) {
                        var12.getChunk().AddCorpses(var0 / 10, var1 / 10);
                    } else {
                        VirtualZombieManager.instance.choices.clear();
                        VirtualZombieManager.instance.choices.add(var12);
                        IsoZombie var14 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
                        if (var14 != null) {
                            if (var5 != null) {
                                var14.setFemaleEtc(Rand.Next(100) < var5.intValue());
                            }
                            if (var4 != null) {
                                var14.dressInPersistentOutfit(var4);
                                var14.bDressInRandomOutfit = false;
                            } else {
                                var14.bDressInRandomOutfit = true;
                            }
                            var14.bLunger = true;
                            var14.setKnockedDown(var9);
                            if (var6) {
                                var14.setCrawler(true);
                                var14.setCanWalk(false);
                                var14.setOnFloor(true);
                                var14.setKnockedDown(true);
                                var14.setCrawlerType(1);
                                var14.DoZombieStats();
                            }
                            var14.setFakeDead(var8);
                            var14.setFallOnFront(var7);
                            var14.setHealth(var10);
                            var11.add(var14);
                        }
                    }
                }
                ZombieSpawnRecorder.instance.record(var11, GlobalObject.class.getSimpleName());
            }
            return var11;
        }

        @LuaMethod(name = "addZombiesInBuilding", global = true)
        public ArrayList<IsoZombie> addZombiesInBuilding(BuildingDef var1, int var2, String var3, RoomDef var4, Integer var5) {
            IsoGridSquare var12;
            boolean var6 = var4 == null;
            ArrayList<IsoZombie> var7 = new ArrayList<>();
            if (!IsoWorld.getZombiesDisabled()) {
                if (var4 == null) {
                    var4 = var1.getRandomRoom(6);
                }
                int var8 = 2;
                int var9 = var4.area / 2;
                if (var2 == 0) {
                    if (SandboxOptions.instance.Zombies.getValue() == 1) {
                        var9 += 4;
                    } else if (SandboxOptions.instance.Zombies.getValue() == 2) {
                        var9 += 3;
                    } else if (SandboxOptions.instance.Zombies.getValue() == 3) {
                        var9 += 2;
                    } else if (SandboxOptions.instance.Zombies.getValue() == 5) {
                        var9 -= 4;
                    }
                    if (var9 > 8) {
                        var9 = 8;
                    }
                    if (var9 < 2) {
                        var9 = 2 + 1;
                    }
                } else {
                    var8 = var2;
                    var9 = var2;
                }
                int var10 = Rand.Next(var8, var9);
                for (int var11 = 0; var11 < var10 && (var12 = RandomizedBuildingBase.getRandomSpawnSquare(var4)) != null; var11++) {
                    VirtualZombieManager.instance.choices.clear();
                    VirtualZombieManager.instance.choices.add(var12);
                    IsoZombie var13 = VirtualZombieManager.instance.createRealZombieAlways(IsoDirections.getRandom().index(), false);
                    if (var13 != null) {
                        if (var5 != null) {
                            var13.setFemaleEtc(Rand.Next(100) < var5.intValue());
                        }
                        if (var3 != null) {
                            var13.dressInPersistentOutfit(var3);
                            var13.bDressInRandomOutfit = false;
                        } else {
                            var13.bDressInRandomOutfit = true;
                        }
                        var7.add(var13);
                        if (var6) {
                            var4 = var1.getRandomRoom(6);
                        }
                    }
                }
                ZombieSpawnRecorder.instance.record(var7, getClass().getSimpleName());
            }
            return var7;
        }

        @LuaMethod(name = "addVehicleDebug", global = true)
        public static BaseVehicle addVehicleDebug(String var0, IsoDirections var1, Integer var2, IsoGridSquare var3) {
            float var5;
            if (var1 == null) {
                var1 = IsoDirections.getRandom();
            }
            BaseVehicle var4 = new BaseVehicle(IsoWorld.instance.CurrentCell);
            if (!StringUtils.isNullOrEmpty(var0)) {
                var4.setScriptName(var0);
                var4.setScript();
                if (var2 != null) {
                    var4.setSkinIndex(var2.intValue());
                }
            }
            var4.setDir(var1);
            float angle = var1.toAngle() + 3.1415927f + Rand.Next(-0.2f, 0.2f);
            while (true) {
                var5 = angle;
                if (var5 <= 6.283185307179586d) {
                    break;
                }
                angle = (float) (var5 - 6.283185307179586d);
            }
            var4.savedRot.setAngleAxis(var5, 0.0f, 1.0f, 0.0f);
            var4.jniTransform.setRotation(var4.savedRot);
            var4.setX(var3.x);
            var4.setY(var3.y);
            var4.setZ(var3.z);
            if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var4)) {
                var4.setSquare(var3);
                var3.chunk.vehicles.add(var4);
                var4.chunk = var3.chunk;
                var4.addToWorld();
                VehiclesDB2.instance.addVehicle(var4);
            }
            var4.setGeneralPartCondition(1.3f, 10.0f);
            var4.rust = 0.0f;
            return var4;
        }

        @LuaMethod(name = "addVehicle", global = true)
        public static BaseVehicle addVehicle(String var0) {
            if (!StringUtils.isNullOrWhitespace(var0) && ScriptManager.instance.getVehicle(var0) == null) {
                DebugLog.Lua.warn("No such vehicle script \"" + var0 + "\"");
                return null;
            }
            ArrayList<VehicleScript> var1 = ScriptManager.instance.getAllVehicleScripts();
            if (var1.isEmpty()) {
                DebugLog.Lua.warn("No vehicle scripts defined");
                return null;
            }
            WorldSimulation.instance.create();
            BaseVehicle var2 = new BaseVehicle(IsoWorld.instance.CurrentCell);
            if (StringUtils.isNullOrWhitespace(var0)) {
                VehicleScript var3 = (VehicleScript) PZArrayUtil.pickRandom(var1);
                var0 = var3.getFullName();
            }
            var2.setScriptName(var0);
            var2.setX(IsoPlayer.getInstance().getX());
            var2.setY(IsoPlayer.getInstance().getY());
            var2.setZ(0.0f);
            if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var2)) {
                var2.setSquare(IsoPlayer.getInstance().getSquare());
                var2.square.chunk.vehicles.add(var2);
                var2.chunk = var2.square.chunk;
                var2.addToWorld();
                VehiclesDB2.instance.addVehicle(var2);
                return null;
            }
            DebugLog.Lua.error("ERROR: I can not spawn the vehicle. Invalid position. Try to change position.");
            return null;
        }

        @LuaMethod(name = "attachTrailerToPlayerVehicle", global = true)
        public static void attachTrailerToPlayerVehicle(int var0) {
            IsoPlayer var1 = IsoPlayer.players[var0];
            IsoGridSquare var2 = var1.getCurrentSquare();
            BaseVehicle var3 = var1.getVehicle();
            if (var3 == null) {
                var3 = addVehicleDebug("Base.OffRoad", IsoDirections.N, 0, var2);
                var3.repair();
                var1.getInventory().AddItem(var3.createVehicleKey());
            }
            BaseVehicle var4 = addVehicleDebug("Base.Trailer", IsoDirections.N, 0, IsoWorld.instance.CurrentCell.getGridSquare(var2.x, var2.y + 5, var2.z));
            var4.repair();
            var3.addPointConstraint(var1, var4, "trailer", "trailer");
        }

        @LuaMethod(name = "getKeyName", global = true)
        public static String getKeyName(int var0) {
            return Input.getKeyName(var0);
        }

        @LuaMethod(name = "getKeyCode", global = true)
        public static int getKeyCode(String var0) {
            return Input.getKeyCode(var0);
        }

        @LuaMethod(name = "queueCharEvent", global = true)
        public static void queueCharEvent(String var0) {
            RenderThread.queueInvokeOnRenderContext(() -> {
                GameKeyboard.getEventQueuePolling().addCharEvent(var0.charAt(0));
            });
        }

        @LuaMethod(name = "queueKeyEvent", global = true)
        public static void queueKeyEvent(int var0) {
            RenderThread.queueInvokeOnRenderContext(() -> {
                int var1 = KeyCodes.toGlfwKey(var0);
                GameKeyboard.getEventQueuePolling().addKeyEvent(var1, 1);
                GameKeyboard.getEventQueuePolling().addKeyEvent(var1, 0);
            });
        }

        @LuaMethod(name = "addAllVehicles", global = true)
        public static void addAllVehicles() {
            addAllVehicles(var0 -> {
                return (var0.getName().contains("Smashed") || var0.getName().contains("Burnt")) ? false : true;
            });
        }

        @LuaMethod(name = "addAllBurntVehicles", global = true)
        public static void addAllBurntVehicles() {
            addAllVehicles(var0 -> {
                return var0.getName().contains("Burnt");
            });
        }

        @LuaMethod(name = "addAllSmashedVehicles", global = true)
        public static void addAllSmashedVehicles() {
            addAllVehicles(var0 -> {
                return var0.getName().contains("Smashed");
            });
        }

        public static void addAllVehicles(Predicate<VehicleScript> var0) {
            ArrayList<VehicleScript> var1 = ScriptManager.instance.getAllVehicleScripts();
            var1.sort(Comparator.comparing((v0) -> {
                return v0.getName();
            }));
            float var2 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles() + 5;
            float var3 = IsoPlayer.getInstance().getY();
            Iterator<VehicleScript> it = var1.iterator();
            while (it.hasNext()) {
                VehicleScript o = it.next();
                if (o.getModel() != null && var0.test(o) && IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, 0.0f) != null) {
                    WorldSimulation.instance.create();
                    BaseVehicle var7 = new BaseVehicle(IsoWorld.instance.CurrentCell);
                    var7.setScriptName(o.getFullName());
                    var7.setX(var2);
                    var7.setY(var3);
                    var7.setZ(0.0f);
                    if (IsoChunk.doSpawnedVehiclesInInvalidPosition(var7)) {
                        var7.setSquare(IsoPlayer.getInstance().getSquare());
                        var7.square.chunk.vehicles.add(var7);
                        var7.chunk = var7.square.chunk;
                        var7.addToWorld();
                        VehiclesDB2.instance.addVehicle(var7);
                        IsoChunk.addFromCheckedVehicles(var7);
                    } else {
                        DebugLog.Lua.warn(o.getName() + " not spawned, position invalid");
                    }
                    var2 += 4.0f;
                    if (var2 > IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMaxTiles() - 5) {
                        var2 = IsoWorld.instance.CurrentCell.ChunkMap[0].getWorldXMinTiles() + 5;
                        var3 += 8.0f;
                    }
                }
            }
        }

        @LuaMethod(name = "addPhysicsObject", global = true)
        public static BaseVehicle addPhysicsObject() {
            MPStatistic.getInstance().Bullet.Start();
            int var0 = Bullet.addPhysicsObject(getPlayer().getX(), getPlayer().getY());
            MPStatistic.getInstance().Bullet.End();
            IsoPushableObject var1 = new IsoPushableObject(IsoWorld.instance.getCell(), IsoPlayer.getInstance().getCurrentSquare(), IsoSpriteManager.instance.getSprite("trashcontainers_01_16"));
            WorldSimulation.instance.physicsObjectMap.put(Integer.valueOf(var0), var1);
            return null;
        }

        @LuaMethod(name = "toggleVehicleRenderToTexture", global = true)
        public static void toggleVehicleRenderToTexture() {
            BaseVehicle.RENDER_TO_TEXTURE = !BaseVehicle.RENDER_TO_TEXTURE;
        }

        @LuaMethod(name = "reloadSoundFiles", global = true)
        public static void reloadSoundFiles() {
            try {
                for (String var1 : ZomboidFileSystem.instance.ActiveFileMap.keySet()) {
                    if (var1.matches(".*/sounds_.+\\.txt")) {
                        GameSounds.ReloadFile(var1);
                    }
                }
            } catch (Throwable var2) {
                ExceptionLogger.logException(var2);
            }
        }

        @LuaMethod(name = "getAnimationViewerState", global = true)
        public static AnimationViewerState getAnimationViewerState() {
            return AnimationViewerState.instance;
        }

        @LuaMethod(name = "getAttachmentEditorState", global = true)
        public static AttachmentEditorState getAttachmentEditorState() {
            return AttachmentEditorState.instance;
        }

        @LuaMethod(name = "getEditVehicleState", global = true)
        public static EditVehicleState getEditVehicleState() {
            return EditVehicleState.instance;
        }

        @LuaMethod(name = "showAnimationViewer", global = true)
        public static void showAnimationViewer() {
            IngameState.instance.showAnimationViewer = true;
        }

        @LuaMethod(name = "showAttachmentEditor", global = true)
        public static void showAttachmentEditor() {
            IngameState.instance.showAttachmentEditor = true;
        }

        @LuaMethod(name = "showChunkDebugger", global = true)
        public static void showChunkDebugger() {
            IngameState.instance.showChunkDebugger = true;
        }

        @LuaMethod(name = "showGlobalObjectDebugger", global = true)
        public static void showGlobalObjectDebugger() {
            IngameState.instance.showGlobalObjectDebugger = true;
        }

        @LuaMethod(name = "showVehicleEditor", global = true)
        public static void showVehicleEditor(String var0) {
            IngameState.instance.showVehicleEditor = StringUtils.isNullOrWhitespace(var0) ? "" : var0;
        }

        @LuaMethod(name = "showWorldMapEditor", global = true)
        public static void showWorldMapEditor(String var0) {
            IngameState.instance.showWorldMapEditor = StringUtils.isNullOrWhitespace(var0) ? "" : var0;
        }

        @LuaMethod(name = "reloadVehicles", global = true)
        public static void reloadVehicles() {
            try {
                Iterator it = ScriptManager.instance.scriptsWithVehicleTemplates.iterator();
                while (it.hasNext()) {
                    String value = (String) it.next();
                    ScriptManager.instance.LoadFile(value, true);
                }
                Iterator it2 = ScriptManager.instance.scriptsWithVehicles.iterator();
                while (it2.hasNext()) {
                    String s = (String) it2.next();
                    ScriptManager.instance.LoadFile(s, true);
                }
                BaseVehicle.LoadAllVehicleTextures();
                Iterator it3 = IsoWorld.instance.CurrentCell.vehicles.iterator();
                while (it3.hasNext()) {
                    BaseVehicle var3 = (BaseVehicle) it3.next();
                    var3.scriptReloaded();
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        @LuaMethod(name = "reloadEngineRPM", global = true)
        public static void reloadEngineRPM() {
            try {
                ScriptManager.instance.LoadFile(ZomboidFileSystem.instance.getString("media/scripts/vehicles/engine_rpm.txt"), true);
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

        @LuaMethod(name = "proceedPM", global = true)
        public static String proceedPM(String var0) {
            Matcher var3 = Pattern.compile("(\"[^\"]*\\s+[^\"]*\"|[^\"]\\S*)\\s(.+)").matcher(var0.trim());
            if (var3.matches()) {
                String var1 = var3.group(1);
                String var2 = var3.group(2);
                String var12 = var1.replaceAll("\"", "");
                ChatManager.getInstance().sendWhisperMessage(var12, var2);
                return var12;
            }
            ChatManager.getInstance().addMessage("Error", getText("IGUI_Commands_Whisper"));
            return "";
        }

        @LuaMethod(name = "processSayMessage", global = true)
        public static void processSayMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.say, var0.trim());
            }
        }

        @LuaMethod(name = "processGeneralMessage", global = true)
        public static void processGeneralMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.general, var0.trim());
            }
        }

        @LuaMethod(name = "processShoutMessage", global = true)
        public static void processShoutMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.shout, var0.trim());
            }
        }

        @LuaMethod(name = "proceedFactionMessage", global = true)
        public static void ProceedFactionMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.faction, var0.trim());
            }
        }

        @LuaMethod(name = "processSafehouseMessage", global = true)
        public static void ProcessSafehouseMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.safehouse, var0.trim());
            }
        }

        @LuaMethod(name = "processAdminChatMessage", global = true)
        public static void ProcessAdminChatMessage(String var0) {
            if (var0 != null && !var0.isEmpty()) {
                ChatManager.getInstance().sendMessageToChat(ChatType.admin, var0.trim());
            }
        }

        @LuaMethod(name = "showWrongChatTabMessage", global = true)
        public static void showWrongChatTabMessage(int var0, int var1, String var2) {
            String var3 = ChatManager.getInstance().getTabName((short) var0);
            String var4 = ChatManager.getInstance().getTabName((short) var1);
            String var5 = Translator.getText("UI_chat_wrong_tab", var3, var4, var2);
            ChatManager.getInstance().showServerChatMessage(var5);
        }

        @LuaMethod(name = "focusOnTab", global = true)
        public static void focusOnTab(Short var0) {
            ChatManager.getInstance().focusOnTab(var0);
        }

        @LuaMethod(name = "updateChatSettings", global = true)
        public static void updateChatSettings(String var0, boolean var1, boolean var2) {
            ChatManager.getInstance().updateChatSettings(var0, var1, var2);
        }

        @LuaMethod(name = "checkPlayerCanUseChat", global = true)
        public static Boolean checkPlayerCanUseChat(String var0) {
            ChatType var1;
            switch (var0.trim()) {
                case "/all":
                    var1 = ChatType.general;
                    break;
                case "/a":
                case "/admin":
                    var1 = ChatType.admin;
                    break;
                case "/s":
                case "/say":
                    var1 = ChatType.say;
                    break;
                case "/y":
                case "/yell":
                    var1 = ChatType.shout;
                    break;
                case "/f":
                case "/faction":
                    var1 = ChatType.faction;
                    break;
                case "/sh":
                case "/safehouse":
                    var1 = ChatType.safehouse;
                    break;
                case "/w":
                case "/whisper":
                    var1 = ChatType.whisper;
                    break;
                case "/radio":
                case "/r":
                    var1 = ChatType.radio;
                    break;
                default:
                    var1 = ChatType.notDefined;
                    DebugLog.Lua.warn("Chat command not found");
                    break;
            }
            return ChatManager.getInstance().isPlayerCanUseChat(var1);
        }

        @LuaMethod(name = "reloadVehicleTextures", global = true)
        public static void reloadVehicleTextures(String var0) {
            VehicleScript var1 = ScriptManager.instance.getVehicle(var0);
            if (var1 == null) {
                DebugLog.Lua.warn("no such vehicle script");
                return;
            }
            for (int var2 = 0; var2 < var1.getSkinCount(); var2++) {
                VehicleScript.Skin var3 = var1.getSkin(var2);
                if (var3.texture != null) {
                    Texture.reload("media/textures/" + var3.texture + ".png");
                }
                if (var3.textureRust != null) {
                    Texture.reload("media/textures/" + var3.textureRust + ".png");
                }
                if (var3.textureMask != null) {
                    Texture.reload("media/textures/" + var3.textureMask + ".png");
                }
                if (var3.textureLights != null) {
                    Texture.reload("media/textures/" + var3.textureLights + ".png");
                }
                if (var3.textureDamage1Overlay != null) {
                    Texture.reload("media/textures/" + var3.textureDamage1Overlay + ".png");
                }
                if (var3.textureDamage1Shell != null) {
                    Texture.reload("media/textures/" + var3.textureDamage1Shell + ".png");
                }
                if (var3.textureDamage2Overlay != null) {
                    Texture.reload("media/textures/" + var3.textureDamage2Overlay + ".png");
                }
                if (var3.textureDamage2Shell != null) {
                    Texture.reload("media/textures/" + var3.textureDamage2Shell + ".png");
                }
                if (var3.textureShadow != null) {
                    Texture.reload("media/textures/" + var3.textureShadow + ".png");
                }
            }
        }

        @LuaMethod(name = "useStaticErosionRand", global = true)
        public static void useStaticErosionRand(boolean var0) {
            ErosionData.staticRand = var0;
        }

        @LuaMethod(name = "getClimateManager", global = true)
        public static ClimateManager getClimateManager() {
            return ClimateManager.getInstance();
        }

        @LuaMethod(name = "getClimateMoon", global = true)
        public static ClimateMoon getClimateMoon() {
            return ClimateMoon.getInstance();
        }

        @LuaMethod(name = "getWorldMarkers", global = true)
        public static WorldMarkers getWorldMarkers() {
            return WorldMarkers.instance;
        }

        @LuaMethod(name = "getIsoMarkers", global = true)
        public static IsoMarkers getIsoMarkers() {
            return IsoMarkers.instance;
        }

        @LuaMethod(name = "getErosion", global = true)
        public static ErosionMain getErosion() {
            return ErosionMain.getInstance();
        }

        @LuaMethod(name = "getAllOutfits", global = true)
        public static ArrayList<String> getAllOutfits(boolean var0) {
            ArrayList<String> var1 = new ArrayList<>();
            ModelManager.instance.create();
            if (OutfitManager.instance != null) {
                ArrayList<Outfit> var2 = var0 ? OutfitManager.instance.m_FemaleOutfits : OutfitManager.instance.m_MaleOutfits;
                Iterator<Outfit> it = var2.iterator();
                while (it.hasNext()) {
                    Outfit var4 = it.next();
                    var1.add(var4.m_Name);
                }
                Collections.sort(var1);
            }
            return var1;
        }

        @LuaMethod(name = "getAllVehicles", global = true)
        public static ArrayList<String> getAllVehicles() {
            return (ArrayList) ScriptManager.instance.getAllVehicleScripts().stream().map((v0) -> {
                return v0.getFullName();
            }).sorted().collect(Collectors.toCollection(ArrayList::new));
        }

        @LuaMethod(name = "getAllHairStyles", global = true)
        public static ArrayList<String> getAllHairStyles(boolean var0) {
            ArrayList<String> var1 = new ArrayList<>();
            if (HairStyles.instance != null) {
                ArrayList<HairStyle> var2 = new ArrayList<>(var0 ? HairStyles.instance.m_FemaleStyles : HairStyles.instance.m_MaleStyles);
                var2.sort((var0x, var1x) -> {
                    if (var0x.name.isEmpty()) {
                        return -1;
                    }
                    if (var1x.name.isEmpty()) {
                        return 1;
                    }
                    String var2x = getText("IGUI_Hair_" + var0x.name);
                    String var3 = getText("IGUI_Hair_" + var1x.name);
                    return var2x.compareTo(var3);
                });
                Iterator<HairStyle> it = var2.iterator();
                while (it.hasNext()) {
                    HairStyle var4 = it.next();
                    var1.add(var4.name);
                }
            }
            return var1;
        }

        @LuaMethod(name = "getHairStylesInstance", global = true)
        public static HairStyles getHairStylesInstance() {
            return HairStyles.instance;
        }

        @LuaMethod(name = "getBeardStylesInstance", global = true)
        public static BeardStyles getBeardStylesInstance() {
            return BeardStyles.instance;
        }

        @LuaMethod(name = "getAllBeardStyles", global = true)
        public static ArrayList<String> getAllBeardStyles() {
            ArrayList<String> var0 = new ArrayList<>();
            if (BeardStyles.instance != null) {
                ArrayList<BeardStyle> var1 = new ArrayList<>(BeardStyles.instance.m_Styles);
                var1.sort((var0x, var1x) -> {
                    if (var0x.name.isEmpty()) {
                        return -1;
                    }
                    if (var1x.name.isEmpty()) {
                        return 1;
                    }
                    String var2 = getText("IGUI_Beard_" + var0x.name);
                    String var3 = getText("IGUI_Beard_" + var1x.name);
                    return var2.compareTo(var3);
                });
                Iterator<BeardStyle> it = var1.iterator();
                while (it.hasNext()) {
                    BeardStyle var3 = it.next();
                    var0.add(var3.name);
                }
            }
            return var0;
        }

        @LuaMethod(name = "getAllItemsForBodyLocation", global = true)
        public static KahluaTable getAllItemsForBodyLocation(String var0) {
            KahluaTable var1 = LuaManager.platform.newTable();
            if (StringUtils.isNullOrWhitespace(var0)) {
                return var1;
            }
            int var2 = 1;
            ArrayList<Item> var3 = ScriptManager.instance.getAllItems();
            Iterator<Item> var4 = var3.iterator();
            while (var4.hasNext()) {
                Item var5 = var4.next();
                if (!StringUtils.isNullOrWhitespace(var5.getClothingItem()) && (var0.equals(var5.getBodyLocation()) || var0.equals(var5.CanBeEquipped))) {
                    int i = var2;
                    var2++;
                    var1.rawset(i, var5.getFullName());
                }
            }
            return var1;
        }

        @LuaMethod(name = "getAllDecalNamesForItem", global = true)
        public static ArrayList<String> getAllDecalNamesForItem(InventoryItem var0) {
            ClothingItem var2;
            ClothingDecalGroup var4;
            ArrayList<String> var1 = new ArrayList<>();
            if (var0 != null && ClothingDecals.instance != null && (var2 = var0.getClothingItem()) != null) {
                String var3 = var2.getDecalGroup();
                if (!StringUtils.isNullOrWhitespace(var3) && (var4 = ClothingDecals.instance.FindGroup(var3)) != null) {
                    var4.getDecals(var1);
                }
            }
            return var1;
        }

        @LuaMethod(name = "screenZoomIn", global = true)
        public void screenZoomIn() {
        }

        @LuaMethod(name = "screenZoomOut", global = true)
        public void screenZoomOut() {
        }

        @LuaMethod(name = "addSound", global = true)
        public void addSound(IsoObject var1, int var2, int var3, int var4, int var5, int var6) {
            WorldSoundManager.instance.addSound(var1, var2, var3, var4, var5, var6);
        }

        @LuaMethod(name = "sendAddXp", global = true)
        public void sendAddXp(IsoPlayer var1, PerkFactory.Perk var2, int var3) {
            if (GameClient.bClient && var1.isExistInTheWorld()) {
                GameClient.instance.sendAddXp(var1, var2, var3);
            }
        }

        @LuaMethod(name = "SyncXp", global = true)
        public void SyncXp(IsoPlayer var1) {
            if (GameClient.bClient) {
                GameClient.instance.sendSyncXp(var1);
            }
        }

        @LuaMethod(name = "checkServerName", global = true)
        public String checkServerName(String var1) {
            String var2 = ProfanityFilter.getInstance().validateString(var1, true, true, true);
            if (StringUtils.isNullOrEmpty(var2)) {
                return null;
            }
            return Translator.getText("UI_BadWordCheck", var2);
        }

        @LuaMethod(name = "Render3DItem", global = true)
        public void Render3DItem(InventoryItem var1, IsoGridSquare var2, float var3, float var4, float var5, float var6) {
            WorldItemModelDrawer.renderMain(var1, var2, var3, var4, var5, 0.0f, var6);
        }

        @LuaMethod(name = "getContainerOverlays", global = true)
        public ContainerOverlays getContainerOverlays() {
            return ContainerOverlays.instance;
        }

        @LuaMethod(name = "getTileOverlays", global = true)
        public TileOverlays getTileOverlays() {
            return TileOverlays.instance;
        }

        @LuaMethod(name = "getAverageFPS", global = true)
        public Double getAverageFSP() {
            float var1 = GameWindow.averageFPS;
            if (!PerformanceSettings.isUncappedFPS()) {
                var1 = Math.min(var1, PerformanceSettings.getLockFPS());
            }
            return BoxedStaticValues.toDouble(Math.floor(var1));
        }

        @LuaMethod(name = "createItemTransaction", global = true)
        public static void createItemTransaction(InventoryItem var0, ItemContainer var1, ItemContainer var2) {
            if (GameClient.bClient && var0 != null) {
                int var3 = ((Integer) Optional.ofNullable(var1).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                int var4 = ((Integer) Optional.ofNullable(var2).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                ItemTransactionManager.createItemTransaction(var0.getID(), var3, var4);
            }
        }

        @LuaMethod(name = "removeItemTransaction", global = true)
        public static void removeItemTransaction(InventoryItem var0, ItemContainer var1, ItemContainer var2) {
            if (GameClient.bClient && var0 != null) {
                int var3 = ((Integer) Optional.ofNullable(var1).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                int var4 = ((Integer) Optional.ofNullable(var2).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                ItemTransactionManager.removeItemTransaction(var0.getID(), var3, var4);
            }
        }

        @LuaMethod(name = "isItemTransactionConsistent", global = true)
        public static boolean isItemTransactionConsistent(InventoryItem var0, ItemContainer var1, ItemContainer var2) {
            if (GameClient.bClient && var0 != null) {
                int var3 = ((Integer) Optional.ofNullable(var1).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                int var4 = ((Integer) Optional.ofNullable(var2).map((v0) -> {
                    return v0.getContainingItem();
                }).map((v0) -> {
                    return v0.getID();
                }).orElse(-1)).intValue();
                return ItemTransactionManager.isConsistent(var0.getID(), var3, var4);
            }
            return true;
        }

        @LuaMethod(name = "getServerStatistic", global = true)
        public static KahluaTable getServerStatistic() {
            return MPStatistic.getInstance().getStatisticTableForLua();
        }

        @LuaMethod(name = "setServerStatisticEnable", global = true)
        public static void setServerStatisticEnable(boolean var0) {
            if (GameClient.bClient) {
                GameClient.setServerStatisticEnable(var0);
            }
        }

        @LuaMethod(name = "getServerStatisticEnable", global = true)
        public static boolean getServerStatisticEnable() {
            return GameClient.bClient && GameClient.getServerStatisticEnable();
        }

        @LuaMethod(name = "checkModsNeedUpdate", global = true)
        public static void checkModsNeedUpdate(UdpConnection var0) {
            DebugLog.log("CheckModsNeedUpdate: Checking...");
            if (SteamUtils.isSteamModeEnabled() && isServer()) {
                ArrayList<String> var1 = getSteamWorkshopItemIDs();
                new ItemQueryJava(var1, var0);
            }
        }

        @LuaMethod(name = "getSearchMode", global = true)
        public static SearchMode getSearchMode() {
            return SearchMode.getInstance();
        }

        @LuaMethod(name = "timSort", global = true)
        public static void timSort(KahluaTable var0, Object var1) {
            KahluaTableImpl var2 = (KahluaTableImpl) Type.tryCastTo(var0, KahluaTableImpl.class);
            if (var2 != null && var2.len() >= 2 && var1 != null) {
                timSortComparator.comp = var1;
                Object[] var3 = var2.delegate.values().toArray();
                Arrays.sort(var3, timSortComparator);
                for (int var4 = 0; var4 < var3.length; var4++) {
                    var2.rawset(var4 + 1, var3[var4]);
                    var3[var4] = null;
                }
            }
        }

        /* loaded from: craftboid.jar:zombie/Lua/LuaManager$GlobalObject$LuaFileWriter.class */
        public static final class LuaFileWriter {
            private final PrintWriter writer;

            public LuaFileWriter(PrintWriter var1) {
                this.writer = var1;
            }

            public void write(String var1) throws IOException {
                this.writer.write(var1);
            }

            public void writeln(String var1) throws IOException {
                this.writer.write(var1);
                this.writer.write(System.lineSeparator());
            }

            public void close() throws IOException {
                this.writer.close();
            }
        }

        /* loaded from: craftboid.jar:zombie/Lua/LuaManager$GlobalObject$ItemQuery.class */
        private static final class ItemQuery implements ISteamWorkshopCallback {
            private final LuaClosure functionObj;
            private final Object arg1;
            private final long handle;

            public ItemQuery(ArrayList<String> var1, LuaClosure var2, Object var3) {
                this.functionObj = var2;
                this.arg1 = var3;
                long[] var4 = new long[var1.size()];
                int var5 = 0;
                Iterator<String> it = var1.iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    long var7 = SteamUtils.convertStringToSteamID(s);
                    if (var7 != -1) {
                        int i = var5;
                        var5++;
                        var4[i] = var7;
                    }
                }
                this.handle = SteamWorkshop.instance.CreateQueryUGCDetailsRequest(var4, this);
                if (this.handle == 0) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    if (var3 == null) {
                        LuaManager.caller.pcall(LuaManager.thread, var2, "NotCompleted");
                    } else {
                        LuaManager.caller.pcall(LuaManager.thread, var2, new Object[]{var3, "NotCompleted"});
                    }
                }
            }

            public void onItemCreated(long var1, boolean var3) {
            }

            public void onItemNotCreated(int var1) {
            }

            public void onItemUpdated(boolean var1) {
            }

            public void onItemNotUpdated(int var1) {
            }

            public void onItemSubscribed(long var1) {
            }

            public void onItemNotSubscribed(long var1, int var3) {
            }

            public void onItemDownloaded(long var1) {
            }

            public void onItemNotDownloaded(long var1, int var3) {
            }

            public void onItemQueryCompleted(long var1, int var3) {
                if (var1 == this.handle) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    ArrayList<SteamUGCDetails> var4 = new ArrayList<>();
                    for (int var5 = 0; var5 < var3; var5++) {
                        SteamUGCDetails var6 = SteamWorkshop.instance.GetQueryUGCResult(var1, var5);
                        if (var6 != null) {
                            var4.add(var6);
                        }
                    }
                    SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
                    if (this.arg1 == null) {
                        LuaManager.caller.pcall(LuaManager.thread, this.functionObj, new Object[]{"Completed", var4});
                    } else {
                        LuaManager.caller.pcall(LuaManager.thread, this.functionObj, new Object[]{this.arg1, "Completed", var4});
                    }
                }
            }

            public void onItemQueryNotCompleted(long var1, int var3) {
                if (var1 == this.handle) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
                    if (this.arg1 == null) {
                        LuaManager.caller.pcall(LuaManager.thread, this.functionObj, "NotCompleted");
                    } else {
                        LuaManager.caller.pcall(LuaManager.thread, this.functionObj, new Object[]{this.arg1, "NotCompleted"});
                    }
                }
            }
        }

        /* loaded from: craftboid.jar:zombie/Lua/LuaManager$GlobalObject$ItemQueryJava.class */
        private static final class ItemQueryJava implements ISteamWorkshopCallback {
            private final long handle;
            private final UdpConnection connection;

            public ItemQueryJava(ArrayList<String> var1, UdpConnection var2) {
                this.connection = var2;
                long[] var3 = new long[var1.size()];
                int var4 = 0;
                Iterator<String> it = var1.iterator();
                while (it.hasNext()) {
                    String s = it.next();
                    long var6 = SteamUtils.convertStringToSteamID(s);
                    if (var6 != -1) {
                        int i = var4;
                        var4++;
                        var3[i] = var6;
                    }
                }
                this.handle = SteamWorkshop.instance.CreateQueryUGCDetailsRequest(var3, this);
                if (this.handle == 0) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    inform("CheckModsNeedUpdate: Check not completed");
                }
            }

            private void inform(String var1) {
                if (this.connection != null) {
                    ChatServer.getInstance().sendMessageToServerChat(this.connection, var1);
                }
                DebugLog.log(var1);
            }

            public void onItemCreated(long var1, boolean var3) {
            }

            public void onItemNotCreated(int var1) {
            }

            public void onItemUpdated(boolean var1) {
            }

            public void onItemNotUpdated(int var1) {
            }

            public void onItemSubscribed(long var1) {
            }

            public void onItemNotSubscribed(long var1, int var3) {
            }

            public void onItemDownloaded(long var1) {
            }

            public void onItemNotDownloaded(long var1, int var3) {
            }

            public void onItemQueryCompleted(long var1, int var3) {
                if (var1 == this.handle) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    for (int var4 = 0; var4 < var3; var4++) {
                        SteamUGCDetails var5 = SteamWorkshop.instance.GetQueryUGCResult(var1, var4);
                        if (var5 != null) {
                            long var6 = var5.getID();
                            long var8 = SteamWorkshop.instance.GetItemState(var6);
                            if (SteamWorkshopItem.ItemState.Installed.and(var8) && SteamWorkshopItem.ItemState.NeedsUpdate.not(var8) && var5.getTimeCreated() != 0 && var5.getTimeUpdated() != SteamWorkshop.instance.GetItemInstallTimeStamp(var6)) {
                                var8 |= SteamWorkshopItem.ItemState.NeedsUpdate.getValue();
                            }
                            if (SteamWorkshopItem.ItemState.NeedsUpdate.and(var8)) {
                                inform("CheckModsNeedUpdate: Mods need update");
                                SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
                                return;
                            }
                        }
                    }
                    inform("CheckModsNeedUpdate: Mods updated");
                    SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
                }
            }

            public void onItemQueryNotCompleted(long var1, int var3) {
                if (var1 == this.handle) {
                    SteamWorkshop.instance.RemoveCallback(this);
                    SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
                    inform("CheckModsNeedUpdate: Check not completed");
                }
            }
        }

        /* loaded from: craftboid.jar:zombie/Lua/LuaManager$GlobalObject$TimSortComparator.class */
        private static final class TimSortComparator implements Comparator<Object> {
            Object comp;

            private TimSortComparator() {
            }

            @Override // java.util.Comparator
            public int compare(Object var1, Object var2) {
                if (Objects.equals(var1, var2)) {
                    return 0;
                }
                Boolean var3 = LuaManager.thread.pcallBoolean(this.comp, var1, var2);
                return var3 == Boolean.TRUE ? -1 : 1;
            }
        }
    }
}