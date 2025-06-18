@file:Suppress("unused")

package com.asledgehammer.crafthammer.api.event.log

enum class LogType {
  NetworkPacketDebug,
  NetworkFileDebug,
  Network,
  General,
  Lua,
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
  Vehicle,
  Voice,

  // Ours
  CraftHammer,
  Security,
  Sledgehammer;
}